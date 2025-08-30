package com.zombachu.stick.feedback

import com.zombachu.stick.Environment
import com.zombachu.stick.Invocation
import com.zombachu.stick.Result
import com.zombachu.stick.impl.InvocationImpl

interface FailureHandler<out E : Environment, S> {
    fun onFailure(inv: Invocation<@UnsafeVariance E, S>, result: Result.Failure<*>)
}

internal class TransformedFailureHandler<out E : Environment, S, S2 : Any>(
    val failureHandler: FailureHandler<E, S2>,
    val transform: (S) -> S2
) : FailureHandler<E, S> {

    override fun onFailure(inv: Invocation<@UnsafeVariance E, S>, result: Result.Failure<*>) {
        val newInvocation = (inv as InvocationImpl).forSender(transform)
        failureHandler.onFailure(newInvocation, result)
    }
}
