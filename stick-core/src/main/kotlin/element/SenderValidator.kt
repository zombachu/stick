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

internal interface SenderValidator<S : Environment, O> {
    context(env: S)
    fun validateSender(): Result<Unit>
}

context(env: S)
internal fun <S : Environment, O> SyntaxElement<S, O, Any>.validateSender(): Result<Unit> {
    return if (this !is SenderValidator<*, *>) {
        SenderValidationResult.success()
    } else {
        @Suppress("UNCHECKED_CAST")
        (this as SenderValidator<S, O>).validateSender()
    }
}

internal class ValidatedParameterImpl<S : Environment, O : Any, O2 : Any, T : Any>(
    val parameter: Parameter<S, O2, T>,
    val requirement: Requirement<S, O>,
    val transform: (O) -> O2,
) : ValidatedParameter<S, O, T>, SenderValidator<S, O> {

    override val size: Size = parameter.size
    override val type: ElementType = parameter.type
    override val id: TypedIdentifier<out T> = parameter.id
    override val description: String = parameter.description

    context(env: S, inv: Invocation<S, O>)
    override fun parse(args: List<String>): Result<out T> {
        val newInvocation = (inv as InvocationImpl<S, O>).forSender(transform)
        context(newInvocation) {
            return parameter.parse(args)
        }
    }

    context(env: S)
    override fun getSyntax(): String {
        val newEnvironment = env.transformSender<S, O, O2>(transform)
        context(newEnvironment) {
           return parameter.getSyntax()
        }
    }

    context(env: S)
    override fun validateSender(): Result<Unit> = requirement.validateSender()
}

internal class ValidatedFlag<S : Environment, O, O2 : Any, T : Any>(
    val flag: Flag<S, O2, T>,
    val requirement: Requirement<S, O>,
    val invalidDefault: ContextualValue<S, O, T>,
    val transform: (O) -> O2,
) : FlagImpl<S, O, T>((flag as FlagImpl<S, O2, T>).default as ContextualValue<S, O, T>, flag.flagParameter as FlagParameter<S, O, T>), SenderValidator<S, O> { // TODO: Handle casts better

    context(env: S, inv: Invocation<S, O>)
    override fun parse(args: List<String>): Result<out T> {
        val newInvocation = (inv as InvocationImpl<S, O>).forSender(transform)
        context(newInvocation) {
            return flag.parse(args)
        }
    }

    context(env: S)
    override fun validateSender(): Result<Unit> = requirement.validateSender()
}

internal class ValidatedCommand<S : Environment, O, O2 : Any>(
    val command: Structure<S, O2>,
    requirement: Requirement<S, O>,
    val transform: (O) -> O2,
) : StructureImpl<S, O>(
    command.id,
    command.aliases,
    command.description,
    requirement,
    (command as StructureImpl<S, O>).signature, // TODO: Handle properly
) {

    override val size: Size = command.size
    override val type: ElementType = command.type

    context(env: S, inv: Invocation<S, O>)
    override fun parse(args: List<String>): Result<out Unit> {
        val newInvocation = (inv as InvocationImpl<S, O>).forSender(transform)
        context(newInvocation) {
            return command.parse(args)
        }
    }
}
