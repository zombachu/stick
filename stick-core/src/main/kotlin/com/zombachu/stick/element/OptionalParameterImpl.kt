package com.zombachu.stick.element

import com.zombachu.stick.CommandResult
import com.zombachu.stick.Environment
import com.zombachu.stick.Invocation
import com.zombachu.stick.ParsingResult
import com.zombachu.stick.ValidationContext
import com.zombachu.stick.impl.InvalidSenderDefault
import com.zombachu.stick.impl.Size
import com.zombachu.stick.impl.ValidSenderDefault
import com.zombachu.stick.isSuccess
import com.zombachu.stick.propagateError

internal class OptionalParameterImpl<E : Environment, S, T>(
    val requirementDefault: InvalidSenderDefault<E, S, T>,
    val presenceDefault: ValidSenderDefault<E, S, T>,
    val parameter: Parameter<E, S, T>
) : Parameter.UnknownSize<E, S, T>(Size.Deferred, parameter.name, parameter.description), OptionalParameter<E, S, T> {

    context(inv: Invocation<E, S>)
    override fun parse(args: List<String>): CommandResult<T> {
        if (args.isEmpty()) {
            // If the sender isn't allowed to provide a value use the default
            requirementDefault.validateSender().propagateError { return requirementDefault.value(inv) }
            // Check if the value is required to be specified by the sender
            presenceDefault.validateSender().propagateError { return it }
            return presenceDefault.value(inv)
        }

        // Check if the sender provided a value when they're not allowed to
        requirementDefault.validateSender().propagateError { return it }

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
