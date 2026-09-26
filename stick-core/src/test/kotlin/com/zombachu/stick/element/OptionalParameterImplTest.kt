package com.zombachu.stick.element

import com.zombachu.stick.CommandResult
import com.zombachu.stick.MatchResult
import com.zombachu.stick.Position
import com.zombachu.stick.TestEnv
import com.zombachu.stick.ValidationContext
import com.zombachu.stick.element.parameters.IntParameter
import com.zombachu.stick.element.parameters.LiteralParameter
import com.zombachu.stick.element.parameters.StringParameter
import com.zombachu.stick.expectReason
import com.zombachu.stick.expectSuccessValue
import com.zombachu.stick.failSender
import com.zombachu.stick.failure.Reason
import com.zombachu.stick.invalidSenderDefault
import com.zombachu.stick.success
import com.zombachu.stick.validSenderDefault
import com.zombachu.stick.withInvocation
import com.zombachu.stick.withValidationContext
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertSame

class OptionalParameterImplTest {

    private val parameter = StringParameter<TestEnv, Unit>("item", "")

    @Test
    fun `empty args with parameter not allowed returns requirement default`() {
        val optional =
            OptionalParameterImpl<TestEnv, Unit, String, Position.Optional>(
                requirementDefault = invalidDefault("forbidden-default", allowed = false),
                presenceDefault = validDefault("presence-default", allowed = true),
                parameter = parameter,
            )
        val result = withInvocation { optional.parse([]) }
        assertEquals("forbidden-default", result.expectSuccessValue())
    }

    @Test
    fun `empty args with parameter allowed but presence not allowed fails with InvalidSyntax`() {
        val optional =
            OptionalParameterImpl<TestEnv, Unit, String, Position.Optional>(
                requirementDefault = invalidDefault("forbidden-default", allowed = true),
                presenceDefault = validDefault("presence-default", allowed = false),
                parameter = parameter,
            )
        val result = withInvocation { optional.parse([]) }
        assertIs<Reason.InvalidSyntax>(result.expectReason())
    }

    @Test
    fun `empty args returns presence default`() {
        val optional =
            OptionalParameterImpl<TestEnv, Unit, String, Position.Optional>(
                requirementDefault = invalidDefault("forbidden-default", allowed = true),
                presenceDefault = validDefault("presence-default", allowed = true),
                parameter = parameter,
            )
        val result = withInvocation { optional.parse([]) }
        assertEquals("presence-default", result.expectSuccessValue())
    }

    @Test
    fun `non-empty args with parameter not allowed fails with InvalidSender`() {
        val optional =
            OptionalParameterImpl<TestEnv, Unit, String, Position.Optional>(
                requirementDefault = invalidDefault("forbidden-default", allowed = false),
                presenceDefault = validDefault("presence-default", allowed = true),
                parameter = parameter,
            )
        val result = withInvocation("value") { optional.parse(["value"]) }
        assertSame(Reason.InvalidSender, result.expectReason())
    }

    @Test
    fun `non-empty args with wrong size fails with InvalidSyntax`() {
        val optional =
            OptionalParameterImpl<TestEnv, Unit, String, Position.Optional>(
                requirementDefault = invalidDefault("forbidden-default", allowed = true),
                presenceDefault = validDefault("presence-default", allowed = true),
                parameter = parameter,
            )
        val result = withInvocation("a", "b") { optional.parse(["a", "b"]) }
        assertIs<Reason.InvalidSyntax>(result.expectReason())
    }

    @Test
    fun `invalid args fails with TypeNotMatched`() {
        val optional =
            OptionalParameterImpl<TestEnv, Unit, Int, Position.Optional>(
                requirementDefault = invalidSenderDefault(-1),
                presenceDefault = validSenderDefault(-1),
                parameter = IntParameter("int", "", Int.MIN_VALUE, Int.MAX_VALUE),
            )
        val result = withInvocation("word") { optional.parse(["word"]) }

        assertIs<Reason.TypeNotMatched>(result.expectReason())
    }

    @Test
    fun `non-empty args with matching size delegates to parameter`() {
        val optional =
            OptionalParameterImpl<TestEnv, Unit, String, Position.Optional>(
                requirementDefault = invalidDefault("forbidden-default", allowed = true),
                presenceDefault = validDefault("presence-default", allowed = true),
                parameter = parameter,
            )
        val result = withInvocation("value") { optional.parse(["value"]) }
        assertEquals("value", result.expectSuccessValue())
    }

    @Test
    fun `match on empty args claims nothing`() {
        val optional =
            OptionalParameterImpl<TestEnv, Unit, String, Position.Optional>(
                requirementDefault = invalidDefault("x", allowed = true),
                presenceDefault = validDefault("x", allowed = true),
                parameter = parameter,
            )
        assertEquals(MatchResult.matchedAtLeast(0), withValidationContext { optional.match([]) })
    }

    @Test
    fun `match on non-empty args delegates to parameter`() {
        val optional =
            OptionalParameterImpl<TestEnv, Unit, String, Position.Optional>(
                requirementDefault = invalidDefault("x", allowed = true),
                presenceDefault = validDefault("x", allowed = true),
                parameter = LiteralParameter("here", [], ""),
            )
        assertEquals(MatchResult.matchedExactly(1), withValidationContext { optional.match(["here"]) })
    }

    @Test
    fun `match on non-empty args with parameter not allowed fails with InvalidSender`() {
        val optional =
            OptionalParameterImpl<TestEnv, Unit, Int, Position.Optional>(
                requirementDefault = invalidSenderDefault(-1) { validation(allowed = false) },
                presenceDefault = validSenderDefault(-1),
                parameter = IntParameter("int", "", Int.MIN_VALUE, Int.MAX_VALUE),
            )
        val result = withValidationContext { optional.match(["word"]) }
        assertSame(Reason.InvalidSender, assertIs<MatchResult.Unmatched>(result).failure.expectReason())
    }

    @Test
    fun `getSyntax returns bracketed name when optional for sender`() {
        val optional =
            OptionalParameterImpl<TestEnv, Unit, String, Position.Optional>(
                requirementDefault = invalidDefault("x", allowed = true),
                presenceDefault = validDefault("x", allowed = true),
                parameter = parameter,
            )
        val syntax = withValidationContext { optional.getSyntax() }
        assertEquals("[item]", syntax)
    }

    @Test
    fun `getSyntax returns angle bracketed name when required for sender`() {
        val optional =
            OptionalParameterImpl<TestEnv, Unit, String, Position.Optional>(
                requirementDefault = invalidDefault("x", allowed = true),
                presenceDefault = validDefault("x", allowed = false),
                parameter = parameter,
            )
        val syntax = withValidationContext { optional.getSyntax() }
        assertEquals("<item>", syntax)
    }

    @Test
    fun `getSyntax returns empty when parameter not allowed`() {
        val optional =
            OptionalParameterImpl<TestEnv, Unit, String, Position.Optional>(
                requirementDefault = invalidDefault("x", allowed = false),
                presenceDefault = validDefault("x", allowed = true),
                parameter = parameter,
            )
        val syntax = withValidationContext(Unit) { optional.getSyntax() }
        assertEquals("", syntax)
    }

    private fun invalidDefault(value: String, allowed: Boolean): InvalidSenderDefault<TestEnv, Unit, String> =
        invalidSenderDefault(value) { validation(allowed) }

    private fun validDefault(value: String, allowed: Boolean): ValidSenderDefault<TestEnv, Unit, String> =
        validSenderDefault(value) { validation(allowed) }

    context(_: ValidationContext<*, *>)
    private fun validation(allowed: Boolean): CommandResult<Unit> =
        if (allowed) success() else failSender()
}
