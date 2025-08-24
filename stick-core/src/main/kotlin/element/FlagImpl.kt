package com.zombachu.stick.element

import com.zombachu.stick.Aliasable
import com.zombachu.stick.ContextualValue
import com.zombachu.stick.ExecutionContext
import com.zombachu.stick.ParsingResult
import com.zombachu.stick.Result
import com.zombachu.stick.SenderContext
import com.zombachu.stick.TypedIdentifier
import com.zombachu.stick.impl.Size

internal open class FlagImpl<S : SenderContext, T : Any>(
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

    override fun getSyntax(senderContext: S): String = flagParameter.getSyntax(senderContext)
}

internal sealed class FlagParameter<S : SenderContext, T : Any>(
    size: Size.Fixed,
    id: TypedIdentifier<T>,
    aliases: Set<String>,
    description: String,
) : Parameter.FixedSize<S, T>(size, id, description), Aliasable {

    override val label: String = "-${id.name}"
    override val aliases: Set<String> = aliases.map { "-$it" }.toSet()

    internal class PresenceFlagParameter<S : SenderContext, T : Any>(
        id: TypedIdentifier<T>,
        val presentValue: ContextualValue<S, T>,
        aliases: Set<String>,
        description: String,
    ) : FlagParameter<S, T>(Size.Companion(1), id, aliases, description) {
        override fun parse(context: ExecutionContext<S>, args: List<String>): Result<out T> {
            if (matches(args[0].lowercase())) {
                return ParsingResult.success(context.presentValue())
            }
            return ParsingResult.failTypeInternal()
        }

        override fun getSyntax(senderContext: S): String = "[$label]"
    }

    internal class ValueFlagParameter<S : SenderContext, T : Any>(
        id: TypedIdentifier<T>,
        val valueElement: FixedSize<S, T>,
        aliases: Set<String>,
        description: String,
    ) : FlagParameter<S, T>(Size.Companion(1) + valueElement.size, id, aliases, description) {
        override fun parse(context: ExecutionContext<S>, args: List<String>): Result<out T> {
            if (matches(args[0].lowercase())) {
                return valueElement.parse(context, args.subList(1, args.size))
            }
            return ParsingResult.failTypeInternal()
        }

        override fun getSyntax(senderContext: S): String = "[$label ${valueElement.getSyntax(senderContext)}]"
    }
}
