package com.zombachu.stick.element

import com.zombachu.stick.CommandResult
import com.zombachu.stick.ConsumingResult
import com.zombachu.stick.ContextualValue
import com.zombachu.stick.Environment
import com.zombachu.stick.Execution
import com.zombachu.stick.Invocation
import com.zombachu.stick.MatchResult
import com.zombachu.stick.Position
import com.zombachu.stick.SenderValidator
import com.zombachu.stick.Size
import com.zombachu.stick.Suggestion
import com.zombachu.stick.consuming
import com.zombachu.stick.failSyntax
import com.zombachu.stick.isSuccess
import com.zombachu.stick.propagateFailure
import com.zombachu.stick.success

internal class OptionalParameterImpl<E : Environment, S, T, P : Position>(
    private val parameter: Parameter<E, S, out T, *>,
    private val default: ContextualValue<E, S, T>,
    private val defaultRequirement: SenderValidator<E, S>?,
) : OptionalParameter<E, S, T, P>, InternalConsumingElement<E, S, T>, InternalOptional<E, S> {

    override val size: Size = parameter.size.orNothing()
    override val name: String = parameter.name
    override val description: String = parameter.description

    context(inv: Invocation<E, S>)
    override fun match(args: List<String>): MatchResult {
        if (args.isEmpty()) return MatchResult.matchedAtLeast(0)
        return parameter.match(args)
    }

    context(inv: Invocation<E, S>)
    override fun suggest(preceding: List<String>, partial: String): List<Suggestion> =
        parameter.suggest(preceding, partial)

    context(ex: Execution<E, S>)
    override suspend fun parse(args: List<String>): ConsumingResult<T> {
        if (args.isEmpty()) {
            validateDefault().propagateFailure {
                return failSyntax()
            }
            return default(ex).consuming(0)
        }

        if (!parameter.size.matches(args.size)) return failSyntax()
        return parameter.parse(args)
    }

    context(inv: Invocation<E, S>)
    override fun getSyntax(): String {
        if (!validateDefault().isSuccess()) return parameter.getSyntax()
        return "[${name}]"
    }

    context(inv: Invocation<E, S>)
    override fun validateDefault(): CommandResult<Unit> = defaultRequirement?.validateSender() ?: success()
}

internal fun Size.orNothing(): Size = if (this is Size.Bounded) Size.between(0, max) else Size.atLeast(0)
