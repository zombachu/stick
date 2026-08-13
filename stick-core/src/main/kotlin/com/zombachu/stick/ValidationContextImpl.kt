package com.zombachu.stick

internal class ValidationContextImpl<E : Environment, S>(override val env: E, override val sender: S) :
    ValidationContext<E, S> {
    override fun <S2 : Any> forSender(transform: (S) -> S2): ValidationContext<E, S2> {
        return ValidationContextImpl(env, transform(sender))
    }
}
