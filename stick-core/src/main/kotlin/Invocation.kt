package com.zombachu.stick

import com.zombachu.stick.element.Structure
import com.zombachu.stick.impl.InvocationImpl
import com.zombachu.stick.impl.SenderScope

interface Invocation<E : Environment, O> : SenderScope<E, O> {
    // TODO: Remove
    val sender: O
    val env: E

    val label: String
    val args: List<String>

    fun <T : Any> get(id: TypedIdentifier<T>): T
    fun <T : Any> getOrPut(id: TypedIdentifier<T>, defaultValue: () -> T): T
    fun getSyntax(): String

    companion object {
        operator fun <E : Environment, O> invoke(env: E, label: String, args: List<String>, structure: Structure<E, O>): Invocation<E, O> {
            return InvocationImpl(env, label, args, structure, parent = null)
        }
    }
}
