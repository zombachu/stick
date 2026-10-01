package com.zombachu.stick.element

import com.zombachu.stick.Aliasable
import com.zombachu.stick.ConsumingResult
import com.zombachu.stick.Environment
import com.zombachu.stick.Execution
import com.zombachu.stick.ExecutionImpl
import com.zombachu.stick.HybridFlagResult
import com.zombachu.stick.Invocation
import com.zombachu.stick.MatchResult
import com.zombachu.stick.Size
import com.zombachu.stick.Suggestion
import com.zombachu.stick.commit
import com.zombachu.stick.consuming
import com.zombachu.stick.noMatch
import com.zombachu.stick.propagateFailure
import com.zombachu.stick.success
import com.zombachu.stick.suggestAliases

internal class HybridFlagImpl<E : Environment, S, T>(
    override val name: String,
    private val parameter: Parameter.Bounded<E, S, T>,
    aliases: Set<String>,
) : HybridFlag<E, S, T>, InternalConsumingElement<E, S, HybridFlagResult<T>>, Aliasable {

    override val size: Size.Bounded = Size.between(1, 1 + parameter.size.max)
    override val description: String = parameter.description
    override val label: String = "-${name.lowercase()}"
    override val aliases: Set<String> = aliases.map { "-$it" }.toSet()

    context(inv: Invocation<E, S>)
    override fun match(args: List<String>): MatchResult {
        if (args.isEmpty()) return MatchResult.partial()
        if (!matches(args.first().lowercase())) return MatchResult.unmatched()
        if (args.size == 1) return MatchResult.matchedAtLeast(1)
        return parameter.match(args.subList(1, args.size)).includeLabelClaimedBy(this)
    }

    context(inv: Invocation<E, S>)
    override fun suggest(preceding: List<String>, partial: String): List<Suggestion> {
        if (preceding.isEmpty()) return suggestAliases()
        if (!matches(preceding.first().lowercase())) return []
        return parameter.suggest(preceding.subList(1, preceding.size), partial)
    }

    context(ex: Execution<E, S>)
    override fun parse(args: List<String>): ConsumingResult<HybridFlagResult<T>> {
        if (args.isEmpty()) return success(HybridFlagResult.Absent<T>()).consuming(0)
        if (matches(args.first().lowercase())) {
            if (args.size == 1) {
                return success(HybridFlagResult.Present<T>()).consuming(1)
            } else {
                val matched = (ex as ExecutionImpl).currentMatch
                if (matched != null && matched.resolvedBy === this) {
                    @Suppress("UNCHECKED_CAST")
                    return success(HybridFlagResult.Value(matched.resolved as T)).consuming(matched.consumed)
                }
                val result = parameter.parse(args.subList(1, args.size))
                result.propagateFailure {
                    return it.commit()
                }
                return success(HybridFlagResult.Value(result.value)).consuming(1 + result.consumed)
            }
        }
        return noMatch()
    }

    context(inv: Invocation<E, S>)
    override fun getSyntax(): String = "[$label [${parameter.getGroupedSyntax()}]]"
}
