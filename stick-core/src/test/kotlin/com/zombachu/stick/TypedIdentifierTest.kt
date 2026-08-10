package com.zombachu.stick

import com.zombachu.stick.structure.id
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
    fun `KNOWN LIMITATION - identifiers with the same name but different types collide`() {
        // T::class.hashCode() erases to List::class for any List<...>, so id<List<String>> and
        // id<List<Int>> with the same name are indistinguishable - a real, documented type-erasure
        // limitation of the id() scheme, not something a test can "fix". This test locks in the
        // CURRENT (colliding) behavior so a future change here doesn't happen unnoticed.
        val stringList: TypedIdentifier<*> = id<List<String>>("items")
        val intList: TypedIdentifier<*> = id<List<Int>>("items")
        assertEquals(stringList, intList)
    }

    @Test
    fun `identifiers with different names are never equal`() {
        val a = id<String>("a")
        val b = id<String>("b")
        assertNotEquals(a, b)
    }
}
