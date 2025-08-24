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

internal interface SenderValidator<S : SenderContext> {
    fun validateSender(senderContext: S): Result<Unit>
}

internal fun <S : SenderContext> SyntaxElement<S, Any>.validateSender(senderContext: S): Result<Unit> {
    return if (this !is SenderValidator<*>) {
        SenderValidationResult.success()
    } else {
        @Suppress("UNCHECKED_CAST")
        (this as SenderValidator<S>).validateSender(senderContext)
    }
}

internal class ValidatedParameterImpl<S : SenderContext, S2 : SenderContext, T : Any>(
    val parameter: Parameter<S2, T>,
    val requirement: Requirement<S>,
    val transform: (S) -> S2,
) : ValidatedParameter<S, T>, SenderValidator<S> {

    override val size: Size = parameter.size
    override val type: ElementType = parameter.type
    override val id: TypedIdentifier<out T> = parameter.id
    override val description: String = parameter.description

    override fun parse(context: ExecutionContext<S>, args: List<String>): Result<out T> {
        val newContext = (context as ExecutionContextImpl<S>).forSender(transform)
        return parameter.parse(newContext, args)
    }

    override fun validateSender(senderContext: S): Result<Unit> = requirement.validateSender(senderContext)

    override fun getSyntax(senderContext: S): String = parameter.getSyntax(transform(senderContext))
}

internal class ValidatedFlag<S : SenderContext, S2 : SenderContext, T : Any>(
    val flag: Flag<S2, T>,
    val requirement: Requirement<S>,
    val invalidDefault: ContextualValue<S, T>,
    val transform: (S) -> S2,
) : FlagImpl<S, T>((flag as FlagImpl<S2, T>).default as ContextualValue<S, T>, flag.flagParameter as FlagParameter<S, T>), SenderValidator<S> { // TODO: Handle casts better

    override fun parse(context: ExecutionContext<S>, args: List<String>): Result<out T> {
        val newContext = (context as ExecutionContextImpl<S>).forSender(transform)
        return flag.parse(newContext, args)
    }

    override fun validateSender(senderContext: S): Result<Unit> = requirement.validateSender(senderContext)
}

internal class ValidatedCommand<S : SenderContext, S2 : SenderContext>(
    val command: Structure<S2>,
    requirement: Requirement<S>,
    val transform: (S) -> S2,
) : StructureImpl<S>(
    command.id,
    command.aliases,
    command.description,
    requirement,
    (command as StructureImpl<S>).signature, // TODO: Handle properly
) {

    override val size: Size = command.size
    override val type: ElementType = command.type

    override fun parse(context: ExecutionContext<S>, args: List<String>): Result<out Unit> {
        val newContext = (context as ExecutionContextImpl<S>).forSender(transform)
        return command.parse(newContext, args)
    }
}
