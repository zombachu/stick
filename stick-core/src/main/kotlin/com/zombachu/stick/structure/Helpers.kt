package com.zombachu.stick.structure

import com.zombachu.stick.ContextualValue
import com.zombachu.stick.Environment
import com.zombachu.stick.TypedIdentifier
import com.zombachu.stick.element.Helper
import com.zombachu.stick.element.HelperImpl
import com.zombachu.stick.impl.BuilderScope
import com.zombachu.stick.impl.StructureElement

fun <E : Environment, S, T : Any> BuilderScope<E, S>.helper(
    value: ContextualValue<E, S, T>
): StructureElement<E, S, Helper<E, S, T>> = {
    HelperImpl(value)
}

fun <E : Environment, S, T : Any> BuilderScope<E, S>.helper(
    id: TypedIdentifier<T>,
): StructureElement<E, S, Helper<E, S, T>> =
    helper { get(id) }
