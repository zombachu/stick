package com.zombachu.stick

import kotlin.reflect.KClass
import kotlin.reflect.KProperty

class TypedIdentifier<T : Any>(
    val name: String,
    val type: KClass<T>,
) {
    operator fun getValue(thisRef: Any?, property: KProperty<*>): String = name
}
