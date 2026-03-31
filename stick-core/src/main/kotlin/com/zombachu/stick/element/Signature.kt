@file:OptIn(ExperimentalContracts::class)

package com.zombachu.stick.element

import com.zombachu.stick.CommandResult
import com.zombachu.stick.Environment
import com.zombachu.stick.Invocation
import com.zombachu.stick.ParsingResult
import com.zombachu.stick.PeekingResult
import com.zombachu.stick.ValidationContext
import com.zombachu.stick.handleInternal
import com.zombachu.stick.impl.Arguments
import com.zombachu.stick.impl.InvocationImpl
import com.zombachu.stick.isSuccess
import com.zombachu.stick.propagateError
import com.zombachu.stick.valueOrPropagateError
import kotlin.contracts.ExperimentalContracts

internal sealed class Signature<E : Environment, S, T_ : Arguments>(
    elements: List<Element<E, S, Any?>>
) {
    private val flags: List<IndexedElement<E, S, Flag<E, S, Any?>>>
    private val linearElements: List<IndexedElement<E, S, Element<E, S, Any?>>>

    init {
        val partitioned = elements
            .mapIndexed { i, e -> IndexedElement(i, e) }
            .partition { it.element is Flag<*, *, *> }
        @Suppress("UNCHECKED_CAST")
        flags = partitioned.first as List<IndexedElement<E, S, Flag<E, S, Any?>>>
        linearElements = partitioned.second
    }

    context(inv: Invocation<E, S>)
    protected abstract fun executeParsed(parsedValues: List<Any?>): T_

    context(inv: InvocationImpl<E, S>)
    fun execute(): CommandResult<T_> {
        val parsedValues = parse().valueOrPropagateError { return it }
        val parsedValuesTuple = executeParsed(parsedValues)
        return ParsingResult.success(parsedValuesTuple)
    }

    context(validationContext: ValidationContext<E, S>)
    fun getSyntax(): String {
        var linearSyntax: List<String> = linearElements
            .map { it.element }
            .filterIsInstance<SyntaxElement<E, S, *>>()
            .filter { it.validateSender().isSuccess() }
            .map { it.getSyntax() }
        val flagSyntax: List<String> = flags
            .map { it.element }
            .filter { it.validateSender().isSuccess() }
            .map { it.getSyntax() }

        // Add terminating element after flags
        val lastLinearElement = linearElements.lastOrNull()?.element
        var terminatingElementSyntax: String? = null
        if (lastLinearElement !is Element.NonTerminating) {
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

    context(inv: InvocationImpl<E, S>)
    private fun processElement(
        values: MutableList<Any?>,
        element: IndexedElement<E, S, Element<E, S, Any?>>,
    ): CommandResult<Any?> {
        val processResult = inv.processElement(element.element)
        if (processResult.isSuccess()) {
            values[element.index] = processResult.value
        }
        return processResult
    }

    context(inv: InvocationImpl<E, S>)
    private fun parse(): CommandResult<List<Any?>> {
        val values: MutableList<Any?> = MutableList(flags.size + linearElements.size) {}

        val unprocessedFlags = flags.toMutableList()
        var parameterIndex = 0

        while (parameterIndex < linearElements.size) {
            processFlags(unprocessedFlags, values).propagateError { return it }

            // Parse with the element as a syntax element
            processElement(values, linearElements[parameterIndex]).propagateError {
                // Give a syntax failure for invalid arg length for parameters
                return if (it is PeekingResult.InvalidSizeError) ParsingResult.failSyntax(inv.getSyntax()) else it
            }
            parameterIndex++
        }

        processFlags(unprocessedFlags, values).propagateError { return it }

        // If there are unused args then the sender used invalid syntax
        if (inv.unparsed.isNotEmpty()) return ParsingResult.failSyntax(inv.getSyntax())

        // Populate unused flag values with defaults
        for (indexedFlag in unprocessedFlags) {
            val flag = indexedFlag.element

            val default = flag.validateSender().handleInternal(
                onSuccess = { flag.default(inv) },
                onFailure = { (flag as Flag.Validated<E, S, *>).invalidDefault(inv) }
            )
            val value = default.valueOrPropagateError { return it }
            values[indexedFlag.index] = value
        }

        return ParsingResult.success(values)
    }

    context(inv: InvocationImpl<E, S>)
    private fun processFlags(
        unprocessedFlags: MutableList<IndexedElement<E, S, Flag<E, S, Any?>>>,
        values: MutableList<Any?>,
    ): CommandResult<Unit> {
        // Attempt to parse the input as a flag (potentially multiple in a row, in any order)
        var unprocessedFlagsSize = -1
        while (unprocessedFlagsSize != unprocessedFlags.size) {
            unprocessedFlagsSize = unprocessedFlags.size

            val flagsIt = unprocessedFlags.iterator()
            while (flagsIt.hasNext()) {
                val indexedFlag = flagsIt.next()
                val flag: Flag<E, S, Any?> = indexedFlag.element

                // Ignore flags unable to be accessed by the sender
                flag.validateSender().propagateError { continue }

                processElement(values, indexedFlag).propagateError {
                    when (it) {
                        // Ignore type errors (flag didn't match)
                        is ParsingResult.TypeNotMatchedInternal, is PeekingResult.InvalidSizeError -> continue
                        // If the flag matched and an error occurred in parsing then propagate it up
                        else -> return it
                    }
                }
                // Mark the flag as processed if it succeeded
                flagsIt.remove()
            }
        }
        return ParsingResult.success(Unit)
    }

    data class IndexedElement<E : Environment, S, out L : Element<E, S, *>>(
        val index: Int,
        val element: L
    )
}
