package com.zombachu.stick.paper

import com.zombachu.stick.AsyncNameParameter
import com.zombachu.stick.asCoroutineContext
import com.zombachu.stick.dsl.command
import com.zombachu.stick.dsl.invoke
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents
import java.util.concurrent.Executor
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
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

    @Test
    fun `registerCommand registers one listener for all commands`() {
        val plugin = FakeLifecyclePlugin()
        val stick = PaperStick(plugin)
        val first = paperStructure { command("first")() }
        val second = paperStructure { command("second")() }

        stick.withContext(FakePaperEnvironment(plugin)) {
            register(first)
            register(second)
        }

        assertIs<PluginDisableListener>(plugin.listeners.single())
    }

    @Test
    fun `plugin disable abandons open suggestions`() {
        val plugin = FakeLifecyclePlugin()
        val stick = PaperStick(plugin, Executor {}.asCoroutineContext())
        val structure = paperStructure {
            command("hello")(AsyncNameParameter()) { }
        }
        val registrar = FakeRegistrar()
        stick.withContext(FakePaperEnvironment(plugin)) { register(structure) }
        plugin.lifecycleEvents.fire(LifecycleEvents.COMMANDS, FakeCommandsEvent(registrar))
        val suggestions = registrar.suggestions("hello ")

        val listener = plugin.listeners.single() as PluginDisableListener
        listener.onDisable(plugin)

        assertEquals([], suggestions.getNow(null))
    }

    @Test
    fun `plugin disable ignores other plugins`() {
        val plugin = FakeLifecyclePlugin()
        val stick = PaperStick(plugin, Executor {}.asCoroutineContext())
        val structure = paperStructure {
            command("hello")(AsyncNameParameter()) { }
        }
        val registrar = FakeRegistrar()
        stick.withContext(FakePaperEnvironment(plugin)) { register(structure) }
        plugin.lifecycleEvents.fire(LifecycleEvents.COMMANDS, FakeCommandsEvent(registrar))
        val suggestions = registrar.suggestions("hello ")

        val listener = plugin.listeners.single() as PluginDisableListener
        listener.onDisable(FakeLifecyclePlugin())

        assertFalse(suggestions.isDone)
    }
}
