package com.zombachu.stick.element

import com.zombachu.stick.CommandResult
import com.zombachu.stick.Environment
import com.zombachu.stick.Invocation
import com.zombachu.stick.ParsingResult
import com.zombachu.stick.ValidationContext
import com.zombachu.stick.impl.Size
import com.zombachu.stick.impl.ValidatedDefault
import com.zombachu.stick.propagateError

internal class OptionalParameter<E : Environment, S, T>(
    val validatedDefault: ValidatedDefault<E, S, T>,
    val parameter: Parameter<E, S, T>
) : Parameter.UnknownSize<E, S, T>(Size.Deferred, parameter.id, parameter.description) {

    context(inv: Invocation<E, S>)
    override fun parse(args: List<String>): CommandResult<T> {
        if (args.isEmpty()) {
            validatedDefault.validateSender().propagateError<T> { return it }
            return validatedDefault.value(inv)
        }

        if (!parameter.size.matches(args.size)) {
            return ParsingResult.failSyntax(inv.getSyntax())
        }
        return parameter.parse(args)
    }

    context(validationContext: ValidationContext<E, S>)
    override fun getSyntax(): String = "[${id.name}]"
}
