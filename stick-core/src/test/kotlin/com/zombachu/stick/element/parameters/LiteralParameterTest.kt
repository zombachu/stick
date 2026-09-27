package com.zombachu.stick.element.parameters

import com.zombachu.stick.MatchResult
import com.zombachu.stick.SimpleSuggestion
import com.zombachu.stick.TestEnv
import com.zombachu.stick.element.GroupableType
import com.zombachu.stick.expectReason
import com.zombachu.stick.expectSuccessValue
import com.zombachu.stick.expectUnmatched
import com.zombachu.stick.failure.Reason
import com.zombachu.stick.withExecution
import com.zombachu.stick.withInvocation
import kotlin.test.Test
import kotlin.test.assertEquals

class LiteralParameterTest {

    private val parameter = LiteralParameter<TestEnv, Unit>("foo", [], "")

    @Test
    fun `matches lowercase label`() {
        assertEquals("foo", withExecution { parameter.parse(["foo"]) }.expectSuccessValue())
    }

    @Test
    fun `matching is case-insensitive`() {
        assertEquals("FOO", withExecution { parameter.parse(["FOO"]) }.expectSuccessValue())
    }

    @Test
    fun `matches alias case-insensitively`() {
        val aliased = LiteralParameter<TestEnv, Unit>("foo", ["bar", "baz"], "")
        assertEquals("BAR", withExecution { aliased.parse(["BAR"]) }.expectSuccessValue())
    }

    @Test
    fun `mismatch reports label, not aliases`() {
        val aliased = LiteralParameter<TestEnv, Unit>("foo", ["bar"], "")
        val result = withExecution { aliased.parse(["baz"]) }
        assertEquals(Reason.LiteralNotMatched(["foo"], "baz"), result.expectReason())
    }

    @Test
    fun `matches a mixed-case name`() {
        val mixedCase = LiteralParameter<TestEnv, Unit>("Foo", [], "")
        assertEquals("Foo", withExecution { mixedCase.parse(["Foo"]) }.expectSuccessValue())
        assertEquals("foo", withExecution { mixedCase.parse(["foo"]) }.expectSuccessValue())
    }

    @Test
    fun `match claims one arg`() {
        assertEquals(MatchResult.matchedExactly(1), withInvocation { parameter.match("FOO") })
    }

    @Test
    fun `match unmatched carries LiteralNotMatched`() {
        val result = withInvocation { parameter.match("bar") }
        assertEquals(Reason.LiteralNotMatched(["foo"], "bar"), result.expectUnmatched().expectReason())
    }

    @Test
    fun `suggests tags alias`() {
        val aliased = LiteralParameter<TestEnv, Unit>("foo", ["bar"], "")
        assertEquals(
            [SimpleSuggestion("bar", isAlias = true), SimpleSuggestion("foo")],
            withInvocation { aliased.suggest([], "") },
        )
    }

    @Test
    fun `type is Literal`() {
        assertEquals(GroupableType.Literal, parameter.type)
    }
}
