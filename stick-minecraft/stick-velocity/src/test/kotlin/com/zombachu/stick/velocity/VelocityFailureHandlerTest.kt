package com.zombachu.stick.velocity

import com.velocitypowered.api.command.CommandSource
import com.zombachu.stick.Invocation
import com.zombachu.stick.MessageReason
import com.zombachu.stick.failure.Reason
import com.zombachu.stick.failureOrigin
import com.zombachu.stick.testInvocation
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertSame
import kotlin.test.assertTrue

class VelocityFailureHandlerTest {

    private val env = BasicVelocityEnvironment(FakeProxyServer())

    @Test
    fun `sends component with reason`() {
        val sender = FakeCommandSource()

        context(invocation(sender)) {
            BasicVelocityFailureHandler(FakeLogger()).onFailure(Reason.Unknown(), failureOrigin())
        }

        assertEquals(1, sender.sentMessages.size)
        assertTrue(sender.sentMessages.first().toString().contains("unknown"))
    }

    @Test
    fun `sends nothing when empty message`() {
        val sender = FakeCommandSource()

        context(invocation(sender)) {
            BasicVelocityFailureHandler(FakeLogger()).onFailure(MessageReason(""), failureOrigin())
        }

        assertEquals(0, sender.sentMessages.size)
    }

    @Test
    fun `logs the cause of an unknown failure`() {
        val logger = FakeLogger()
        val cause = IllegalStateException("boom")

        context(invocation(FakeCommandSource())) {
            BasicVelocityFailureHandler(logger).onFailure(Reason.Unknown(cause), failureOrigin())
        }

        assertSame(cause, logger.logged.single().second)
    }

    private fun invocation(sender: CommandSource): Invocation<VelocityEnvironment, CommandSource> =
        testInvocation(env, sender)
}
