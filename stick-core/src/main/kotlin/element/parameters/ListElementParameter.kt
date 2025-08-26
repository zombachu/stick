package com.zombachu.stick.element.parameters

import com.zombachu.stick.ContextualValue
import com.zombachu.stick.ExecutionResult
import com.zombachu.stick.Invocation
import com.zombachu.stick.ParsingResult
import com.zombachu.stick.Result
import com.zombachu.stick.SenderContext
import com.zombachu.stick.TypedIdentifier
import com.zombachu.stick.element.Parameter
import com.zombachu.stick.propagateError

open class ListElementParameter<S : SenderContext, O, T : Any>(
    id: TypedIdentifier<T>,
    description: String,
    val list: ContextualValue<S, O, List<T>>,
    val onEmpty: Invocation<S, O>.() -> ExecutionResult,
) : Parameter.Size1<S, O, T>(id, description) {

    context(senderContext: S, invocation: Invocation<S, O>)
    override fun parse(arg0: String): Result<out T> {
        val list = list(invocation)
        if (list.isEmpty()) {
            onEmpty(invocation).propagateError { return it }
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
