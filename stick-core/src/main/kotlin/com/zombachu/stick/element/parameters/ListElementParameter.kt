package com.zombachu.stick.element.parameters

import com.zombachu.stick.ContextualValue
import com.zombachu.stick.Environment
import com.zombachu.stick.Invocation
import com.zombachu.stick.ParsingResult
import com.zombachu.stick.Position
import com.zombachu.stick.element.Parameter
import com.zombachu.stick.element.PipelineOperation
import com.zombachu.stick.element.PipelinedParameter
import com.zombachu.stick.valueOrPropagateError

internal fun <E : Environment, S, T> listElementParameter(
    name: String,
    description: String,
    list: ContextualValue<E, S, List<T>>,
    oneIndexed: Boolean,
    onEmpty: (Invocation<E, S>.() -> Unit)?,
): Parameter<E, S, ListElementResult<T>, Position.Leading> {
    val index = NumberParameter<E, S, Int>(name, description, { toIntOrNull() }, Int.MIN_VALUE, Int.MAX_VALUE, "index")

    val toResult: PipelineOperation<E, S, Int, ListElementResult<T>> = lookUp@{ userIndex ->
        val elements =
            list(this).valueOrPropagateError {
                return@lookUp it
            }

        if (onEmpty != null && elements.isEmpty()) {
            onEmpty(this)
            return@lookUp ParsingResult.failHandled()
        }

        val oneIndexedAdjustment = if (oneIndexed) 1 else 0
        val min = 0 + oneIndexedAdjustment
        val max = elements.size - 1 + oneIndexedAdjustment
        if (userIndex !in min..max) {
            return@lookUp ParsingResult.failRange(min.toString(), max.toString(), userIndex.toString())
        }

        val elementIndex = userIndex - oneIndexedAdjustment
        ParsingResult.success(ListElementResult(elements[elementIndex], elements, elementIndex))
    }

    return PipelinedParameter(index, [toResult])
}

data class ListElementResult<T>(val result: T, val list: List<T>, val index: Int)
