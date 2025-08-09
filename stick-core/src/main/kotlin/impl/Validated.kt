package com.zombachu.stick.impl

import com.zombachu.stick.ContextualValue
import com.zombachu.stick.ExecutionContext
import com.zombachu.stick.ParsingResult
import com.zombachu.stick.TypedIdentifier

fun interface Requirement<S> {
    operator fun invoke(sender: S): Boolean

    operator fun plus(other: Requirement<S>): Requirement<S> {
        return Requirement {
                sender -> this(sender) && other(sender)
        }
    }
}

interface Validator<S> {
    val validate: (S) -> Boolean
}

class ValidatedParameter<S, S2, T : Any>(
    val parameter: Parameter<S2, T>,
    override val validate: (S) -> Boolean,
    val transform: (S) -> S2,
) : Groupable<S, T>, Validator<S> {

    override val size: Size = parameter.size
    override val type: ElementType = parameter.type
    override val id: TypedIdentifier<out T> = parameter.id
    override val description: String = parameter.description

    override fun parse(context: ExecutionContext<S>, args: List<String>): ParsingResult<out T> {
        val newContext = context.forSender(transform(context.sender))
        return parameter.parse(newContext, args)
    }

    override fun getSyntax(sender: S): String = parameter.getSyntax(transform(sender))
}

class ValidatedFlag<S, S2, T : Any>(
    val flag: Flag<S2, T>,
    override val validate: (S) -> Boolean,
    val invalidDefault: ContextualValue<S, T>,
    val transform: (S) -> S2,
) : Flag<S, T>(flag.default as ContextualValue<S, T>, flag.flagParameter as FlagParameter<S, T>), Validator<S> { // TODO: Handle casts better

    override fun parse(context: ExecutionContext<S>, args: List<String>): ParsingResult<out T> {
        val newContext = context.forSender(transform(context.sender))
        return flag.parse(newContext, args)
    }
}

internal class ValidatedCommand<S, S2>(
    val command: Structure<S2>,
    override val validate: (S) -> Boolean,
    val transform: (S) -> S2,
) : StructureImpl<S>(
    command.id,
    command.aliases,
    command.description,
    validate,
    (command as StructureImpl<S>).signature, // TODO: Handle properly
) {

    override val size: Size = command.size
    override val type: ElementType = command.type

    override fun parse(context: ExecutionContext<S>, args: List<String>): ParsingResult<out Unit> {
        val newContext = context.forSender(transform(context.sender))
        return command.parse(newContext, args)
    }
}
