package com.zombachu.stick.element

import com.zombachu.stick.Aliasable
import com.zombachu.stick.ContextualValue
import com.zombachu.stick.ExecutionContext
import com.zombachu.stick.ParsingResult
import com.zombachu.stick.Result
import com.zombachu.stick.TypedIdentifier
import com.zombachu.stick.impl.Size

internal open class FlagImpl<S, T : Any>(
    val default: ContextualValue<S, T>,
    val flagParameter: FlagParameter<S, T>,
): Flag<S, T> {

    override val size: Size = flagParameter.size
    override val type: ElementType = ElementType.Flag
    override val id: TypedIdentifier<out T> = flagParameter.id
    override val description: String = flagParameter.description

    override fun parse(context: ExecutionContext<S>, args: List<String>): Result<out T> {
        return flagParameter.parse(context, args)
    }

    override fun getSyntax(sender: S): String = flagParameter.getSyntax(sender)
}

internal sealed class FlagParameter<S, T : Any>(
    size: Size.Fixed,
    id: TypedIdentifier<T>,
    aliases: Set<String>,
    description: String,
) : Parameter.FixedSize<S, T>(size, id, description), Aliasable {

    override val label: String = "-${id.name}"
    override val aliases: Set<String> = aliases.map { "-$it" }.toSet()

    internal class PresenceFlagParameter<S, T : Any>(
        id: TypedIdentifier<T>,
        val presentValue: ContextualValue<S, T>,
        aliases: Set<String>,
        description: String,
    ) : FlagParameter<S, T>(Size.Companion(1), id, aliases, description) {
        override fun parse(context: ExecutionContext<S>, args: List<String>): Result<out T> {
            if (matches(args[0].lowercase())) {
                return ParsingResult.success(context.presentValue())
            }
            return ParsingResult.failType()
        }

        override fun getSyntax(sender: S): String = "[$label]"
    }

    internal class ValueFlagParameter<S, T : Any>(
        id: TypedIdentifier<T>,
        val valueElement: FixedSize<S, T>,
        aliases: Set<String>,
        description: String,
    ) : FlagParameter<S, T>(Size.Companion(1) + valueElement.size, id, aliases, description) {
        override fun parse(context: ExecutionContext<S>, args: List<String>): Result<out T> {
            if (matches(args[0].lowercase())) {
                return valueElement.parse(context, args.subList(1, args.size))
            }
            return ParsingResult.failType()
        }

        override fun getSyntax(sender: S): String = "[$label ${valueElement.getSyntax(sender)}]"
    }
}
