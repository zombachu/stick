package com.zombachu.stick.dsl

import com.zombachu.stick.expectSuccessValue
import com.zombachu.stick.isSuccess
import com.zombachu.stick.structureTest
import com.zombachu.stick.withInvocation
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class SignaturesTest {

    @Test
    fun `structure with no arguments executes`() = structureTest {
        var executed = false
        val structure = command("cmd")() { executed = true }

        val result = withInvocation("cmd") { structure.parse(["cmd"]) }

        assertTrue(result.isSuccess())
        assertTrue(executed)
    }

    @Test
    fun `structure passes parsed values in order`() = structureTest {
        val structure = command("cmd")(stringParameter(""), intParameter(""), stringParameter("")) { a, b, c -> }

        val args = withInvocation("cmd", "x", "5", "y") { structure.parse(["cmd", "x", "5", "y"]) }.expectSuccessValue()

        assertEquals(["x", 5, "y"], [args.a, args.b, args.c])
    }
}
