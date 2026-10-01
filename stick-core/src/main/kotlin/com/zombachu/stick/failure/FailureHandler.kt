package com.zombachu.stick.failure

import com.zombachu.stick.Environment
import com.zombachu.stick.Execution

interface FailureHandler<in E : Environment, S> {
    context(ex: Execution<E, S>)
    fun onFailure(reason: Reason, origin: FailureOrigin)
}

internal class TransformedFailureHandler<E : Environment, S, S2 : Any>(
    private val base: FailureHandler<E, S2>,
    private val transform: (S) -> S2,
) : FailureHandler<E, S> {
    context(ex: Execution<E, S>)
    override fun onFailure(reason: Reason, origin: FailureOrigin) {
        val transformedExecution = ex.forSender(transform)
        context(transformedExecution) { base.onFailure(reason, origin) }
    }
}
