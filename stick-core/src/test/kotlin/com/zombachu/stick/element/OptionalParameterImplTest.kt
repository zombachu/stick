package com.zombachu.stick.element

import com.zombachu.stick.CommandResult
import com.zombachu.stick.SenderValidationResult
import com.zombachu.stick.TestEnv
import com.zombachu.stick.element.parameters.StringParameter
import com.zombachu.stick.expectFailure
import com.zombachu.stick.expectSuccessValue
import com.zombachu.stick.feedback.Feedback
import com.zombachu.stick.impl.InvalidSenderDefault
import com.zombachu.stick.impl.ValidSenderDefault
import com.zombachu.stick.invalidSenderDefault
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
            OptionalParameterImpl(
                requirementDefault = invalidDefault("forbidden-default", allowed = false),
                presenceDefault = validDefault("presence-default", allowed = true),
                parameter = parameter,
            )
        val result = withInvocation { optional.parse([]) }
        assertEquals("forbidden-default", result.expectSuccessValue())
    }

    @Test
    fun `empty args with parameter allowed but presence not allowed fails with InvalidSender`() {
        val optional =
            OptionalParameterImpl(
                requirementDefault = invalidDefault("forbidden-default", allowed = true),
                presenceDefault = validDefault("presence-default", allowed = false),
                parameter = parameter,
            )
        val result = withInvocation { optional.parse([]) }
        assertSame(Feedback.InvalidSender, result.expectFailure().feedback)
    }

    @Test
    fun `empty args returns presence default`() {
        val optional =
            OptionalParameterImpl(
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
            OptionalParameterImpl(
                requirementDefault = invalidDefault("forbidden-default", allowed = false),
                presenceDefault = validDefault("presence-default", allowed = true),
                parameter = parameter,
            )
        val result = withInvocation("value") { optional.parse(["value"]) }
        assertSame(Feedback.InvalidSender, result.expectFailure().feedback)
    }

    @Test
    fun `non-empty args with wrong size fails with InvalidSyntax`() {
        val optional =
            OptionalParameterImpl(
                requirementDefault = invalidDefault("forbidden-default", allowed = true),
                presenceDefault = validDefault("presence-default", allowed = true),
                parameter = parameter,
            )
        val result = withInvocation("a", "b") { optional.parse(["a", "b"]) }
        assertIs<Feedback.InvalidSyntax>(result.expectFailure().feedback)
    }

    @Test
    fun `non-empty args with matching size delegates to parameter`() {
        val optional =
            OptionalParameterImpl(
                requirementDefault = invalidDefault("forbidden-default", allowed = true),
                presenceDefault = validDefault("presence-default", allowed = true),
                parameter = parameter,
            )
        val result = withInvocation("value") { optional.parse(["value"]) }
        assertEquals("value", result.expectSuccessValue())
    }

    @Test
    fun `getSyntax returns bracketed name when optional for sender`() {
        val optional =
            OptionalParameterImpl(
                requirementDefault = invalidDefault("x", allowed = true),
                presenceDefault = validDefault("x", allowed = true),
                parameter = parameter,
            )
        val syntax = withValidationContext { optional.getSyntax() }
        assertEquals("[item]", syntax)
    }

    @Test
    fun `getSyntax returns empty when parameter not allowed`() {
        val optional =
            OptionalParameterImpl(
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

    private fun validation(allowed: Boolean): CommandResult<Unit> =
        if (allowed) SenderValidationResult.success() else SenderValidationResult.failSender()
}
