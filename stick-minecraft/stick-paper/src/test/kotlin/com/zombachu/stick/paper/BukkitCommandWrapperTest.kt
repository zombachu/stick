package com.zombachu.stick.paper

import com.zombachu.stick.dsl.command
import com.zombachu.stick.dsl.invoke
import com.zombachu.stick.dsl.literalParameter
import com.zombachu.stick.dsl.requireIs
import com.zombachu.stick.dsl.textParameter
import com.zombachu.stick.noopFailureHandler
import com.zombachu.stick.paper.dsl.permission
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertSame
import kotlin.test.assertTrue

class BukkitCommandWrapperTest {

    @Test
    fun `execute joins label and args and returns true`() {
        var text: String? = null
        val structure =
            bukkitStructure {
                command("cmd")(textParameter("")) { text = it }
            }
        val wrapper = BukkitCommandWrapper(FakeBukkitEnvironment(), noopFailureHandler(), structure)

        val result = wrapper.execute(FakeCommandSender(), "cmd", arrayOf("hello", "world"))

        assertTrue(result)
        assertEquals("hello world", text)
    }

    @Test
    fun `execute with no args runs command`() {
        var executed = false
        val structure = bukkitStructure {
            command("cmd")() { executed = true }
        }
        val wrapper = BukkitCommandWrapper(FakeBukkitEnvironment(), noopFailureHandler(), structure)

        val result = wrapper.execute(FakeCommandSender(), "cmd", arrayOf())

        assertTrue(result)
        assertTrue(executed)
    }

    @Test
    fun `execute accepts a namespaced label`() {
        var text: String? = null
        val structure =
            bukkitStructure {
                command("cmd")(textParameter("")) { text = it }
            }
        val wrapper = BukkitCommandWrapper(FakeBukkitEnvironment(), noopFailureHandler(), structure)

        val result = wrapper.execute(FakeCommandSender(), "fake-plugin:cmd", arrayOf("hello", "world"))

        assertTrue(result)
        assertEquals("hello world", text)
    }

    @Test
    fun `tabComplete completes arg`() {
        val structure = bukkitStructure {
            command("hello")(
                literalParameter("there")
            ) { }
        }
        val wrapper = BukkitCommandWrapper(FakeBukkitEnvironment(), noopFailureHandler(), structure)

        assertEquals(["there"], wrapper.tabComplete(FakeCommandSender(), "hello", arrayOf("")))
        assertEquals(["there"], wrapper.tabComplete(FakeCommandSender(), "hello", arrayOf("the")))
        assertEquals([], wrapper.tabComplete(FakeCommandSender(), "hello", arrayOf("general")))
    }

    @Test
    fun `tabComplete completes arg for a namespaced label`() {
        val structure = bukkitStructure {
            command("hello")(
                literalParameter("there")
            ) { }
        }
        val wrapper = BukkitCommandWrapper(FakeBukkitEnvironment(), noopFailureHandler(), structure)

        assertEquals(["there"], wrapper.tabComplete(FakeCommandSender(), "fake-plugin:hello", arrayOf("the")))
    }

    @Test
    fun `tabComplete ignores consecutive spaces`() {
        val structure = bukkitStructure {
            command("hello")(
                literalParameter("there")
            ) { }
        }
        val wrapper = BukkitCommandWrapper(FakeBukkitEnvironment(), noopFailureHandler(), structure)

        assertEquals(["there"], wrapper.tabComplete(FakeCommandSender(), "hello", arrayOf("", "the")))
    }

    @Test
    fun `testPermissionSilent delegates to sender validation`() {
        val structure = bukkitStructure {
            command("cmd", requirement = permission("stick.cmd"))() { }
        }
        val wrapper = BukkitCommandWrapper(FakeBukkitEnvironment(), noopFailureHandler(), structure)

        assertTrue(wrapper.testPermissionSilent(FakeCommandSender(["stick.cmd"])))
        assertFalse(wrapper.testPermissionSilent(FakeCommandSender()))
    }

    @Test
    fun `testPermissionSilent sees base permission of a sender-narrowed command`() {
        val structure = bukkitStructure {
            requireIs(FakeCommandSender::class) {
                command("cmd", requirement = permission("stick.cmd"))() { }
            }
        }
        val wrapper = BukkitCommandWrapper(FakeBukkitEnvironment(), noopFailureHandler(), structure)

        assertTrue(wrapper.testPermissionSilent(FakeCommandSender(["stick.cmd"])))
        assertFalse(wrapper.testPermissionSilent(FakeCommandSender()))
    }

    @Test
    fun `getPlugin returns environment plugin`() {
        val structure = bukkitStructure { command("cmd")() }
        val wrapper = BukkitCommandWrapper(FakeBukkitEnvironment(FakePlugin), noopFailureHandler(), structure)

        assertSame(FakePlugin, wrapper.getPlugin())
    }
}
