package com.zombachu.stick.structure

import com.zombachu.stick.TestEnv
import com.zombachu.stick.structureTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class CommandsTest {

    @Test
    fun `structure delegates to empty scope`() {
        var capturedName: String? = null
        val ignored =
            structure(TestEnv::class, Unit::class) {
                capturedName = name
                command("cmd")()
            }
        assertEquals("", capturedName)
    }

    @Test
    fun `command lowercases name and aliases`() = structureTest {
        val sub = command("CMD", ["ALIAS"])
        assertEquals("cmd", sub.name)
        assertEquals(["alias"], sub.aliases)
    }

    @Test
    fun `first command off parentless scope stays parentless`() = structureTest {
        val top = command("top")
        assertNull(top.parent)
    }
}
