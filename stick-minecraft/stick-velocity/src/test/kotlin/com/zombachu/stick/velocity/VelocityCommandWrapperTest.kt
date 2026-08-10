package com.zombachu.stick.velocity

import com.zombachu.stick.noopFailureHandler
import com.zombachu.stick.structure.command
import com.zombachu.stick.structure.invoke
import com.zombachu.stick.structure.textParameter
import com.zombachu.stick.velocity.structure.permission
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class VelocityCommandWrapperTest {

    @Test
    fun `consecutive spaces are collapsed`() {
        var text: String? = null
        val wrapper = textWrapper { text = it }

        wrapper.execute(FakeInvocation(FakeCommandSource(), "cmd", "hello   world"))

        assertEquals("hello world", text)
    }

    @Test
    fun `leading and trailing spaces produce no empty args`() {
        var text: String? = null
        val wrapper = textWrapper { text = it }

        wrapper.execute(FakeInvocation(FakeCommandSource(), "cmd", "  hello world  "))

        assertEquals("hello world", text)
    }

    @Test
    fun `empty string produces zero args`() {
        var executed = false
        val structure =
            velocityStructure { command("cmd")() { executed = true } }
        val wrapper = VelocityCommandWrapper(environment(), noopFailureHandler(), structure)

        wrapper.execute(FakeInvocation(FakeCommandSource(), "cmd", ""))

        assertTrue(executed)
    }

    @Test
    fun `hasPermission delegates to sender validation`() {
        val structure =
            velocityStructure {
                command("cmd", requirement = permission("stick.cmd"))() {}
            }
        val wrapper = VelocityCommandWrapper(environment(), noopFailureHandler(), structure)

        assertTrue(wrapper.hasPermission(FakeInvocation(FakeCommandSource(["stick.cmd"]), "cmd", "")))
        assertFalse(wrapper.hasPermission(FakeInvocation(FakeCommandSource([]), "cmd", "")))
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
