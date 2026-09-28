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

class GatedElementTest {

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
    fun `GatedParameterImpl forwards suggest to base`() {
        val gated = GatedParameterImpl(SenderParameter<TestEnv, String>(), allowed)

        val suggestions = withInvocation("zombachu") { gated.suggest([], "") }

        assertEquals(["zombachu"], suggestions.map { it.value })
    }

    @Test
    fun `GatedValueFlag forwards suggest to base`() {
        val flagParameter = FlagParameter.ParameterFlagParameter("f", SenderParameter<TestEnv, String>(), [])
        val gated = GatedValueFlag(ValueFlagImpl("f", { success("") }, flagParameter), allowedDefault)

        val suggestions = withInvocation("zombachu") { gated.suggest(["-f"], "") }

        assertEquals(["zombachu"], suggestions.map { it.value })
    }

    @Test
    fun `GatedHybridFlag forwards suggest to base`() {
        val base = HybridFlagImpl<TestEnv, String, String>("f", SenderParameter(), [])
        val gated =
            GatedHybridFlag(
                base,
                invalidSenderDefault<TestEnv, String, HybridFlagResult<String>>(HybridFlagResult.Absent()),
            )

        val suggestions = withInvocation("zombachu") { gated.suggest(["-f"], "") }

        assertEquals(["zombachu"], suggestions.map { it.value })
    }

    @Test
    fun `GatedStructure suggests its label`() {
        val base =
            StructureImpl("teleport", ["tp"], "", allowed) {
                Signature0({}, LeadingParameterRole.Label, [it])
            }
        val gated = GatedStructure(base, allowed)

        val suggestions = withInvocation("zombachu") { gated.suggest([], "") }

        assertEquals(["tp", "teleport"], suggestions.map { it.value })
    }

    @Test
    fun `GatedOptionalParameter forwards suggest to base`() {
        val gated = GatedOptionalParameter(optionalParameter<String>(), allowedDefault)

        val suggestions = withInvocation("zombachu") { gated.suggest([], "") }

        assertEquals(["zombachu"], suggestions.map { it.value })
    }

    @Test
    fun `GatedOptionalParameter parses absent default from base`() {
        val gated = GatedOptionalParameter(optionalParameter<String>(), allowedDefault)
        val result = withExecutionSender("zombachu") { gated.parse([]) }
        assertEquals("zombachu", result.expectSuccessValue())
    }

    @Test
    fun `GatedOptionalParameter empty args with sender not allowed returns invalidSenderDefault`() {
        val gated = GatedOptionalParameter(rejectedOptionalParameter(), deniedDefault)
        val result = withExecutionSender("zombachu") { gated.parse([]) }
        assertEquals("denied", result.expectSuccessValue())
    }

    @Test
    fun `GatedOptionalParameter non-empty args with sender not allowed fails with InvalidSender`() {
        val gated = GatedOptionalParameter(rejectedOptionalParameter(), deniedDefault)
        val result = withExecutionSender("zombachu", "value") { gated.parse(["value"]) }
        assertSame(Reason.InvalidSender, result.expectReason())
    }

    @Test
    fun `GatedOptionalParameter match on empty args with sender not allowed claims nothing`() {
        val gated = GatedOptionalParameter(rejectedOptionalParameter(), deniedDefault)
        assertEquals(MatchResult.matchedAtLeast(0), withInvocation("zombachu") { gated.match([]) })
    }

    @Test
    fun `GatedOptionalParameter match on non-empty args with sender not allowed fails with InvalidSender`() {
        val gated = GatedOptionalParameter(rejectedOptionalParameter(), deniedDefault)
        val result = withInvocation("zombachu") { gated.match(["value"]) }
        assertSame(Reason.InvalidSender, result.expectUnmatched().expectReason())
    }

    @Test
    fun `GatedOptionalParameter suggest with sender not allowed returns nothing`() {
        val gated = GatedOptionalParameter(rejectedOptionalParameter(), deniedDefault)
        assertEquals([], withInvocation("zombachu") { gated.suggest([], "") })
    }

    @Test
    fun `GatedOptionalParameter getSyntax returns empty when sender not allowed`() {
        val gated = GatedOptionalParameter(rejectedOptionalParameter(), deniedDefault)
        assertEquals("", withInvocation("zombachu") { gated.getSyntax() })
    }

    @Test
    fun `GatedOptionalGroup parses absent default from base`() {
        val gated = GatedOptionalGroup(optionalGroup<String>(), allowedGroupDefault)
        val result = withExecutionSender("zombachu") { gated.parse([]) }
        assertEquals(GroupResult.ResultA("zombachu"), result.expectSuccessValue())
    }

    @Test
    fun `GatedOptionalGroup empty args with sender not allowed returns invalidSenderDefault`() {
        val gated = GatedOptionalGroup(rejectedOptionalGroup(), deniedGroupDefault)
        val result = withExecutionSender("zombachu") { gated.parse([]) }
        assertEquals(GroupResult.ResultA("denied"), result.expectSuccessValue())
    }

    @Test
    fun `GatedOptionalGroup non-empty args with sender not allowed fails with InvalidSender`() {
        val gated = GatedOptionalGroup(rejectedOptionalGroup(), deniedGroupDefault)
        val result = withExecutionSender("zombachu", "orange") { gated.parse(["orange"]) }
        assertSame(Reason.InvalidSender, result.expectReason())
    }

    @Test
    fun `GatedOptionalGroup match on empty args with sender not allowed claims nothing`() {
        val gated = GatedOptionalGroup(rejectedOptionalGroup(), deniedGroupDefault)
        assertEquals(MatchResult.matchedAtLeast(0), withInvocation("zombachu") { gated.match([]) })
    }

    @Test
    fun `GatedOptionalGroup match on non-empty args with sender not allowed fails with InvalidSender`() {
        val gated = GatedOptionalGroup(rejectedOptionalGroup(), deniedGroupDefault)
        val result = withInvocation("zombachu") { gated.match(["apple"]) }
        assertSame(Reason.InvalidSender, result.expectUnmatched().expectReason())
    }

    @Test
    fun `GatedOptionalGroup suggest with sender not allowed returns nothing`() {
        val gated = GatedOptionalGroup(rejectedOptionalGroup(), deniedGroupDefault)
        assertEquals([], withInvocation("zombachu") { gated.suggest([], "") })
    }

    @Test
    fun `GatedOptionalGroup getSyntax returns empty when sender not allowed`() {
        val gated = GatedOptionalGroup(rejectedOptionalGroup(), deniedGroupDefault)
        assertEquals("", withInvocation("zombachu") { gated.getSyntax() })
    }

    private fun rejectedOptionalParameter(): OptionalParameter<TestEnv, String, String, Position.Optional> =
        SenderMappedOptionalParameter(optionalParameter<Int>(), rejectedTransform)

    private fun rejectedOptionalGroup():
        OptionalGroup<TestEnv, String, GroupResult2<String, String>, Position.Optional> =
        SenderMappedOptionalGroup(optionalGroup<Int>(), rejectedTransform)

    private fun <S> optionalParameter(): OptionalParameterImpl<TestEnv, S, String, Position.Optional> =
        OptionalParameterImpl(ValidatedDefaultImpl({ success("$sender") }) { success() }, SenderParameter())

    private fun <S> optionalGroup(): OptionalGroupImpl<TestEnv, S, GroupResult2<String, String>, Position.Optional> =
        OptionalGroupImpl(
            ValidatedDefaultImpl({ success(GroupResult.ResultA("$sender")) }) { success() },
            Group2Impl<TestEnv, S, String, String, Position.Leading>(
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
