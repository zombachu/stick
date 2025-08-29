package com.zombachu.stick

import kotlin.reflect.KClass
import kotlin.reflect.KProperty

sealed interface TypedIdentifier<T : Any> {
    val name: String
    val type: KClass<T>

    operator fun getValue(thisRef: Any?, property: KProperty<*>): String = name
}

@PublishedApi
internal class TypedIdentifierImpl<T : Any>(
    override val name: String,
    override val type: KClass<T>,
) : TypedIdentifier<T>
