package com.zombachu.stick

import com.zombachu.stick.element.Structure
import com.zombachu.stick.impl.InvocationImpl
import com.zombachu.stick.impl.SenderScope

interface Invocation<S : Environment, O> : SenderScope<S, O> {
    // TODO: Remove
    val sender: O
    val env: S

    val label: String
    val args: List<String>

    fun <T : Any> get(id: TypedIdentifier<T>): T
    fun <T : Any> getOrPut(id: TypedIdentifier<T>, defaultValue: () -> T): T
    fun getSyntax(): String

    companion object {
        operator fun <S : Environment, O> invoke(env: S, label: String, args: List<String>, structure: Structure<S, O>): Invocation<S, O> {
            return InvocationImpl(env, label, args, structure, parent = null)
        }
    }
}
