package com.zombachu.stick.element.parameters

import com.zombachu.stick.MatchResult
import com.zombachu.stick.SimpleSuggestion
import com.zombachu.stick.TestEnv
import com.zombachu.stick.expectReason
import com.zombachu.stick.expectSuccessValue
import com.zombachu.stick.expectUnmatched
import com.zombachu.stick.failure.Reason
import com.zombachu.stick.withExecution
import com.zombachu.stick.withInvocation
import kotlin.test.Test
import kotlin.test.assertEquals

class EnumParameterTest {

    private val parameter =
        EnumParameter<TestEnv, Unit, Color>(
            "",
            "",
            primaryValues = mapOf("red" to Color.RED, "green" to Color.GREEN, "blue" to Color.BLUE),
            aliasedValues = mapOf("r" to Color.RED),
        )

    @Test
    fun `parses primary value`() {
        assertEquals(Color.RED, withExecution { parameter.parse(["red"]) }.expectSuccessValue())
    }

    @Test
    fun `falls back to aliased value`() {
        assertEquals(Color.RED, withExecution { parameter.parse(["r"]) }.expectSuccessValue())
    }

    @Test
    fun `matching is case-insensitive`() {
        assertEquals(Color.RED, withExecution { parameter.parse(["RED"]) }.expectSuccessValue())
        assertEquals(Color.RED, withExecution { parameter.parse(["R"]) }.expectSuccessValue())
    }

    @Test
    fun `match claims one arg for an aliased value`() {
        assertEquals(MatchResult.matchedExactly(1), withInvocation { parameter.match(["R"]) })
    }

    @Test
    fun `match unmatched carries LiteralNotMatched`() {
        val result = withInvocation { parameter.match(["Unknown"]) }
        val reason = result.expectUnmatched().expectReason()
        assertEquals(Reason.LiteralNotMatched(["red", "green", "blue"], "Unknown"), reason)
    }

    @Test
    fun `failure reports primary keys, not aliases`() {
        val result = withExecution { parameter.parse(["Unknown"]) }
        assertEquals(Reason.LiteralNotMatched(["red", "green", "blue"], "Unknown"), result.expectReason())
    }

    @Test
    fun `suggests primary values before aliased ones`() {
        assertEquals(
            [
                SimpleSuggestion("red"),
                SimpleSuggestion("green"),
                SimpleSuggestion("blue"),
                SimpleSuggestion("r", isAlias = true),
            ],
            withInvocation { parameter.suggest([], "") },
        )
    }

    private enum class Color {
        RED,
        GREEN,
        BLUE,
    }
}
