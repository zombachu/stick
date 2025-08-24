@file:OptIn(ExperimentalContracts::class)

package com.zombachu.stick.element

import com.zombachu.stick.ExecutionResult
import com.zombachu.stick.ParsingResult
import com.zombachu.stick.Result
import com.zombachu.stick.SenderContext
import com.zombachu.stick.handle
import com.zombachu.stick.impl.ExecutionContextImpl
import com.zombachu.stick.impl.Tuple
import com.zombachu.stick.isSuccess
import com.zombachu.stick.propagateError
import com.zombachu.stick.valueOrPropagateError
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract

internal sealed class Signature<O, S : SenderContext>(
    elements: Tuple<SignatureConstraint<O, S, Any>>
) {
    private val flags: List<IndexedElement<O, S, FlagImpl<O, S, Any>>>
    private val linearElements: List<IndexedElement<O, S, Element<O, S, Any>>>

    init {
        val partitioned = elements.toList().mapIndexed { i, e -> IndexedElement(i, e) }.partition { it.element is FlagImpl }
        @Suppress("UNCHECKED_CAST")
        flags = partitioned.first as List<IndexedElement<O, S, FlagImpl<O, S, Any>>>
        linearElements = partitioned.second
    }

    protected abstract fun executeParsed(context: ExecutionContextImpl<O, S>, parsedValues: List<Any>): ExecutionResult

    fun execute(context: ExecutionContextImpl<O, S>): Result<*> {
        val value = parse(context).valueOrPropagateError { it: Result<List<Any>> -> return it }
        return executeParsed(context, value)
    }

    fun getSyntax(senderContext: S): String {
        var linearSyntax: List<String> = linearElements
            .map { it.element }
            .filterIsInstance<SyntaxElement<O, S, *>>()
            .filter { it.validateSender(senderContext).isSuccess() }
            .map { it.getSyntax(senderContext) }
        val flagSyntax: List<String> = flags
            .map { it.element }
            .filter { it.validateSender(senderContext).isSuccess() }
            .map { it.getSyntax(senderContext) }

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

    private fun processSyntaxElement(
        context: ExecutionContextImpl<O, S>,
        values: MutableList<Any>,
        element: SyntaxElement<O, S, Any>,
        index: Int
    ): Result<out Any> {
        val processResult = context.processSyntaxElement(element)
        if (processResult.isSuccess()) {
            values[index] = processResult.value
        }
        return processResult
    }

    private fun parse(
        context: ExecutionContextImpl<O, S>,
    ): Result<List<Any>> {
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
                val flag: FlagImpl<O, S, Any> = indexedFlag.element

                // Ignore flags unable to be accessed by the sender
                flag.validateSender(context.senderContext).propagateError<List<Any>> { continue }

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

            val default = flag.validateSender(context.senderContext).handle(
                onSuccess = { flag.default(context) },
                onFailure = { (flag as ValidatedFlag<O, S, *, *>).invalidDefault(context) }
            )
            values[indexedFlag.index] = default
        }

        return ParsingResult.success(values)
    }

    private fun Element<O, S, Any>.isHelper(): Boolean {
        contract {
            returns(true) implies (this@isHelper is HelperImpl<O, S, Any>)
            returns(false) implies (this@isHelper is SyntaxElement<O, S, Any>)
        }
        return this is HelperImpl<O, S, Any>
    }

    data class IndexedElement<O, S : SenderContext, out E : Element<O, S, *>>(
        val index: Int,
        val element: E
    )
}
