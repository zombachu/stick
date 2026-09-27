package com.zombachu.stick

sealed class ValidationContext<out E : Environment, S> : SenderContext<S> {
    abstract val env: E

    internal abstract fun <S2 : Any> forSender(transform: (S) -> S2): ValidationContext<E, S2>

    companion object {
        internal operator fun <E : Environment, S> invoke(env: E, sender: S): ValidationContext<E, S> {
            return ValidationContextImpl(env, sender)
        }
    }
}
