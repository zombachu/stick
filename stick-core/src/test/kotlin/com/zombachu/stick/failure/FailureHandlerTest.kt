package com.zombachu.stick.failure

import com.zombachu.stick.Execution
import com.zombachu.stick.TestEnv
import com.zombachu.stick.failureOrigin
import com.zombachu.stick.testExecutionSender
import kotlin.test.Test
import kotlin.test.assertEquals

class FailureHandlerTest {

    @Test
    fun `TransformedFailureHandler transforms sender`() {
        var sender: String? = null
        val base =
            object : FailureHandler<TestEnv, String> {
                context(ex: Execution<TestEnv, String>)
                override fun onFailure(reason: Reason, origin: FailureOrigin) {
                    sender = ex.sender
                }
            }
        val transformed = TransformedFailureHandler(base, { it: Int -> it.toString() })

        val ex = testExecutionSender(42)
        context(ex) { transformed.onFailure(Reason.Unknown(), failureOrigin()) }

        assertEquals("42", sender)
    }
}
