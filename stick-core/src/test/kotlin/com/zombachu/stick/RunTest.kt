package com.zombachu.stick

import kotlin.coroutines.EmptyCoroutineContext
import kotlin.coroutines.suspendCoroutine
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

class RunTest {

    @Test
    fun `cancelled run completes once`() {
        var completions = 0
        val run = Run<Unit>(EmptyCoroutineContext, EmptyCoroutineContext) { completions++ }

        run.cancel()
        run.startUndispatched {}

        assertEquals(1, completions)
    }

    @Test
    fun `cancelled group cancels open run`() {
        var result: Result<Unit>? = null
        val group = RunGroup()
        val run = Run(EmptyCoroutineContext, asyncContext = group) { result = it }
        run.startUndispatched { suspendCoroutine { } }

        group.cancel()

        assertIs<RunCancelled>(result?.exceptionOrNull())
    }

    @Test
    fun `cancelled group leaves completed run alone`() {
        var completions = 0
        val group = RunGroup()
        val run = Run<Unit>(EmptyCoroutineContext, asyncContext = group) { completions++ }
        run.startUndispatched {}

        group.cancel()

        assertEquals(1, completions)
    }
}
