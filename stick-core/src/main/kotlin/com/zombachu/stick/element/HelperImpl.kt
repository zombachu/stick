package com.zombachu.stick.element

import com.zombachu.stick.CommandResult
import com.zombachu.stick.ContextualValue
import com.zombachu.stick.Environment
import com.zombachu.stick.Execution

internal class HelperImpl<E : Environment, S, T>(private val value: ContextualValue<E, S, T>) :
    Helper<E, S, T>, InternalElement<E, S, T> {
    context(ex: Execution<E, S>)
    override fun parse(args: List<String>): CommandResult<T> {
        return ex.value()
    }
}
