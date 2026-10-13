package com.zombachu.stick

import java.util.concurrent.ConcurrentHashMap
import kotlin.coroutines.AbstractCoroutineContextElement
import kotlin.coroutines.CoroutineContext

internal class RunGroup : AbstractCoroutineContextElement(RunGroup) {
    companion object Key : CoroutineContext.Key<RunGroup>

    private val runs = ConcurrentHashMap.newKeySet<Run<*>>()

    internal fun add(run: Run<*>) {
        runs += run
    }

    internal fun remove(run: Run<*>) {
        runs -= run
    }

    fun cancel() {
        runs.forEach { it.cancel() }
    }
}
