package com.zombachu.stick.element

import com.zombachu.stick.CommandResult
import com.zombachu.stick.ParsingResult
import com.zombachu.stick.SenderValidationResult
import com.zombachu.stick.TestEnv
import com.zombachu.stick.element.parameters.EnumParameter
import com.zombachu.stick.element.parameters.IntParameter
import com.zombachu.stick.expectSuccessValue
import com.zombachu.stick.impl.InvalidSenderDefault
import com.zombachu.stick.impl.ValidatedDefaultImpl
import com.zombachu.stick.isSuccess
import com.zombachu.stick.testInvocation
import com.zombachu.stick.withInvocation
import com.zombachu.stick.withInvocationSender
import com.zombachu.stick.withValidationContext
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertSame
import kotlin.test.assertTrue

class ValueFlagTest {

    private val amountParameter = IntParameter<TestEnv, Unit>("amount", "", Int.MIN_VALUE, Int.MAX_VALUE)
    private val colorParameter =
        EnumParameter<TestEnv, Unit, Color>("", "", mapOf("red" to Color.RED, "green" to Color.GREEN), mapOf())

    @Test
    fun `PresenceFlagParameter returns present value on match`() {
        val result = withInvocation { presenceFlagParameter<Unit>().parse(["-silent"]) }
        assertEquals(true, result.expectSuccessValue())
    }

    @Test
    fun `PresenceFlagParameter mismatch fails with TypeNotMatchedInternal`() {
        val result = withInvocation { presenceFlagParameter<Unit>().parse(["-other"]) }
        assertSame(ParsingResult.TypeNotMatchedInternal, result)
    }

    @Test
    fun `PresenceFlagParameter getSyntax brackets label`() {
        val syntax = withValidationContext { presenceFlagParameter<Unit>().getSyntax() }
        assertEquals("[-silent]", syntax)
    }

    @Test
    fun `ParameterFlagParameter delegates to parameter and sums consumed size`() {
        val flagParameter = FlagParameter.ParameterFlagParameter(amountParameter, [])

        val result = withInvocation { flagParameter.parse(["-amount", "42"]) }

        assertIs<CommandResult.Success<Int>>(result)
        assertEquals(42, result.value)
        assertEquals(2, result.consumed.size)
    }

    @Test
    fun `ParameterFlagParameter invalid argument fails with TypeNotMatchedInternal`() {
        val flagParameter = FlagParameter.ParameterFlagParameter(amountParameter, [])
        val result = withInvocation { flagParameter.parse(["-other", "42"]) }
        assertSame(ParsingResult.TypeNotMatchedInternal, result)
    }

    @Test
    fun `EnumFlagParameter parses flag token as enum key`() {
        val flagParameter = FlagParameter.EnumFlagParameter(colorParameter)
        val result = withInvocation { flagParameter.parse(["-red"]) }
        assertEquals(Color.RED, result.expectSuccessValue())
    }

    @Test
    fun `EnumFlagParameter invalid argument fails with TypeNotMatchedInternal`() {
        val flagParameter = FlagParameter.EnumFlagParameter(colorParameter)
        val result = withInvocation { flagParameter.parse(["-blue"]) }
        assertSame(ParsingResult.TypeNotMatchedInternal, result)
    }

    @Test
    fun `EnumFlagParameter empty args fails with TypeNotMatchedInternal`() {
        val flagParameter = FlagParameter.EnumFlagParameter(colorParameter)
        val result = withInvocation { flagParameter.parse([]) }
        assertSame(ParsingResult.TypeNotMatchedInternal, result)
    }

    @Test
    fun `ValueFlagImpl delegates to flag parameter`() {
        val flag = ValueFlagImpl("silent", { ParsingResult.success(false) }, presenceFlagParameter<Unit>())

        assertEquals(true, withInvocation { flag.parse(["-silent"]) }.expectSuccessValue())
        assertEquals("[-silent]", withValidationContext { flag.getSyntax() })
        assertEquals(false, flag.default(testInvocation()).expectSuccessValue())
    }

    @Test
    fun `TransformedValueFlag delegates to flag parameter`() {
        val base = ValueFlagImpl("silent", { ParsingResult.success(false) }, presenceFlagParameter<String>())
        val invalidSenderDefault: InvalidSenderDefault<TestEnv, Int, Boolean> =
            ValidatedDefaultImpl({ ParsingResult.success(false) }) { SenderValidationResult.success() }
        val transformed = TransformedValueFlag(base, { it: Int -> it.toString() }, invalidSenderDefault)

        val result = withInvocationSender(1) { transformed.parse(["-silent"]) }

        assertEquals(true, result.expectSuccessValue())
    }

    @Test
    fun `TransformedValueFlag validateSender delegates to invalid sender default`() {
        var validated = false
        val base = ValueFlagImpl("silent", { ParsingResult.success(false) }, presenceFlagParameter<String>())
        val invalidSenderDefault: InvalidSenderDefault<TestEnv, Int, Boolean> =
            ValidatedDefaultImpl({ ParsingResult.success(false) }) {
                validated = true
                SenderValidationResult.success()
            }
        val transformed = TransformedValueFlag(base, { it: Int -> it.toString() }, invalidSenderDefault)

        val result = withValidationContext(1) { transformed.validateSender() }

        assertTrue(result.isSuccess())
        assertTrue(validated)
    }

    private fun <S> presenceFlagParameter(): FlagParameter.PresenceFlagParameter<TestEnv, S, Boolean> =
        FlagParameter.PresenceFlagParameter("silent", { ParsingResult.success(true) }, [], "")

    private enum class Color {
        RED,
        GREEN,
    }
}
