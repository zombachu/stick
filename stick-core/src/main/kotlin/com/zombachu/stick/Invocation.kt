package com.zombachu.stick

import com.zombachu.stick.element.Structure
import com.zombachu.stick.impl.InvocationImpl

interface Invocation<E : Environment, S> : ValidationContext<E, S> {
    val label: String
    val args: List<String>

    fun <T : Any> get(id: TypedIdentifier<T>): T
    fun <T : Any> getOrPut(id: TypedIdentifier<T>, defaultValue: () -> T): T
    fun getSyntax(): String

    companion object {
        operator fun <E : Environment, S> invoke(sender: S, env: E, label: String, args: List<String>,
                                                 structure: Structure<E, S>): Invocation<E, S> {
            return InvocationImpl(sender, env, label, args, structure, parent = null)
        }
    }
}
