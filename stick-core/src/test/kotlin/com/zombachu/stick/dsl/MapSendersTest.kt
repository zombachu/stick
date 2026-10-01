package com.zombachu.stick.dsl

import com.zombachu.stick.GroupResult
import com.zombachu.stick.TestEnv
import com.zombachu.stick.element.LeadingParameterRole
import com.zombachu.stick.element.Signature1
import com.zombachu.stick.element.parse
import com.zombachu.stick.element.validateSender
import com.zombachu.stick.expectReason
import com.zombachu.stick.expectSuccessValue
import com.zombachu.stick.failure.Reason
import com.zombachu.stick.isSuccess
import com.zombachu.stick.structureTest
import com.zombachu.stick.success
import com.zombachu.stick.withExecutionSender
import com.zombachu.stick.withInvocation
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertSame
import kotlin.test.assertTrue

class MapSendersTest {

    private val length: (String) -> Int = String::length

    @Test
    fun `mapSender on Parameter produces linear element`() = structureTest<String> {
        val parameter = mapSender(length) { intParameter("") }
        val signature =
            Signature1<TestEnv, String, Int>({}, LeadingParameterRole.Label, [literalParameter("cmd"), parameter])

        val result = withExecutionSender("zombachu", "cmd", "5") { signature.execute() }

        assertEquals(5, result.expectSuccessValue().a)
    }

    @Test
    fun `mapSender on GatedParameter validates requirement with transformed sender`() = structureTest<String> {
        val gated = mapSender(length) { require(requirement { sender == 8 }) { intParameter("") } }
        assertTrue(withInvocation("zombachu") { gated.validateSender() }.isSuccess())
        assertSame(Reason.InvalidSender, withInvocation("steve") { gated.validateSender() }.expectReason())
    }

    @Test
    fun `mapSender on GatedParameter parses with transformed sender`() = structureTest<String> {
        val gated = mapSender(length) {
            require(requirement { true }) { intParameter("").map { success(it + sender) } }
        }
        val group = group(gated, stringParameter(""))
        assertEquals(
            GroupResult.ResultA(9),
            withExecutionSender("zombachu", "1") { group.parse(["1"]) }.expectSuccessValue(),
        )
    }

    @Test
    fun `mapSender on ValueFlag parses default with transformed sender`() = structureTest<String> {
        val flag = mapSender(length) { valueFlag("n", intParameter("n"), { success(sender) }) }
        val signature =
            Signature1<TestEnv, String, Int>({}, LeadingParameterRole.Label, [literalParameter("cmd"), flag])

        val result = withExecutionSender("zombachu", "cmd") { signature.execute() }

        assertEquals(8, result.expectSuccessValue().a)
    }

    @Test
    fun `mapSender on OptionalParameter parses with transformed sender`() = structureTest<String> {
        val optional = mapSender(length) { optional(intParameter(""), { success(sender) }) }
        assertEquals(8, withExecutionSender("zombachu") { optional.parse([]) }.expectSuccessValue())
    }

    @Test
    fun `mapSender on OptionalGroup parses with transformed sender`() = structureTest<String> {
        val optional = mapSender(length) {
            optional(group(literalParameter("on")), { success(GroupResult.ResultA("$sender")) })
        }
        assertEquals(
            GroupResult.ResultA("8"),
            withExecutionSender("zombachu") { optional.parse([]) }.expectSuccessValue(),
        )
    }
}
