package com.zombachu.stick.element.parameters

import com.zombachu.stick.MatchResult
import com.zombachu.stick.TestEnv
import com.zombachu.stick.expectReason
import com.zombachu.stick.expectSuccessValue
import com.zombachu.stick.expectUnmatched
import com.zombachu.stick.failure.Reason
import com.zombachu.stick.withExecution
import com.zombachu.stick.withInvocation
import kotlin.test.Test
import kotlin.test.assertEquals

class BooleanParameterTest {

    private val parameter = BooleanParameter<TestEnv, Unit>("", "")

    @Test
    fun `parses lowercase true and false`() {
        assertEquals(true, withExecution { parameter.parse(["true"]) }.expectSuccessValue())
        assertEquals(false, withExecution { parameter.parse(["false"]) }.expectSuccessValue())
    }

    @Test
    fun `parses mixed case true and false`() {
        assertEquals(true, withExecution { parameter.parse(["True"]) }.expectSuccessValue())
        assertEquals(false, withExecution { parameter.parse(["FALSE"]) }.expectSuccessValue())
    }

    @Test
    fun `rejects non-boolean input`() {
        val result = withExecution { parameter.parse(["maybe"]) }
        assertEquals(Reason.TypeNotMatched("boolean", "maybe"), result.expectReason())
    }

    @Test
    fun `matches only boolean input`() {
        assertEquals(MatchResult.matchedExactly(1), withInvocation { parameter.match(["true"]) })

        val result = withInvocation { parameter.match(["maybe"]) }
        assertEquals(Reason.TypeNotMatched("boolean", "maybe"), result.expectUnmatched().expectReason())
    }
}
