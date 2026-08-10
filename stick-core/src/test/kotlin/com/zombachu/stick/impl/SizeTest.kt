package com.zombachu.stick.impl

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertSame
import kotlin.test.assertTrue

class SizeTest {

    @Test
    fun `Fixed only matches exact size`() {
        val size = Size(3)
        assertTrue(size.matches(3))
        assertFalse(size.matches(2))
        assertFalse(size.matches(4))
    }

    @Test
    fun `non-Fixed Sizes match any size`() {
        val unbounded = Size.Unbounded
        val deferred = Size.Deferred

        assertTrue(unbounded.matches(0))
        assertTrue(unbounded.matches(100))
        assertTrue(deferred.matches(0))
        assertTrue(deferred.matches(100))
    }

    @Test
    fun `Fixed plus Fixed sums sizes`() {
        val combined = Size(2) + Size(3)
        assertEquals(5, combined.size)
    }

    @Test
    fun `Unbounded plus Fixed yields Unbounded`() {
        val unbounded = Size.Unbounded
        val fixed = Size(3)
        assertSame(Size.Unbounded, unbounded + fixed)
        assertSame(Size.Unbounded, fixed + unbounded)
    }

    @Test
    fun `Deferred plus any yields Deferred`() {
        val deferred = Size.Deferred
        val fixed = Size(3)
        val unbounded = Size.Unbounded

        assertSame(Size.Deferred, deferred + fixed)
        assertSame(Size.Deferred, fixed + deferred)
        assertSame(Size.Deferred, deferred + unbounded)
        assertSame(Size.Deferred, unbounded + deferred)
    }

    @Test
    fun `parsingPriority ranks Sizes`() {
        val fixed = Size(1)
        val unbounded = Size.Unbounded
        val deferred = Size.Deferred
        assertTrue(fixed.parsingPriority < unbounded.parsingPriority)
        assertTrue(unbounded.parsingPriority < deferred.parsingPriority)
    }
}
