package com.zombachu.stick.element

import com.zombachu.stick.Invocation
import com.zombachu.stick.ParsingResult
import com.zombachu.stick.Result
import com.zombachu.stick.Environment
import com.zombachu.stick.impl.Size
import com.zombachu.stick.impl.ValidatedDefault
import com.zombachu.stick.propagateError

internal class OptionalParameter<E : Environment, O, T : Any>(
    val validatedDefault: ValidatedDefault<E, O, T>,
    val parameter: Parameter<E, O, T>
) : Parameter.UnknownSize<E, O, T>(Size.Deferred, parameter.id, parameter.description) {

    context(env: E, inv: Invocation<E, O>)
    override fun parse(args: List<String>): Result<out T> {
        if (args.isEmpty()) {
            validatedDefault.validateSender().propagateError<T> { return it }
            return ParsingResult.success(validatedDefault.value(inv))
        }

        if (!parameter.size.matches(args.size)) {
            return ParsingResult.failSyntax(inv.getSyntax())
        }
        return parameter.parse(args)
    }

    context(env: E)
    override fun getSyntax(): String = "[${id.name}]"
}
