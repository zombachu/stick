package com.zombachu.stick.dsl

import com.zombachu.stick.TypedIdentifier
import com.zombachu.stick.TypedIdentifierImpl
import kotlin.reflect.typeOf

inline fun <reified T> id(name: String): TypedIdentifier<T> {
    return TypedIdentifierImpl(name.replace(" ", "").lowercase(), typeOf<T>().hashCode(), null is T)
}
