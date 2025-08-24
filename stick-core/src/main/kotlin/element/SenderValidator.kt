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

internal interface SenderValidator<O, S : SenderContext<O>> {
    fun validateSender(senderContext: S): Result<Unit>
}

internal fun <O, S : SenderContext<O>> SyntaxElement<O, S, Any>.validateSender(senderContext: S): Result<Unit> {
    return if (this !is SenderValidator<*, *>) {
        SenderValidationResult.success()
    } else {
        @Suppress("UNCHECKED_CAST")
        (this as SenderValidator<O, S>).validateSender(senderContext)
    }
}

internal class ValidatedParameterImpl<O, S : SenderContext<O>, O2, S2 : SenderContext<O2>, T : Any>(
    val parameter: Parameter<O2, S2, T>,
    val requirement: Requirement<O, S>,
    val transform: (O) -> O2,
) : ValidatedParameter<O, S, T>, SenderValidator<O, S> {

    override val size: Size = parameter.size
    override val type: ElementType = parameter.type
    override val id: TypedIdentifier<out T> = parameter.id
    override val description: String = parameter.description

    override fun parse(context: ExecutionContext<O, S>, args: List<String>): Result<out T> {
        val newContext = (context as ExecutionContextImpl<O, S>).forSender<O2, S2>(transform)
        return parameter.parse(newContext, args)
    }

    override fun validateSender(senderContext: S): Result<Unit> = requirement.validateSender(senderContext)

    override fun getSyntax(senderContext: S): String = parameter.getSyntax(senderContext.transformSender(transform))
}

internal class ValidatedFlag<O, S : SenderContext<O>, O2, S2 : SenderContext<O2>, T : Any>(
    val flag: Flag<O2, S2, T>,
    val requirement: Requirement<O, S>,
    val invalidDefault: ContextualValue<O, S, T>,
    val transform: (O) -> O2,
) : FlagImpl<O, S, T>((flag as FlagImpl<O2, S2, T>).default as ContextualValue<O, S, T>, flag.flagParameter as FlagParameter<O, S, T>), SenderValidator<O, S> { // TODO: Handle casts better

    override fun parse(context: ExecutionContext<O, S>, args: List<String>): Result<out T> {
        val newContext = (context as ExecutionContextImpl<O, S>).forSender<O2, S2>(transform)
        return flag.parse(newContext, args)
    }

    override fun validateSender(senderContext: S): Result<Unit> = requirement.validateSender(senderContext)
}

internal class ValidatedCommand<O, S : SenderContext<O>, O2, S2 : SenderContext<O2>>(
    val command: Structure<O2, S2>,
    requirement: Requirement<O, S>,
    val transform: (O) -> O2,
) : StructureImpl<O, S>(
    command.id,
    command.aliases,
    command.description,
    requirement,
    (command as StructureImpl<O, S>).signature, // TODO: Handle properly
) {

    override val size: Size = command.size
    override val type: ElementType = command.type

    override fun parse(context: ExecutionContext<O, S>, args: List<String>): Result<out Unit> {
        val newContext = (context as ExecutionContextImpl<O, S>).forSender<O2, S2>(transform)
        return command.parse(newContext, args)
    }
}
