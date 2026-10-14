package com.zombachu.stick.velocity

import com.velocitypowered.api.command.CommandSource
import com.zombachu.stick.Arguments
import com.zombachu.stick.AsyncNameParameter
import com.zombachu.stick.StructureScope
import com.zombachu.stick.SuspendingNameParameter
import com.zombachu.stick.asCoroutineContext
import com.zombachu.stick.dsl.command
import com.zombachu.stick.dsl.flag
import com.zombachu.stick.dsl.invoke
import com.zombachu.stick.dsl.literalParameter
import com.zombachu.stick.dsl.textParameter
import com.zombachu.stick.element.Structure
import com.zombachu.stick.noopFailureHandler
import com.zombachu.stick.velocity.dsl.permission
import java.util.concurrent.Executor
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class VelocityCommandAdapterTest {

    @Test
    fun `execute collapses consecutive spaces`() {
        var text: String? = null
        val adapter = adapter {
            command("cmd")(textParameter("")) { text = it }
        }

        adapter.execute(FakeInvocation(FakeCommandSource(), "cmd", "hello   world"))

        assertEquals("hello world", text)
    }

    @Test
    fun `execute drops leading and trailing spaces`() {
        var text: String? = null
        val adapter = adapter {
            command("cmd")(textParameter("")) { text = it }
        }

        adapter.execute(FakeInvocation(FakeCommandSource(), "cmd", "  hello world  "))

        assertEquals("hello world", text)
    }

    @Test
    fun `execute with no args runs command`() {
        var executed = false
        val adapter = adapter {
            command("cmd")() { executed = true }
        }

        adapter.execute(FakeInvocation(FakeCommandSource(), "cmd", ""))

        assertTrue(executed)
    }

    @Test
    fun `suggest completes arg`() {
        val adapter = adapter {
            command("hello")(
                literalParameter("there")
            ) { }
        }

        assertEquals(["there"], adapter.suggestAsync(FakeInvocation(FakeCommandSource(), "hello", "")).join())
        assertEquals(["there"], adapter.suggestAsync(FakeInvocation(FakeCommandSource(), "hello", "the")).join())
        assertEquals([], adapter.suggestAsync(FakeInvocation(FakeCommandSource(), "hello", "general")).join())
    }

    @Test
    fun `suggest includes suspending parameter suggestions`() {
        val adapter = adapter {
            command("hello")(
                SuspendingNameParameter(),
                flag("loud"),
            ) { _, _ -> }
        }

        assertEquals(["-loud", "there"], adapter.suggestAsync(FakeInvocation(FakeCommandSource(), "hello", "")).join())
    }

    @Test
    fun `suggest ignores consecutive spaces`() {
        val adapter = adapter {
            command("hello")(
                literalParameter("there")
            ) { }
        }

        assertEquals(["there"], adapter.suggestAsync(FakeInvocation(FakeCommandSource(), "hello", "  the")).join())
    }

    @Test
    fun `suggest abandons sender's previous suggestions`() {
        val adapter = adapter(Executor {}.asCoroutineContext()) {
            command("hello")(AsyncNameParameter()) { }
        }
        val source = FakeCommandSource()

        val first = adapter.suggestAsync(FakeInvocation(source, "hello", ""))
        val second = adapter.suggestAsync(FakeInvocation(source, "hello", "t"))

        assertEquals([], first.getNow(null))
        assertFalse(second.isDone)
    }

    @Test
    fun `hasPermission delegates to sender validation`() {
        val adapter = adapter {
            command("cmd", requirement = permission("stick.cmd"))() { }
        }

        assertTrue(adapter.hasPermission(FakeInvocation(FakeCommandSource(["stick.cmd"]), "cmd", "")))
        assertFalse(adapter.hasPermission(FakeInvocation(FakeCommandSource(), "cmd", "")))
    }

    private fun <T_ : Arguments> adapter(
        asyncContext: CoroutineContext = EmptyCoroutineContext,
        block:
            StructureScope<VelocityEnvironment, CommandSource>.() -> Structure<VelocityEnvironment, CommandSource, T_>,
    ): VelocityCommandAdapter<VelocityEnvironment> {
        val environment = BasicVelocityEnvironment(FakeProxyServer())
        return VelocityCommandAdapter(environment, noopFailureHandler(), velocityStructure(block), asyncContext)
    }
}
