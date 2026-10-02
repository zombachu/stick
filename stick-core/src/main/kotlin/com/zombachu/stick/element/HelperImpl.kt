package com.zombachu.stick.element

import com.zombachu.stick.CommandResult
import com.zombachu.stick.Environment
import com.zombachu.stick.Execution

internal class HelperImpl<E : Environment, S, T>(private val value: suspend Execution<E, S>.() -> CommandResult<T>) :
    Helper<E, S, T>, InternalElement<E, S, T> {
    context(ex: Execution<E, S>)
    override suspend fun parse(args: List<String>): CommandResult<T> {
        return ex.value()
    }
}
