package com.zombachu.stick

import com.zombachu.stick.element.ConsumingElement

sealed interface MatchResult {

    @ConsistentCopyVisibility
    data class Matched internal constructor(val consumed: Int, val canConsumeMore: Boolean) : MatchResult {

        internal var resolvedBy: ConsumingElement<*, *, *>? = null
            private set

        internal var resolved: Any? = null
            private set

        internal constructor(
            consumed: Int,
            canConsumeMore: Boolean,
            element: ConsumingElement<*, *, *>,
            value: Any?,
        ) : this(consumed, canConsumeMore) {
            resolvedBy = element
            resolved = value
        }
    }

    data object Partial : MatchResult

    @ConsistentCopyVisibility
    data class Unmatched internal constructor(val failure: CommandResult.InternalFailure) : MatchResult

    companion object {
        private val silent: Unmatched = Unmatched(ParsingResult.failTypeInternal())

        fun matchedExactly(consumed: Int): Matched = Matched(consumed, canConsumeMore = false)

        fun matchedAtLeast(consumed: Int): Matched = Matched(consumed, canConsumeMore = true)

        fun partial(): Partial = Partial

        fun unmatched(): Unmatched = silent

        fun unmatched(failure: CommandResult.InternalFailure): Unmatched = Unmatched(failure)
    }
}

internal fun <T> ConsumingResult<T>.toMatchResult(element: ConsumingElement<*, *, *>): MatchResult =
    when (this) {
        is ConsumingResult.Success ->
            MatchResult.Matched(consumed, canConsumeMore && !element.size.isFull(consumed), element, value)
        is CommandResult.InternalFailure ->
            if (this is PeekingResult.InvalidSizeError) MatchResult.partial() else MatchResult.unmatched(this)
    }

private fun Size.isFull(consumed: Int): Boolean = this is Size.Bounded && consumed >= max
