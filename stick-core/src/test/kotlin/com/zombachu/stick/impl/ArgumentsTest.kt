package com.zombachu.stick.impl

import com.zombachu.stick.withInvocation
import kotlin.test.Test
import kotlin.test.assertEquals

class ArgumentsTest {

    @Test
    fun `Arguments1 executes with single value`() {
        var captured: Int? = null
        val args = Arguments1(42)

        withInvocation { args.execute { a -> captured = a } }

        assertEquals(42, captured)
    }

    @Test
    fun `Arguments12 executes with values in order`() {
        var captured: List<Int>? = null
        val args = Arguments12(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12)

        withInvocation { args.execute { a, b, c, d, e, f, g, h, i, j, k, l -> captured = [a, b, c, d, e, f, g, h, i, j, k, l] } }

        assertEquals([1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12], captured)
    }
}
