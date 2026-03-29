package com.zombachu.stick.element

import com.zombachu.stick.Aliasable
import com.zombachu.stick.CommandResult
import com.zombachu.stick.ContextualValue
import com.zombachu.stick.Environment
import com.zombachu.stick.Invocation
import com.zombachu.stick.ParsingResult
import com.zombachu.stick.ParsingResult.LiteralNotMatchedError
import com.zombachu.stick.ValidationContext
import com.zombachu.stick.element.parameters.EnumParameter
import com.zombachu.stick.impl.InvalidSenderDefault
import com.zombachu.stick.impl.InvocationImpl
import com.zombachu.stick.impl.Size

internal open class ValueFlagImpl<E : Environment, S, T>(
    override val name: String,
    override val default: ContextualValue<E, S, T>,
    private val flagParameter: FlagParameter<E, S, T>,
): ValueFlag<E, S, T> {

    override val size: Size = flagParameter.size
    override val type: ElementType = ElementType.Flag
    override val description: String = flagParameter.description

    context(inv: Invocation<E, S>)
    override fun parse(args: List<String>): CommandResult<T> {
        return flagParameter.parse(args)
    }

    context(validationContext: ValidationContext<E, S>)
    override fun getSyntax(): String = flagParameter.getSyntax()
}

internal sealed class FlagParameter<E : Environment, S, T>(
    size: Size.Fixed,
    name: String,
    aliases: Set<String>,
    description: String,
) : Parameter.FixedSize<E, S, T>(size, name, description), Aliasable {

    override val label: String = "-${name}"
    override val aliases: Set<String> = aliases.map { "-$it" }.toSet()

    internal class PresenceFlagParameter<E : Environment, S, T>(
        name: String,
        private val presentValue: ContextualValue<E, S, T>,
        aliases: Set<String>,
        description: String,
    ) : FlagParameter<E, S, T>(Size(1), name, aliases, description) {

        context(inv: Invocation<E, S>)
        override fun parse(args: List<String>): CommandResult<T> {
            if (matches(args[0].lowercase())) {
                return inv.presentValue()
            }
            return ParsingResult.failTypeInternal()
        }

        context(validationContext: ValidationContext<E, S>)
        override fun getSyntax(): String = "[$label]"
    }

    internal class ParameterFlagParameter<E : Environment, S, T>(
        private val parameter: FixedSize<E, S, T>,
        aliases: Set<String>,
    ) : FlagParameter<E, S, T>(Size(1) + parameter.size, parameter.name, aliases, parameter.description) {

        context(inv: Invocation<E, S>)
        override fun parse(args: List<String>): CommandResult<T> {
            if (matches(args[0].lowercase())) {
                return parameter.parse(args.subList(1, args.size))
            }
            return ParsingResult.failTypeInternal()
        }

        context(validationContext: ValidationContext<E, S>)
        override fun getSyntax(): String = "[$label ${parameter.getSyntax()}]"
    }

    internal class MultiFlagParameter<E : Environment, S, T : Enum<T>>(
        private val enumParameter: EnumParameter<E, S, T>,
    ) : FlagParameter<E, S, T>(
        enumParameter.size,
        enumParameter.name,
        enumParameter.primaryValues.keys + enumParameter.aliasedValues.keys,
        enumParameter.description,
    ) {
        private val primaryValues = enumParameter.primaryValues.keys.toList().map { "-$it" }

        context(inv: Invocation<E, S>)
        override fun parse(args: List<String>): CommandResult<T> {
            // Ignore the - before passing it to the enum parameter
            val arg = args.first().substring(1)
            val result = enumParameter.parse(arg)
            // Override the failed syntax message
            if (result is LiteralNotMatchedError) {
                return ParsingResult.failLiteral(primaryValues, args.first())
            }
            return result
        }

        context(validationContext: ValidationContext<E, S>)
        override fun getSyntax(): String = "[${primaryValues.joinToString("|")}]"
    }
}

internal class TransformedValueFlag<E : Environment, S, S2 : Any, T>(
    private val base: ValueFlag<E, S2, T>,
    private val transform: (S) -> S2,
    private val invalidSenderDefault: InvalidSenderDefault<E, S, T>,
) : ValueFlag<E, S, T>, Flag.Validated<E, S, T> {

    override val default: ContextualValue<E, S, T> = {
        val transformedInvocation = (this as InvocationImpl).forSender(transform)
        base.default(transformedInvocation)
    }

    override val size: Size = base.size
    override val type: ElementType = ElementType.Flag
    override val name: String = base.name
    override val description: String = base.description
    override val invalidDefault: ContextualValue<E, S, T> = invalidSenderDefault.value

    context(inv: Invocation<E, S>)
    override fun parse(args: List<String>): CommandResult<T> {
        val transformedInvocation = (inv as InvocationImpl).forSender(transform)
        context(transformedInvocation) {
            return base.parse(args)
        }
    }

    context(validationContext: ValidationContext<E, S>)
    override fun getSyntax(): String {
        val transformedValidationContext = validationContext.forSender(transform)
        context(transformedValidationContext) {
            return base.getSyntax()
        }
    }

    context(validationContext: ValidationContext<E, S>)
    override fun validateSender(): CommandResult<Unit> = invalidSenderDefault.validateSender()
}
