package com.zombachu.stick.element.parameters

import com.zombachu.stick.CommandResult
import com.zombachu.stick.ContextualValue
import com.zombachu.stick.Environment
import com.zombachu.stick.Invocation
import com.zombachu.stick.ParsingResult
import com.zombachu.stick.TypedIdentifier
import com.zombachu.stick.element.Parameter
import com.zombachu.stick.valueOrPropagateError

open class ListElementParameter<E : Environment, S, T>(
    id: TypedIdentifier<T>,
    description: String,
    val list: ContextualValue<E, S, List<T>>,
    val onEmpty: Invocation<E, S>.() -> Unit
) : Parameter.Size1<E, S, T>(id, description) {

    context(inv: Invocation<E, S>)
    override fun parse(arg0: String): CommandResult<T> {
        val list = list(inv).valueOrPropagateError { return it }
        if (list.isEmpty()) {
            onEmpty(inv)
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
