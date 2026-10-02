package com.zombachu.stick

import java.util.concurrent.Executor
import kotlin.coroutines.AbstractCoroutineContextElement
import kotlin.coroutines.Continuation
import kotlin.coroutines.ContinuationInterceptor
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.intrinsics.createCoroutineUnintercepted
import kotlin.coroutines.resume

fun Executor.asCoroutineContext(): CoroutineContext = ExecutorInterceptor(this)

private class ExecutorInterceptor(private val executor: Executor) :
    AbstractCoroutineContextElement(ContinuationInterceptor), ContinuationInterceptor {
    override fun <T> interceptContinuation(continuation: Continuation<T>): Continuation<T> =
        Continuation(continuation.context) { result -> executor.execute { continuation.resumeWith(result) } }
}

/** Runs [block] on the calling thread until it first suspends. */
internal fun <T> startUndispatched(context: CoroutineContext, block: suspend () -> T, onComplete: (Result<T>) -> Unit) {
    block.createCoroutineUnintercepted(Continuation(context, onComplete)).resume(Unit)
}
