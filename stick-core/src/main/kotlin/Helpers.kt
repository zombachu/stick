package com.zombachu.stick

import com.zombachu.stick.impl.Helper
import com.zombachu.stick.impl.HelperImpl
import com.zombachu.stick.impl.SenderScope
import com.zombachu.stick.impl.StructureElement

fun <S, T : Any> SenderScope<S>.helper(
    value: ContextualValue<S, T>
): StructureElement<S, Helper<S, T>> = {
    HelperImpl(value)
}

fun <S, T : Any> SenderScope<S>.helper(
    id: TypedIdentifier<T>,
): StructureElement<S, Helper<S, T>> =
    helper { get(id) }
