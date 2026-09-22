package com.zombachu.stick

sealed interface ValidationContext<out E : Environment, S> : SenderContext<S> {
    val env: E

    companion object {
        operator fun <E : Environment, S> invoke(env: E, sender: S): ValidationContext<E, S> {
            return ValidationContextImpl(env, sender)
        }
    }
}
