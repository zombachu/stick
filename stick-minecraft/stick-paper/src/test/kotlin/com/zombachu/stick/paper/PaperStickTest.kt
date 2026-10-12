package com.zombachu.stick.paper

import com.zombachu.stick.dsl.command
import com.zombachu.stick.dsl.invoke
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

class PaperStickTest {

    @Test
    fun `registerCommand registers adapter on commands lifecycle event`() {
        val plugin = FakeLifecyclePlugin()
        val stick = PaperStick(plugin)
        val structure = paperStructure { command("cmd", aliases = ["c"])() }
        val registrar = FakeRegistrar()

        stick.withContext(FakePaperEnvironment(plugin)) { register(structure) }
        plugin.lifecycleEvents.fire(LifecycleEvents.COMMANDS, FakeCommandsEvent(registrar))

        val (node, aliases) = registrar.registered.single()
        assertEquals("cmd", node.literal)
        assertEquals(["c"], aliases)
        assertIs<PaperCommandAdapter<*>>(node.command)
    }
}
