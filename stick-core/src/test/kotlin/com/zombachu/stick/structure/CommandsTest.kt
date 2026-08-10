package com.zombachu.stick.structure

import com.zombachu.stick.TestEnv
import com.zombachu.stick.structureTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertSame

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

    // TODO
    @Test
    fun `KNOWN BUG - a nested command never actually chains to its parent`() = structureTest {
        // command()'s parent assignment is `if (this.parent == null) null else this` - it checks
        // whether the CURRENT scope's own parent is null, not whether the current scope IS the root.
        // Since StructureScope.empty() always starts with parent == null, and this check never sets
        // parent to non-null unless it was already non-null, `parent` stays null forever through any
        // chain of .command() calls starting from an empty scope - "top.command(\"nested\")" gets
        // parent = null too, not `top`. This test documents the INTENDED chaining behavior and is
        // expected to fail against the current source - do not "fix" this test to match the bug.
        val top = command("top")
        val nested = top.command("nested")
        assertSame(top, nested.parent)
    }
}
