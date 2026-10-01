package com.zombachu.stick

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

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
        assertFalse(entry.matches("baz"))
    }

    @Test
    fun `matching is case-sensitive`() {
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
    fun `AliasEntry defaults to no aliases`() {
        val entry = AliasEntry("foo")
        assertEquals([], entry.aliases)
    }
}
