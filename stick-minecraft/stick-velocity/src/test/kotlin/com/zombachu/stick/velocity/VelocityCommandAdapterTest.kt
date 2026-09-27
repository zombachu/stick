package com.zombachu.stick.velocity

import com.zombachu.stick.dsl.command
import com.zombachu.stick.dsl.invoke
import com.zombachu.stick.dsl.literalParameter
import com.zombachu.stick.dsl.textParameter
import com.zombachu.stick.noopFailureHandler
import com.zombachu.stick.velocity.dsl.permission
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class VelocityCommandAdapterTest {

    @Test
    fun `execute collapses consecutive spaces`() {
        var text: String? = null
        val adapter = textAdapter { text = it }

        adapter.execute(FakeInvocation(FakeCommandSource(), "cmd", "hello   world"))

        assertEquals("hello world", text)
    }

    @Test
    fun `execute drops leading and trailing spaces`() {
        var text: String? = null
        val adapter = textAdapter { text = it }

        adapter.execute(FakeInvocation(FakeCommandSource(), "cmd", "  hello world  "))

        assertEquals("hello world", text)
    }

    @Test
    fun `execute with no args runs command`() {
        var executed = false
        val structure = velocityStructure {
            command("cmd")() { executed = true }
        }
        val adapter = VelocityCommandAdapter(environment(), noopFailureHandler(), structure)

        adapter.execute(FakeInvocation(FakeCommandSource(), "cmd", ""))

        assertTrue(executed)
    }

    @Test
    fun `suggest completes arg`() {
        val structure = velocityStructure {
            command("hello")(
                literalParameter("there")
            ) { }
        }
        val adapter = VelocityCommandAdapter(environment(), noopFailureHandler(), structure)

        assertEquals(["there"], adapter.suggest(FakeInvocation(FakeCommandSource(), "hello", "")))
        assertEquals(["there"], adapter.suggest(FakeInvocation(FakeCommandSource(), "hello", "the")))
        assertEquals([], adapter.suggest(FakeInvocation(FakeCommandSource(), "hello", "general")))
    }

    @Test
    fun `suggest ignores consecutive spaces`() {
        val structure = velocityStructure {
            command("hello")(
                literalParameter("there")
            ) { }
        }
        val adapter = VelocityCommandAdapter(environment(), noopFailureHandler(), structure)

        assertEquals(["there"], adapter.suggest(FakeInvocation(FakeCommandSource(), "hello", "  the")))
    }

    @Test
    fun `hasPermission delegates to sender validation`() {
        val structure = velocityStructure {
            command("cmd", requirement = permission("stick.cmd"))() { }
        }
        val adapter = VelocityCommandAdapter(environment(), noopFailureHandler(), structure)

        assertTrue(adapter.hasPermission(FakeInvocation(FakeCommandSource(["stick.cmd"]), "cmd", "")))
        assertFalse(adapter.hasPermission(FakeInvocation(FakeCommandSource(), "cmd", "")))
    }

    private fun environment(): VelocityEnvironment = BasicVelocityEnvironment(FakeProxyServer())

    private fun textAdapter(onExecute: (String) -> Unit): VelocityCommandAdapter<VelocityEnvironment> {
        val structure =
            velocityStructure {
                command("cmd")(textParameter("")) { rest -> onExecute(rest) }
            }
        return VelocityCommandAdapter(environment(), noopFailureHandler(), structure)
    }
}
