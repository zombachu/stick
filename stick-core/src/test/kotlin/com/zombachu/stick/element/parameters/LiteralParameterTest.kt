package com.zombachu.stick.element.parameters

import com.zombachu.stick.TestEnv
import com.zombachu.stick.element.ElementType
import com.zombachu.stick.expectFailure
import com.zombachu.stick.expectSuccessValue
import com.zombachu.stick.feedback.Feedback
import com.zombachu.stick.withInvocation
import kotlin.test.Test
import kotlin.test.assertEquals

class LiteralParameterTest {

    private val parameter = LiteralParameter<TestEnv, Unit>("foo", [], "")

    @Test
    fun `matches lowercase label`() {
        assertEquals("foo", withInvocation { parameter.parse("foo") }.expectSuccessValue())
    }

    @Test
    fun `matching is case-insensitive`() {
        assertEquals("FOO", withInvocation { parameter.parse("FOO") }.expectSuccessValue())
    }

    @Test
    fun `matches alias case-insensitively`() {
        val aliased = LiteralParameter<TestEnv, Unit>("foo", ["bar", "baz"], "")
        assertEquals("BAR", withInvocation { aliased.parse("BAR") }.expectSuccessValue())
    }

    @Test
    fun `mismatch reports label, not aliases`() {
        val aliased = LiteralParameter<TestEnv, Unit>("foo", ["bar"], "")
        val result = withInvocation { aliased.parse("qux") }
        assertEquals(Feedback.LiteralNotMatched(["foo"], "qux"), result.expectFailure().feedback)
    }

    @Test
    fun `matches a mixed-case name`() {
        val mixedCase = LiteralParameter<TestEnv, Unit>("Foo", [], "")
        assertEquals("Foo", withInvocation { mixedCase.parse("Foo") }.expectSuccessValue())
        assertEquals("foo", withInvocation { mixedCase.parse("foo") }.expectSuccessValue())
    }

    @Test
    fun `type is Literal`() {
        assertEquals(ElementType.Literal, parameter.type)
    }
}
