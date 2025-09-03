package com.zombachu.stick

import kotlin.reflect.KProperty

sealed interface TypedIdentifier<T> {
    val name: String
    val typeHashCode: Int
    val nullable: Boolean

    operator fun getValue(thisRef: Any?, property: KProperty<*>): String = name
}

@PublishedApi
internal data class TypedIdentifierImpl<T>(
    override val name: String,
    override val typeHashCode: Int,
    override val nullable: Boolean,
) : TypedIdentifier<T>
