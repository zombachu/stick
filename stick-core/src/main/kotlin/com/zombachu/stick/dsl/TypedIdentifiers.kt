package com.zombachu.stick.dsl

import com.zombachu.stick.TypedIdentifier
import kotlin.reflect.typeOf

inline fun <reified T> id(name: String): TypedIdentifier<T> {
    return TypedIdentifier(name.replace(" ", "").lowercase(), typeOf<T>().hashCode())
}
