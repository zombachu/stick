package com.zombachu.stick.structure

import com.zombachu.stick.TypedIdentifier

inline fun <reified T : Any> id(name: String): TypedIdentifier<T> {
    return TypedIdentifier(name.lowercase(), T::class)
}
