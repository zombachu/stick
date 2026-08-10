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

    // TODO
    @Test
    fun `KNOWN BUG - a mixed-case name can never match, not even its own exact casing`() {
        // parse() always lowercases the incoming arg before comparing, but `label` (= the raw `name`
        // constructor argument) is never lowercased itself. So LiteralParameter("Foo", ...) can never
        // match ANY input, including the literal string "Foo", because "foo" (lowered input) != "Foo" (label).
        // This test documents the INTENDED case-insensitive behavior and is expected to fail against the
        // current source - do not "fix" this test to match the bug, the source needs the fix instead.
        val mixedCase = LiteralParameter<TestEnv, Unit>("Foo", [], "")
        val result = withInvocation { mixedCase.parse("Foo") }
        assertEquals("Foo", result.expectSuccessValue())
    }

    @Test
    fun `type is Literal`() {
        assertEquals(ElementType.Literal, parameter.type)
    }
}
