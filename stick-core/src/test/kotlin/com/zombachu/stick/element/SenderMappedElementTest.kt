package com.zombachu.stick.element

import com.zombachu.stick.Arguments0
import com.zombachu.stick.CommandResult
import com.zombachu.stick.Environment
import com.zombachu.stick.GroupResult
import com.zombachu.stick.GroupResult2
import com.zombachu.stick.HybridFlagResult
import com.zombachu.stick.Invocation
import com.zombachu.stick.Position
import com.zombachu.stick.Requirement
import com.zombachu.stick.SimpleSuggestion
import com.zombachu.stick.Suggestion
import com.zombachu.stick.TestEnv
import com.zombachu.stick.element.parameters.LiteralParameter
import com.zombachu.stick.expectReason
import com.zombachu.stick.expectSuccessValue
import com.zombachu.stick.failSender
import com.zombachu.stick.failure.Reason
import com.zombachu.stick.invalidSenderDefault
import com.zombachu.stick.isSuccess
import com.zombachu.stick.presenceValueFlag
import com.zombachu.stick.success
import com.zombachu.stick.testExecutionSender
import com.zombachu.stick.withExecutionSender
import com.zombachu.stick.withInvocation
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertSame
import kotlin.test.assertTrue

class SenderMappedElementTest {

    private val lengthIs8 = Requirement<TestEnv, Int> { if (sender == 8) success() else failSender() }

    @Test
    fun `SenderMappedParameter suggests with the transformed sender`() {
        val mapped = SenderMappedParameter(SenderParameter<TestEnv, Int>(), String::length)

        val suggestions = withInvocation("zombachu") { mapped.suggest([], "") }

        assertEquals(["8"], suggestions.map { it.value })
    }

    @Test
    fun `SenderMappedParameter parses with the transformed sender`() {
        val mapped = SenderMappedParameter(SenderParameter<TestEnv, Int>(), String::length)
        val result = withExecutionSender("zombachu", "x") { mapped.parse(["x"]) }
        assertEquals("8", result.expectSuccessValue())
    }

    @Test
    fun `SenderMappedParameter getSyntax uses the transformed sender`() {
        val mapped = SenderMappedParameter(SenderParameter<TestEnv, Int>(), String::length)
        assertEquals("<8>", withInvocation("zombachu") { mapped.getSyntax() })
    }

    @Test
    fun `SenderMappedParameter reports base element type`() {
        val mapped = SenderMappedParameter(LiteralParameter<TestEnv, Int>("give", [], ""), String::length)
        assertEquals(GroupableType.Literal, mapped.type)
    }

    @Test
    fun `SenderMappedValueFlag suggests with the transformed sender`() {
        val flagParameter = FlagParameter.ParameterFlagParameter("f", SenderParameter<TestEnv, Int>(), [])
        val mapped = SenderMappedValueFlag(ValueFlagImpl("f", { success("") }, flagParameter), String::length)

        val suggestions = withInvocation("zombachu") { mapped.suggest(["-f"], "") }

        assertEquals(["8"], suggestions.map { it.value })
    }

    @Test
    fun `SenderMappedValueFlag default runs with the transformed sender`() {
        val flagParameter = FlagParameter.ParameterFlagParameter("f", SenderParameter<TestEnv, Int>(), [])
        val mapped = SenderMappedValueFlag(ValueFlagImpl("f", { success("$sender") }, flagParameter), String::length)

        val result = mapped.default(testExecutionSender("zombachu"))

        assertEquals("8", result.expectSuccessValue())
    }

    @Test
    fun `SenderMappedValueFlag validateSender forwards to base with the transformed sender`() {
        val base = presenceValueFlag<TestEnv, Int, Boolean>("f", false, true)
        val gated =
            GatedValueFlag(base, invalidSenderDefault<TestEnv, Int, Boolean>(false) { lengthIs8.validateSender() })
        val mapped = SenderMappedValueFlag(gated, String::length)

        assertTrue(withInvocation("zombachu") { mapped.validateSender() }.isSuccess())
        assertSame(Reason.InvalidSender, withInvocation("steve") { mapped.validateSender() }.expectReason())
    }

    @Test
    fun `SenderMappedHybridFlag suggests with the transformed sender`() {
        val base = HybridFlagImpl<TestEnv, Int, String>("f", SenderParameter(), [])
        val mapped = SenderMappedHybridFlag(base, String::length)

        val suggestions = withInvocation("zombachu") { mapped.suggest(["-f"], "") }

        assertEquals(["8"], suggestions.map { it.value })
    }

    @Test
    fun `SenderMappedHybridFlag default forwards to base`() {
        val base = HybridFlagImpl<TestEnv, Int, String>("f", SenderParameter(), [])
        val gated =
            GatedHybridFlag(
                base,
                invalidSenderDefault<TestEnv, Int, HybridFlagResult<String>>(HybridFlagResult.Present()) {
                    lengthIs8.validateSender()
                },
            )
        val mapped = SenderMappedHybridFlag(gated, String::length)

        val result = mapped.default(testExecutionSender("steve"))

        assertIs<HybridFlagResult.Present<String>>(result.expectSuccessValue())
    }

    @Test
    fun `SenderMappedHybridFlag validateSender forwards to base with the transformed sender`() {
        val base = HybridFlagImpl<TestEnv, Int, String>("f", SenderParameter(), [])
        val gated =
            GatedHybridFlag(
                base,
                invalidSenderDefault<TestEnv, Int, HybridFlagResult<String>>(HybridFlagResult.Absent()) {
                    lengthIs8.validateSender()
                },
            )
        val mapped = SenderMappedHybridFlag(gated, String::length)

        assertTrue(withInvocation("zombachu") { mapped.validateSender() }.isSuccess())
        assertSame(Reason.InvalidSender, withInvocation("steve") { mapped.validateSender() }.expectReason())
    }

    @Test
    fun `SenderMappedOptionalParameter suggests with the transformed sender`() {
        val mapped = SenderMappedOptionalParameter(optionalParameter(), String::length)

        val suggestions = withInvocation("zombachu") { mapped.suggest([], "") }

        assertEquals(["8"], suggestions.map { it.value })
    }

    @Test
    fun `SenderMappedOptionalParameter parses absent default with the transformed sender`() {
        val mapped = SenderMappedOptionalParameter(optionalParameter(), String::length)
        val result = withExecutionSender("zombachu") { mapped.parse([]) }
        assertEquals("8", result.expectSuccessValue())
    }

    @Test
    fun `SenderMappedOptionalGroup suggests from base`() {
        val mapped = SenderMappedOptionalGroup(optionalGroup(), String::length)

        val suggestions = withInvocation("zombachu") { mapped.suggest([], "") }

        assertEquals(["apple", "orange"], suggestions.map { it.value })
    }

    @Test
    fun `SenderMappedOptionalGroup parses absent default with the transformed sender`() {
        val mapped = SenderMappedOptionalGroup(optionalGroup(), String::length)
        val result = withExecutionSender("zombachu") { mapped.parse([]) }
        assertEquals(GroupResult.ResultA("8"), result.expectSuccessValue())
    }

    @Test
    fun `SenderMappedStructure suggests its label`() {
        val base =
            StructureImpl<TestEnv, Int, Arguments0>("teleport", ["tp"], "") {
                Signature0({}, LeadingParameterRole.Label, [it])
            }
        val mapped = SenderMappedStructure(base, String::length)

        val suggestions = withInvocation("zombachu") { mapped.suggest([], "") }

        assertEquals(["tp", "teleport"], suggestions.map { it.value })
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
        override fun resolve(arg0: String): CommandResult<String> = success("${inv.sender}")

        context(inv: Invocation<E, S>)
        override fun getSyntax(): String = "<${inv.sender}>"
    }
}
