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

internal interface SenderValidator<E : Environment, S> {
    context(env: E)
    fun validateSender(): Result<Unit>
}

context(env: E)
internal fun <E : Environment, S> SyntaxElement<E, S, Any>.validateSender(): Result<Unit> {
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
    override val id: TypedIdentifier<out T> = parameter.id
    override val description: String = parameter.description

    context(env: E, inv: Invocation<E, S>)
    override fun parse(args: List<String>): Result<out T> {
        val newInvocation = (inv as InvocationImpl<E, S>).forSender(transform)
        context(newInvocation) {
            return parameter.parse(args)
        }
    }

    context(env: E)
    override fun getSyntax(): String {
        val newEnvironment = env.transformSender<E, S, S2>(transform)
        context(newEnvironment) {
           return parameter.getSyntax()
        }
    }

    context(env: E)
    override fun validateSender(): Result<Unit> = requirement.validateSender()
}

internal class ValidatedFlag<E : Environment, S, S2 : Any, T : Any>(
    val flag: Flag<E, S2, T>,
    val requirement: Requirement<E, S>,
    val invalidDefault: ContextualValue<E, S, T>,
    val transform: (S) -> S2,
) : FlagImpl<E, S, T>((flag as FlagImpl<E, S2, T>).default as ContextualValue<E, S, T>, flag.flagParameter as FlagParameter<E, S, T>), SenderValidator<E, S> { // TODO: Handle casts better

    context(env: E, inv: Invocation<E, S>)
    override fun parse(args: List<String>): Result<out T> {
        val newInvocation = (inv as InvocationImpl<E, S>).forSender(transform)
        context(newInvocation) {
            return flag.parse(args)
        }
    }

    context(env: E)
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
    (command as StructureImpl<E, S>).signature, // TODO: Handle properly
) {

    override val size: Size = command.size
    override val type: ElementType = command.type

    context(env: E, inv: Invocation<E, S>)
    override fun parse(args: List<String>): Result<out Unit> {
        val newInvocation = (inv as InvocationImpl<E, S>).forSender(transform)
        context(newInvocation) {
            return command.parse(args)
        }
    }
}
