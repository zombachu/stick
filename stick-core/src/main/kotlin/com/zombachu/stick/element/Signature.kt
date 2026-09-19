package com.zombachu.stick.element

import com.zombachu.stick.Arguments
import com.zombachu.stick.CommandResult
import com.zombachu.stick.Environment
import com.zombachu.stick.Invocation
import com.zombachu.stick.InvocationImpl
import com.zombachu.stick.MatchResult
import com.zombachu.stick.ParsingResult
import com.zombachu.stick.PeekingResult
import com.zombachu.stick.Size
import com.zombachu.stick.Suggestion
import com.zombachu.stick.ValidationContext
import com.zombachu.stick.isSuccess
import com.zombachu.stick.propagateError
import com.zombachu.stick.valueOrPropagateError

internal sealed class Signature<E : Environment, S, T_ : Arguments>(elements: List<Element<E, S, Any?>>) {

    private val flags: List<IndexedElement<E, S, Flag<E, S, Any?>>>
    private val linearElements: List<IndexedElement<E, S, Element<E, S, Any?>>>
    private val trailingOptionals: OptionalsImpl<E, S, *>? = elements.lastOrNull() as? OptionalsImpl<E, S, *>

    private val elementsCount: Int = elements.size
    private val flattenedElementsCount: Int = elementsCount + (trailingOptionals?.elements?.size ?: 0)

    init {
        val flattenedElements =
            elements.dropLast(if (trailingOptionals == null) 0 else 1).mapIndexed { i, e -> IndexedElement(i, e) } +
                trailingOptionals?.elements.orEmpty().mapIndexed { i, e -> IndexedElement(elementsCount + i, e) }

        val partitioned = flattenedElements.partition { it.element is Flag<*, *, *> }
        @Suppress("UNCHECKED_CAST")
        flags = partitioned.first as List<IndexedElement<E, S, Flag<E, S, Any?>>>
        linearElements = partitioned.second
    }

    context(inv: Invocation<E, S>)
    protected abstract fun executeParsed(parsedValues: List<Any?>): T_

    context(inv: InvocationImpl<E, S>)
    fun execute(): CommandResult<T_> {
        val parsedValues =
            parse().valueOrPropagateError {
                return it
            }
        val parsedValuesTuple = executeParsed(parsedValues)
        return ParsingResult.success(parsedValuesTuple)
    }

    context(validationContext: ValidationContext<E, S>)
    fun getSyntax(): String {
        // unboundedElements should be at most 1
        val (boundedElements, unboundedElements) =
            linearElements
                .map { it.element }
                .filterIsInstance<SyntaxElement<E, S, *>>()
                .partition { it.size is Size.Bounded }
        val syntax =
            boundedElements.getSyntaxes() + flags.map { it.element }.getSyntaxes() + unboundedElements.getSyntaxes()
        return syntax.filter { it.isNotEmpty() }.joinToString(" ")
    }

    context(validationContext: ValidationContext<E, S>)
    private fun List<SyntaxElement<E, S, *>>.getSyntaxes(): List<String> {
        return filter { it.validateSender().isSuccess() }.map { it.getSyntax() }
    }

    context(validationContext: ValidationContext<E, S>)
    fun suggest(preceding: List<String>, partial: String): List<Suggestion> {
        return SuggestionProcessor(preceding).process().flatMap { candidate ->
            val element = candidate.element
            val window = preceding.subList(candidate.startIndex, preceding.size)
            if (element is GroupImpl<E, S, *, *>) {
                element.suggest(window, partial, candidate.groupMatch)
            } else {
                element.suggest(window, partial)
            }
        }
    }

    context(inv: InvocationImpl<E, S>)
    private fun parseElement(
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
        val values: MutableList<Any?> = MutableList(flattenedElementsCount) {}
        val unprocessedFlags = flags.toMutableList()

        processElements(
                unprocessedFlags,
                processFlag = { flag ->
                    parseElement(values, flag).propagateError {
                        when (it) {
                            // Ignore matching errors
                            is ParsingResult.TypeNotMatchedInternal,
                            is PeekingResult.InvalidSizeError -> return@processElements false
                            // If the flag matched and an error occurred in parsing then propagate it up
                            else -> return it
                        }
                    }
                    true
                },
                processLinear = { element ->
                    parseElement(values, element).propagateError {
                        return if (it is PeekingResult.InvalidSizeError || it is ParsingResult.TypeNotMatchedInternal) {
                            ParsingResult.failSyntax(inv.getSyntax())
                        } else {
                            it
                        }
                    }
                },
            )
            .propagateError {
                return it
            }

        // If there are unused args then the sender used invalid syntax
        if (inv.unparsed.isNotEmpty()) return ParsingResult.failSyntax(inv.getSyntax())

        // Populate unused flag values with defaults
        for ((index, flag) in unprocessedFlags) {
            values[index] =
                flag.default(inv).valueOrPropagateError {
                    return it
                }
        }

        // Set Arguments value for optionals
        trailingOptionals?.let {
            values[elementsCount - 1] = it.combine(values.subList(elementsCount, flattenedElementsCount))
        }
        return ParsingResult.success(values.subList(0, elementsCount))
    }

    context(validationContext: ValidationContext<E, S>)
    private inline fun processElements(
        unprocessedFlags: MutableList<IndexedElement<E, S, Flag<E, S, Any?>>>,
        processFlag: (IndexedElement<E, S, Flag<E, S, Any?>>) -> Boolean,
        processLinear: (IndexedElement<E, S, Element<E, S, Any?>>) -> Unit,
    ): CommandResult<Unit> {
        // Flags may appear in any order, so attempt to process them around each element
        for (linear in linearElements) {
            processFlags(unprocessedFlags, processFlag)
            linear.element.validateSender().propagateError {
                return it
            }
            processLinear(linear)
        }
        processFlags(unprocessedFlags, processFlag)
        return ParsingResult.success(Unit)
    }

    context(validationContext: ValidationContext<E, S>)
    private inline fun processFlags(
        unprocessedFlags: MutableList<IndexedElement<E, S, Flag<E, S, Any?>>>,
        processFlag: (IndexedElement<E, S, Flag<E, S, Any?>>) -> Boolean,
    ) {
        do {
            var progressed = false
            val flagsIt = unprocessedFlags.iterator()
            while (flagsIt.hasNext()) {
                val indexedFlag = flagsIt.next()
                indexedFlag.element.validateSender().propagateError { continue }
                if (processFlag(indexedFlag)) {
                    flagsIt.remove()
                    progressed = true
                }
            }
        } while (progressed)
    }

    private data class IndexedElement<E : Environment, S, out L : Element<E, S, *>>(val index: Int, val element: L)

    private inner class SuggestionProcessor(private val preceding: List<String>) {
        private val unprocessedFlags: MutableList<IndexedElement<E, S, Flag<E, S, Any?>>> = flags.toMutableList()
        private var index: Int = 0
        private var openCandidate: Candidate? = null

        context(validationContext: ValidationContext<E, S>)
        private fun matchElement(element: SyntaxElement<E, S, *>): MatchResult {
            val window = preceding.subList(index, preceding.size)
            val groupMatch = if (element is GroupImpl<E, S, *, *>) element.matchBranches(window) else null
            val match = groupMatch?.result ?: element.match(window)
            if (match is MatchResult.Matched) {
                openCandidate = if (match.canConsumeMore) Candidate(element, index, groupMatch) else null
                index += match.consumed
            } else if (match is MatchResult.Partial) {
                openCandidate = Candidate(element, index, groupMatch)
                index = preceding.size
                unprocessedFlags.clear()
            }
            return match
        }

        context(validationContext: ValidationContext<E, S>)
        private fun matchElements(): SyntaxElement<E, S, *>? {
            processElements(
                    unprocessedFlags,
                    processFlag = { (_, flag) ->
                        if (index == preceding.size) false
                        else
                            when (matchElement(flag)) {
                                is MatchResult.Matched -> true
                                is MatchResult.Partial -> return null
                                is MatchResult.Unmatched -> false
                            }
                    },
                    processLinear = { (_, element) ->
                        // Helpers can't make suggestions
                        if (element is SyntaxElement) {
                            if (index == preceding.size) return element
                            if (matchElement(element) !is MatchResult.Matched) return null
                        }
                    },
                )
                .propagateError {
                    openCandidate = null
                    unprocessedFlags.clear()
                    return null
                }
            return null
        }

        context(validationContext: ValidationContext<E, S>)
        fun process(): List<Candidate> {
            val nextLinear: SyntaxElement<E, S, *>? = matchElements()
            if (index != preceding.size) return []
            return buildList {
                openCandidate?.let { add(it) }
                for ((_, flag) in unprocessedFlags) {
                    flag.validateSender().propagateError { continue }
                    add(Candidate(flag, index))
                }
                if (nextLinear != null) add(Candidate(nextLinear, index))
            }
        }

        inner class Candidate(
            val element: SyntaxElement<E, S, *>,
            val startIndex: Int,
            val groupMatch: GroupMatch? = null,
        )
    }
}
