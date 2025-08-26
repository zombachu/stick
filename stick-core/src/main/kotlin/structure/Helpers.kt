package com.zombachu.stick.structure

import com.zombachu.stick.ContextualValue
import com.zombachu.stick.Environment
import com.zombachu.stick.TypedIdentifier
import com.zombachu.stick.element.Helper
import com.zombachu.stick.element.HelperImpl
import com.zombachu.stick.impl.SenderScope
import com.zombachu.stick.impl.StructureElement

fun <E : Environment, O, T : Any> SenderScope<E, O>.helper(
    value: ContextualValue<E, O, T>
): StructureElement<E, O, Helper<E, O, T>> = {
    HelperImpl(value)
}

fun <E : Environment, O, T : Any> SenderScope<E, O>.helper(
    id: TypedIdentifier<T>,
): StructureElement<E, O, Helper<E, O, T>> =
    helper { get(id) }
