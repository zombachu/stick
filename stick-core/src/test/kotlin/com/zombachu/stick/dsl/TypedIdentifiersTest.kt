package com.zombachu.stick.dsl

import com.zombachu.stick.TypedIdentifier
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

class TypedIdentifiersTest {

    @Test
    fun `id strips spaces and lowercases name`() {
        val identifier by id<Int>("My Id")
        assertEquals("myid", identifier)
    }

    @Test
    fun `id detects nullable types`() {
        val int: TypedIdentifier<*> = id<Int>("n")
        val nullableInt: TypedIdentifier<*> = id<Int?>("n")
        assertNotEquals(int, nullableInt)
    }
}
