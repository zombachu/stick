package com.zombachu.stick

import com.zombachu.stick.failure.FailureOrigin

private val unknownOrigin = FailureOrigin(null) { "" }

internal class ValidationContextImpl<E : Environment, S>(override val env: E, override val sender: S) :
    ValidationContext<E, S>() {
    override fun createFailureOrigin(): FailureOrigin = unknownOrigin

    override fun <S2 : Any> forSender(transform: (S) -> S2): ValidationContext<E, S2> =
        ValidationContextImpl(env, transform(sender))
}
