package com.zombachu.stick.structure

import com.zombachu.stick.ContextualValue
import com.zombachu.stick.SenderContext
import com.zombachu.stick.TypedIdentifier
import com.zombachu.stick.element.Helper
import com.zombachu.stick.element.HelperImpl
import com.zombachu.stick.impl.SenderScope
import com.zombachu.stick.impl.StructureElement

fun <S : SenderContext, T : Any> SenderScope<S>.helper(
    value: ContextualValue<S, T>
): StructureElement<S, Helper<S, T>> = {
    HelperImpl(value)
}

fun <S : SenderContext, T : Any> SenderScope<S>.helper(
    id: TypedIdentifier<T>,
): StructureElement<S, Helper<S, T>> =
    helper { get(id) }
