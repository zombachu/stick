package com.zombachu.stick

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotSame
import kotlin.test.assertSame

class InvocationTest {

    @Test
    fun `factory returns env and sender`() {
        val sender = "sender"
        val inv = Invocation(TestEnv, sender)

        assertSame(TestEnv, inv.env)
        assertSame(sender, inv.sender)
    }

    @Test
    fun `forSender produces new invocation with transformed sender but same env`() {
        val inv = Invocation(TestEnv, "sender")

        val transformed = inv.forSender { it.length }

        assertSame(TestEnv, transformed.env)
        assertEquals(6, transformed.sender)
        assertNotSame<Any>(inv, transformed)
    }

    @Test
    fun `forSender does not mutate original invocation`() {
        val inv = Invocation(TestEnv, "sender")

        val transformed = inv.forSender { it.length }

        assertEquals("sender", inv.sender)
        assertEquals(6, transformed.sender)
    }
}
