package com.zombachu.stick.element

import com.zombachu.stick.Aliasable
import com.zombachu.stick.ConsumingResult
import com.zombachu.stick.ContextualValue
import com.zombachu.stick.Environment
import com.zombachu.stick.HybridFlagResult
import com.zombachu.stick.Invocation
import com.zombachu.stick.InvocationImpl
import com.zombachu.stick.MatchResult
import com.zombachu.stick.ParsingResult
import com.zombachu.stick.Size
import com.zombachu.stick.Suggestion
import com.zombachu.stick.ValidationContext
import com.zombachu.stick.consuming
import com.zombachu.stick.propagateError
import com.zombachu.stick.suggestAliases

internal open class HybridFlagImpl<E : Environment, S, T>(
    override val name: String,
    private val parameter: Parameter.Bounded<E, S, T>,
    aliases: Set<String>,
) : HybridFlag<E, S, T>, Aliasable {

    override val size: Size.Bounded = Size.between(1, 1 + parameter.size.max)
    override val description: String = parameter.description
    override val default: ContextualValue<E, S, HybridFlagResult<T>> = {
        ParsingResult.success(HybridFlagResult.Absent())
    }
    override val label: String = "-${name.lowercase()}"
    override val aliases: Set<String> = aliases.map { "-$it" }.toSet()

    context(validationContext: ValidationContext<E, S>)
    override fun match(args: List<String>): MatchResult {
        if (args.isEmpty()) return MatchResult.partial()
        if (!matches(args.first().lowercase())) return MatchResult.unmatched()
        if (args.size == 1) return MatchResult.matchedAtLeast(1)
        return parameter.match(args.subList(1, args.size)).includeLabelClaimedBy(this)
    }

    context(validationContext: ValidationContext<E, S>)
    override fun suggest(preceding: List<String>, partial: String): List<Suggestion> {
        if (preceding.isEmpty()) return suggestAliases()
        if (!matches(preceding.first().lowercase())) return []
        return parameter.suggest(preceding.subList(1, preceding.size), partial)
    }

    context(inv: Invocation<E, S>)
    override fun parse(args: List<String>): ConsumingResult<HybridFlagResult<T>> {
        if (args.isEmpty()) return ParsingResult.failTypeInternal()
        if (matches(args.first().lowercase())) {
            if (args.size == 1) {
                return ParsingResult.success(HybridFlagResult.Present<T>()).consuming(1)
            } else {
                val matched = (inv as InvocationImpl).currentMatch
                if (matched != null && matched.resolvedBy === this) {
                    @Suppress("UNCHECKED_CAST")
                    return ParsingResult.success(HybridFlagResult.Value(matched.resolved as T))
                        .consuming(matched.consumed)
                }
                val result = parameter.parse(args.subList(1, args.size))
                result.propagateError {
                    return it
                }
                return ParsingResult.success(HybridFlagResult.Value(result.value)).consuming(1 + result.consumed)
            }
        }
        return ParsingResult.failTypeInternal()
    }

    context(validationContext: ValidationContext<E, S>)
    override fun getSyntax(): String = "[$label [${parameter.getGroupedSyntax()}]]"
}
