package com.zombachu.stick.element

import com.zombachu.stick.Aliasable
import com.zombachu.stick.ContextualValue
import com.zombachu.stick.Environment
import com.zombachu.stick.Invocation
import com.zombachu.stick.ParsingResult
import com.zombachu.stick.Result
import com.zombachu.stick.TypedIdentifier
import com.zombachu.stick.ValidationContext
import com.zombachu.stick.impl.InvocationImpl
import com.zombachu.stick.impl.Requirement
import com.zombachu.stick.impl.Size

internal open class FlagImpl<E : Environment, S, T : Any>(
    open val default: ContextualValue<E, S, T>,
    val flagParameter: FlagParameter<E, S, T>,
): Flag<E, S, T> {

    override val size: Size = flagParameter.size
    override val type: ElementType = ElementType.Flag
    override val id: TypedIdentifier<T> = flagParameter.id
    override val description: String = flagParameter.description

    context(inv: Invocation<E, S>)
    override fun parse(args: List<String>): Result<T> {
        return flagParameter.parse(args)
    }

    context(validationContext: ValidationContext<E, S>)
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

        context(inv: Invocation<E, S>)
        override fun parse(args: List<String>): Result<T> {
            if (matches(args[0].lowercase())) {
                return inv.presentValue()
            }
            return ParsingResult.failTypeInternal()
        }

        context(validationContext: ValidationContext<E, S>)
        override fun getSyntax(): String = "[$label]"
    }

    internal class ValueFlagParameter<E : Environment, S, T : Any>(
        id: TypedIdentifier<T>,
        val valueElement: FixedSize<E, S, T>,
        aliases: Set<String>,
        description: String,
    ) : FlagParameter<E, S, T>(Size.Companion(1) + valueElement.size, id, aliases, description) {

        context(inv: Invocation<E, S>)
        override fun parse(args: List<String>): Result<T> {
            if (matches(args[0].lowercase())) {
                return valueElement.parse(args.subList(1, args.size))
            }
            return ParsingResult.failTypeInternal()
        }

        context(validationContext: ValidationContext<E, S>)
        override fun getSyntax(): String = "[$label ${valueElement.getSyntax()}]"
    }
}

internal class TransformedFlag<E : Environment, S, S2 : Any, T : Any>(
    val base: Flag<E, S2, T>,
    val transform: (S) -> S2,
    val requirement: Requirement<E, S>,
    val invalidDefault: ContextualValue<E, S, T>,
) : FlagImpl<E, S, T>(
    { unusedValue() },
    FlagParameter.PresenceFlagParameter(base.id, { unusedValue() }, setOf(), ""),
), SenderValidator<E, S> {

    override val default: ContextualValue<E, S, T> = {
        val transformedInvocation = (this as InvocationImpl).forSender(transform)
        (base as FlagImpl).default(transformedInvocation)
    }

    override val size: Size = base.size
    override val id: TypedIdentifier<T> = base.id
    override val description: String = base.description

    context(inv: Invocation<E, S>)
    override fun parse(args: List<String>): Result<T> {
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
    override fun validateSender(): Result<Unit> = requirement.validateSender()
}
