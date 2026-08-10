package com.zombachu.stick.element.parameters

import com.zombachu.stick.TestEnv
import com.zombachu.stick.expectFailure
import com.zombachu.stick.expectSuccessValue
import com.zombachu.stick.feedback.Feedback
import com.zombachu.stick.withInvocation
import kotlin.test.Test
import kotlin.test.assertEquals

class BooleanParameterTest {

    private val parameter = BooleanParameter<TestEnv, Unit>("", "")

    @Test
    fun `parses lowercase true and false`() {
        assertEquals(true, withInvocation { parameter.parse("true") }.expectSuccessValue())
        assertEquals(false, withInvocation { parameter.parse("false") }.expectSuccessValue())
    }

    @Test
    fun `rejects mixed case input`() {
        val result = withInvocation { parameter.parse("True") }
        assertEquals(Feedback.TypeNotMatched("boolean", "True"), result.expectFailure().feedback)
    }

    @Test
    fun `rejects non-boolean input`() {
        val result = withInvocation { parameter.parse("maybe") }
        assertEquals(Feedback.TypeNotMatched("boolean", "maybe"), result.expectFailure().feedback)
    }
}
