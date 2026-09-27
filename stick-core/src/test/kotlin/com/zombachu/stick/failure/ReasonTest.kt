package com.zombachu.stick.failure

import com.zombachu.stick.failureOrigin
import kotlin.test.Test
import kotlin.test.assertEquals

class ReasonTest {

    private val origin = failureOrigin("")

    @Test
    fun `Unknown message`() {
        assertEquals("An unknown error has occurred.", Reason.Unknown().message(origin))
    }

    @Test
    fun `TypeNotMatched message`() {
        assertEquals(
            "The argument provided is not a boolean: xyz.",
            Reason.TypeNotMatched("boolean", "xyz").message(origin),
        )
    }

    @Test
    fun `InvalidSyntax message`() {
        assertEquals(
            "Invalid syntax. Correct usage: /foo <bar>.",
            Reason.InvalidSyntax.message(failureOrigin("/foo <bar>"))
        )
    }

    @Test
    fun `OutOfRange message`() {
        assertEquals(
            "The number provided is not in the valid range of 0 to 10: 20.",
            Reason.OutOfRange("0", "10", "20").message(origin),
        )
    }

    @Test
    fun `LiteralNotMatched message joins valid values`() {
        assertEquals(
            "The value provided is not one of a, b, c: d.",
            Reason.LiteralNotMatched(["a", "b", "c"], "d").message(origin),
        )
    }

    @Test
    fun `InvalidSender and InvalidSenderType share message`() {
        assertEquals("You are unable to use this command.", Reason.InvalidSender.message(origin))
        assertEquals("You are unable to use this command.", Reason.InvalidSenderType(Int::class).message(origin))
    }

    @Test
    fun `InvalidPermission message`() {
        assertEquals("You do not have permission to use this command.", Reason.InvalidPermission.message(origin))
    }
}
