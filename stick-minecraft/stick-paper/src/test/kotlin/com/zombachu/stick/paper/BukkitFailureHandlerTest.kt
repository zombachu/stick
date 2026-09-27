package com.zombachu.stick.paper

import com.zombachu.stick.Execution
import com.zombachu.stick.MessageReason
import com.zombachu.stick.failure.Reason
import com.zombachu.stick.failureOrigin
import com.zombachu.stick.testExecution
import java.util.logging.Handler
import java.util.logging.LogRecord
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertSame
import kotlin.test.assertTrue
import org.bukkit.command.CommandSender

class BukkitFailureHandlerTest {

    private val env = FakeBukkitEnvironment()

    @Test
    fun `sends component with reason`() {
        val sender = FakeCommandSender()

        context(execution(sender)) { BasicBukkitFailureHandler().onFailure(Reason.Unknown(), failureOrigin()) }

        assertEquals(1, sender.sentMessages.size)
        assertTrue(sender.sentMessages.first().toString().contains("unknown"))
    }

    @Test
    fun `sends nothing when empty message`() {
        val sender = FakeCommandSender()

        context(execution(sender)) { BasicBukkitFailureHandler().onFailure(MessageReason(""), failureOrigin()) }

        assertEquals(0, sender.sentMessages.size)
    }

    @Test
    fun `logs the cause of an unknown failure`() {
        val records = mutableListOf<LogRecord>()
        val captor =
            object : Handler() {
                override fun publish(record: LogRecord) {
                    records += record
                }

                override fun flush() = Unit

                override fun close() = Unit
            }
        val cause = IllegalStateException("boom")
        FakePlugin.logger.addHandler(captor)

        try {
            context(execution(FakeCommandSender())) {
                BasicBukkitFailureHandler().onFailure(Reason.Unknown(cause), failureOrigin())
            }
        } finally {
            FakePlugin.logger.removeHandler(captor)
        }

        assertSame(cause, records.single().thrown)
    }

    private fun execution(sender: CommandSender): Execution<BukkitEnvironment, CommandSender> =
        testExecution(env, sender)
}
