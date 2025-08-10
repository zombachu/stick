@file:OptIn(ExperimentalContracts::class)

package com.zombachu.stick.impl

import com.zombachu.stick.ExecutionResult
import com.zombachu.stick.ParsingResult
import com.zombachu.stick.ParsingResult.Companion.isSuccess
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract

internal sealed class Signature<S>(
    elements: Tuple<SignatureConstraint<S, Any>>
) {
    private val flags: List<IndexedElement<S, FlagImpl<S, Any>>>
    private val linearElements: List<IndexedElement<S, Element<S, Any>>>

    init {
        val partitioned = elements.toList().mapIndexed { i, e -> IndexedElement(i, e) }.partition { it.element is FlagImpl }
        flags = partitioned.first as List<IndexedElement<S, FlagImpl<S, Any>>>
        linearElements = partitioned.second
    }

    // TODO("Return execution result")
    protected abstract fun executeParsed(context: ExecutionContextImpl<S>, parsedValues: List<Any>): ExecutionResult

    fun execute(context: ExecutionContextImpl<S>): ParsingResult<*> {
        val result = parse(context)
        if (!result.isSuccess()) {
            return result
        }
        return executeParsed(context, result.value)
    }

    fun getSyntax(sender: S): String {
        var linearSyntax: List<String> = linearElements
            .map { it.element }
            .filterIsInstance<SyntaxElement<S, *>>()
            .filter { it.isSenderValid(sender) }
            .map { it.getSyntax(sender) }
        val flagSyntax: List<String> = flags
            .map { it.element }
            .filter { it.isSenderValid(sender) }
            .map { it.getSyntax(sender) }

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
        context: ExecutionContextImpl<S>,
        values: MutableList<Any>,
        element: SyntaxElement<S, Any>,
        index: Int
    ): ParsingResult<out Any> {
        val processResult = context.processSyntaxElement(element)
        if (processResult.isSuccess()) {
            values[index] = processResult.value
        }
        return processResult
    }

    private fun parse(
        context: ExecutionContextImpl<S>,
    ): ParsingResult<List<Any>> {
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
                val flag: FlagImpl<S, Any> = indexedFlag.element

                // Ignore flags unable to be accessed by the sender
                if (!flag.isSenderValid(context.sender)) {
                    continue
                }

                val processResult = processSyntaxElement(context, values, flag, indexedFlag.index)
                if (processResult.isSuccess()) {
                    // Mark the flag as processed if it succeeded
                    flagsIt.remove()
                } else if (processResult is ParsingResult.UnknownTypeError) {
                    // Ignore type errors (flag didn't match)
                    continue
                } else {
                    // If the flag matched and an error occurred in parsing then propagate it up
                    return processResult.cast()
                }
            }

            // Parse with the element as a syntax element
            val processResult = processSyntaxElement(context, values, element, indexedElement.index)
            if (!processResult.isSuccess()) {
                // Propagate errors up
                return processResult.cast()
            }
            parameterIndex++
        }

        // Populate unused flag values with defaults
        for (indexedFlag in unprocessedFlags) {
            val flag = indexedFlag.element
            val default = if (flag.isSenderValid(context.sender)) {
                flag.default(context)
            } else {
                flag.invalidDefault(context)
            }
            values[indexedFlag.index] = default
        }

        return ParsingResult.success(values)
    }

    private fun Element<S, Any>.isHelper(): Boolean {
        contract {
            returns(true) implies (this@isHelper is HelperImpl<S, Any>)
            returns(false) implies (this@isHelper is SyntaxElement<S, Any>)
        }
        return this is HelperImpl<S, Any>
    }

    private fun FlagImpl<S, Any>.isSenderValid(sender: S): Boolean {
        contract {
            returns(false) implies (this@isSenderValid is ValidatedFlag<S, *, *>)
        }
        return this !is Validator<*> || (this as Validator<S>).validate(sender)
    }

    data class IndexedElement<S, out E : Element<S, *>>(
        val index: Int,
        val element: E
    )
}
