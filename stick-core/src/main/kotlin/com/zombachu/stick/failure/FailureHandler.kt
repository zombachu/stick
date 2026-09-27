package com.zombachu.stick.failure

import com.zombachu.stick.Environment
import com.zombachu.stick.Invocation
import com.zombachu.stick.InvocationImpl

interface FailureHandler<in E : Environment, S> {
    context(inv: Invocation<E, S>)
    fun onFailure(reason: Reason, origin: FailureOrigin)
}

internal class TransformedFailureHandler<E : Environment, S, S2 : Any>(
    val base: FailureHandler<E, S2>,
    val transform: (S) -> S2,
) : FailureHandler<E, S> {
    context(inv: Invocation<E, S>)
    override fun onFailure(reason: Reason, origin: FailureOrigin) {
        val transformedInvocation = (inv as InvocationImpl).forSender(transform)
        context(transformedInvocation) { base.onFailure(reason, origin) }
    }
}
