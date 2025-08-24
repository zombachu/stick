package com.zombachu.stick.element

import com.zombachu.stick.ExecutionContext
import com.zombachu.stick.ParsingResult
import com.zombachu.stick.Result
import com.zombachu.stick.SenderContext
import com.zombachu.stick.impl.Size
import com.zombachu.stick.impl.ValidatedDefault
import com.zombachu.stick.propagateError

internal class OptionalParameter<S : SenderContext, O, T : Any>(
    val validatedDefault: ValidatedDefault<S, O, T>,
    val parameter: Parameter<S, O, T>
) : Parameter.UnknownSize<S, O, T>(Size.Deferred, parameter.id, parameter.description) {

    context(senderContext: S, executionContext: ExecutionContext<S, O>)
    override fun parse(args: List<String>): Result<out T> {
        if (args.isEmpty()) {
            validatedDefault.validateSender().propagateError<T> { return it }
            return ParsingResult.success(validatedDefault.value(executionContext))
        }

        if (!parameter.size.matches(args.size)) {
            return ParsingResult.failSyntax(executionContext.getSyntax())
        }
        return parameter.parse(args)
    }

    context(senderContext: S)
    override fun getSyntax(): String = "[${id.name}]"
}
