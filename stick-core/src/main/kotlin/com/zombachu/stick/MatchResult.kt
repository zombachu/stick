package com.zombachu.stick

sealed interface MatchResult {

    @ConsistentCopyVisibility data class Matched internal constructor(val consumed: Int) : MatchResult

    @ConsistentCopyVisibility data class Partial internal constructor(val matched: Int) : MatchResult

    @ConsistentCopyVisibility
    data class Unmatched internal constructor(val failure: CommandResult.InternalFailure) : MatchResult

    companion object {
        private val silent: Unmatched = Unmatched(ParsingResult.failTypeInternal())

        fun matched(consumed: Int): Matched = Matched(consumed)

        fun partial(matched: Int): Partial = Partial(matched)

        fun unmatched(): Unmatched = silent

        fun unmatched(failure: CommandResult.InternalFailure): Unmatched = Unmatched(failure)
    }
}

internal fun <T> CommandResult<T>.toMatchResult(consumed: Int): MatchResult =
    handleInternal(onSuccess = { MatchResult.matched(consumed) }, onFailure = { MatchResult.unmatched(it) })

internal fun <T> CommandResult<T>.toMatchResultIn(args: List<String>): MatchResult =
    handleInternal(
        onSuccess = { MatchResult.matched(it.consumed) },
        onFailure = {
            if (it is PeekingResult.InvalidSizeError) MatchResult.partial(args.size) else MatchResult.unmatched(it)
        },
    )
