package com.zombachu.stick.element

import com.zombachu.stick.Arguments1
import com.zombachu.stick.CommandResult
import com.zombachu.stick.ConsumingResult
import com.zombachu.stick.GroupResult
import com.zombachu.stick.MatchResult
import com.zombachu.stick.ParsingResult
import com.zombachu.stick.Position
import com.zombachu.stick.Requirement
import com.zombachu.stick.SenderValidationResult
import com.zombachu.stick.Size
import com.zombachu.stick.TestEnv
import com.zombachu.stick.ValidationContext
import com.zombachu.stick.consuming
import com.zombachu.stick.element.parameters.LiteralParameter
import com.zombachu.stick.element.parameters.StringParameter
import com.zombachu.stick.element.parameters.TextParameter
import com.zombachu.stick.expectFailure
import com.zombachu.stick.expectSuccessValue
import com.zombachu.stick.expectUnmatched
import com.zombachu.stick.feedback.Feedback
import com.zombachu.stick.withInvocation
import com.zombachu.stick.withValidationContext
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertSame

class GroupImplTest {

    @Test
    fun `matches are tried in priority order, not declaration order`() {
        val stringParameter = StringParameter<TestEnv, Unit>("str", "")
        val literalParameter = LiteralParameter<TestEnv, Unit>("foo", [], "")
        val group = group2(stringParameter, literalParameter)

        val result = withInvocation("foo") { group.parse(["foo"]) }

        assertIs<GroupResult.ResultB<String>>(result.expectSuccessValue())
    }

    @Test
    fun `bounded matches are tried longest first`() {
        val shortParameter = variableParameter("short", Size.between(1, 2), consumed = 1)
        val longParameter = variableParameter("long", Size.between(1, 3), consumed = 3)
        val group = group2(shortParameter, longParameter)

        val result = withInvocation("a", "b", "c") { group.parse(["a", "b", "c"]) }

        assertIs<GroupResult.ResultB<String>>(result.expectSuccessValue())
    }

    @Test
    fun `bounded matches are ordered by max, not by exactness`() {
        val exactParameter = variableParameter("exact", Size(2), consumed = 2)
        val longParameter = variableParameter("long", Size.between(1, 3), consumed = 3)
        val group = group2(exactParameter, longParameter)

        val result = withInvocation("a", "b", "c") { group.parse(["a", "b", "c"]) }

        assertIs<GroupResult.ResultB<String>>(result.expectSuccessValue())
    }

    @Test
    fun `no matches is silent, not validation error`() {
        val requirement = Requirement<TestEnv, Unit> { SenderValidationResult.failSender() }
        val gated = transformed(StringParameter("gated", ""), requirement)
        val group = group1(gated)

        val result = withInvocation("x") { group.parse(["x"]) }

        assertSame(ParsingResult.TypeNotMatchedInternal, result)
    }

    @Test
    fun `mismatch falls through to next element`() {
        val mismatching =
            object : Parameter.Size1<TestEnv, Unit, String>("bad", "") {
                context(validationContext: ValidationContext<TestEnv, Unit>)
                override fun match(arg0: String): MatchResult = MatchResult.matched(1)

                context(validationContext: ValidationContext<TestEnv, Unit>)
                override fun resolve(arg0: String): CommandResult<String> = ParsingResult.failType("bad", arg0)
            }
        val fallback = StringParameter<TestEnv, Unit>("ok", "")
        val group = group2(mismatching, fallback)

        val result = withInvocation("x") { group.parse(["x"]) }

        assertIs<GroupResult.ResultB<String>>(result.expectSuccessValue())
    }

    @Test
    fun `InvalidSizeError falls through to next element`() {
        val twoArgParam =
            object : Parameter.Size2<TestEnv, Unit, String>("two", "") {
                context(validationContext: ValidationContext<TestEnv, Unit>)
                override fun match(arg0: String, arg1: String): MatchResult = MatchResult.matched(2)

                context(validationContext: ValidationContext<TestEnv, Unit>)
                override fun resolve(arg0: String, arg1: String): CommandResult<String> =
                    ParsingResult.success("$arg0$arg1")
            }
        val fallback = StringParameter<TestEnv, Unit>("ok", "")
        val group = group2(twoArgParam, fallback)

        val result = withInvocation("x") { group.parse(["x"]) }

        assertIs<GroupResult.ResultB<String>>(result.expectSuccessValue())
    }

    @Test
    fun `non-internal error propagates, not falls through`() {
        val hardFailure =
            object : Parameter.Size1<TestEnv, Unit, String>("bad", "") {
                context(validationContext: ValidationContext<TestEnv, Unit>)
                override fun match(arg0: String): MatchResult = MatchResult.matched(1)

                context(validationContext: ValidationContext<TestEnv, Unit>)
                override fun resolve(arg0: String): CommandResult<String> = ParsingResult.failRange("0", "10", arg0)
            }
        val neverTried = StringParameter<TestEnv, Unit>("ok", "")
        val group = group2(hardFailure, neverTried)

        val result = withInvocation("x") { group.parse(["x"]) }

        assertEquals(Feedback.OutOfRange("0", "10", "x"), result.expectFailure().feedback)
    }

    @Test
    fun `LiteralNotMatchedError falls through to next element`() {
        val give = LiteralParameter<TestEnv, Unit>("give", [], "")
        val take = LiteralParameter<TestEnv, Unit>("take", [], "")
        val group = group2(give, take)

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
        val group = group2(committing, neverTried)

        val result = withInvocation("info", "moon") { group.parse(["info", "moon"]) }

        assertEquals(Feedback.LiteralNotMatched(["sun"], "moon"), result.expectFailure().feedback)
    }

    @Test
    fun `KNOWN LIMITATION - groups allow duplicate literals`() {
        val first = LiteralParameter<TestEnv, Unit>("foo", [], "")
        val second = LiteralParameter<TestEnv, Unit>("foo", [], "")
        val group = group2(first, second)

        val result = withInvocation("foo") { group.parse(["foo"]) }

        assertIs<GroupResult.ResultA<String>>(result.expectSuccessValue())
    }

    @Test
    fun `match returns match of first matching element`() {
        val one = LiteralParameter<TestEnv, Unit>("one", [], "")
        val two = LiteralParameter<TestEnv, Unit>("two", [], "")
        val group = group2(one, two)

        val result = withValidationContext { group.match(["two"]) }

        assertEquals(MatchResult.matched(1), result)
    }

    @Test
    fun `match with no matching element fails with TypeNotMatchedInternal`() {
        val give = LiteralParameter<TestEnv, Unit>("give", [], "")
        val take = LiteralParameter<TestEnv, Unit>("take", [], "")
        val group = group2(give, take)

        val result = withValidationContext { group.match(["drop"]) }

        assertSame(ParsingResult.TypeNotMatchedInternal, result.expectUnmatched())
    }

    @Test
    fun `match skips elements sender fails validation for`() {
        val requirement = Requirement<TestEnv, Unit> { SenderValidationResult.failSender() }
        val gated = transformed(StringParameter("gated", ""), requirement)
        val group = group1(gated)

        val result = withValidationContext { group.match(["x"]) }

        assertSame(ParsingResult.TypeNotMatchedInternal, result.expectUnmatched())
    }

    @Test
    fun `match returns failure of first branch when all unmatched`() {
        val first = decliningParameter("first", ParsingResult.failRange("1", "2", "9"))
        val second = decliningParameter("second", ParsingResult.failRange("3", "4", "9"))
        val group = group2(first, second)

        val result = withValidationContext { group.match(["9"]) }

        assertEquals(Feedback.OutOfRange("1", "2", "9"), assertIs<ParsingResult.OutOfRangeError>(result.expectUnmatched()).feedback)
    }

    @Test
    fun `match reports partial element when nothing matches`() {
        val twoArgParam =
            object : Parameter.Size2<TestEnv, Unit, String>("two", "") {
                context(validationContext: ValidationContext<TestEnv, Unit>)
                override fun match(arg0: String, arg1: String): MatchResult = MatchResult.matched(2)

                context(validationContext: ValidationContext<TestEnv, Unit>)
                override fun resolve(arg0: String, arg1: String): CommandResult<String> = ParsingResult.success("")
            }
        val give = LiteralParameter<TestEnv, Unit>("give", [], "")
        val group = group2(twoArgParam, give)

        val result = withValidationContext { group.match(["take"]) }

        assertEquals(MatchResult.partial(1), result)
    }

    @Test
    fun `non-matching nested group falls through to next element`() {
        val give = LiteralParameter<TestEnv, Unit>("give", [], "")
        val take = LiteralParameter<TestEnv, Unit>("take", [], "")
        val nested = group2(give, take)
        val group = group2(nested, StringParameter<TestEnv, Unit>("ok", ""))

        val result = withInvocation("drop") { group.parse(["drop"]) }

        assertIs<GroupResult.ResultB<String>>(result.expectSuccessValue())
    }

    @Test
    fun `getSyntax returns only sender-visible syntax`() {
        val visible = StringParameter<TestEnv, Unit>("str", "")
        val requirement = Requirement<TestEnv, Unit> { SenderValidationResult.failSender() }
        val hidden = transformed(StringParameter("hidden", ""), requirement)
        val group = group2(visible, hidden)

        val syntax = withValidationContext { group.getSyntax() }

        assertEquals("<str>", syntax)
    }

    @Test
    fun `size constrains to elements for bounded sizes`() {
        val twoArgParam =
            object : Parameter.Size2<TestEnv, Unit, String>("two", "") {
                context(validationContext: ValidationContext<TestEnv, Unit>)
                override fun match(arg0: String, arg1: String): MatchResult = MatchResult.matched(2)

                context(validationContext: ValidationContext<TestEnv, Unit>)
                override fun resolve(arg0: String, arg1: String): CommandResult<String> = ParsingResult.success("")
            }
        val group = group2(twoArgParam, StringParameter("one", ""))

        assertEquals(1, group.size.min)
        assertEquals(2, assertIs<Size.Bounded>(group.size).max)
    }

    @Test
    fun `size is unbounded with unbounded element`() {
        val group = group2(StringParameter<TestEnv, Unit>("one", ""), TextParameter<TestEnv, Unit>("rest", ""))

        assertIs<Size.Unbounded>(group.size)
        assertEquals(1, group.size.min)
    }

    private fun decliningParameter(name: String, failure: CommandResult<String>) =
        object : Parameter.Size1<TestEnv, Unit, String>(name, "") {
            context(validationContext: ValidationContext<TestEnv, Unit>)
            override fun resolve(arg0: String): CommandResult<String> = failure
        }

    private fun variableParameter(name: String, size: Size.Bounded, consumed: Int) =
        object : Parameter.Bounded<TestEnv, Unit, String>(size, name, "") {
            context(validationContext: ValidationContext<TestEnv, Unit>)
            override fun resolve(args: List<String>): ConsumingResult<String> = ParsingResult.success(name).consuming(consumed)
        }

    private fun <A, P : Position> group1(element: Groupable.Positioned<TestEnv, Unit, A, P>) =
        Group1Impl<TestEnv, Unit, A, P>("", "", element)

    private fun <A, B, P : Position> group2(
        elementA: Groupable.Positioned<TestEnv, Unit, A, P>,
        elementB: Groupable.Positioned<TestEnv, Unit, B, P>,
    ) = Group2Impl<TestEnv, Unit, A, B, P>("", "", elementA, elementB)

    private fun <T> transformed(
        base: Parameter<TestEnv, Unit, T, Position.Leading>,
        requirement: Requirement<TestEnv, Unit>,
    ) =
        TransformedParameter<TestEnv, Unit, Unit, T, Position.Leading>(base, { it }, requirement)
}
