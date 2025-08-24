package com.zombachu.stick.structure

import com.zombachu.stick.ContextualValue
import com.zombachu.stick.SenderContext
import com.zombachu.stick.TypedIdentifier
import com.zombachu.stick.element.Helper
import com.zombachu.stick.element.HelperImpl
import com.zombachu.stick.impl.SenderScope
import com.zombachu.stick.impl.StructureElement

fun <O, S : SenderContext<O>, T : Any> SenderScope<O, S>.helper(
    value: ContextualValue<O, S, T>
): StructureElement<O, S, Helper<O, S, T>> = {
    HelperImpl(value)
}

fun <O, S : SenderContext<O>, T : Any> SenderScope<O, S>.helper(
    id: TypedIdentifier<T>,
): StructureElement<O, S, Helper<O, S, T>> =
    helper { get(id) }
