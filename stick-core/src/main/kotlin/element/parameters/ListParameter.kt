package com.zombachu.stick.element.parameters

import com.zombachu.stick.ExecutionContext
import com.zombachu.stick.ParsingResult
import com.zombachu.stick.Result
import com.zombachu.stick.SenderContext
import com.zombachu.stick.TypedIdentifier
import com.zombachu.stick.element.Parameter
import com.zombachu.stick.valueOrPropagateError

open class ListParameter<O, S : SenderContext, T : Any>(
    id: TypedIdentifier<List<T>>,
    description: String,
    val parameter: Size1<O, S, T>,
) : Parameter.Size1<O, S, List<T>>(id, description) {

    override fun parse(context: ExecutionContext<O, S>, arg0: String): Result<List<T>> {
        val args = arg0.split(",")
        val parsedValues = args.map { arg ->
            parameter.parse(context, arg).valueOrPropagateError { return it }
        }
        return ParsingResult.success(parsedValues)
    }
}
