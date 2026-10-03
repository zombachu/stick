package com.zombachu.stick

import java.util.concurrent.Executor
import kotlin.coroutines.AbstractCoroutineContextElement
import kotlin.coroutines.Continuation
import kotlin.coroutines.ContinuationInterceptor
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.coroutineContext
import kotlin.coroutines.intrinsics.createCoroutineUnintercepted
import kotlin.coroutines.resume
import kotlin.coroutines.startCoroutine
import kotlin.coroutines.suspendCoroutine

fun Executor.asCoroutineContext(): CoroutineContext = ExecutorInterceptor(this)

private class ExecutorInterceptor(private val executor: Executor) :
    AbstractCoroutineContextElement(ContinuationInterceptor), ContinuationInterceptor {
    override fun <T> interceptContinuation(continuation: Continuation<T>): Continuation<T> =
        Continuation(continuation.context) { result -> executor.execute { continuation.resumeWith(result) } }
}

/** Runs [block] on the async context, then continues in the caller's context. */
internal suspend fun <T> withAsyncContext(block: suspend () -> T): T {
    val routing = checkNotNull(coroutineContext[StickCoroutineContext]) { "withAsyncContext called outside Stick" }
    return runOn(routing.asyncContext + routing, block)
}

internal object SkipAsyncSuggestions : CoroutineContext.Element, CoroutineContext.Key<SkipAsyncSuggestions> {
    override val key: CoroutineContext.Key<*> = this
}

/** Runs [block] on the command's main context, then continues in the caller's context. */
suspend fun <T> withMainContext(block: () -> T): T {
    val routing = checkNotNull(coroutineContext[StickCoroutineContext]) { "withMainContext called outside Stick" }
    return runOn(routing.mainContext) { block() }
}

/** Runs [block] on [context], then continues in the caller's context. */
private suspend fun <T> runOn(context: CoroutineContext, block: suspend () -> T): T = suspendCoroutine { caller ->
    block.startCoroutine(Continuation(context) { caller.resumeWith(it) })
}

/** Runs [block] on the calling thread until it first suspends. */
internal fun <T> startUndispatched(context: CoroutineContext, block: suspend () -> T, onComplete: (Result<T>) -> Unit) {
    block.createCoroutineUnintercepted(Continuation(context, onComplete)).resume(Unit)
}
