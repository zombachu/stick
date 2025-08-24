package com.zombachu.stick.element

import com.zombachu.stick.Aliasable
import com.zombachu.stick.ContextualValue
import com.zombachu.stick.ExecutionContext
import com.zombachu.stick.ParsingResult
import com.zombachu.stick.Result
import com.zombachu.stick.SenderContext
import com.zombachu.stick.TypedIdentifier
import com.zombachu.stick.impl.Size

internal open class FlagImpl<S : SenderContext, O, T : Any>(
    val default: ContextualValue<S, O, T>,
    val flagParameter: FlagParameter<S, O, T>,
): Flag<S, O, T> {

    override val size: Size = flagParameter.size
    override val type: ElementType = ElementType.Flag
    override val id: TypedIdentifier<out T> = flagParameter.id
    override val description: String = flagParameter.description

    context(senderContext: S, executionContext: ExecutionContext<S, O>)
    override fun parse(args: List<String>): Result<out T> {
        return flagParameter.parse(args)
    }

    context(senderContext: S)
    override fun getSyntax(): String = flagParameter.getSyntax()
}

internal sealed class FlagParameter<S : SenderContext, O, T : Any>(
    size: Size.Fixed,
    id: TypedIdentifier<T>,
    aliases: Set<String>,
    description: String,
) : Parameter.FixedSize<S, O, T>(size, id, description), Aliasable {

    override val label: String = "-${id.name}"
    override val aliases: Set<String> = aliases.map { "-$it" }.toSet()

    internal class PresenceFlagParameter<S : SenderContext, O, T : Any>(
        id: TypedIdentifier<T>,
        val presentValue: ContextualValue<S, O, T>,
        aliases: Set<String>,
        description: String,
    ) : FlagParameter<S, O, T>(Size.Companion(1), id, aliases, description) {

        context(senderContext: S, executionContext: ExecutionContext<S, O>)
        override fun parse(args: List<String>): Result<out T> {
            if (matches(args[0].lowercase())) {
                return ParsingResult.success(executionContext.presentValue())
            }
            return ParsingResult.failTypeInternal()
        }

        context(senderContext: S)
        override fun getSyntax(): String = "[$label]"
    }

    internal class ValueFlagParameter<S : SenderContext, O, T : Any>(
        id: TypedIdentifier<T>,
        val valueElement: FixedSize<S, O, T>,
        aliases: Set<String>,
        description: String,
    ) : FlagParameter<S, O, T>(Size.Companion(1) + valueElement.size, id, aliases, description) {

        context(senderContext: S, executionContext: ExecutionContext<S, O>)
        override fun parse(args: List<String>): Result<out T> {
            if (matches(args[0].lowercase())) {
                return valueElement.parse(args.subList(1, args.size))
            }
            return ParsingResult.failTypeInternal()
        }

        context(senderContext: S)
        override fun getSyntax(): String = "[$label ${valueElement.getSyntax()}]"
    }
}
