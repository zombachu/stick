package com.zombachu.stick.element.parameters

import com.zombachu.stick.CommandResult
import com.zombachu.stick.ContextualValue
import com.zombachu.stick.Environment
import com.zombachu.stick.Invocation
import com.zombachu.stick.ParsingResult
import com.zombachu.stick.element.Parameter
import com.zombachu.stick.valueOrPropagateError

open class ListElementParameter<E : Environment, S, T>(
    name: String,
    description: String,
    private val list: ContextualValue<E, S, List<T>>,
    private val oneIndexed: Boolean,
    private val onEmpty: (Invocation<E, S>.() -> Unit)? = null,
) : Parameter.Size1<E, S, ListElementResult<T>>(name, description) {

    context(inv: Invocation<E, S>)
    override fun parse(arg0: String): CommandResult<ListElementResult<T>> {
        val list =
            list(inv).valueOrPropagateError {
                return it
            }
        if (onEmpty != null && list.isEmpty()) {
            onEmpty(inv)
            return ParsingResult.failHandled()
        }

        val userIndex = arg0.toIntOrNull() ?: return ParsingResult.failType("index", arg0)

        // If the given number is not in the valid range then give the sender an error
        val oneIndexedAdjustment = if (oneIndexed) 1 else 0
        val min = 0 + oneIndexedAdjustment
        val max = list.size - 1 + oneIndexedAdjustment
        if (userIndex !in min..max) {
            return ParsingResult.failRange(min.toString(), max.toString(), arg0)
        }
        val index = userIndex - oneIndexedAdjustment

        return ParsingResult.success(ListElementResult(list[index], list, index))
    }
}

data class ListElementResult<T>(val result: T, val list: List<T>, val index: Int)
