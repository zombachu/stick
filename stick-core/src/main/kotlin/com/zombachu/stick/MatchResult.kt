package com.zombachu.stick

import com.zombachu.stick.element.ConsumingElement

sealed interface MatchResult {
    val canConsumeMore: Boolean

    @ConsistentCopyVisibility
    data class Matched internal constructor(val consumed: Int, override val canConsumeMore: Boolean) : MatchResult {

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

    data object Partial : MatchResult {
        override val canConsumeMore = true
    }

    @ConsistentCopyVisibility
    data class Unmatched internal constructor(internal val failure: CommandResult.Failure) : MatchResult {
        override val canConsumeMore = false
    }

    companion object {
        fun matchedExactly(consumed: Int): Matched = Matched(consumed, canConsumeMore = false)

        fun matchedAtLeast(consumed: Int): Matched = Matched(consumed, canConsumeMore = true)

        fun partial(): Partial = Partial

        context(_: Invocation<*, *>)
        fun unmatched(): Unmatched = Unmatched(noMatch())

        fun unmatched(failure: CommandResult.Failure): Unmatched = Unmatched(failure)
    }
}

internal fun <T> ConsumingResult<T>.toMatchResult(element: ConsumingElement<*, *, *>): MatchResult =
    when (this) {
        is ConsumingResult.Success ->
            MatchResult.Matched(consumed, canConsumeMore && !element.size.isFull(consumed), element, value)
        is CommandResult.Failure.NoMatch -> if (incomplete) MatchResult.partial() else MatchResult.unmatched(this)
        is CommandResult.Failure -> MatchResult.unmatched(this)
    }

private fun Size.isFull(consumed: Int): Boolean = this is Size.Bounded && consumed >= max
