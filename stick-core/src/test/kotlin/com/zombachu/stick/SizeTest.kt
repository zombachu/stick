package com.zombachu.stick

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertIs
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
    fun `Variable matches sizes within its range`() {
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
    fun `between for equal bounds collapses to Fixed`() {
        assertIs<Size.Fixed>(Size.between(2, 2))
        assertIs<Size.Variable>(Size.between(2, 3))
    }

    @Test
    fun `Fixed and Variable are Bounded but Unbounded is not`() {
        assertIs<Size.Bounded>(Size(3))
        assertIs<Size.Bounded>(Size.between(1, 3))
        assertFalse((Size.atLeast(0) as Size) is Size.Bounded)
    }

    @Test
    fun `Fixed plus Fixed sums sizes`() {
        val combined = Size(2) + Size(3)
        assertEquals(5, combined.size)
    }

    @Test
    fun `plus Unbounded yields Unbounded`() {
        val combined = Size.between(1, 2) + Size.atLeast(0)

        assertIs<Size.Unbounded>(combined)
        assertEquals(1, combined.min)
    }

    @Test
    fun `plus sums bounds`() {
        val combined = Size.between(1, 2) + Size.between(2, 3)

        assertIs<Size.Bounded>(combined)
        assertEquals(3, combined.min)
        assertEquals(5, combined.max)
    }

    @Test
    fun `plus narrows to typed Size`() {
        val bounded: Size = Size.between(2, 3)
        val unbounded: Size = Size.atLeast(0)
        val fixed: Size = Size(2)

        assertIs<Size.Bounded>(Size.between(1, 2) + bounded)
        assertIs<Size.Unbounded>(Size.between(1, 2) + unbounded)
        assertIs<Size.Fixed>(fixed + fixed)
    }

    @Test
    fun `parsingPriority ranks Sizes`() {
        val fixed = Size(1)
        val variable = Size.between(1, 2)
        val unbounded = Size.atLeast(0)

        assertTrue(fixed.parsingPriority < variable.parsingPriority)
        assertTrue(variable.parsingPriority < unbounded.parsingPriority)
    }
}
