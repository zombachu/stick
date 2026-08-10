package com.zombachu.stick.element

import kotlin.test.Test
import kotlin.test.assertTrue

class ElementTypeTest {

    @Test
    fun `parsingPriority ranks ElementTypes`() {
        assertTrue(ElementType.Helper.parsingPriority < ElementType.Flag.parsingPriority)
        assertTrue(ElementType.Flag.parsingPriority < ElementType.Literal.parsingPriority)
        assertTrue(ElementType.Literal.parsingPriority < ElementType.Default.parsingPriority)
        assertTrue(ElementType.Default.parsingPriority < ElementType.Passthrough.parsingPriority)
    }
}
