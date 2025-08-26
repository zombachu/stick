package com.zombachu.stick.structure

import com.zombachu.stick.ContextualValue
import com.zombachu.stick.Environment
import com.zombachu.stick.TypedIdentifier
import com.zombachu.stick.element.Helper
import com.zombachu.stick.element.HelperImpl
import com.zombachu.stick.impl.SenderScope
import com.zombachu.stick.impl.StructureElement

fun <S : Environment, O, T : Any> SenderScope<S, O>.helper(
    value: ContextualValue<S, O, T>
): StructureElement<S, O, Helper<S, O, T>> = {
    HelperImpl(value)
}

fun <S : Environment, O, T : Any> SenderScope<S, O>.helper(
    id: TypedIdentifier<T>,
): StructureElement<S, O, Helper<S, O, T>> =
    helper { get(id) }
