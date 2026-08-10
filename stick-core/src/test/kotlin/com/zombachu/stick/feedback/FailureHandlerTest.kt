package com.zombachu.stick.feedback

import com.zombachu.stick.CommandResult
import com.zombachu.stick.Invocation
import com.zombachu.stick.ParsingResult
import com.zombachu.stick.TestEnv
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
                override fun <F : Feedback> onFailure(failure: CommandResult.Failure<F>) {
                    sender = inv.sender
                }
            }
        val transformed = TransformedFailureHandler(base, { it: Int -> it.toString() })

        val inv = testInvocationSender(42)
        context(inv) { transformed.onFailure(ParsingResult.failUnknown()) }

        assertEquals("42", sender)
    }
}
