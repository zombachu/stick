package com.zombachu.stick.element

import com.zombachu.stick.CommandResult
import com.zombachu.stick.ContextualValue
import com.zombachu.stick.Environment
import com.zombachu.stick.Invocation

internal class HelperImpl<E : Environment, S, T>(private val value: ContextualValue<E, S, T>) : Helper<E, S, T> {
    context(inv: Invocation<E, S>)
    override fun parse(args: List<String>): CommandResult<T> {
        return inv.value()
    }
}
