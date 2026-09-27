package com.zombachu.stick

import com.zombachu.stick.failure.FailureOrigin

private val unknownOrigin = FailureOrigin(null) { "" }

internal class InvocationImpl<E : Environment, S>(override val env: E, override val sender: S) : Invocation<E, S>() {
    override fun createFailureOrigin(): FailureOrigin = unknownOrigin

    override fun <S2 : Any> forSender(transform: (S) -> S2): Invocation<E, S2> = InvocationImpl(env, transform(sender))
}
