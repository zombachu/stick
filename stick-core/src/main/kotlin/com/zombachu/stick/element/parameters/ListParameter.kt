package com.zombachu.stick.element.parameters

import com.zombachu.stick.CommandResult
import com.zombachu.stick.Environment
import com.zombachu.stick.Invocation
import com.zombachu.stick.ParsingResult
import com.zombachu.stick.TypedIdentifier
import com.zombachu.stick.element.Parameter
import com.zombachu.stick.valueOrPropagateError

open class ListParameter<E : Environment, S, T : Any>(
    id: TypedIdentifier<List<T>>,
    description: String,
    val parameter: Size1<E, S, T>,
) : Parameter.Size1<E, S, List<T>>(id, description) {

    context(inv: Invocation<E, S>)
    override fun parse(arg0: String): CommandResult<List<T>> {
        val args = arg0.split(",")
        val parsedValues = args.map { arg ->
            parameter.parse(arg).valueOrPropagateError { return it }
        }
        return ParsingResult.success(parsedValues)
    }
}
