package com.zombachu.stick

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertIs
import kotlin.test.assertTrue

class SizeTest {

    @Test
    fun `Bounded exact size matches only that size`() {
        val size = Size(3)

        assertTrue(size.matches(3))
        assertFalse(size.matches(2))
        assertFalse(size.matches(4))
    }

    @Test
    fun `Bounded matches sizes within its range`() {
        val size = Size.between(1, 3)

        assertTrue(size.matches(1))
        assertTrue(size.matches(3))
        assertFalse(size.matches(0))
        assertFalse(size.matches(4))
    }

    @Test
    fun `Unbounded matches any size at or above its min`() {
        val size = Size.atLeast(2)

        assertTrue(size.matches(2))
        assertTrue(size.matches(100))
        assertFalse(size.matches(1))

        val zeroMinimum: Size = Size.atLeast(0)
        assertTrue(zeroMinimum.matches(0))
    }

    @Test
    fun `Bounded exact Size has equal bounds`() {
        val size = Size(2)

        assertEquals(2, size.min)
        assertEquals(2, size.max)
        assertEquals(Size.between(2, 2).max, size.max)
    }

    @Test
    fun `plus sums bounds`() {
        val combined = Size.between(1, 2) + Size.between(2, 3)

        assertEquals(3, combined.min)
        assertEquals(5, combined.max)
    }

    @Test
    fun `plus of exact sizes stays exact`() {
        val combined = Size(2) + Size(3)

        assertEquals(5, combined.min)
        assertEquals(5, combined.max)
    }

    @Test
    fun `plus Unbounded yields Unbounded`() {
        val combined = Size.between(1, 2) + Size.atLeast(0)

        assertIs<Size.Unbounded>(combined)
        assertEquals(1, combined.min)
    }

    @Test
    fun `plus narrows to typed Size`() {
        val bounded: Size = Size.between(2, 3)
        val unbounded: Size = Size.atLeast(0)

        assertIs<Size.Bounded>(Size.between(1, 2) + bounded)
        assertIs<Size.Unbounded>(Size.between(1, 2) + unbounded)
    }

    @Test
    fun `parsingPriority ranks Sizes`() {
        val bounded = Size.between(1, 2)
        val unbounded = Size.atLeast(0)

        assertTrue(bounded.parsingPriority < unbounded.parsingPriority)
    }
}
