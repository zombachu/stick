package com.zombachu.stick.element.parameters

import com.zombachu.stick.ContextualValue
import com.zombachu.stick.Environment
import com.zombachu.stick.Execution
import com.zombachu.stick.Position
import com.zombachu.stick.element.MappedParameter
import com.zombachu.stick.element.Parameter
import com.zombachu.stick.failRange
import com.zombachu.stick.handled
import com.zombachu.stick.success
import com.zombachu.stick.valueOrPropagateFailure

internal fun <E : Environment, S, T> listElementParameter(
    name: String,
    description: String,
    list: ContextualValue<E, S, List<T>>,
    oneIndexed: Boolean,
    onEmpty: (Execution<E, S>.() -> Unit)?,
): Parameter<E, S, ListElementResult<T>, Position.Leading> {
    val indexParameter =
        NumberParameter<E, S, Int>(name, description, { toIntOrNull() }, Int.MIN_VALUE, Int.MAX_VALUE, "index")
    return MappedParameter(indexParameter) { userIndex ->
        val elements =
            list(this).valueOrPropagateFailure {
                return@MappedParameter it
            }

        if (onEmpty != null && elements.isEmpty()) {
            onEmpty(this)
            return@MappedParameter handled()
        }

        val oneIndexedAdjustment = if (oneIndexed) 1 else 0
        val min = 0 + oneIndexedAdjustment
        val max = elements.size - 1 + oneIndexedAdjustment
        if (userIndex !in min..max) {
            return@MappedParameter failRange(min.toString(), max.toString(), userIndex.toString())
        }

        val elementIndex = userIndex - oneIndexedAdjustment
        success(ListElementResult(elements[elementIndex], elements, elementIndex))
    }
}

data class ListElementResult<T>(val result: T, val list: List<T>, val index: Int)
