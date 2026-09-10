package com.zombachu.stick.element

import kotlin.test.Test
import kotlin.test.assertTrue

class GroupableTypeTest {

    @Test
    fun `parsingPriority ranks GroupableTypes`() {
        assertTrue(GroupableType.Literal.parsingPriority < GroupableType.Default.parsingPriority)
        assertTrue(GroupableType.Default.parsingPriority < GroupableType.Passthrough.parsingPriority)
    }
}
