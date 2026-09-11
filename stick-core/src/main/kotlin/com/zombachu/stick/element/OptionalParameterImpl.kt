package com.zombachu.stick.element

import com.zombachu.stick.ConsumingResult
import com.zombachu.stick.Environment
import com.zombachu.stick.Invocation
import com.zombachu.stick.MatchResult
import com.zombachu.stick.ParsingResult
import com.zombachu.stick.Position
import com.zombachu.stick.Size
import com.zombachu.stick.Suggestion
import com.zombachu.stick.ValidationContext
import com.zombachu.stick.consuming
import com.zombachu.stick.isSuccess
import com.zombachu.stick.propagateError

internal class OptionalParameterImpl<E : Environment, S, T, P : Position>(
    val requirementDefault: InvalidSenderDefault<E, S, T>,
    val presenceDefault: ValidSenderDefault<E, S, T>,
    val parameter: Parameter<E, S, T, *>,
) : OptionalParameter<E, S, T, P> {

    override val size: Size = parameter.size.orNothing()
    override val name: String = parameter.name
    override val description: String = parameter.description

    context(validationContext: ValidationContext<E, S>)
    override fun match(args: List<String>): MatchResult {
        if (args.isEmpty()) return MatchResult.matched(0)
        return parameter.match(args)
    }

    context(validationContext: ValidationContext<E, S>)
    override fun suggest(preceding: List<String>, partial: String): List<Suggestion> {
        requirementDefault.validateSender().propagateError {
            return []
        }
        return parameter.suggest(preceding, partial)
    }

    context(inv: Invocation<E, S>)
    override fun parse(args: List<String>): ConsumingResult<T> {
        if (args.isEmpty()) {
            // If the sender isn't allowed to provide a value use the default
            requirementDefault.validateSender().propagateError {
                return requirementDefault.value(inv).consuming(0)
            }
            // Check if the value is required to be specified by the sender
            presenceDefault.validateSender().propagateError {
                return it
            }
            return presenceDefault.value(inv).consuming(0)
        }

        // Check if the sender provided a value when they're not allowed to
        requirementDefault.validateSender().propagateError {
            return it
        }

        if (!parameter.size.matches(args.size)) return ParsingResult.failSyntax(inv.getSyntax())
        return parameter.parse(args)
    }

    context(validationContext: ValidationContext<E, S>)
    override fun getSyntax(): String {
        // Check if the sender is allowed to provide a value
        if (requirementDefault.validateSender().isSuccess()) {
            return "[${name}]"
        }
        return ""
    }
}

private fun Size.orNothing(): Size = if (this is Size.Bounded) Size.between(0, max) else Size.atLeast(0)
