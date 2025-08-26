package com.zombachu.stick.element

import com.zombachu.stick.Invocation
import com.zombachu.stick.ParsingResult
import com.zombachu.stick.Result
import com.zombachu.stick.Environment
import com.zombachu.stick.impl.Size
import com.zombachu.stick.impl.ValidatedDefault
import com.zombachu.stick.propagateError

internal class OptionalParameter<S : Environment, O, T : Any>(
    val validatedDefault: ValidatedDefault<S, O, T>,
    val parameter: Parameter<S, O, T>
) : Parameter.UnknownSize<S, O, T>(Size.Deferred, parameter.id, parameter.description) {

    context(env: S, inv: Invocation<S, O>)
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

    context(env: S)
    override fun getSyntax(): String = "[${id.name}]"
}
