package com.zombachu.stick.element.parameters

import com.zombachu.stick.ConsumingResult
import com.zombachu.stick.MatchResult
import com.zombachu.stick.Size
import com.zombachu.stick.TestEnv
import com.zombachu.stick.element.GroupableType
import com.zombachu.stick.expectSuccessValue
import com.zombachu.stick.withExecution
import com.zombachu.stick.withInvocation
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

class TextParameterTest {

    private val parameter = TextParameter<TestEnv, Unit>("", "")

    @Test
    fun `joins args with single space`() {
        val result = withExecution { parameter.parse(["hello", "There", "world"]) }
        assertEquals("hello There world", result.expectSuccessValue())
    }

    @Test
    fun `consumes all args`() {
        val result = withExecution { parameter.parse(["a", "b", "c"]) }
        assertIs<ConsumingResult.Success<String>>(result)
        assertEquals(3, result.consumed)
    }

    @Test
    fun `match claims all args`() {
        assertEquals(MatchResult.matchedAtLeast(3), withInvocation { parameter.match(["a", "b", "c"]) })
    }

    @Test
    fun `match on empty args is partial`() {
        assertEquals(MatchResult.partial(), withInvocation { parameter.match([]) })
    }

    @Test
    fun `has correct properties`() {
        assertIs<Size.Unbounded>(parameter.size)
        assertEquals(GroupableType.Passthrough, parameter.type)
    }
}
