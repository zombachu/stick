package com.zombachu.stick.failure

import kotlin.test.Test
import kotlin.test.assertEquals

class ReasonTest {

    @Test
    fun `Unknown message`() {
        assertEquals("An unknown error has occurred.", Reason.Unknown().message())
    }

    @Test
    fun `TypeNotMatched message`() {
        assertEquals(
            "The argument provided is not a boolean: xyz.",
            Reason.TypeNotMatched("boolean", "xyz").message(),
        )
    }

    @Test
    fun `InvalidSyntax message`() {
        assertEquals("Invalid syntax. Correct usage: /foo <bar>.", Reason.InvalidSyntax("/foo <bar>").message())
    }

    @Test
    fun `OutOfRange message`() {
        assertEquals(
            "The number provided is not in the valid range of 0 to 10: 20.",
            Reason.OutOfRange("0", "10", "20").message(),
        )
    }

    @Test
    fun `LiteralNotMatched message joins valid values`() {
        assertEquals(
            "The value provided is not one of a, b, c: d.",
            Reason.LiteralNotMatched(["a", "b", "c"], "d").message(),
        )
    }

    @Test
    fun `InvalidSender and InvalidSenderType share message`() {
        assertEquals("You are unable to use this command.", Reason.InvalidSender.message())
        assertEquals("You are unable to use this command.", Reason.InvalidSenderType(Int::class).message())
    }

    @Test
    fun `InvalidPermission message`() {
        assertEquals("You do not have permission to use this command.", Reason.InvalidPermission.message())
    }
}
