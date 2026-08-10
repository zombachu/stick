package com.zombachu.stick.element

import com.zombachu.stick.CommandResult
import com.zombachu.stick.GroupResult
import com.zombachu.stick.Invocation
import com.zombachu.stick.ParsingResult
import com.zombachu.stick.SenderValidationResult
import com.zombachu.stick.TestEnv
import com.zombachu.stick.element.parameters.LiteralParameter
import com.zombachu.stick.element.parameters.StringParameter
import com.zombachu.stick.expectFailure
import com.zombachu.stick.expectSuccessValue
import com.zombachu.stick.feedback.Feedback
import com.zombachu.stick.impl.Requirement
import com.zombachu.stick.withInvocation
import com.zombachu.stick.withValidationContext
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

class GroupImplTest {

    @Test
    fun `matches are tried in priority order, not declaration order`() {
        val stringParam = StringParameter<TestEnv, Unit>("str", "")
        val literalParam = LiteralParameter<TestEnv, Unit>("foo", [], "")
        val group = Group2Impl("", "", stringParam, literalParam)

        val result = withInvocation("foo") { group.parse(["foo"]) }

        assertIs<GroupResult.ResultB<String>>(result.expectSuccessValue())
    }

    @Test
    fun `no matches returns InvalidSyntax, not validation error`() {
        val requirement = Requirement<TestEnv, Unit> { SenderValidationResult.failSender() }
        val gated = TransformedParameter(StringParameter("gated", ""), { it }, requirement)
        val group = Group1Impl("", "", gated)

        val result = withInvocation("x") { group.parse(["x"]) }

        assertIs<Feedback.InvalidSyntax>(result.expectFailure().feedback)
    }

    @Test
    fun `mismatch falls through to next element`() {
        val mismatching =
            object : Parameter.Size1<TestEnv, Unit, String>("bad", "") {
                context(inv: Invocation<TestEnv, Unit>)
                override fun parse(arg0: String): CommandResult<String> = ParsingResult.failType("bad", arg0)
            }
        val fallback = StringParameter<TestEnv, Unit>("ok", "")
        val group = Group2Impl("", "", mismatching, fallback)

        val result = withInvocation("x") { group.parse(["x"]) }

        assertIs<GroupResult.ResultB<String>>(result.expectSuccessValue())
    }

    @Test
    fun `InvalidSizeError falls through to next element`() {
        val twoArgParam =
            object : Parameter.Size2<TestEnv, Unit, String>("two", "") {
                context(inv: Invocation<TestEnv, Unit>)
                override fun parse(arg0: String, arg1: String): CommandResult<String> =
                    ParsingResult.success("$arg0$arg1")
            }
        val fallback = StringParameter<TestEnv, Unit>("ok", "")
        val group = Group2Impl("", "", twoArgParam, fallback)

        val result = withInvocation("x") { group.parse(["x"]) }

        assertIs<GroupResult.ResultB<String>>(result.expectSuccessValue())
    }

    @Test
    fun `non-internal error propagates, not falls through`() {
        val hardFailure =
            object : Parameter.Size1<TestEnv, Unit, String>("bad", "") {
                context(inv: Invocation<TestEnv, Unit>)
                override fun parse(arg0: String): CommandResult<String> = ParsingResult.failRange("0", "10", arg0)
            }
        val neverTried = StringParameter<TestEnv, Unit>("ok", "")
        val group = Group2Impl("", "", hardFailure, neverTried)

        val result = withInvocation("x") { group.parse(["x"]) }

        assertEquals(Feedback.OutOfRange("0", "10", "x"), result.expectFailure().feedback)
    }

    // TODO
    @Test
    fun `KNOWN BUG - multiple literalParameter alternatives in a group do not fall through`() {
        // LiteralParameter's mismatch (LiteralNotMatchedError) is NOT one of GroupImpl's three
        // "try next" exempted failure types (TypeNotMatchedInternal/TypeNotMatchedError/InvalidSizeError).
        // So group(literalParameter("give"), literalParameter("take")) - a natural, expected use case for
        // sub-command-name alternation - only ever gets a chance at its highest-priority (here: first
        // declared, since all literals tie on priority) alternative: if THAT doesn't match, the whole
        // group hard-fails with that literal's own LiteralNotMatchedError instead of trying the next
        // literal. This test documents the INTENDED try-each-literal behavior and is expected to fail
        // against the current source - do not "fix" this test to match the current behavior.
        val give = LiteralParameter<TestEnv, Unit>("give", [], "")
        val take = LiteralParameter<TestEnv, Unit>("take", [], "")
        val group = Group2Impl("", "", give, take)

        val result = withInvocation("take") { group.parse(["take"]) }

        assertIs<GroupResult.ResultB<String>>(result.expectSuccessValue())
    }

    @Test
    fun `getSyntax returns only sender-visible syntax`() {
        val visible = StringParameter<TestEnv, Unit>("str", "")
        val requirement = Requirement<TestEnv, Unit> { SenderValidationResult.failSender() }
        val hidden = TransformedParameter(StringParameter("hidden", ""), { it }, requirement)
        val group = Group2Impl("", "", visible, hidden)

        val syntax = withValidationContext { group.getSyntax() }

        assertEquals("<str>", syntax)
    }
}
