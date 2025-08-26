package com.zombachu.stick.element

import com.zombachu.stick.Aliasable
import com.zombachu.stick.ContextualValue
import com.zombachu.stick.Invocation
import com.zombachu.stick.ParsingResult
import com.zombachu.stick.Result
import com.zombachu.stick.Environment
import com.zombachu.stick.TypedIdentifier
import com.zombachu.stick.impl.Size

internal open class FlagImpl<E : Environment, S, T : Any>(
    val default: ContextualValue<E, S, T>,
    val flagParameter: FlagParameter<E, S, T>,
): Flag<E, S, T> {

    override val size: Size = flagParameter.size
    override val type: ElementType = ElementType.Flag
    override val id: TypedIdentifier<out T> = flagParameter.id
    override val description: String = flagParameter.description

    context(env: E, inv: Invocation<E, S>)
    override fun parse(args: List<String>): Result<out T> {
        return flagParameter.parse(args)
    }

    context(env: E)
    override fun getSyntax(): String = flagParameter.getSyntax()
}

internal sealed class FlagParameter<E : Environment, S, T : Any>(
    size: Size.Fixed,
    id: TypedIdentifier<T>,
    aliases: Set<String>,
    description: String,
) : Parameter.FixedSize<E, S, T>(size, id, description), Aliasable {

    override val label: String = "-${id.name}"
    override val aliases: Set<String> = aliases.map { "-$it" }.toSet()

    internal class PresenceFlagParameter<E : Environment, S, T : Any>(
        id: TypedIdentifier<T>,
        val presentValue: ContextualValue<E, S, T>,
        aliases: Set<String>,
        description: String,
    ) : FlagParameter<E, S, T>(Size.Companion(1), id, aliases, description) {

        context(env: E, inv: Invocation<E, S>)
        override fun parse(args: List<String>): Result<out T> {
            if (matches(args[0].lowercase())) {
                return ParsingResult.success(inv.presentValue())
            }
            return ParsingResult.failTypeInternal()
        }

        context(env: E)
        override fun getSyntax(): String = "[$label]"
    }

    internal class ValueFlagParameter<E : Environment, S, T : Any>(
        id: TypedIdentifier<T>,
        val valueElement: FixedSize<E, S, T>,
        aliases: Set<String>,
        description: String,
    ) : FlagParameter<E, S, T>(Size.Companion(1) + valueElement.size, id, aliases, description) {

        context(env: E, inv: Invocation<E, S>)
        override fun parse(args: List<String>): Result<out T> {
            if (matches(args[0].lowercase())) {
                return valueElement.parse(args.subList(1, args.size))
            }
            return ParsingResult.failTypeInternal()
        }

        context(env: E)
        override fun getSyntax(): String = "[$label ${valueElement.getSyntax()}]"
    }
}
