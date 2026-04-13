package com.zombachu.stick.feedback

import com.zombachu.stick.CommandResult
import com.zombachu.stick.Environment
import com.zombachu.stick.Invocation
import com.zombachu.stick.impl.InvocationImpl

interface FailureHandler<E : Environment, S> {
    context(inv: Invocation<E, S>)
    fun <F : Feedback> onFailure(failure: CommandResult.Failure<F>)
}

internal class TransformedFailureHandler<E : Environment, S, S2 : Any>(
    val base: FailureHandler<E, S2>,
    val transform: (S) -> S2,
) : FailureHandler<E, S> {
    context(inv: Invocation<E, S>)
    override fun <F : Feedback> onFailure(failure: CommandResult.Failure<F>) {
        val transformedInvocation = (inv as InvocationImpl).forSender(transform)
        context(transformedInvocation) { base.onFailure(failure) }
    }
}
