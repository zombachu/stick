package com.zombachu.stick.element

import com.zombachu.stick.ContextualValue
import com.zombachu.stick.Invocation
import com.zombachu.stick.Result
import com.zombachu.stick.Environment
import com.zombachu.stick.SenderValidationResult
import com.zombachu.stick.TypedIdentifier
import com.zombachu.stick.impl.InvocationImpl
import com.zombachu.stick.impl.Requirement
import com.zombachu.stick.impl.Size
import com.zombachu.stick.transformSender

internal interface SenderValidator<E : Environment, O> {
    context(env: E)
    fun validateSender(): Result<Unit>
}

context(env: E)
internal fun <E : Environment, O> SyntaxElement<E, O, Any>.validateSender(): Result<Unit> {
    return if (this !is SenderValidator<*, *>) {
        SenderValidationResult.success()
    } else {
        @Suppress("UNCHECKED_CAST")
        (this as SenderValidator<E, O>).validateSender()
    }
}

internal class ValidatedParameterImpl<E : Environment, O : Any, O2 : Any, T : Any>(
    val parameter: Parameter<E, O2, T>,
    val requirement: Requirement<E, O>,
    val transform: (O) -> O2,
) : ValidatedParameter<E, O, T>, SenderValidator<E, O> {

    override val size: Size = parameter.size
    override val type: ElementType = parameter.type
    override val id: TypedIdentifier<out T> = parameter.id
    override val description: String = parameter.description

    context(env: E, inv: Invocation<E, O>)
    override fun parse(args: List<String>): Result<out T> {
        val newInvocation = (inv as InvocationImpl<E, O>).forSender(transform)
        context(newInvocation) {
            return parameter.parse(args)
        }
    }

    context(env: E)
    override fun getSyntax(): String {
        val newEnvironment = env.transformSender<E, O, O2>(transform)
        context(newEnvironment) {
           return parameter.getSyntax()
        }
    }

    context(env: E)
    override fun validateSender(): Result<Unit> = requirement.validateSender()
}

internal class ValidatedFlag<E : Environment, O, O2 : Any, T : Any>(
    val flag: Flag<E, O2, T>,
    val requirement: Requirement<E, O>,
    val invalidDefault: ContextualValue<E, O, T>,
    val transform: (O) -> O2,
) : FlagImpl<E, O, T>((flag as FlagImpl<E, O2, T>).default as ContextualValue<E, O, T>, flag.flagParameter as FlagParameter<E, O, T>), SenderValidator<E, O> { // TODO: Handle casts better

    context(env: E, inv: Invocation<E, O>)
    override fun parse(args: List<String>): Result<out T> {
        val newInvocation = (inv as InvocationImpl<E, O>).forSender(transform)
        context(newInvocation) {
            return flag.parse(args)
        }
    }

    context(env: E)
    override fun validateSender(): Result<Unit> = requirement.validateSender()
}

internal class ValidatedCommand<E : Environment, O, O2 : Any>(
    val command: Structure<E, O2>,
    requirement: Requirement<E, O>,
    val transform: (O) -> O2,
) : StructureImpl<E, O>(
    command.id,
    command.aliases,
    command.description,
    requirement,
    (command as StructureImpl<E, O>).signature, // TODO: Handle properly
) {

    override val size: Size = command.size
    override val type: ElementType = command.type

    context(env: E, inv: Invocation<E, O>)
    override fun parse(args: List<String>): Result<out Unit> {
        val newInvocation = (inv as InvocationImpl<E, O>).forSender(transform)
        context(newInvocation) {
            return command.parse(args)
        }
    }
}
