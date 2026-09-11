package com.zombachu.stick.element.parameters

import com.zombachu.stick.MatchResult
import com.zombachu.stick.TestEnv
import com.zombachu.stick.expectFailure
import com.zombachu.stick.expectSuccessValue
import com.zombachu.stick.expectUnmatched
import com.zombachu.stick.feedback.Feedback
import com.zombachu.stick.withInvocation
import com.zombachu.stick.withValidationContext
import kotlin.test.Test
import kotlin.test.assertEquals

class BooleanParameterTest {

    private val parameter = BooleanParameter<TestEnv, Unit>("", "")

    @Test
    fun `parses lowercase true and false`() {
        assertEquals(true, withInvocation { parameter.parse(["true"]) }.expectSuccessValue())
        assertEquals(false, withInvocation { parameter.parse(["false"]) }.expectSuccessValue())
    }

    @Test
    fun `parses mixed case true and false`() {
        assertEquals(true, withInvocation { parameter.parse(["True"]) }.expectSuccessValue())
        assertEquals(false, withInvocation { parameter.parse(["FALSE"]) }.expectSuccessValue())
    }

    @Test
    fun `rejects non-boolean input`() {
        val result = withInvocation { parameter.parse(["maybe"]) }
        assertEquals(Feedback.TypeNotMatched("boolean", "maybe"), result.expectFailure().feedback)
    }

    @Test
    fun `matches only boolean input`() {
        assertEquals(MatchResult.matchedExactly(1), withValidationContext { parameter.match(["true"]) })

        val result = withValidationContext { parameter.match(["maybe"]) }
        assertEquals(Feedback.TypeNotMatched("boolean", "maybe"), result.expectUnmatched().expectFailure().feedback)
    }
}
