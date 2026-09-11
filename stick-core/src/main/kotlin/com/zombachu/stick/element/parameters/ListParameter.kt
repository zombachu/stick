package com.zombachu.stick.element.parameters

import com.zombachu.stick.CommandResult
import com.zombachu.stick.Environment
import com.zombachu.stick.MatchResult
import com.zombachu.stick.ParsingResult
import com.zombachu.stick.Suggestion
import com.zombachu.stick.ValidationContext
import com.zombachu.stick.element.Parameter
import com.zombachu.stick.valueOrPropagateError

open class ListParameter<E : Environment, S, T>(name: String, description: String, val parameter: Size1<E, S, T>) :
    Parameter.Size1<E, S, List<T>>(name, description) {

    context(validationContext: ValidationContext<E, S>)
    override fun match(arg0: String): MatchResult {
        for (arg in arg0.split(DELIMITER)) {
            val match = parameter.match(arg)
            if (match is MatchResult.Unmatched) return match
        }
        return MatchResult.matchedExactly(1)
    }

    context(validationContext: ValidationContext<E, S>)
    override fun suggest(preceding: List<String>, partial: String): List<Suggestion> {
        val entryStart = partial.lastIndexOf(DELIMITER) + 1
        return parameter.suggest([], partial.substring(entryStart)).map { it.withIndex(entryStart + it.index) }
    }

    context(validationContext: ValidationContext<E, S>)
    override fun resolve(arg0: String): CommandResult<List<T>> {
        val args = arg0.split(DELIMITER)
        val parsedValues = args.map { arg ->
            parameter.resolve(arg).valueOrPropagateError {
                return it
            }
        }
        return ParsingResult.success(parsedValues)
    }

    companion object {
        private const val DELIMITER = ","
    }
}
