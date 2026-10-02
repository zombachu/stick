package com.zombachu.stick.dsl

import com.zombachu.stick.CommandResult
import com.zombachu.stick.ContextualValue
import com.zombachu.stick.Environment
import com.zombachu.stick.Execution
import com.zombachu.stick.StructureScope
import com.zombachu.stick.TypedIdentifier
import com.zombachu.stick.element.Helper
import com.zombachu.stick.element.HelperImpl
import com.zombachu.stick.success
import com.zombachu.stick.withAsyncContext

fun <E : Environment, S, T> StructureScope<E, S>.helper(value: ContextualValue<E, S, T>): Helper<E, S, T> =
    HelperImpl(value)

fun <E : Environment, S, T> StructureScope<E, S>.helper(id: TypedIdentifier<T>): Helper<E, S, T> = helper {
    success(get(id))
}

fun <E : Environment, S, T> StructureScope<E, S>.helperAsync(
    value: suspend Execution<E, S>.() -> CommandResult<T>
): Helper<E, S, T> = HelperImpl { withAsyncContext { value(this) } }
