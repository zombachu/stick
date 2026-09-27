package com.zombachu.stick

import com.zombachu.stick.element.Structure

sealed class Invocation<out E : Environment, S> : ValidationContext<E, S>() {
    abstract val label: String
    abstract val args: List<String>

    abstract fun <T> get(id: TypedIdentifier<T>): T

    abstract fun <T> put(id: TypedIdentifier<T>, value: T)

    abstract fun <T> getOrPut(id: TypedIdentifier<T>, value: T): T

    abstract fun getSyntax(): String

    companion object {
        operator fun <E : Environment, S> invoke(
            sender: S,
            env: E,
            label: String,
            args: List<String>,
            structure: Structure<E, S, *>,
        ): Invocation<E, S> {
            return InvocationImpl(sender, env, label, args, structure, parent = null)
        }
    }
}
