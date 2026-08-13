package com.zombachu.stick.element.parameters

import com.zombachu.stick.TestEnv
import com.zombachu.stick.expectFailure
import com.zombachu.stick.expectSuccessValue
import com.zombachu.stick.feedback.Feedback
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
        assertEquals(Color.RED, withInvocation { parameter.parse("red") }.expectSuccessValue())
    }

    @Test
    fun `falls back to aliased value`() {
        assertEquals(Color.RED, withInvocation { parameter.parse("r") }.expectSuccessValue())
    }

    @Test
    fun `matching is case-insensitive`() {
        assertEquals(Color.RED, withInvocation { parameter.parse("RED") }.expectSuccessValue())
        assertEquals(Color.RED, withInvocation { parameter.parse("R") }.expectSuccessValue())
    }

    @Test
    fun `failure reports primary keys, not aliases`() {
        val result = withInvocation { parameter.parse("Unknown") }
        assertEquals(Feedback.LiteralNotMatched(["red", "green", "blue"], "Unknown"), result.expectFailure().feedback)
    }

    private enum class Color {
        RED,
        GREEN,
        BLUE,
    }
}
