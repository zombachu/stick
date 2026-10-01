package com.zombachu.stick

import kotlin.reflect.KProperty

class TypedIdentifier<T>
@PublishedApi
internal constructor(
    private val name: String,
    private val typeHashCode: Int,
) {

    operator fun getValue(thisRef: Any?, property: KProperty<*>): String = name

    override fun equals(other: Any?): Boolean =
        other is TypedIdentifier<*> && name == other.name && typeHashCode == other.typeHashCode

    override fun hashCode(): Int = 31 * name.hashCode() + typeHashCode
}
