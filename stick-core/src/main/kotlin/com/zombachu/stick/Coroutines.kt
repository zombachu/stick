package com.zombachu.stick

import java.util.concurrent.Executor
import kotlin.coroutines.AbstractCoroutineContextElement
import kotlin.coroutines.Continuation
import kotlin.coroutines.ContinuationInterceptor
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.cancellation.CancellationException
import kotlin.coroutines.coroutineContext
import kotlin.coroutines.startCoroutine
import kotlin.coroutines.suspendCoroutine

fun Executor.asCoroutineContext(): CoroutineContext = ExecutorInterceptor(this)

private class ExecutorInterceptor(private val executor: Executor) :
    AbstractCoroutineContextElement(ContinuationInterceptor), ContinuationInterceptor {
    override fun <T> interceptContinuation(continuation: Continuation<T>): Continuation<T> =
        Continuation(continuation.context) { result -> executor.execute { continuation.resumeWith(result) } }
}

/** Runs [block] on the async context, then continues in the caller's context. */
suspend fun <T> withAsyncContext(block: suspend () -> T): T {
    val run = checkNotNull(coroutineContext[Run]) { "withAsyncContext called outside Stick" }
    run.ensureActive()
    val asyncContext = run.asyncContext ?: throw AsyncSuggestionSkipped()
    val result = run.runOn(asyncContext + run, block)
    run.ensureActive()
    return result
}

internal class AsyncSuggestionSkipped : CancellationException()

/** Runs [block] on the command's main context, then continues in the caller's context. */
suspend fun <T> withMainContext(block: () -> T): T {
    val run = checkNotNull(coroutineContext[Run]) { "withMainContext called outside Stick" }
    return run.runOn(run.mainContext) { block() }
}

/** Runs [block] on [context], then continues in the caller's context. */
private suspend fun <T> Run<*>.runOn(context: CoroutineContext, block: suspend () -> T): T =
    suspendCoroutine { caller ->
        block.startCoroutine(Continuation(context) { resume(caller, it) })
    }
