package com.zombachu.stick.structure

import com.zombachu.stick.ContextualValue
import com.zombachu.stick.Environment
import com.zombachu.stick.ParsingResult
import com.zombachu.stick.TypedIdentifier
import com.zombachu.stick.element.Helper
import com.zombachu.stick.element.HelperImpl
import com.zombachu.stick.impl.StructureScope

fun <E : Environment, S, T> StructureScope<E, S>.helper(
    value: ContextualValue<E, S, T>
): Helper<E, S, T> =
    HelperImpl(value)

fun <E : Environment, S, T> StructureScope<E, S>.helper(
    id: TypedIdentifier<T>,
): Helper<E, S, T> =
    helper { ParsingResult.success(get(id)) }
