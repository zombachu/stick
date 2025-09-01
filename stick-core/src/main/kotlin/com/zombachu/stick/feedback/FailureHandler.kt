package com.zombachu.stick.feedback

import com.zombachu.stick.CommandResult
import com.zombachu.stick.Environment
import com.zombachu.stick.Invocation
import com.zombachu.stick.impl.InvocationImpl

interface FailureHandler<out E : Environment, S> {
    context(inv: Invocation<@UnsafeVariance E, S>)
    fun onFailure(failure: CommandResult.Failure<*>)
}

internal class TransformedFailureHandler<out E : Environment, S, S2 : Any>(
    val base: FailureHandler<E, S2>,
    val transform: (S) -> S2
) : FailureHandler<E, S> {
    context(inv: Invocation<@UnsafeVariance E, S>)
    override fun onFailure(failure: CommandResult.Failure<*>) {
        val transformedInvocation = (inv as InvocationImpl).forSender(transform)
        context(transformedInvocation) {
            base.onFailure(failure)
        }
    }
}
