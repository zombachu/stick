package com.zombachu.stick.element

import com.zombachu.stick.ExecutionContext
import com.zombachu.stick.ParsingResult
import com.zombachu.stick.Result
import com.zombachu.stick.impl.Size
import com.zombachu.stick.impl.ValidatedDefault

internal class OptionalParameter<S, T : Any>(
    val validatedDefault: ValidatedDefault<S, T>,
    val parameter: Parameter<S, T>
) : Parameter.UnknownSize<S, T>(Size.Deferred, parameter.id, parameter.description) {
    override fun parse(context: ExecutionContext<S>, args: List<String>): Result<out T> {
        if (args.isEmpty()) {
            if (!validatedDefault.validate(context.sender)) {
                return ParsingResult.failSyntax()
            }
            return ParsingResult.success(validatedDefault.value(context))
        }

        if (!parameter.size.matches(args.size)) {
            return ParsingResult.failSyntax()
        }
        return parameter.parse(context, args)
    }

    override fun getSyntax(sender: S): String = "[${id.name}]"
}
