package com.zombachu.stick

import com.zombachu.stick.element.Structure

sealed class Execution<out E : Environment, S> : Invocation<E, S>() {
    abstract val label: String
    abstract val args: List<String>

    abstract fun <T> get(id: TypedIdentifier<T>): T

    abstract fun <T> put(id: TypedIdentifier<T>, value: T)

    abstract fun <T> getOrPut(id: TypedIdentifier<T>, value: T): T

    abstract fun getSyntax(): String

    abstract override fun <S2 : Any> forSender(transform: (S) -> S2): Execution<E, S2>

    companion object {
        internal operator fun <E : Environment, S> invoke(
            sender: S,
            env: E,
            label: String,
            args: List<String>,
            structure: Structure<E, S, *>,
        ): Execution<E, S> {
            return ExecutionImpl(sender, env, label, args, structure)
        }
    }
}
