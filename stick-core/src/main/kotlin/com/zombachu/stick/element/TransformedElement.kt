package com.zombachu.stick.element

import com.zombachu.stick.Arguments
import com.zombachu.stick.CommandResult
import com.zombachu.stick.ConsumingResult
import com.zombachu.stick.ContextualValue
import com.zombachu.stick.Environment
import com.zombachu.stick.HybridFlagResult
import com.zombachu.stick.Invocation
import com.zombachu.stick.InvocationImpl
import com.zombachu.stick.MatchResult
import com.zombachu.stick.ParsingResult
import com.zombachu.stick.Position
import com.zombachu.stick.Requirement
import com.zombachu.stick.SenderValidator
import com.zombachu.stick.Size
import com.zombachu.stick.Suggestion
import com.zombachu.stick.ValidationContext
import com.zombachu.stick.forSender
import com.zombachu.stick.isSuccess
import com.zombachu.stick.propagateError

internal class TransformedParameter<E : Environment, S : Any, S2 : Any, T, P : Position>(
    val base: Parameter<E, S2, T, P>,
    val transform: (S) -> S2,
    val requirement: Requirement<E, S>,
) : ValidatedParameter<E, S, T, P>, InternalConsumingElement<E, S, T>, SenderValidator<E, S> {

    override val size: Size = base.size
    override val type: GroupableType = base.type
    override val name: String = base.name
    override val description: String = base.description

    context(validationContext: ValidationContext<E, S>)
    override fun match(args: List<String>): MatchResult {
        val transformedValidationContext = validationContext.forSender(transform)
        context(transformedValidationContext) {
            return base.match(args)
        }
    }

    context(validationContext: ValidationContext<E, S>)
    override fun suggest(preceding: List<String>, partial: String): List<Suggestion> {
        val transformedValidationContext = validationContext.forSender(transform)
        context(transformedValidationContext) {
            return base.suggest(preceding, partial)
        }
    }

    context(inv: Invocation<E, S>)
    override fun parse(args: List<String>): ConsumingResult<T> {
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
) : ValueFlag<E, S, T>, InternalConsumingElement<E, S, T> {

    override val default: ContextualValue<E, S, T> = {
        if (validateSender().isSuccess()) {
            val transformedInvocation = (this as InvocationImpl).forSender(transform)
            base.default(transformedInvocation)
        } else {
            invalidSenderDefault.value(this)
        }
    }

    override val size: Size.Bounded = base.size
    override val name: String = base.name
    override val description: String = base.description

    context(validationContext: ValidationContext<E, S>)
    override fun match(args: List<String>): MatchResult {
        val transformedValidationContext = validationContext.forSender(transform)
        context(transformedValidationContext) {
            return base.match(args)
        }
    }

    context(validationContext: ValidationContext<E, S>)
    override fun suggest(preceding: List<String>, partial: String): List<Suggestion> {
        val transformedValidationContext = validationContext.forSender(transform)
        context(transformedValidationContext) {
            return base.suggest(preceding, partial)
        }
    }

    context(inv: Invocation<E, S>)
    override fun parse(args: List<String>): ConsumingResult<T> {
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
) : HybridFlag<E, S, T>, InternalConsumingElement<E, S, HybridFlagResult<T>> {

    override val size: Size.Bounded = base.size
    override val name: String = base.name
    override val description: String = base.description
    override val default: ContextualValue<E, S, HybridFlagResult<T>> = {
        if (validateSender().isSuccess()) {
            ParsingResult.success(HybridFlagResult.Absent())
        } else {
            invalidSenderDefault.value(this)
        }
    }

    context(validationContext: ValidationContext<E, S>)
    override fun match(args: List<String>): MatchResult {
        val transformedValidationContext = validationContext.forSender(transform)
        context(transformedValidationContext) {
            return base.match(args)
        }
    }

    context(validationContext: ValidationContext<E, S>)
    override fun suggest(preceding: List<String>, partial: String): List<Suggestion> {
        val transformedValidationContext = validationContext.forSender(transform)
        context(transformedValidationContext) {
            return base.suggest(preceding, partial)
        }
    }

    context(inv: Invocation<E, S>)
    override fun parse(args: List<String>): ConsumingResult<HybridFlagResult<T>> {
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
    base: Structure<E, S2, T_>,
    transform: (S) -> S2,
    requirement: Requirement<E, S>,
) : TransformedBranch<E, S, S2, T_>(base, transform, requirement), Structure<E, S, T_> {
    override val label: String = base.label
    override val aliases: Set<String> = base.aliases
}

internal open class TransformedBranch<E : Environment, S, S2 : Any, T_ : Arguments>(
    base: Branch<E, S2, T_>,
    private val transform: (S) -> S2,
    private val requirement: Requirement<E, S>,
) : InternalBranch<E, S, T_>, SenderValidator<E, S> {

    private val base: InternalBranch<E, S2, T_> = base as InternalBranch<E, S2, T_>

    override val name: String = base.name
    override val description: String = base.description
    override val size: Size = base.size
    override val type: GroupableType = base.type

    context(validationContext: ValidationContext<E, S>)
    override fun match(args: List<String>): MatchResult {
        val transformedValidationContext = validationContext.forSender(transform)
        context(transformedValidationContext) {
            return base.match(args)
        }
    }

    context(validationContext: ValidationContext<E, S>)
    override fun suggestBranch(
        preceding: List<String>,
        partial: String,
        leadingParameterMatch: MatchResult?,
    ): List<Suggestion> {
        val transformedValidationContext = validationContext.forSender(transform)
        context(transformedValidationContext) {
            return base.suggestBranch(preceding, partial, leadingParameterMatch)
        }
    }

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
    override fun getGroupedSyntax(): String {
        val transformedValidationContext = validationContext.forSender(transform)
        context(transformedValidationContext) {
            return base.getGroupedSyntax()
        }
    }

    context(validationContext: ValidationContext<E, S>)
    override fun validateSender(): CommandResult<Unit> {
        requirement.validateSender().propagateError {
            return it
        }
        val transformedValidationContext = validationContext.forSender(transform)
        context(transformedValidationContext) {
            return base.validateSender()
        }
    }
}
