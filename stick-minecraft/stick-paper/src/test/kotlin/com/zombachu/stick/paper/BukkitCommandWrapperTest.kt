package com.zombachu.stick.paper

import com.zombachu.stick.dsl.command
import com.zombachu.stick.dsl.invoke
import com.zombachu.stick.dsl.textParameter
import com.zombachu.stick.noopFailureHandler
import kotlin.test.Test
import kotlin.test.assertEquals
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
    fun `getPlugin returns environment plugin`() {
        val structure = bukkitStructure { command("cmd")() }
        val wrapper = BukkitCommandWrapper(FakeBukkitEnvironment(FakePlugin), noopFailureHandler(), structure)

        assertSame(FakePlugin, wrapper.getPlugin())
    }
}
