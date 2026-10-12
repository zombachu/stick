package com.zombachu.stick

import kotlin.coroutines.EmptyCoroutineContext
import kotlin.test.Test
import kotlin.test.assertEquals

class RunTest {

    @Test
    fun `cancelled run completes once`() {
        var completions = 0
        val run = Run<Unit>(EmptyCoroutineContext, EmptyCoroutineContext) { completions++ }

        run.cancel()
        run.startUndispatched {}

        assertEquals(1, completions)
    }
}
