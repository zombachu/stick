package com.zombachu.stick.element

import com.zombachu.stick.Aliasable
import com.zombachu.stick.ContextualValue
import com.zombachu.stick.ExecutionContext
import com.zombachu.stick.ParsingResult
import com.zombachu.stick.Result
import com.zombachu.stick.SenderContext
import com.zombachu.stick.TypedIdentifier
import com.zombachu.stick.impl.Size

internal open class FlagImpl<O, S : SenderContext<O>, T : Any>(
    val default: ContextualValue<O, S, T>,
    val flagParameter: FlagParameter<O, S, T>,
): Flag<O, S, T> {

    override val size: Size = flagParameter.size
    override val type: ElementType = ElementType.Flag
    override val id: TypedIdentifier<out T> = flagParameter.id
    override val description: String = flagParameter.description

    override fun parse(context: ExecutionContext<O, S>, args: List<String>): Result<out T> {
        return flagParameter.parse(context, args)
    }

    override fun getSyntax(senderContext: S): String = flagParameter.getSyntax(senderContext)
}

internal sealed class FlagParameter<O, S : SenderContext<O>, T : Any>(
    size: Size.Fixed,
    id: TypedIdentifier<T>,
    aliases: Set<String>,
    description: String,
) : Parameter.FixedSize<O, S, T>(size, id, description), Aliasable {

    override val label: String = "-${id.name}"
    override val aliases: Set<String> = aliases.map { "-$it" }.toSet()

    internal class PresenceFlagParameter<O, S : SenderContext<O>, T : Any>(
        id: TypedIdentifier<T>,
        val presentValue: ContextualValue<O, S, T>,
        aliases: Set<String>,
        description: String,
    ) : FlagParameter<O, S, T>(Size.Companion(1), id, aliases, description) {
        override fun parse(context: ExecutionContext<O, S>, args: List<String>): Result<out T> {
            if (matches(args[0].lowercase())) {
                return ParsingResult.success(context.presentValue())
            }
            return ParsingResult.failTypeInternal()
        }

        override fun getSyntax(senderContext: S): String = "[$label]"
    }

    internal class ValueFlagParameter<O, S : SenderContext<O>, T : Any>(
        id: TypedIdentifier<T>,
        val valueElement: FixedSize<O, S, T>,
        aliases: Set<String>,
        description: String,
    ) : FlagParameter<O, S, T>(Size.Companion(1) + valueElement.size, id, aliases, description) {
        override fun parse(context: ExecutionContext<O, S>, args: List<String>): Result<out T> {
            if (matches(args[0].lowercase())) {
                return valueElement.parse(context, args.subList(1, args.size))
            }
            return ParsingResult.failTypeInternal()
        }

        override fun getSyntax(senderContext: S): String = "[$label ${valueElement.getSyntax(senderContext)}]"
    }
}
