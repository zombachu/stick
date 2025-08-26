@file:OptIn(ExperimentalContracts::class)

package com.zombachu.stick.element

import com.zombachu.stick.ExecutionResult
import com.zombachu.stick.Invocation
import com.zombachu.stick.ParsingResult
import com.zombachu.stick.Result
import com.zombachu.stick.SenderContext
import com.zombachu.stick.handle
import com.zombachu.stick.impl.InvocationImpl
import com.zombachu.stick.impl.Tuple
import com.zombachu.stick.isSuccess
import com.zombachu.stick.propagateError
import com.zombachu.stick.valueOrPropagateError
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract

internal sealed class Signature<S : SenderContext, O>(
    elements: Tuple<SignatureConstraint<S, O, Any>>
) {
    private val flags: List<IndexedElement<S, O, FlagImpl<S, O, Any>>>
    private val linearElements: List<IndexedElement<S, O, Element<S, O, Any>>>

    init {
        val partitioned = elements.toList().mapIndexed { i, e -> IndexedElement(i, e) }.partition { it.element is FlagImpl }
        @Suppress("UNCHECKED_CAST")
        flags = partitioned.first as List<IndexedElement<S, O, FlagImpl<S, O, Any>>>
        linearElements = partitioned.second
    }

    protected abstract fun executeParsed(context: Invocation<S, O>, parsedValues: List<Any>): ExecutionResult

    context(senderContext: S, executionContext: InvocationImpl<S, O>)
    fun execute(): Result<*> {
        val value = parse().valueOrPropagateError { it: Result<List<Any>> -> return it }
        return executeParsed(executionContext, value)
    }

    context(senderContext: S)
    fun getSyntax(): String {
        var linearSyntax: List<String> = linearElements
            .map { it.element }
            .filterIsInstance<SyntaxElement<S, O, *>>()
            .filter { it.validateSender().isSuccess() }
            .map { it.getSyntax() }
        val flagSyntax: List<String> = flags
            .map { it.element }
            .filter { it.validateSender().isSuccess() }
            .map { it.getSyntax() }

        // Add terminating element after flags
        val lastLinearElement = linearElements.lastOrNull()?.element
        var terminatingElementSyntax: String? = null
        if (lastLinearElement is SignatureConstraint.Terminating) {
            terminatingElementSyntax = linearSyntax.last()
            linearSyntax = linearSyntax.subList(0, linearSyntax.size - 1)
        }

        val syntax = if (terminatingElementSyntax == null) {
            linearSyntax + flagSyntax
        } else {
            linearSyntax + flagSyntax + terminatingElementSyntax
        }

        return syntax.joinToString(" ")
    }

    context(senderContext: S)
    private fun processSyntaxElement(
        context: InvocationImpl<S, O>,
        values: MutableList<Any>,
        element: SyntaxElement<S, O, Any>,
        index: Int
    ): Result<out Any> {
        val processResult = context.processSyntaxElement(element)
        if (processResult.isSuccess()) {
            values[index] = processResult.value
        }
        return processResult
    }

    context(senderContext: S, context: InvocationImpl<S, O>)
    private fun parse(): Result<List<Any>> {
        val values: MutableList<Any> = MutableList(flags.size + linearElements.size) {}

        val unprocessedFlags = flags.toMutableList()
        var parameterIndex = 0

        while (parameterIndex < linearElements.size) {
            val indexedElement = linearElements[parameterIndex]
            val element = indexedElement.element

            // First check if it's a helper as helpers don't need to consume any args
            if (element.isHelper()) {
                values[indexedElement.index] = element.value(context)
                parameterIndex++
                continue
            }

            // Attempt to parse the input as a flag (potentially multiple in a row)
            val flagsIt = unprocessedFlags.iterator()
            while (flagsIt.hasNext()) {
                val indexedFlag = flagsIt.next()
                val flag: FlagImpl<S, O, Any> = indexedFlag.element

                // Ignore flags unable to be accessed by the sender
                flag.validateSender().propagateError<List<Any>> { continue }

                processSyntaxElement(context, values, flag, indexedFlag.index).propagateError {
                    if (it is ParsingResult.TypeNotMatchedError) {
                        // Ignore type errors (flag didn't match)
                        continue
                    } else {
                        // If the flag matched and an error occurred in parsing then propagate it up
                        return it
                    }
                }
                // Mark the flag as processed if it succeeded
                flagsIt.remove()
            }

            // Parse with the element as a syntax element
            processSyntaxElement(context, values, element, indexedElement.index).propagateError { return it }
            parameterIndex++
        }

        // Populate unused flag values with defaults
        for (indexedFlag in unprocessedFlags) {
            val flag = indexedFlag.element

            val default = flag.validateSender().handle(
                onSuccess = { flag.default(context) },
                onFailure = { (flag as ValidatedFlag<S, O, *, *>).invalidDefault(context) }
            )
            values[indexedFlag.index] = default
        }

        return ParsingResult.success(values)
    }

    private fun Element<S, O, Any>.isHelper(): Boolean {
        contract {
            returns(true) implies (this@isHelper is HelperImpl<S, O, Any>)
            returns(false) implies (this@isHelper is SyntaxElement<S, O, Any>)
        }
        return this is HelperImpl<S, O, Any>
    }

    data class IndexedElement<S : SenderContext, O, out E : Element<S, O, *>>(
        val index: Int,
        val element: E
    )
}
