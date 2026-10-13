package com.zombachu.stick

import java.util.concurrent.RejectedExecutionException
import java.util.concurrent.atomic.AtomicBoolean
import kotlin.coroutines.AbstractCoroutineContextElement
import kotlin.coroutines.Continuation
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.cancellation.CancellationException
import kotlin.coroutines.intrinsics.createCoroutineUnintercepted
import kotlin.coroutines.resume

internal class Run<T>(
    val mainContext: CoroutineContext,
    val asyncContext: CoroutineContext?,
    private val onComplete: (Result<T>) -> Unit,
) : AbstractCoroutineContextElement(Run) {
    companion object Key : CoroutineContext.Key<Run<*>>

    private val group = asyncContext?.get(RunGroup)
    @Volatile private var isCancelled = false
    private val isComplete = AtomicBoolean()

    fun startUndispatched(block: suspend () -> T) {
        group?.add(this)
        block.createCoroutineUnintercepted(Continuation(mainContext + this, ::complete)).resume(Unit)
    }

    fun ensureActive() {
        if (isCancelled) throw RunCancelled()
    }

    fun cancel() {
        isCancelled = true
        complete(Result.failure(RunCancelled()))
    }

    fun <R> resume(caller: Continuation<R>, result: Result<R>) {
        try {
            caller.resumeWith(result)
        } catch (_: RejectedExecutionException) {
            cancel()
        }
    }

    private fun complete(result: Result<T>) {
        group?.remove(this)
        if (isComplete.compareAndSet(false, true)) onComplete(result)
    }
}

internal class RunCancelled : CancellationException()
