package com.zombachu.stick.element.parameters

import com.zombachu.stick.ContextualValue
import com.zombachu.stick.ExecutionContext
import com.zombachu.stick.ExecutionResult
import com.zombachu.stick.ParsingResult
import com.zombachu.stick.Result
import com.zombachu.stick.SenderContext
import com.zombachu.stick.TypedIdentifier
import com.zombachu.stick.element.Parameter
import com.zombachu.stick.propagateError

open class ListElementParameter<O, S : SenderContext<O>, T : Any>(
    id: TypedIdentifier<T>,
    description: String,
    val list: ContextualValue<O, S, List<T>>,
    val onEmpty: ExecutionContext<O, S>.() -> ExecutionResult,
) : Parameter.Size1<O, S, T>(id, description) {

    override fun parse(context: ExecutionContext<O, S>, arg0: String): Result<T> {
        val list = list(context)
        if (list.isEmpty()) {
            onEmpty(context).propagateError { return it }
            return ParsingResult.failHandled()
        }

        val index = arg0.toIntOrNull() ?: return ParsingResult.failType("index", arg0)

        // If the given number is not in the valid range then give the sender an error
        val min = 0
        val max = list.size - 1
        if (index !in min..max) {
            return ParsingResult.failRange(min.toString(), max.toString(), arg0)
        }

        return ParsingResult.success(list[index])
    }
}
