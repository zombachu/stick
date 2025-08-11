package com.zombachu.stick.element.parameters

import com.zombachu.stick.ExecutionContext
import com.zombachu.stick.ParsingResult
import com.zombachu.stick.Result
import com.zombachu.stick.TypedIdentifier
import com.zombachu.stick.cast
import com.zombachu.stick.element.Parameter
import com.zombachu.stick.isSuccess

open class ListParameter<S, T : Any>(
    id: TypedIdentifier<List<T>>,
    description: String,
    val parameter: Size1<S, T>,
) : Parameter.Size1<S, List<T>>(id, description) {

    override fun parse(context: ExecutionContext<S>, arg0: String): Result<List<T>> {
        val values = arg0.split(",")
        val parsedValues = values.map {
            val parsedValue = parameter.parse(context, listOf(it))
            // Propagate errors up
            if (!parsedValue.isSuccess()) {
                return@parse parsedValue.cast()
            }
            parsedValue.value
        }
        return ParsingResult.success(parsedValues)
    }
}
