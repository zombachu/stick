package com.zombachu.stick.element

import com.zombachu.stick.MatchResult
import com.zombachu.stick.Position
import com.zombachu.stick.Requirement
import com.zombachu.stick.TestEnv
import com.zombachu.stick.element.parameters.IntParameter
import com.zombachu.stick.element.parameters.LiteralParameter
import com.zombachu.stick.element.parameters.StringParameter
import com.zombachu.stick.expectReason
import com.zombachu.stick.expectSuccessValue
import com.zombachu.stick.failSender
import com.zombachu.stick.failure.Reason
import com.zombachu.stick.isSuccess
import com.zombachu.stick.success
import com.zombachu.stick.withExecution
import com.zombachu.stick.withInvocation
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertSame
import kotlin.test.assertTrue

class OptionalParameterImplTest {

    private val parameter = StringParameter<TestEnv, Unit>("item", "")
    private val deniedRequirement = Requirement<TestEnv, Unit> { failSender() }

    @Test
    fun `empty args with presence not allowed fails with InvalidSyntax`() {
        val optional =
            OptionalParameterImpl<TestEnv, Unit, String, Position.Optional>(
                parameter = parameter,
                default = { success("presence-default") },
                defaultRequirement = deniedRequirement,
            )
        val result = withExecution { optional.parse([]) }
        assertIs<Reason.InvalidSyntax>(result.expectReason())
    }

    @Test
    fun `empty args returns presence default`() {
        val optional =
            OptionalParameterImpl<TestEnv, Unit, String, Position.Optional>(
                parameter = parameter,
                default = { success("presence-default") },
                defaultRequirement = null,
            )
        val result = withExecution { optional.parse([]) }
        assertEquals("presence-default", result.expectSuccessValue())
    }

    @Test
    fun `non-empty args with wrong size fails with InvalidSyntax`() {
        val optional =
            OptionalParameterImpl<TestEnv, Unit, String, Position.Optional>(
                parameter = parameter,
                default = { success("presence-default") },
                defaultRequirement = null,
            )
        val result = withExecution("a", "b") { optional.parse(["a", "b"]) }
        assertIs<Reason.InvalidSyntax>(result.expectReason())
    }

    @Test
    fun `invalid args fails with TypeNotMatched`() {
        val optional =
            OptionalParameterImpl<TestEnv, Unit, Int, Position.Optional>(
                parameter = IntParameter("int", "", Int.MIN_VALUE, Int.MAX_VALUE),
                default = { success(-1) },
                defaultRequirement = null,
            )
        val result = withExecution("word") { optional.parse(["word"]) }

        assertIs<Reason.TypeNotMatched>(result.expectReason())
    }

    @Test
    fun `non-empty args with matching size delegates to parameter`() {
        val optional =
            OptionalParameterImpl<TestEnv, Unit, String, Position.Optional>(
                parameter = parameter,
                default = { success("presence-default") },
                defaultRequirement = null,
            )
        val result = withExecution("value") { optional.parse(["value"]) }
        assertEquals("value", result.expectSuccessValue())
    }

    @Test
    fun `match on empty args claims nothing`() {
        val optional =
            OptionalParameterImpl<TestEnv, Unit, String, Position.Optional>(
                parameter = parameter,
                default = { success("x") },
                defaultRequirement = null,
            )
        assertEquals(MatchResult.matchedAtLeast(0), withInvocation { optional.match([]) })
    }

    @Test
    fun `match on non-empty args delegates to parameter`() {
        val optional =
            OptionalParameterImpl<TestEnv, Unit, String, Position.Optional>(
                parameter = LiteralParameter("here", [], ""),
                default = { success("x") },
                defaultRequirement = null,
            )
        assertEquals(MatchResult.matchedExactly(1), withInvocation { optional.match(["here"]) })
    }

    @Test
    fun `getSyntax returns bracketed name when optional for sender`() {
        val optional =
            OptionalParameterImpl<TestEnv, Unit, String, Position.Optional>(
                parameter = parameter,
                default = { success("x") },
                defaultRequirement = null,
            )
        val syntax = withInvocation { optional.getSyntax() }
        assertEquals("[item]", syntax)
    }

    @Test
    fun `getSyntax returns angle bracketed name when required for sender`() {
        val optional =
            OptionalParameterImpl<TestEnv, Unit, String, Position.Optional>(
                parameter = parameter,
                default = { success("x") },
                defaultRequirement = deniedRequirement,
            )
        val syntax = withInvocation { optional.getSyntax() }
        assertEquals("<item>", syntax)
    }

    @Test
    fun `validateDefault without default requirement succeeds`() {
        val optional =
            OptionalParameterImpl<TestEnv, Unit, String, Position.Optional>(
                parameter = parameter,
                default = { success("x") },
                defaultRequirement = null,
            )
        assertTrue(withInvocation { optional.validateDefault() }.isSuccess())
    }

    @Test
    fun `validateDefault fails with default requirement failure`() {
        val optional =
            OptionalParameterImpl<TestEnv, Unit, String, Position.Optional>(
                parameter = parameter,
                default = { success("x") },
                defaultRequirement = deniedRequirement,
            )
        assertSame(Reason.InvalidSender, withInvocation { optional.validateDefault() }.expectReason())
    }
}
