package com.zombachu.stick.dsl

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class TypedIdentifiersTest {

    @Test
    fun `id strips spaces and lowercases name`() {
        val identifier = id<Int>("My Id")
        assertEquals("myid", identifier.name)
    }

    @Test
    fun `id detects nullable types`() {
        assertFalse(id<Int>("n").nullable)
        assertTrue(id<Int?>("n").nullable)
    }
}
