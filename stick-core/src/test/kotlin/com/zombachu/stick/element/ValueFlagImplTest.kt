package com.zombachu.stick.element

import com.zombachu.stick.CommandResult
import com.zombachu.stick.MatchResult
import com.zombachu.stick.ParsingResult
import com.zombachu.stick.SenderValidationResult
import com.zombachu.stick.ValidationContext
import com.zombachu.stick.TestEnv
import com.zombachu.stick.element.parameters.EnumParameter
import com.zombachu.stick.element.parameters.IntParameter
import com.zombachu.stick.expectUnmatched
import com.zombachu.stick.expectSuccessValue
import com.zombachu.stick.invalidSenderDefault
import com.zombachu.stick.isSuccess
import com.zombachu.stick.presenceFlagParameter
import com.zombachu.stick.presenceValueFlag
import com.zombachu.stick.testInvocation
import com.zombachu.stick.withInvocation
import com.zombachu.stick.withInvocationSender
import com.zombachu.stick.withValidationContext
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertSame
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
            { ParsingResult.success(Color.GREEN) },
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
                    return ParsingResult.success(arg0)
                }
            }
        val flag = ValueFlagImpl<TestEnv, Unit, String>(
            "player",
            { ParsingResult.success("nobody") },
            FlagParameter.ParameterFlagParameter("player", counting, []),
        )

        val inv = testInvocation("-player", "steve")
        val result = inv.processElement(flag)

        assertEquals("steve", result.expectSuccessValue())
        assertEquals(2, inv.consumedArgs)
        assertEquals(1, resolves)
    }

    @Test
    fun `ParameterFlagParameter resolves parameter with vacuous match`() {
        var resolves = 0
        val cheaplyMatched =
            object : Parameter.Size1<TestEnv, Unit, String>("name", "") {
                context(validationContext: ValidationContext<TestEnv, Unit>)
                override fun match(arg0: String): MatchResult = MatchResult.matched(1)

                context(validationContext: ValidationContext<TestEnv, Unit>)
                override fun resolve(arg0: String): CommandResult<String> {
                    resolves++
                    return ParsingResult.success(arg0)
                }
            }
        val flag = ValueFlagImpl<TestEnv, Unit, String>(
            "player",
            { ParsingResult.success("nobody") },
            FlagParameter.ParameterFlagParameter("player", cheaplyMatched, []),
        )

        val inv = testInvocation("-player", "steve")
        val result = inv.processElement(flag)

        assertEquals("steve", result.expectSuccessValue())
        assertEquals(1, resolves)
    }

    @Test
    fun `PresenceFlagParameter returns present value on match`() {
        val result = withInvocation { presenceFlagParameter<TestEnv, Unit, Boolean>("silent", true).parse(["-silent"]) }
        assertEquals(true, result.expectSuccessValue())
    }

    @Test
    fun `PresenceFlagParameter mismatch fails with TypeNotMatchedInternal`() {
        val result = withInvocation { presenceFlagParameter<TestEnv, Unit, Boolean>("silent", true).parse(["-other"]) }
        assertSame(ParsingResult.TypeNotMatchedInternal, result)
    }

    @Test
    fun `PresenceFlagParameter match claims label`() {
        val flagParameter = presenceFlagParameter<TestEnv, Unit, Boolean>("silent", true)
        assertEquals(MatchResult.matched(1), withValidationContext { flagParameter.match(["-silent"]) })
    }

    @Test
    fun `PresenceFlagParameter match unmatched fails with TypeNotMatchedInternal`() {
        val flagParameter = presenceFlagParameter<TestEnv, Unit, Boolean>("silent", true)
        val result = withValidationContext { flagParameter.match(["-other"]) }
        assertSame(ParsingResult.TypeNotMatchedInternal, result.expectUnmatched())
    }

    @Test
    fun `PresenceFlagParameter getSyntax brackets label`() {
        val syntax = withValidationContext { presenceFlagParameter<TestEnv, Unit, Boolean>("silent", true).getSyntax() }
        assertEquals("[-silent]", syntax)
    }

    @Test
    fun `ParameterFlagParameter delegates to parameter and sums consumed size`() {
        val flagParameter = FlagParameter.ParameterFlagParameter("amount", amountParameter, [])

        val result = withInvocation { flagParameter.parse(["-amount", "42"]) }

        assertIs<CommandResult.Success<Int>>(result)
        assertEquals(42, result.value)
        assertEquals(2, result.consumed)
    }

    @Test
    fun `ParameterFlagParameter match claims label and value`() {
        val flagParameter = FlagParameter.ParameterFlagParameter("amount", amountParameter, [])
        assertEquals(MatchResult.matched(2), withValidationContext { flagParameter.match(["-amount", "42"]) })
    }

    @Test
    fun `ParameterFlagParameter match without a value is partial`() {
        val flagParameter = FlagParameter.ParameterFlagParameter("amount", amountParameter, [])
        assertEquals(MatchResult.partial(1), withValidationContext { flagParameter.match(["-amount"]) })
    }

    @Test
    fun `ParameterFlagParameter invalid argument fails with TypeNotMatchedInternal`() {
        val flagParameter = FlagParameter.ParameterFlagParameter("amount", amountParameter, [])
        val result = withInvocation { flagParameter.parse(["-other", "42"]) }
        assertSame(ParsingResult.TypeNotMatchedInternal, result)
    }

    @Test
    fun `EnumFlagParameter match claims enum token`() {
        val flagParameter = FlagParameter.EnumFlagParameter(colorParameter)
        assertEquals(MatchResult.matched(1), withValidationContext { flagParameter.match(["-red"]) })
    }

    @Test
    fun `EnumFlagParameter match of an unknown key fails with TypeNotMatchedInternal`() {
        val flagParameter = FlagParameter.EnumFlagParameter(colorParameter)
        val result = withValidationContext { flagParameter.match(["-purple"]) }
        assertSame(ParsingResult.TypeNotMatchedInternal, result.expectUnmatched())
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
    fun `EnumFlagParameter argument with no prefix fails with TypeNotMatchedInternal`() {
        val flagParameter = FlagParameter.EnumFlagParameter(colorParameter)

        assertSame(ParsingResult.TypeNotMatchedInternal, withInvocation { flagParameter.parse([""]) })
        assertSame(ParsingResult.TypeNotMatchedInternal, withInvocation { flagParameter.parse(["red"]) })
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
                SenderValidationResult.success()
            }
        val transformed = TransformedValueFlag(base, { it: Int -> it.toString() }, invalidDefault)

        val result = withValidationContext(1) { transformed.validateSender() }

        assertTrue(result.isSuccess())
        assertTrue(validated)
    }

    private enum class Color {
        RED,
        GREEN,
    }
}
