package com.zombachu.stick.element.parameters

import com.zombachu.stick.ConsumingResult
import com.zombachu.stick.MatchResult
import com.zombachu.stick.PeekingResult
import com.zombachu.stick.Size
import com.zombachu.stick.TestEnv
import com.zombachu.stick.element.ElementType
import com.zombachu.stick.expectSuccessValue
import com.zombachu.stick.withInvocation
import com.zombachu.stick.withValidationContext
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertSame

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
        assertIs<ConsumingResult.Success<String>>(result)
        assertEquals(3, result.consumed)
    }

    @Test
    fun `empty args fail with InvalidSizeError`() {
        val result = withInvocation { parameter.parse([]) }
        assertSame(PeekingResult.InvalidSizeError, result)
    }

    @Test
    fun `match claims all args`() {
        assertEquals(MatchResult.matched(3), withValidationContext { parameter.match(["a", "b", "c"]) })
    }

    @Test
    fun `match on empty args is partial`() {
        assertEquals(MatchResult.partial(0), withValidationContext { parameter.match([]) })
    }

    @Test
    fun `has correct properties`() {
        assertIs<Size.Unbounded>(parameter.size)
        assertEquals(ElementType.Passthrough, parameter.type)
    }
}
