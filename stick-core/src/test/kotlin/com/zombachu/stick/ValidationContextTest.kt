package com.zombachu.stick

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotSame
import kotlin.test.assertSame

class ValidationContextTest {

    @Test
    fun `factory returns env and sender`() {
        val sender = "sender"
        val context = ValidationContext(TestEnv, sender)

        assertSame(TestEnv, context.env)
        assertSame(sender, context.sender)
    }

    @Test
    fun `forSender produces new context with transformed sender but same env`() {
        val context = ValidationContext(TestEnv, "sender")

        val transformed = context.forSender { it.length }

        assertSame(TestEnv, transformed.env)
        assertEquals(6, transformed.sender)
        assertNotSame<Any>(context, transformed)
    }

    @Test
    fun `forSender does not mutate original context`() {
        val context = ValidationContext(TestEnv, "sender")

        val transformed = context.forSender { it.length }

        assertEquals("sender", context.sender)
        assertEquals(6, transformed.sender)
    }
}
