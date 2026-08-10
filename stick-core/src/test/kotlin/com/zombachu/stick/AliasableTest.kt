package com.zombachu.stick

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertIs
import kotlin.test.assertTrue
import com.zombachu.stick.plus as aliasEntryPlus

class AliasableTest {

    @Test
    fun `matches label exactly`() {
        val entry = AliasEntry("foo", ["bar"])
        assertTrue(entry.matches("foo"))
    }

    @Test
    fun `matches any alias`() {
        val entry = AliasEntry("foo", ["bar", "baz"])
        assertTrue(entry.matches("bar"))
        assertTrue(entry.matches("baz"))
    }

    @Test
    fun `does not match unrelated input`() {
        val entry = AliasEntry("foo", ["bar"])
        assertFalse(entry.matches("qux"))
    }

    @Test
    fun `matching is case sensitive`() {
        val entry = AliasEntry("foo", ["bar"])
        assertFalse(entry.matches("Foo"))
        assertFalse(entry.matches("BAR"))
    }

    @Test
    fun `lowercase transforms all elements`() {
        val result = setOf("Foo", "BAR", "baz").lowercase()
        assertEquals(setOf("foo", "bar", "baz"), result)
    }

    @Test
    fun `plus extension builds AliasEntry`() {
        // Call via aliased import as natural infix "foo" + setOf(...) is shadowed (see below)
        val entry = "foo".aliasEntryPlus(setOf("bar", "baz"))
        assertEquals("foo", entry.label)
        assertEquals(setOf("bar", "baz"), entry.aliases)
    }

    @Test
    fun `plus operator syntax shadows stdlib plus`() {
        val result: Any = "foo" + setOf("bar", "baz")
        assertIs<String>(result)
    }

    @Test
    fun `AliasEntry defaults to no aliases`() {
        val entry = AliasEntry("foo")
        assertEquals([], entry.aliases)
    }
}
