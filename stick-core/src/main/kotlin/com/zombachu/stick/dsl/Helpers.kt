package com.zombachu.stick.dsl

import com.zombachu.stick.CommandResult
import com.zombachu.stick.Environment
import com.zombachu.stick.Execution
import com.zombachu.stick.StructureScope
import com.zombachu.stick.TypedIdentifier
import com.zombachu.stick.element.Helper
import com.zombachu.stick.element.HelperImpl
import com.zombachu.stick.success

fun <E : Environment, S, T> StructureScope<E, S>.helper(
    value: suspend Execution<E, S>.() -> CommandResult<T>
): Helper<E, S, T> = HelperImpl(value)

fun <E : Environment, S, T> StructureScope<E, S>.helper(id: TypedIdentifier<T>): Helper<E, S, T> = helper {
    success(get(id))
}
