package com.zombachu.stick.element

import com.zombachu.stick.Arguments
import com.zombachu.stick.CommandResult
import com.zombachu.stick.ContextualValue
import com.zombachu.stick.Environment
import com.zombachu.stick.HybridFlagResult
import com.zombachu.stick.Invocation
import com.zombachu.stick.InvocationImpl
import com.zombachu.stick.ParsingResult
import com.zombachu.stick.Position
import com.zombachu.stick.Requirement
import com.zombachu.stick.SenderValidator
import com.zombachu.stick.Size
import com.zombachu.stick.ValidationContext

internal class TransformedParameter<E : Environment, S : Any, S2 : Any, T, P : Position>(
    val base: Parameter<E, S2, T, P>,
    val transform: (S) -> S2,
    val requirement: Requirement<E, S>,
) : ValidatedParameter<E, S, T, P>, SenderValidator<E, S> {

    override val size: Size = base.size
    override val type: ElementType = base.type
    override val name: String = base.name
    override val description: String = base.description

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
    override fun validateSender(): CommandResult<Unit> = requirement.validateSender()
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

internal class TransformedHybridFlag<E : Environment, S, S2 : Any, T>(
    private val base: HybridFlag<E, S2, T>,
    private val transform: (S) -> S2,
    private val invalidSenderDefault: InvalidSenderDefault<E, S, HybridFlagResult<T>>,
) : HybridFlag<E, S, T>, Flag.Validated<E, S, HybridFlagResult<T>> {

    override val size: Size = base.size
    override val type: ElementType = ElementType.Flag
    override val name: String = base.name
    override val description: String = base.description
    override val invalidDefault: ContextualValue<E, S, HybridFlagResult<T>> = invalidSenderDefault.value
    override val default: ContextualValue<E, S, HybridFlagResult<T>> = {
        ParsingResult.success(HybridFlagResult.Absent())
    }

    context(inv: Invocation<E, S>)
    override fun parse(args: List<String>): CommandResult<HybridFlagResult<T>> {
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

internal class TransformedStructure<E : Environment, S, S2 : Any, T_ : Arguments>(
    private val base: Structure<E, S2, T_>,
    private val transform: (S) -> S2,
    private val requirement: Requirement<E, S>,
) : Structure<E, S, T_> {

    override val name: String = base.name
    override val aliases: Set<String> = base.aliases
    override val description: String = base.description
    override val size: Size = base.size
    override val type: ElementType = base.type
    override val label: String = base.label

    context(inv: Invocation<E, S>)
    override fun parse(args: List<String>): CommandResult<T_> {
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
    override fun validateSender(): CommandResult<Unit> = requirement.validateSender()
}
