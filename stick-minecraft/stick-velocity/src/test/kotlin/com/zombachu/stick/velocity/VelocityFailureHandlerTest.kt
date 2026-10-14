package com.zombachu.stick.velocity

import com.velocitypowered.api.command.CommandSource
import com.zombachu.stick.Execution
import com.zombachu.stick.MessageReason
import com.zombachu.stick.failure.Reason
import com.zombachu.stick.failureOrigin
import com.zombachu.stick.testExecution
import io.mockk.mockk
import io.mockk.verify
import org.slf4j.Logger
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class VelocityFailureHandlerTest {

    private val env = BasicVelocityEnvironment(FakeProxyServer())

    @Test
    fun `sends component with reason`() {
        val sender = FakeCommandSource()

        context(execution(sender)) {
            BasicVelocityFailureHandler(mockk()).onFailure(Reason.Unknown(), failureOrigin())
        }

        assertEquals(1, sender.sentMessages.size)
        assertTrue(sender.sentMessages.first().toString().contains("unknown"))
    }

    @Test
    fun `sends nothing when empty message`() {
        val sender = FakeCommandSource()

        context(execution(sender)) {
            BasicVelocityFailureHandler(mockk()).onFailure(MessageReason(""), failureOrigin())
        }

        assertEquals(0, sender.sentMessages.size)
    }

    @Test
    fun `logs the cause of an unknown failure`() {
        val logger = mockk<Logger>(relaxed = true)
        val cause = IllegalStateException("boom")

        context(execution(FakeCommandSource())) {
            BasicVelocityFailureHandler(logger).onFailure(Reason.Unknown(cause), failureOrigin())
        }

        verify { logger.error(any<String>(), cause) }
    }

    private fun execution(sender: CommandSource): Execution<VelocityEnvironment, CommandSource> =
        testExecution(env, sender)
}
