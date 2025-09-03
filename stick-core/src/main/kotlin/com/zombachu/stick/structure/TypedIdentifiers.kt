package com.zombachu.stick.structure

import com.zombachu.stick.TypedIdentifier
import com.zombachu.stick.TypedIdentifierImpl

inline fun <reified T> id(name: String): TypedIdentifier<T> {
    return TypedIdentifierImpl(name.replace(" ", "").lowercase(), T::class.hashCode(), null is T)
}
