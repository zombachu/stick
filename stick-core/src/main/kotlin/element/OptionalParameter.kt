package com.zombachu.stick.element

import com.zombachu.stick.ExecutionContext
import com.zombachu.stick.ParsingResult
import com.zombachu.stick.Result
import com.zombachu.stick.SenderContext
import com.zombachu.stick.impl.Size
import com.zombachu.stick.impl.ValidatedDefault
import com.zombachu.stick.propagateError

internal class OptionalParameter<O, S : SenderContext<O>, T : Any>(
    val validatedDefault: ValidatedDefault<O, S, T>,
    val parameter: Parameter<O, S, T>
) : Parameter.UnknownSize<O, S, T>(Size.Deferred, parameter.id, parameter.description) {
    override fun parse(context: ExecutionContext<O, S>, args: List<String>): Result<out T> {
        if (args.isEmpty()) {
            validatedDefault.validateSender(context.senderContext).propagateError<T> { return it }
            return ParsingResult.success(validatedDefault.value(context))
        }

        if (!parameter.size.matches(args.size)) {
            return ParsingResult.failSyntax(context.getSyntax())
        }
        return parameter.parse(context, args)
    }

    override fun getSyntax(senderContext: S): String = "[${id.name}]"
}
