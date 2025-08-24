package com.zombachu.stick.element

import com.zombachu.stick.ContextualValue
import com.zombachu.stick.ExecutionContext
import com.zombachu.stick.Result
import com.zombachu.stick.SenderContext
import com.zombachu.stick.SenderValidationResult
import com.zombachu.stick.TypedIdentifier
import com.zombachu.stick.impl.ExecutionContextImpl
import com.zombachu.stick.impl.Requirement
import com.zombachu.stick.impl.Size
import com.zombachu.stick.transformSender

internal interface SenderValidator<S : SenderContext, O> {
    fun validateSender(senderContext: S): Result<Unit>
}

internal fun <S : SenderContext, O> SyntaxElement<S, O, Any>.validateSender(senderContext: S): Result<Unit> {
    return if (this !is SenderValidator<*, *>) {
        SenderValidationResult.success()
    } else {
        @Suppress("UNCHECKED_CAST")
        (this as SenderValidator<S, O>).validateSender(senderContext)
    }
}

internal class ValidatedParameterImpl<S : SenderContext, O : Any, O2 : Any, T : Any>(
    val parameter: Parameter<S, O2, T>,
    val requirement: Requirement<S, O>,
    val transform: (O) -> O2,
) : ValidatedParameter<S, O, T>, SenderValidator<S, O> {

    override val size: Size = parameter.size
    override val type: ElementType = parameter.type
    override val id: TypedIdentifier<out T> = parameter.id
    override val description: String = parameter.description

    override fun parse(context: ExecutionContext<S, O>, args: List<String>): Result<out T> {
        val newContext = (context as ExecutionContextImpl<S, O>).forSender(transform)
        return parameter.parse(newContext, args)
    }

    override fun validateSender(senderContext: S): Result<Unit> = requirement.validateSender(senderContext)

    override fun getSyntax(senderContext: S): String = parameter.getSyntax(senderContext.transformSender(transform))
}

internal class ValidatedFlag<S : SenderContext, O, O2 : Any, T : Any>(
    val flag: Flag<S, O2, T>,
    val requirement: Requirement<S, O>,
    val invalidDefault: ContextualValue<S, O, T>,
    val transform: (O) -> O2,
) : FlagImpl<S, O, T>((flag as FlagImpl<S, O2, T>).default as ContextualValue<S, O, T>, flag.flagParameter as FlagParameter<S, O, T>), SenderValidator<S, O> { // TODO: Handle casts better

    override fun parse(context: ExecutionContext<S, O>, args: List<String>): Result<out T> {
        val newContext = (context as ExecutionContextImpl<S, O>).forSender(transform)
        return flag.parse(newContext, args)
    }

    override fun validateSender(senderContext: S): Result<Unit> = requirement.validateSender(senderContext)
}

internal class ValidatedCommand<S : SenderContext, O, O2 : Any>(
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

    override fun parse(context: ExecutionContext<S, O>, args: List<String>): Result<out Unit> {
        val newContext = (context as ExecutionContextImpl<S, O>).forSender(transform)
        return command.parse(newContext, args)
    }
}
