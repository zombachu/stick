package com.zombachu.stick.element.parameters

import com.zombachu.stick.CommandResult
import com.zombachu.stick.Environment
import com.zombachu.stick.MatchResult
import com.zombachu.stick.ParsingResult
import com.zombachu.stick.ValidationContext
import com.zombachu.stick.element.Parameter
import com.zombachu.stick.valueOrPropagateError

open class ListParameter<E : Environment, S, T>(name: String, description: String, val parameter: Size1<E, S, T>) :
    Parameter.Size1<E, S, List<T>>(name, description) {

    context(validationContext: ValidationContext<E, S>)
    override fun match(arg0: String): MatchResult {
        for (arg in arg0.split(",")) {
            val match = parameter.match(arg)
            if (match is MatchResult.Unmatched) return match
        }
        return MatchResult.matched(1)
    }

    context(validationContext: ValidationContext<E, S>)
    override fun resolve(arg0: String): CommandResult<List<T>> {
        val args = arg0.split(",")
        val parsedValues = args.map { arg ->
            parameter.resolve(arg).valueOrPropagateError {
                return it
            }
        }
        return ParsingResult.success(parsedValues)
    }
}
