package com.zombachu.stick.element

import com.zombachu.stick.ContextualValue
import com.zombachu.stick.Environment
import com.zombachu.stick.Invocation
import com.zombachu.stick.Result
import com.zombachu.stick.SenderValidationResult
import com.zombachu.stick.TypedIdentifier
import com.zombachu.stick.ValidationContext
import com.zombachu.stick.impl.InvocationImpl
import com.zombachu.stick.impl.Requirement
import com.zombachu.stick.impl.Size

interface SenderValidator<E : Environment, S> {
    context(validationContext: ValidationContext<E, S>)
    fun validateSender(): Result<Unit>
}

context(validationContext: ValidationContext<E, S>)
internal fun <E : Environment, S, T : Any> SyntaxElement<E, S, T>.validateSender(): Result<Unit> {
    return if (this !is SenderValidator<*, *>) {
        SenderValidationResult.success()
    } else {
        @Suppress("UNCHECKED_CAST")
        (this as SenderValidator<E, S>).validateSender()
    }
}

internal class ValidatedParameterImpl<E : Environment, S : Any, S2 : Any, T : Any>(
    val parameter: Parameter<E, S2, T>,
    val requirement: Requirement<E, S>,
    val transform: (S) -> S2,
) : ValidatedParameter<E, S, T>, SenderValidator<E, S> {

    override val size: Size = parameter.size
    override val type: ElementType = parameter.type
    override val id: TypedIdentifier<T> = parameter.id
    override val description: String = parameter.description

    context(inv: Invocation<E, S>)
    override fun parse(args: List<String>): Result<T> {
        val newInvocation = (inv as InvocationImpl).forSender(transform)
        context(newInvocation) {
            return parameter.parse(args)
        }
    }

    context(validationContext: ValidationContext<E, S>)
    override fun getSyntax(): String {
        val newValidationContext = validationContext.forSender(transform)
        context(newValidationContext) {
           return parameter.getSyntax()
        }
    }

    context(validationContext: ValidationContext<E, S>)
    override fun validateSender(): Result<Unit> = requirement.validateSender()
}

internal class ValidatedFlag<E : Environment, S, S2 : Any, T : Any>(
    val flag: Flag<E, S2, T>,
    val requirement: Requirement<E, S>,
    val invalidDefault: ContextualValue<E, S, T>,
    val transform: (S) -> S2,
) : FlagImpl<E, S, T>(
    { placeholderValue() },
    FlagParameter.PresenceFlagParameter(flag.id, { placeholderValue() }, setOf(), ""),
), SenderValidator<E, S> {

    override val default: ContextualValue<E, S, T> = {
        val newInvocation = (this as InvocationImpl).forSender(transform)
        (flag as FlagImpl).default(newInvocation)
    }

    override val size: Size = flag.size
    override val id: TypedIdentifier<T> = flag.id
    override val description: String = flag.description

    context(inv: Invocation<E, S>)
    override fun parse(args: List<String>): Result<T> {
        val newInvocation = (inv as InvocationImpl).forSender(transform)
        context(newInvocation) {
            return flag.parse(args)
        }
    }

    context(validationContext: ValidationContext<E, S>)
    override fun getSyntax(): String {
        val newValidationContext = validationContext.forSender(transform)
        context(newValidationContext) {
            return flag.getSyntax()
        }
    }

    context(validationContext: ValidationContext<E, S>)
    override fun validateSender(): Result<Unit> = requirement.validateSender()
}

internal class ValidatedCommand<E : Environment, S, S2 : Any>(
    val command: Structure<E, S2>,
    requirement: Requirement<E, S>,
    val transform: (S) -> S2,
) : StructureImpl<E, S>(
    command.id,
    command.aliases,
    command.description,
    requirement,
    // ValidatedCommand forwards to signature of base command
    Signature0(),
) {
    context(inv: Invocation<E, S>)
    override fun parse(args: List<String>): Result<Unit> {
        val newInvocation = (inv as InvocationImpl).forSender(transform)
        context(newInvocation) {
            return command.parse(args)
        }
    }

    context(validationContext: ValidationContext<E, S>)
    override fun getSyntax(): String {
        val newValidationContext = validationContext.forSender(transform)
        context(newValidationContext) {
            return command.getSyntax()
        }
    }

    context(validationContext: ValidationContext<E, S>)
    override fun validateSender(): Result<Unit> {
        val newValidationContext = validationContext.forSender(transform)
        context(newValidationContext) {
            return command.validateSender()
        }
    }
}

private fun placeholderValue(): Nothing {
    throw NotImplementedError("This shouldn't be called")
}
