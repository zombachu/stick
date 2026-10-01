package com.zombachu.stick.paper

import com.zombachu.stick.dsl.command
import com.zombachu.stick.dsl.invoke
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

class PaperStickTest {

    @Test
    fun `registerCommand registers adapter under plugin fallback prefix`() {
        val commandMap = fakeCommandMap()
        val stick = PaperStick(FakePlugin)
        val structure = paperStructure { command("cmd", aliases = ["c"])() }

        stick.withContext { register(structure) }

        val (fallbackPrefix, command) = commandMap.registered.single()
        assertEquals("fake-plugin", fallbackPrefix)
        assertEquals(["c"], command.aliases)
        assertIs<PaperCommandAdapter<*>>(command)
    }
}
