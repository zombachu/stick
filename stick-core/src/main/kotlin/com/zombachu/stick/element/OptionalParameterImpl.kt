package com.zombachu.stick.element

import com.zombachu.stick.ConsumingResult
import com.zombachu.stick.Environment
import com.zombachu.stick.Execution
import com.zombachu.stick.Invocation
import com.zombachu.stick.MatchResult
import com.zombachu.stick.Position
import com.zombachu.stick.Size
import com.zombachu.stick.Suggestion
import com.zombachu.stick.consuming
import com.zombachu.stick.failSyntax
import com.zombachu.stick.isSuccess
import com.zombachu.stick.propagateError

internal class OptionalParameterImpl<E : Environment, S, T, P : Position>(
    val parameter: Parameter<E, S, out T, *>,
    val presenceDefault: ValidSenderDefault<E, S, T>,
) : OptionalParameter<E, S, T, P>, InternalConsumingElement<E, S, T> {

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
    override fun parse(args: List<String>): ConsumingResult<T> {
        if (args.isEmpty()) {
            // Check if the value is required to be specified by the sender
            presenceDefault.validateSender().propagateError {
                return failSyntax()
            }
            return presenceDefault.value(ex).consuming(0)
        }

        if (!parameter.size.matches(args.size)) return failSyntax()
        return parameter.parse(args)
    }

    context(inv: Invocation<E, S>)
    override fun getSyntax(): String {
        if (!presenceDefault.validateSender().isSuccess()) return parameter.getSyntax()
        return "[${name}]"
    }
}

internal fun Size.orNothing(): Size = if (this is Size.Bounded) Size.between(0, max) else Size.atLeast(0)
