package com.zombachu.stick

import com.zombachu.stick.element.Structure
import com.zombachu.stick.impl.InvocationImpl
import com.zombachu.stick.impl.SenderScope

interface Invocation<E : Environment, S> : SenderScope<E, S> {
    // TODO: Remove
    val sender: S
    val env: E

    val label: String
    val args: List<String>

    fun <T : Any> get(id: TypedIdentifier<T>): T
    fun <T : Any> getOrPut(id: TypedIdentifier<T>, defaultValue: () -> T): T
    fun getSyntax(): String

    companion object {
        operator fun <E : Environment, S> invoke(env: E, label: String, args: List<String>, structure: Structure<E, S>): Invocation<E, S> {
            return InvocationImpl(env, label, args, structure, parent = null)
        }
    }
}
