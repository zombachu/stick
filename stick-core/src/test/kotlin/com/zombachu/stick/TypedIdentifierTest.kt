package com.zombachu.stick

import com.zombachu.stick.dsl.id
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

class TypedIdentifierTest {

    @Test
    fun `getValue delegate returns identifier name`() {
        val id by id<String>("name")
        assertEquals("name", id)
    }

    @Test
    fun `identifiers with same name and type are equal`() {
        val a = id<List<String>>("items")
        val b = id<List<String>>("items")
        assertEquals(a, b)
    }

    @Test
    fun `identifiers with same name and different types are not equal`() {
        val stringList: TypedIdentifier<*> = id<List<String>>("items")
        val intList: TypedIdentifier<*> = id<List<Int>>("items")
        assertNotEquals(stringList, intList)
    }

    @Test
    fun `identifiers with different names are never equal`() {
        val a = id<String>("a")
        val b = id<String>("b")
        assertNotEquals(a, b)
    }
}
