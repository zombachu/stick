package com.zombachu.stick

import com.zombachu.stick.failure.FailureOrigin

sealed class Invocation<out E : Environment, S> {
    abstract val env: E
    abstract val sender: S

    internal abstract fun createFailureOrigin(): FailureOrigin

    internal abstract fun <S2 : Any> forSender(transform: (S) -> S2): Invocation<E, S2>

    companion object {
        internal operator fun <E : Environment, S> invoke(env: E, sender: S): Invocation<E, S> {
            return InvocationImpl(env, sender)
        }
    }
}
