package com.zombachu.stick.impl

import com.zombachu.stick.ContextualValue
import com.zombachu.stick.ExecutionContext
import com.zombachu.stick.ParsingResult
import com.zombachu.stick.TypedIdentifier

sealed class FlagParameter<S, T : Any>(
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
    ) : FlagParameter<S, T>(Size(1), id, aliases, description) {
        override fun parse(context: ExecutionContext<S>, args: List<String>): ParsingResult<T> {
            if (matches(args[0].lowercase())) {
                return ParsingResult.success(context.presentValue())
            }
            return ParsingResult.failType()
        }

        override fun getSyntax(sender: S): String = "[${id.name}]"
    }

    internal class ValueFlagParameter<S, T : Any>(
        id: TypedIdentifier<T>,
        val valueElement: FixedSize<S, T>,
        aliases: Set<String>,
        description: String,
    ) : FlagParameter<S, T>(Size(1) + valueElement.size, id, aliases, description) {
        override fun parse(context: ExecutionContext<S>, args: List<String>): ParsingResult<out T> {
            if (matches(args[0].lowercase())) {
                return valueElement.parse(context, args.subList(1, args.size))
            }
            return ParsingResult.failType()
        }

        override fun getSyntax(sender: S): String = "[${id.name} ${super.getSyntax(sender)}]"
    }
}

open class Flag<S, T : Any>(
    val default: ContextualValue<S, T>,
    val flagParameter: FlagParameter<S, T>,
): SyntaxElement<S, T>, SignatureConstraint.NonTerminating<S, T> {

    override val size: Size = flagParameter.size
    override val type: ElementType = ElementType.Flag
    override val id: TypedIdentifier<out T> = flagParameter.id
    override val description: String = flagParameter.description

    override fun parse(context: ExecutionContext<S>, args: List<String>): ParsingResult<out T> {
        return flagParameter.parse(context, args)
    }

    override fun getSyntax(sender: S): String = flagParameter.getSyntax(sender)
}
