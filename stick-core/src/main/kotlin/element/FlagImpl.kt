package com.zombachu.stick.element

import com.zombachu.stick.Aliasable
import com.zombachu.stick.ContextualValue
import com.zombachu.stick.Invocation
import com.zombachu.stick.ParsingResult
import com.zombachu.stick.Result
import com.zombachu.stick.Environment
import com.zombachu.stick.TypedIdentifier
import com.zombachu.stick.impl.Size

internal open class FlagImpl<E : Environment, O, T : Any>(
    val default: ContextualValue<E, O, T>,
    val flagParameter: FlagParameter<E, O, T>,
): Flag<E, O, T> {

    override val size: Size = flagParameter.size
    override val type: ElementType = ElementType.Flag
    override val id: TypedIdentifier<out T> = flagParameter.id
    override val description: String = flagParameter.description

    context(env: E, inv: Invocation<E, O>)
    override fun parse(args: List<String>): Result<out T> {
        return flagParameter.parse(args)
    }

    context(env: E)
    override fun getSyntax(): String = flagParameter.getSyntax()
}

internal sealed class FlagParameter<E : Environment, O, T : Any>(
    size: Size.Fixed,
    id: TypedIdentifier<T>,
    aliases: Set<String>,
    description: String,
) : Parameter.FixedSize<E, O, T>(size, id, description), Aliasable {

    override val label: String = "-${id.name}"
    override val aliases: Set<String> = aliases.map { "-$it" }.toSet()

    internal class PresenceFlagParameter<E : Environment, O, T : Any>(
        id: TypedIdentifier<T>,
        val presentValue: ContextualValue<E, O, T>,
        aliases: Set<String>,
        description: String,
    ) : FlagParameter<E, O, T>(Size.Companion(1), id, aliases, description) {

        context(env: E, inv: Invocation<E, O>)
        override fun parse(args: List<String>): Result<out T> {
            if (matches(args[0].lowercase())) {
                return ParsingResult.success(inv.presentValue())
            }
            return ParsingResult.failTypeInternal()
        }

        context(env: E)
        override fun getSyntax(): String = "[$label]"
    }

    internal class ValueFlagParameter<E : Environment, O, T : Any>(
        id: TypedIdentifier<T>,
        val valueElement: FixedSize<E, O, T>,
        aliases: Set<String>,
        description: String,
    ) : FlagParameter<E, O, T>(Size.Companion(1) + valueElement.size, id, aliases, description) {

        context(env: E, inv: Invocation<E, O>)
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
