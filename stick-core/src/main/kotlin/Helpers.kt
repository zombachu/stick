package com.zombachu.stick

import com.zombachu.stick.impl.HelperElement
import com.zombachu.stick.impl.SenderScope
import com.zombachu.stick.impl.StructureElement

fun <S, T : Any> SenderScope<S>.helper(
    value: ContextualValue<S, T>
): StructureElement<S, HelperElement<S, T>> = {
    HelperElement(value)
}

fun <S, T : Any> SenderScope<S>.helper(
    id: TypedIdentifier<T>,
): StructureElement<S, HelperElement<S, T>> =
    helper { get(id) }
