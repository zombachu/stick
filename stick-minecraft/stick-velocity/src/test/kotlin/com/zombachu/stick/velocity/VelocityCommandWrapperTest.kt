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

class VelocityCommandWrapperTest {

    @Test
    fun `execute collapses consecutive spaces`() {
        var text: String? = null
        val wrapper = textWrapper { text = it }

        wrapper.execute(FakeInvocation(FakeCommandSource(), "cmd", "hello   world"))

        assertEquals("hello world", text)
    }

    @Test
    fun `execute drops leading and trailing spaces`() {
        var text: String? = null
        val wrapper = textWrapper { text = it }

        wrapper.execute(FakeInvocation(FakeCommandSource(), "cmd", "  hello world  "))

        assertEquals("hello world", text)
    }

    @Test
    fun `execute with no args runs command`() {
        var executed = false
        val structure = velocityStructure {
            command("cmd")() { executed = true }
        }
        val wrapper = VelocityCommandWrapper(environment(), noopFailureHandler(), structure)

        wrapper.execute(FakeInvocation(FakeCommandSource(), "cmd", ""))

        assertTrue(executed)
    }

    @Test
    fun `suggest completes arg`() {
        val structure = velocityStructure {
            command("hello")(
                literalParameter("there")
            ) { }
        }
        val wrapper = VelocityCommandWrapper(environment(), noopFailureHandler(), structure)

        assertEquals(["there"], wrapper.suggest(FakeInvocation(FakeCommandSource(), "hello", "")))
        assertEquals(["there"], wrapper.suggest(FakeInvocation(FakeCommandSource(), "hello", "the")))
        assertEquals([], wrapper.suggest(FakeInvocation(FakeCommandSource(), "hello", "general")))
    }

    @Test
    fun `hasPermission delegates to sender validation`() {
        val structure = velocityStructure {
            command("cmd", requirement = permission("stick.cmd"))() { }
        }
        val wrapper = VelocityCommandWrapper(environment(), noopFailureHandler(), structure)

        assertTrue(wrapper.hasPermission(FakeInvocation(FakeCommandSource(["stick.cmd"]), "cmd", "")))
        assertFalse(wrapper.hasPermission(FakeInvocation(FakeCommandSource(), "cmd", "")))
    }

    private fun environment(): VelocityEnvironment = BasicVelocityEnvironment(FakeProxyServer())

    private fun textWrapper(onExecute: (String) -> Unit): VelocityCommandWrapper<VelocityEnvironment> {
        val structure =
            velocityStructure {
                command("cmd")(textParameter("")) { rest -> onExecute(rest) }
            }
        return VelocityCommandWrapper(environment(), noopFailureHandler(), structure)
    }
}
