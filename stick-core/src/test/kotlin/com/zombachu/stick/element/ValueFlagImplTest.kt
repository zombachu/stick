package com.zombachu.stick.element

import com.zombachu.stick.CommandResult
import com.zombachu.stick.ConsumingResult
import com.zombachu.stick.MatchResult
import com.zombachu.stick.SimpleSuggestion
import com.zombachu.stick.TestEnv
import com.zombachu.stick.ValidationContext
import com.zombachu.stick.element.parameters.EnumParameter
import com.zombachu.stick.element.parameters.IntParameter
import com.zombachu.stick.expectError
import com.zombachu.stick.expectNoMatch
import com.zombachu.stick.expectSuccessValue
import com.zombachu.stick.expectUnmatched
import com.zombachu.stick.failSenderType
import com.zombachu.stick.failure.Reason
import com.zombachu.stick.invalidSenderDefault
import com.zombachu.stick.isSuccess
import com.zombachu.stick.noMatch
import com.zombachu.stick.presenceFlagParameter
import com.zombachu.stick.presenceValueFlag
import com.zombachu.stick.success
import com.zombachu.stick.testInvocation
import com.zombachu.stick.testInvocationSender
import com.zombachu.stick.withInvocation
import com.zombachu.stick.withInvocationSender
import com.zombachu.stick.withValidationContext
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertTrue

class ValueFlagImplTest {

    private val amountParameter = IntParameter<TestEnv, Unit>("amount", "", Int.MIN_VALUE, Int.MAX_VALUE)
    private val colorParameter =
        EnumParameter<TestEnv, Unit, Color>("", "", mapOf("red" to Color.RED, "green" to Color.GREEN), mapOf())

    @Test
    fun `EnumFlagParameter resolves parameter once`() {
        var resolves = 0
        val counting =
            object : EnumParameter<TestEnv, Unit, Color>("", "", mapOf("red" to Color.RED), mapOf()) {
                context(validationContext: ValidationContext<TestEnv, Unit>)
                override fun resolve(arg0: String): CommandResult<Color> {
                    resolves++
                    return super.resolve(arg0)
                }
            }
        val flag = ValueFlagImpl<TestEnv, Unit, Color>(
            "color",
            { success(Color.GREEN) },
            FlagParameter.EnumFlagParameter(counting),
        )

        val inv = testInvocation("-red")
        val result = inv.processElement(flag)

        assertEquals(Color.RED, result.expectSuccessValue())
        assertEquals(1, resolves)
    }

    @Test
    fun `ParameterFlagParameter resolves parameter once`() {
        var resolves = 0
        val counting =
            object : Parameter.Size1<TestEnv, Unit, String>("name", "") {
                context(validationContext: ValidationContext<TestEnv, Unit>)
                override fun resolve(arg0: String): CommandResult<String> {
                    resolves++
                    return success(arg0)
                }
            }
        val flag = ValueFlagImpl<TestEnv, Unit, String>(
            "player",
            { success("nobody") },
            FlagParameter.ParameterFlagParameter("player", counting, []),
        )

        val inv = testInvocation("-player", "steve")
        val result = inv.processElement(flag)

        assertEquals("steve", result.expectSuccessValue())
        assertEquals(2, inv.consumedArgs)
        assertEquals(1, resolves)
    }

    @Test
    fun `ParameterFlagParameter resolves parameter once with vacuous match`() {
        var resolves = 0
        val cheaplyMatched =
            object : Parameter.Size1<TestEnv, Unit, String>("name", "") {
                context(validationContext: ValidationContext<TestEnv, Unit>)
                override fun match(arg0: String): MatchResult = MatchResult.matchedExactly(1)

                context(validationContext: ValidationContext<TestEnv, Unit>)
                override fun resolve(arg0: String): CommandResult<String> {
                    resolves++
                    return success(arg0)
                }
            }
        val flag = ValueFlagImpl<TestEnv, Unit, String>(
            "player",
            { success("nobody") },
            FlagParameter.ParameterFlagParameter("player", cheaplyMatched, []),
        )

        val inv = testInvocation("-player", "steve")
        val result = inv.processElement(flag)

        assertEquals("steve", result.expectSuccessValue())
        assertEquals(1, resolves)
    }

    @Test
    fun `PresenceFlagParameter parses present value`() {
        val result = withInvocation { presenceFlagParameter<TestEnv, Unit, Boolean>("silent", true).parse(["-silent"]) }
        assertEquals(true, result.expectSuccessValue())
    }

    @Test
    fun `PresenceFlagParameter parse for mismatched label fails with InvalidSyntax NoMatch`() {
        val result = withInvocation { presenceFlagParameter<TestEnv, Unit, Boolean>("silent", true).parse(["-other"]) }
        assertIs<Reason.InvalidSyntax>(result.expectNoMatch().reason)
    }

    @Test
    fun `PresenceFlagParameter match claims label`() {
        val flagParameter = presenceFlagParameter<TestEnv, Unit, Boolean>("silent", true)
        assertEquals(MatchResult.matchedExactly(1), withValidationContext { flagParameter.match(["-silent"]) })
    }

    @Test
    fun `PresenceFlagParameter match for invalid label fails with InvalidSyntax NoMatch`() {
        val flagParameter = presenceFlagParameter<TestEnv, Unit, Boolean>("silent", true)
        val result = withValidationContext { flagParameter.match(["-other"]) }
        assertIs<Reason.InvalidSyntax>(result.expectUnmatched().expectNoMatch().reason)
    }

    @Test
    fun `PresenceFlagParameter getSyntax brackets label`() {
        val syntax = withValidationContext { presenceFlagParameter<TestEnv, Unit, Boolean>("silent", true).getSyntax() }
        assertEquals("[-silent]", syntax)
    }

    @Test
    fun `ParameterFlagParameter parses value and sums consumed size`() {
        val flagParameter = FlagParameter.ParameterFlagParameter("amount", amountParameter, [])

        val result = withInvocation { flagParameter.parse(["-amount", "42"]) }

        assertIs<ConsumingResult.Success<Int>>(result)
        assertEquals(42, result.value)
        assertEquals(2, result.consumed)
    }

    @Test
    fun `ParameterFlagParameter match claims label and value`() {
        val flagParameter = FlagParameter.ParameterFlagParameter("amount", amountParameter, [])
        assertEquals(MatchResult.matchedExactly(2), withValidationContext { flagParameter.match(["-amount", "42"]) })
    }

    @Test
    fun `ParameterFlagParameter matches partially without a value`() {
        val flagParameter = FlagParameter.ParameterFlagParameter("amount", amountParameter, [])
        assertEquals(MatchResult.partial(), withValidationContext { flagParameter.match(["-amount"]) })
    }

    @Test
    fun `ParameterFlagParameter match for invalid argument fails with TypeNotMatched Error`() {
        val flagParameter = FlagParameter.ParameterFlagParameter("amount", amountParameter, [])

        val result = withValidationContext { flagParameter.match(["-amount", "many"]) }

        assertEquals(Reason.TypeNotMatched("integer", "many"), result.expectUnmatched().expectError().reason)
    }

    @Test
    fun `ParameterFlagParameter match for silent mismatch fails with InvalidSyntax Error`() {
        val silent =
            object : Parameter.Size1<TestEnv, Unit, String>("", "") {
                context(validationContext: ValidationContext<TestEnv, Unit>)
                override fun resolve(arg0: String): CommandResult<String> = noMatch()
            }
        val flagParameter = FlagParameter.ParameterFlagParameter("name", silent, [])

        val result = withValidationContext { flagParameter.match(["-name", "x"]) }

        assertIs<Reason.InvalidSyntax>(result.expectUnmatched().expectError().reason)
    }

    @Test
    fun `ParameterFlagParameter parse for invalid label fails with InvalidSyntax NoMatch`() {
        val flagParameter = FlagParameter.ParameterFlagParameter("amount", amountParameter, [])
        val result = withInvocation { flagParameter.parse(["-other", "42"]) }
        assertIs<Reason.InvalidSyntax>(result.expectNoMatch().reason)
    }

    @Test
    fun `EnumFlagParameter match claims enum token`() {
        val flagParameter = FlagParameter.EnumFlagParameter(colorParameter)
        assertEquals(MatchResult.matchedExactly(1), withValidationContext { flagParameter.match(["-red"]) })
    }

    @Test
    fun `EnumFlagParameter match for invalid argument fails with InvalidSyntax NoMatch`() {
        val flagParameter = FlagParameter.EnumFlagParameter(colorParameter)
        val result = withValidationContext { flagParameter.match(["-blue"]) }
        assertIs<Reason.InvalidSyntax>(result.expectUnmatched().expectNoMatch().reason)
    }

    @Test
    fun `EnumFlagParameter parses flag token as enum key`() {
        val flagParameter = FlagParameter.EnumFlagParameter(colorParameter)
        val result = withInvocation { flagParameter.parse(["-red"]) }
        assertEquals(Color.RED, result.expectSuccessValue())
    }

    @Test
    fun `EnumFlagParameter parse for invalid argument fails with InvalidSyntax NoMatch`() {
        val flagParameter = FlagParameter.EnumFlagParameter(colorParameter)
        val result = withInvocation { flagParameter.parse(["-blue"]) }
        assertIs<Reason.InvalidSyntax>(result.expectNoMatch().reason)
    }

    @Test
    fun `EnumFlagParameter parse for empty args fails with InvalidSyntax NoMatch`() {
        val flagParameter = FlagParameter.EnumFlagParameter(colorParameter)
        val result = withInvocation { flagParameter.parse([]) }
        assertIs<Reason.InvalidSyntax>(result.expectNoMatch().reason)
    }

    @Test
    fun `EnumFlagParameter parse for argument with no prefix fails with InvalidSyntax NoMatch`() {
        val flagParameter = FlagParameter.EnumFlagParameter(colorParameter)

        assertIs<Reason.InvalidSyntax>(withInvocation { flagParameter.parse([""]) }.expectNoMatch().reason)
        assertIs<Reason.InvalidSyntax>(withInvocation { flagParameter.parse(["red"]) }.expectNoMatch().reason)
    }

    @Test
    fun `EnumFlagParameter suggests prefixed primary and aliased values`() {
        val parameter =
            EnumParameter<TestEnv, Unit, Color>("color", "", mapOf("red" to Color.RED, "green" to Color.GREEN), mapOf("r" to Color.RED))
        val flagParameter = FlagParameter.EnumFlagParameter(parameter)
        assertEquals(
            [SimpleSuggestion("-red"), SimpleSuggestion("-green"), SimpleSuggestion("-r", isAlias = true)],
            withValidationContext { flagParameter.suggest([], "") },
        )
    }

    @Test
    fun `ValueFlagImpl delegates to flag parameter`() {
        val flag = presenceValueFlag<TestEnv, Unit, Boolean>("silent", false, true)

        assertEquals(true, withInvocation { flag.parse(["-silent"]) }.expectSuccessValue())
        assertEquals("[-silent]", withValidationContext { flag.getSyntax() })
        assertEquals(false, flag.default(testInvocation()).expectSuccessValue())
    }

    @Test
    fun `TransformedValueFlag delegates to flag parameter`() {
        val base = presenceValueFlag<TestEnv, String, Boolean>("silent", false, true)
        val transformed = TransformedValueFlag(base, { it: Int -> it.toString() }, invalidSenderDefault(false))

        val result = withInvocationSender(1) { transformed.parse(["-silent"]) }

        assertEquals(true, result.expectSuccessValue())
    }

    @Test
    fun `TransformedValueFlag validateSender delegates to invalid sender default`() {
        var validated = false
        val base = presenceValueFlag<TestEnv, String, Boolean>("silent", false, true)
        val invalidDefault =
            invalidSenderDefault<TestEnv, Int, Boolean>(false) {
                validated = true
                success()
            }
        val transformed = TransformedValueFlag(base, { it: Int -> it.toString() }, invalidDefault)

        val result = withValidationContext(1) { transformed.validateSender() }

        assertTrue(result.isSuccess())
        assertTrue(validated)
    }

    @Test
    fun `TransformedValueFlag default for accessible flag returns Absent`() {
        val base = presenceValueFlag<TestEnv, String, Boolean>("silent", false, true)
        val transformed = TransformedValueFlag(base, { it: Int -> it.toString() }, invalidSenderDefault(true))
        val result = transformed.default(testInvocationSender(1))
        assertEquals(false, result.expectSuccessValue())
    }

    @Test
    fun `TransformedValueFlag default for inaccessible flag returns invalid sender default`() {
        val base = presenceValueFlag<TestEnv, String, Boolean>("silent", false, true)
        val invalidDefault =
            invalidSenderDefault<TestEnv, Int, Boolean>(true) { failSenderType(String::class) }
        val transformed = TransformedValueFlag(base, { it: Int -> it.toString() }, invalidDefault)

        val result = transformed.default(testInvocationSender(1))

        assertEquals(true, result.expectSuccessValue())
    }

    private enum class Color {
        RED,
        GREEN,
    }
}
