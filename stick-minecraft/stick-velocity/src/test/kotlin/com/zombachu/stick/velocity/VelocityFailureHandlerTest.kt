package com.zombachu.stick.velocity

import com.velocitypowered.api.command.CommandSource
import com.zombachu.stick.Invocation
import com.zombachu.stick.ParsingResult
import com.zombachu.stick.customFailure
import com.zombachu.stick.dsl.command
import com.zombachu.stick.dsl.invoke
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertSame
import kotlin.test.assertTrue

class VelocityFailureHandlerTest {

    private val env = BasicVelocityEnvironment(FakeProxyServer())
    private val structure = velocityStructure { command("cmd")() {} }

    @Test
    fun `sends component with feedback`() {
        val sender = FakeCommandSource()

        context(invocation(sender)) { BasicVelocityFailureHandler(FakeLogger()).onFailure(ParsingResult.failUnknown()) }

        assertEquals(1, sender.sentMessages.size)
        assertTrue(sender.sentMessages.first().toString().contains("unknown"))
    }

    @Test
    fun `sends nothing when empty message`() {
        val sender = FakeCommandSource()

        context(invocation(sender)) { BasicVelocityFailureHandler(FakeLogger()).onFailure(customFailure("")) }

        assertEquals(0, sender.sentMessages.size)
    }

    @Test
    fun `logs the cause of an unknown failure`() {
        val logger = FakeLogger()
        val cause = IllegalStateException("boom")

        context(invocation(FakeCommandSource())) {
            BasicVelocityFailureHandler(logger).onFailure(ParsingResult.failUnknown(cause))
        }

        assertSame(cause, logger.logged.single().second)
    }

    private fun invocation(sender: CommandSource): Invocation<VelocityEnvironment, CommandSource> =
        Invocation(sender, env, "cmd", ["cmd"], structure)
}
