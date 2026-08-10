package com.zombachu.stick.feedback

import kotlin.test.Test
import kotlin.test.assertEquals

class FeedbackTest {

    @Test
    fun `Unknown message`() {
        assertEquals("An unknown error has occurred.", Feedback.Unknown.message)
    }

    @Test
    fun `TypeNotMatched message`() {
        assertEquals("The argument provided is not a boolean: xyz.", Feedback.TypeNotMatched("boolean", "xyz").message)
    }

    @Test
    fun `InvalidSyntax message`() {
        assertEquals("Invalid syntax. Correct usage: /foo <bar>.", Feedback.InvalidSyntax("/foo <bar>").message)
    }

    @Test
    fun `OutOfRange message`() {
        assertEquals(
            "The number provided is not in the valid range of 0 to 10: 20.",
            Feedback.OutOfRange("0", "10", "20").message,
        )
    }

    @Test
    fun `LiteralNotMatched message joins valid values`() {
        assertEquals(
            "The value provided is not one of a, b, c: d.",
            Feedback.LiteralNotMatched(["a", "b", "c"], "d").message,
        )
    }

    @Test
    fun `InvalidSender and InvalidSenderType share message`() {
        assertEquals("You are unable to use this command.", Feedback.InvalidSender.message)
        assertEquals("You are unable to use this command.", Feedback.InvalidSenderType.message)
    }

    @Test
    fun `InvalidPermission message`() {
        assertEquals("You do not have permission to use this command.", Feedback.InvalidPermission.message)
    }

    @Test
    fun `CustomFeedback re-evaluates lambda on every access`() {
        var count = 0
        val feedback = CustomFeedback { "message #${++count}" }

        assertEquals("message #1", feedback.message)
        assertEquals("message #2", feedback.message)
    }
}
