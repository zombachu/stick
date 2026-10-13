package com.zombachu.stick.velocity

import com.velocitypowered.api.command.CommandSource
import com.velocitypowered.api.event.proxy.ProxyShutdownEvent
import com.zombachu.stick.CommandResult
import com.zombachu.stick.Invocation
import com.zombachu.stick.Suggestion
import com.zombachu.stick.asCoroutineContext
import com.zombachu.stick.dsl.command
import com.zombachu.stick.dsl.invoke
import com.zombachu.stick.element.Parameter
import com.zombachu.stick.success
import com.zombachu.stick.toSuggestions
import com.zombachu.stick.withAsyncContext
import java.util.concurrent.Executor
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

class VelocityStickTest {

    @Test
    fun `registerCommand registers adapter with command meta`() {
        val manager = FakeCommandManager()
        val plugin = Any()
        val stick = VelocityStick(plugin, FakeProxyServer(manager), FakeLogger())
        val structure = velocityStructure { command("cmd", aliases = ["c"])() }

        stick.withContext { register(structure) }

        assertEquals(1, manager.registerCalls)
        assertEquals(["cmd", "c"], manager.registeredMeta?.aliases?.toList())
        assertEquals(plugin, manager.registeredMeta?.plugin)
        assertIs<VelocityCommandAdapter<*>>(manager.registeredCommand)
    }

    @Test
    fun `registerCommand registers one shutdown handler for all commands`() {
        val events = FakeEventManager()
        val stick = VelocityStick(Any(), FakeProxyServer(events = events), FakeLogger())
        val first = velocityStructure { command("first")() }
        val second = velocityStructure { command("second")() }

        stick.withContext {
            register(first)
            register(second)
        }

        assertEquals(ProxyShutdownEvent::class.java, events.handlers.single().first)
    }

    @Test
    fun `proxy shutdown abandons open suggestions`() {
        val manager = FakeCommandManager()
        val events = FakeEventManager()
        val stick =
            VelocityStick(Any(), FakeProxyServer(manager, events), FakeLogger(), Executor {}.asCoroutineContext())
        val structure = velocityStructure {
            command("hello")(AsyncNameParameter()) { }
        }
        stick.withContext { register(structure) }
        val adapter = manager.registeredCommand as VelocityCommandAdapter<*>
        val suggestions = adapter.suggestAsync(FakeInvocation(FakeCommandSource(), "hello", ""))

        val ignored = events.fire(ProxyShutdownEvent())

        assertEquals([], suggestions.getNow(null))
    }

    private class AsyncNameParameter : Parameter.Size1<VelocityEnvironment, CommandSource, String>("name", "") {
        context(inv: Invocation<VelocityEnvironment, CommandSource>)
        override suspend fun suggest(preceding: List<String>, partial: String): List<Suggestion> =
            withAsyncContext { ["there"].toSuggestions() }

        context(inv: Invocation<VelocityEnvironment, CommandSource>)
        override fun resolve(arg0: String): CommandResult<String> = success(arg0)
    }
}
