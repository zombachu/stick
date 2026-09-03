package com.zombachu.stick.element

import com.zombachu.stick.Arguments1
import com.zombachu.stick.CommandResult
import com.zombachu.stick.GroupResult
import com.zombachu.stick.Invocation
import com.zombachu.stick.ParsingResult
import com.zombachu.stick.Requirement
import com.zombachu.stick.SenderValidationResult
import com.zombachu.stick.TestEnv
import com.zombachu.stick.element.parameters.LiteralParameter
import com.zombachu.stick.element.parameters.StringParameter
import com.zombachu.stick.expectFailure
import com.zombachu.stick.expectSuccessValue
import com.zombachu.stick.feedback.Feedback
import com.zombachu.stick.withInvocation
import com.zombachu.stick.withValidationContext
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

class GroupImplTest {

    @Test
    fun `matches are tried in priority order, not declaration order`() {
        val stringParameter = StringParameter<TestEnv, Unit>("str", "")
        val literalParameter = LiteralParameter<TestEnv, Unit>("foo", [], "")
        val group = Group2Impl("", "", stringParameter, literalParameter)

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

    @Test
    fun `LiteralNotMatchedError falls through to next element`() {
        val give = LiteralParameter<TestEnv, Unit>("give", [], "")
        val take = LiteralParameter<TestEnv, Unit>("take", [], "")
        val group = Group2Impl("", "", give, take)

        val result = withInvocation("take") { group.parse(["take"]) }

        assertIs<GroupResult.ResultB<String>>(result.expectSuccessValue())
    }

    @Test
    fun `error from matched groupable propagates`() {
        val committing =
            StructureImpl<TestEnv, Unit, Arguments1<String>>(
                "info",
                [],
                "",
                Requirement { SenderValidationResult.success() },
                Signature1({ _ -> }, [LiteralParameter("sun", [], "")]),
            )
        val neverTried = StringParameter<TestEnv, Unit>("ok", "")
        val group = Group2Impl("", "", committing, neverTried)

        val result = withInvocation("info", "moon") { group.parse(["info", "moon"]) }

        assertEquals(Feedback.LiteralNotMatched(["sun"], "moon"), result.expectFailure().feedback)
    }

    @Test
    fun `KNOWN LIMITATION - groups allow duplicate literals`() {
        val first = LiteralParameter<TestEnv, Unit>("foo", [], "")
        val second = LiteralParameter<TestEnv, Unit>("foo", [], "")
        val group = Group2Impl("", "", first, second)

        val result = withInvocation("foo") { group.parse(["foo"]) }

        assertIs<GroupResult.ResultA<String>>(result.expectSuccessValue())
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
