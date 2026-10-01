package com.zombachu.stick.element

import com.zombachu.stick.Aliasable
import com.zombachu.stick.CommandResult
import com.zombachu.stick.ConsumingResult
import com.zombachu.stick.ContextualValue
import com.zombachu.stick.Environment
import com.zombachu.stick.Execution
import com.zombachu.stick.Invocation
import com.zombachu.stick.MatchResult
import com.zombachu.stick.Size
import com.zombachu.stick.Suggestion
import com.zombachu.stick.commit
import com.zombachu.stick.consuming
import com.zombachu.stick.element.parameters.EnumParameter
import com.zombachu.stick.noMatch
import com.zombachu.stick.propagateError
import com.zombachu.stick.suggestAliases
import com.zombachu.stick.toSuggestions

internal open class ValueFlagImpl<E : Environment, S, T>(
    override val name: String,
    private val default: ContextualValue<E, S, T>,
    private val flagParameter: FlagParameter<E, S, out T>,
) : ValueFlag<E, S, T>, InternalConsumingElement<E, S, T> {

    override val size: Size.Bounded = flagParameter.size
    override val description: String = flagParameter.description

    context(inv: Invocation<E, S>)
    override fun match(args: List<String>): MatchResult = flagParameter.match(args)

    context(inv: Invocation<E, S>)
    override fun suggest(preceding: List<String>, partial: String): List<Suggestion> =
        flagParameter.suggest(preceding, partial)

    context(ex: Execution<E, S>)
    override fun parse(args: List<String>): ConsumingResult<T> {
        if (args.isEmpty()) return default(ex).consuming(0)
        return flagParameter.parse(args)
    }

    context(inv: Invocation<E, S>)
    override fun getSyntax(): String = flagParameter.getSyntax()
}

internal sealed class FlagParameter<E : Environment, S, T>(
    size: Size.Bounded,
    name: String,
    aliases: Set<String>,
    description: String,
) : Parameter.Bounded<E, S, T>(size, name, description), Aliasable {

    override val label: String = "-${name.lowercase()}"
    override val aliases: Set<String> = aliases.map { "-$it" }.toSet()

    context(inv: Invocation<E, S>)
    override fun suggest(preceding: List<String>, partial: String): List<Suggestion> =
        if (preceding.isEmpty()) suggestAliases() else []

    internal class PresenceFlagParameter<E : Environment, S, T>(
        name: String,
        private val presentValue: Invocation<E, S>.() -> CommandResult<T>,
        aliases: Set<String>,
        description: String,
    ) : FlagParameter<E, S, T>(Size(1), name, aliases, description) {

        context(inv: Invocation<E, S>)
        override fun match(args: List<String>): MatchResult {
            if (args.isEmpty()) return MatchResult.partial()
            if (!matches(args.first().lowercase())) return MatchResult.unmatched()
            return MatchResult.matchedExactly(1)
        }

        context(inv: Invocation<E, S>)
        override fun resolve(args: List<String>): ConsumingResult<T> {
            if (args.isEmpty()) return noMatch()
            if (matches(args.first().lowercase())) {
                return inv.presentValue().consuming(1)
            }
            return noMatch()
        }

        context(inv: Invocation<E, S>)
        override fun getSyntax(): String = "[$label]"
    }

    internal class ParameterFlagParameter<E : Environment, S, T>(
        name: String,
        private val parameter: Parameter.Bounded<E, S, T>,
        aliases: Set<String>,
    ) : FlagParameter<E, S, T>(Size(1) + parameter.size, name, aliases, parameter.description) {

        context(inv: Invocation<E, S>)
        override fun match(args: List<String>): MatchResult {
            if (args.isEmpty()) return MatchResult.partial()
            if (!matches(args.first().lowercase())) return MatchResult.unmatched()
            return parameter.match(args.subList(1, args.size)).includeLabelClaimedBy(this)
        }

        context(inv: Invocation<E, S>)
        override fun suggest(preceding: List<String>, partial: String): List<Suggestion> {
            if (preceding.isEmpty()) return suggestAliases()
            if (!matches(preceding.first().lowercase())) return []
            return parameter.suggest(preceding.subList(1, preceding.size), partial)
        }

        context(inv: Invocation<E, S>)
        override fun resolve(args: List<String>): ConsumingResult<T> {
            if (args.isEmpty()) return noMatch()
            if (matches(args.first().lowercase())) {
                val result = parameter.resolve(args.subList(1, args.size))
                result.propagateError {
                    return it.commit()
                }
                return result.consuming(1 + result.consumed)
            }
            return noMatch()
        }

        context(inv: Invocation<E, S>)
        override fun getSyntax(): String = "[$label ${parameter.getSyntax()}]"
    }

    internal class EnumFlagParameter<E : Environment, S, T : Enum<T>>(
        private val enumParameter: EnumParameter<E, S, T>
    ) :
        FlagParameter<E, S, T>(
            enumParameter.size,
            enumParameter.name,
            enumParameter.primaryValues.keys + enumParameter.aliasedValues.keys,
            enumParameter.description,
        ) {

        private val primaryValues = enumParameter.primaryValues.keys.toList().map { "-$it" }
        private val aliasedValues = enumParameter.aliasedValues.keys.map { "-$it" }

        context(inv: Invocation<E, S>)
        override fun match(args: List<String>): MatchResult {
            val flagArg = args.firstOrNull() ?: return MatchResult.partial()
            if (!flagArg.startsWith("-")) return MatchResult.unmatched()

            // Ignore the - before passing it to the enum parameter
            val match = enumParameter.match(flagArg.substring(1))
            if (match !is MatchResult.Matched) return MatchResult.unmatched()
            return match.claimedBy(this, 1)
        }

        context(inv: Invocation<E, S>)
        override fun suggest(preceding: List<String>, partial: String): List<Suggestion> =
            primaryValues.toSuggestions() + aliasedValues.toSuggestions(isAlias = true)

        context(inv: Invocation<E, S>)
        override fun resolve(args: List<String>): ConsumingResult<T> {
            val flagArg = args.firstOrNull()
            if (flagArg == null || !flagArg.startsWith("-")) return noMatch()

            // Ignore the - before passing it to the enum parameter
            val result = enumParameter.resolve(flagArg.substring(1))
            if (result is CommandResult.Failure.NoMatch) {
                return noMatch()
            }
            return result.consuming(1)
        }

        context(inv: Invocation<E, S>)
        override fun getSyntax(): String = "[${primaryValues.joinToString("|")}]"
    }
}

private fun MatchResult.Matched.claimedBy(element: ConsumingElement<*, *, *>, consumed: Int): MatchResult.Matched =
    if (resolvedBy == null) {
        MatchResult.Matched(consumed, canConsumeMore)
    } else {
        MatchResult.Matched(consumed, canConsumeMore, element, resolved)
    }

internal fun MatchResult.includeLabelClaimedBy(element: ConsumingElement<*, *, *>): MatchResult =
    when (this) {
        is MatchResult.Matched -> claimedBy(element, 1 + consumed)
        is MatchResult.Partial -> this
        is MatchResult.Unmatched -> MatchResult.unmatched(failure.commit())
    }
