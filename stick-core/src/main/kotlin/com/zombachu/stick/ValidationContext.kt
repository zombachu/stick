package com.zombachu.stick

import com.zombachu.stick.impl.ValidationContextImpl

interface ValidationContext<E : Environment, S> : SenderContext<S> {
    val env: E

    fun <S2 : Any> forSender(transform: (S) -> S2): ValidationContext<E, S2>

    companion object {
        operator fun <E : Environment, S> invoke(env: E, sender: S): ValidationContext<E, S> {
            return ValidationContextImpl(env, sender)
        }
    }
}
