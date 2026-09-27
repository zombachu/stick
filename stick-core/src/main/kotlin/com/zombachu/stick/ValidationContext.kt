package com.zombachu.stick

import com.zombachu.stick.failure.FailureOrigin

sealed class ValidationContext<out E : Environment, S> {
    abstract val env: E
    abstract val sender: S

    internal abstract fun createFailureOrigin(): FailureOrigin

    internal abstract fun <S2 : Any> forSender(transform: (S) -> S2): ValidationContext<E, S2>

    companion object {
        internal operator fun <E : Environment, S> invoke(env: E, sender: S): ValidationContext<E, S> {
            return ValidationContextImpl(env, sender)
        }
    }
}
