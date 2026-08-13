package com.zombachu.stick.paper

import com.zombachu.stick.Invocation
import com.zombachu.stick.ParsingResult
import com.zombachu.stick.customFailure
import com.zombachu.stick.dsl.command
import com.zombachu.stick.dsl.invoke
import org.bukkit.command.CommandSender
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class BukkitFailureHandlerTest {

    private val env = FakeBukkitEnvironment()
    private val structure = bukkitStructure { command("cmd")() }

    @Test
    fun `sends component with feedback`() {
        val sender = FakeCommandSender()

        context(invocation(sender)) { BasicBukkitFailureHandler().onFailure(ParsingResult.failUnknown()) }

        assertEquals(1, sender.sentMessages.size)
        assertTrue(sender.sentMessages.first().toString().contains("unknown"))
    }

    @Test
    fun `sends nothing when empty message`() {
        val sender = FakeCommandSender()

        context(invocation(sender)) { BasicBukkitFailureHandler().onFailure(customFailure("")) }

        assertEquals(0, sender.sentMessages.size)
    }

    private fun invocation(sender: CommandSender): Invocation<BukkitEnvironment, CommandSender> =
        Invocation(sender, env, "cmd", ["cmd"], structure)
}
