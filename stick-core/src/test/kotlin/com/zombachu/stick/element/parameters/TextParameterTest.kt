package com.zombachu.stick.element.parameters

import com.zombachu.stick.CommandResult
import com.zombachu.stick.TestEnv
import com.zombachu.stick.element.ElementType
import com.zombachu.stick.expectSuccessValue
import com.zombachu.stick.impl.Size
import com.zombachu.stick.withInvocation
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

class TextParameterTest {

    private val parameter = TextParameter<TestEnv, Unit>("", "")

    @Test
    fun `joins args with single space`() {
        val result = withInvocation { parameter.parse(["hello", "There", "world"]) }
        assertEquals("hello There world", result.expectSuccessValue())
    }

    @Test
    fun `consumes all args`() {
        val result = withInvocation { parameter.parse(["a", "b", "c"]) }
        assertIs<CommandResult.Success<String>>(result)
        assertEquals(3, result.consumed.size)
    }

    @Test
    fun `empty args produce empty string`() {
        val result = withInvocation { parameter.parse([]) }
        assertEquals("", result.expectSuccessValue())
    }

    @Test
    fun `has correct properties`() {
        assertIs<Size.Unbounded>(parameter.size)
        assertEquals(ElementType.Passthrough, parameter.type)
    }
}
