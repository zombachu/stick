package com.zombachu.stick.failure

import com.zombachu.stick.Execution
import com.zombachu.stick.TestEnv
import com.zombachu.stick.failureOrigin
import com.zombachu.stick.testExecutionSender
import io.mockk.mockk
import io.mockk.verify
import kotlin.test.Test

class FailureHandlerTest {

    @Test
    fun `TransformedFailureHandler transforms sender`() {
        val base = mockk<FailureHandler<TestEnv, String>>(relaxed = true)
        val transformed = TransformedFailureHandler(base, { it: Int -> it.toString() })

        val ex = testExecutionSender(42)
        context(ex) { transformed.onFailure(Reason.Unknown(), failureOrigin()) }

        verify { context(match<Execution<TestEnv, String>> { it.sender == "42" }) { base.onFailure(any(), any()) } }
    }
}
