package com.zombachu.stick.failure

import com.zombachu.stick.Invocation
import com.zombachu.stick.TestEnv
import com.zombachu.stick.failureOrigin
import com.zombachu.stick.testInvocationSender
import kotlin.test.Test
import kotlin.test.assertEquals

class FailureHandlerTest {

    @Test
    fun `TransformedFailureHandler transforms sender`() {
        var sender: String? = null
        val base =
            object : FailureHandler<TestEnv, String> {
                context(inv: Invocation<TestEnv, String>)
                override fun onFailure(reason: Reason, origin: FailureOrigin) {
                    sender = inv.sender
                }
            }
        val transformed = TransformedFailureHandler(base, { it: Int -> it.toString() })

        val inv = testInvocationSender(42)
        context(inv) { transformed.onFailure(Reason.Unknown(), failureOrigin()) }

        assertEquals("42", sender)
    }
}
