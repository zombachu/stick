package com.zombachu.stick.element

import com.zombachu.stick.CommandResult
import com.zombachu.stick.Environment
import com.zombachu.stick.GroupResult
import com.zombachu.stick.GroupResult2
import com.zombachu.stick.HybridFlagResult
import com.zombachu.stick.Invocation
import com.zombachu.stick.MatchResult
import com.zombachu.stick.Position
import com.zombachu.stick.Requirement
import com.zombachu.stick.SimpleSuggestion
import com.zombachu.stick.Suggestion
import com.zombachu.stick.TestEnv
import com.zombachu.stick.element.parameters.LiteralParameter
import com.zombachu.stick.expectReason
import com.zombachu.stick.expectSuccessValue
import com.zombachu.stick.expectUnmatched
import com.zombachu.stick.failSender
import com.zombachu.stick.failure.Reason
import com.zombachu.stick.invalidSenderDefault
import com.zombachu.stick.success
import com.zombachu.stick.withExecutionSender
import com.zombachu.stick.withInvocation
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertSame
import kotlin.test.fail

class TransformedElementTest {

    private val allowed = Requirement<TestEnv, String> { success() }
    private val allowedDefault = invalidSenderDefault<TestEnv, String, String>("unused")
    private val deniedDefault = invalidSenderDefault<TestEnv, String, String>("denied") { failSender() }
    private val allowedGroupDefault =
        invalidSenderDefault<TestEnv, String, GroupResult2<String, String>>(GroupResult.ResultA("unused"))
    private val deniedGroupDefault =
        invalidSenderDefault<TestEnv, String, GroupResult2<String, String>>(GroupResult.ResultA("denied")) {
            failSender()
        }
    private val rejectedTransform: (String) -> Int = { fail("transformed a rejected sender") }

    @Test
    fun `TransformedParameter suggests with the transformed sender`() {
        val base = SenderParameter<TestEnv, Int>()
        val transformed =
            TransformedParameter<TestEnv, String, Int, String, Position.Leading>(base, String::length, allowed)

        val suggestions = withInvocation("zombachu") { transformed.suggest([], "") }

        assertEquals(["8"], suggestions.map { it.value })
    }

    @Test
    fun `TransformedValueFlag suggests with the transformed sender`() {
        val flagParameter = FlagParameter.ParameterFlagParameter("f", SenderParameter<TestEnv, Int>(), [])
        val base = ValueFlagImpl("f", { success("") }, flagParameter)
        val transformed = TransformedValueFlag(base, String::length, invalidSenderDefault<TestEnv, String, String>(""))

        val suggestions = withInvocation("zombachu") { transformed.suggest(["-f"], "") }

        assertEquals(["8"], suggestions.map { it.value })
    }

    @Test
    fun `TransformedHybridFlag suggests with the transformed sender`() {
        val base = HybridFlagImpl<TestEnv, Int, String>("f", SenderParameter(), [])
        val transformed =
            TransformedHybridFlag(
                base,
                String::length,
                invalidSenderDefault<TestEnv, String, HybridFlagResult<String>>(HybridFlagResult.Absent()),
            )

        val suggestions = withInvocation("zombachu") { transformed.suggest(["-f"], "") }

        assertEquals(["8"], suggestions.map { it.value })
    }

    @Test
    fun `TransformedStructure suggests its label`() {
        val base =
            StructureImpl("teleport", ["tp"], "", Requirement<TestEnv, Int> { success() }) {
                Signature0({}, LeadingParameterRole.Label, [it])
            }
        val transformed = TransformedStructure(base, String::length, allowed)

        val suggestions = withInvocation("zombachu") { transformed.suggest([], "") }

        assertEquals(["tp", "teleport"], suggestions.map { it.value })
    }

    @Test
    fun `TransformedOptionalParameter suggests with the transformed sender`() {
        val transformed = TransformedOptionalParameter(optionalParameter(), String::length, allowedDefault)

        val suggestions = withInvocation("zombachu") { transformed.suggest([], "") }

        assertEquals(["8"], suggestions.map { it.value })
    }

    @Test
    fun `TransformedOptionalParameter parses absent default with the transformed sender`() {
        val transformed = TransformedOptionalParameter(optionalParameter(), String::length, allowedDefault)
        val result = withExecutionSender("zombachu") { transformed.parse([]) }
        assertEquals("8", result.expectSuccessValue())
    }

    @Test
    fun `TransformedOptionalParameter empty args with sender not allowed returns invalidSenderDefault`() {
        val transformed = TransformedOptionalParameter(optionalParameter(), rejectedTransform, deniedDefault)
        val result = withExecutionSender("zombachu") { transformed.parse([]) }
        assertEquals("denied", result.expectSuccessValue())
    }

    @Test
    fun `TransformedOptionalParameter non-empty args with sender not allowed fails with InvalidSender`() {
        val transformed = TransformedOptionalParameter(optionalParameter(), rejectedTransform, deniedDefault)
        val result = withExecutionSender("zombachu", "value") { transformed.parse(["value"]) }
        assertSame(Reason.InvalidSender, result.expectReason())
    }

    @Test
    fun `TransformedOptionalParameter match on empty args with sender not allowed claims nothing`() {
        val transformed = TransformedOptionalParameter(optionalParameter(), rejectedTransform, deniedDefault)
        assertEquals(MatchResult.matchedAtLeast(0), withInvocation("zombachu") { transformed.match([]) })
    }

    @Test
    fun `TransformedOptionalParameter match on non-empty args with sender not allowed fails with InvalidSender`() {
        val transformed = TransformedOptionalParameter(optionalParameter(), rejectedTransform, deniedDefault)
        val result = withInvocation("zombachu") { transformed.match(["value"]) }
        assertSame(Reason.InvalidSender, result.expectUnmatched().expectReason())
    }

    @Test
    fun `TransformedOptionalParameter suggest with sender not allowed returns nothing`() {
        val transformed = TransformedOptionalParameter(optionalParameter(), rejectedTransform, deniedDefault)
        assertEquals([], withInvocation("zombachu") { transformed.suggest([], "") })
    }

    @Test
    fun `TransformedOptionalParameter getSyntax returns empty when sender not allowed`() {
        val transformed = TransformedOptionalParameter(optionalParameter(), rejectedTransform, deniedDefault)
        assertEquals("", withInvocation("zombachu") { transformed.getSyntax() })
    }

    @Test
    fun `TransformedOptionalGroup parses absent default with the transformed sender`() {
        val transformed = TransformedOptionalGroup(optionalGroup(), String::length, allowedGroupDefault)
        val result = withExecutionSender("zombachu") { transformed.parse([]) }
        assertEquals(GroupResult.ResultA("8"), result.expectSuccessValue())
    }

    @Test
    fun `TransformedOptionalGroup empty args with sender not allowed returns invalidSenderDefault`() {
        val transformed = TransformedOptionalGroup(optionalGroup(), rejectedTransform, deniedGroupDefault)
        val result = withExecutionSender("zombachu") { transformed.parse([]) }
        assertEquals(GroupResult.ResultA("denied"), result.expectSuccessValue())
    }

    @Test
    fun `TransformedOptionalGroup non-empty args with sender not allowed fails with InvalidSender`() {
        val transformed = TransformedOptionalGroup(optionalGroup(), rejectedTransform, deniedGroupDefault)
        val result = withExecutionSender("zombachu", "orange") { transformed.parse(["orange"]) }
        assertSame(Reason.InvalidSender, result.expectReason())
    }

    @Test
    fun `TransformedOptionalGroup match on empty args with sender not allowed claims nothing`() {
        val transformed = TransformedOptionalGroup(optionalGroup(), rejectedTransform, deniedGroupDefault)
        assertEquals(MatchResult.matchedAtLeast(0), withInvocation("zombachu") { transformed.match([]) })
    }

    @Test
    fun `TransformedOptionalGroup match on non-empty args with sender not allowed fails with InvalidSender`() {
        val transformed = TransformedOptionalGroup(optionalGroup(), rejectedTransform, deniedGroupDefault)
        val result = withInvocation("zombachu") { transformed.match(["apple"]) }
        assertSame(Reason.InvalidSender, result.expectUnmatched().expectReason())
    }

    @Test
    fun `TransformedOptionalGroup suggest with sender not allowed returns nothing`() {
        val transformed = TransformedOptionalGroup(optionalGroup(), rejectedTransform, deniedGroupDefault)
        assertEquals([], withInvocation("zombachu") { transformed.suggest([], "") })
    }

    @Test
    fun `TransformedOptionalGroup getSyntax returns empty when sender not allowed`() {
        val transformed = TransformedOptionalGroup(optionalGroup(), rejectedTransform, deniedGroupDefault)
        assertEquals("", withInvocation("zombachu") { transformed.getSyntax() })
    }

    private fun optionalParameter(): OptionalParameterImpl<TestEnv, Int, String, Position.Optional> =
        OptionalParameterImpl(ValidatedDefaultImpl({ success("$sender") }) { success() }, SenderParameter())

    private fun optionalGroup(): OptionalGroupImpl<TestEnv, Int, GroupResult2<String, String>, Position.Optional> =
        OptionalGroupImpl(
            ValidatedDefaultImpl({ success(GroupResult.ResultA("$sender")) }) { success() },
            Group2Impl<TestEnv, Int, String, String, Position.Leading>(
                "",
                "",
                LiteralParameter("apple", [], ""),
                LiteralParameter("orange", [], ""),
            ),
        )

    private class SenderParameter<E : Environment, S> : Parameter.Size1<E, S, String>("", "") {

        context(inv: Invocation<E, S>)
        override fun suggest(preceding: List<String>, partial: String): List<Suggestion> =
            [SimpleSuggestion("${inv.sender}")]

        context(inv: Invocation<E, S>)
        override fun resolve(arg0: String): CommandResult<String> = success(arg0)
    }
}
