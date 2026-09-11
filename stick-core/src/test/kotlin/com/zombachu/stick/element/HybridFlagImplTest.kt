package com.zombachu.stick.element

import com.zombachu.stick.CommandResult
import com.zombachu.stick.ConsumingResult
import com.zombachu.stick.HybridFlagResult
import com.zombachu.stick.MatchResult
import com.zombachu.stick.ParsingResult
import com.zombachu.stick.SenderValidationResult
import com.zombachu.stick.Size
import com.zombachu.stick.TestEnv
import com.zombachu.stick.ValidationContext
import com.zombachu.stick.consuming
import com.zombachu.stick.element.parameters.IntParameter
import com.zombachu.stick.expectFailure
import com.zombachu.stick.expectSuccessValue
import com.zombachu.stick.expectUnmatched
import com.zombachu.stick.feedback.Feedback
import com.zombachu.stick.invalidSenderDefault
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

class HybridFlagImplTest {

    private val parameter = IntParameter<TestEnv, Unit>("amount", "", Int.MIN_VALUE, Int.MAX_VALUE)
    private val flag = HybridFlagImpl("boost", parameter, [])

    @Test
    fun `parse resolves parameter once`() {
        var resolves = 0
        val counting =
            object : Parameter.Size1<TestEnv, Unit, String>("name", "") {
                context(validationContext: ValidationContext<TestEnv, Unit>)
                override fun resolve(arg0: String): CommandResult<String> {
                    resolves++
                    return ParsingResult.success(arg0)
                }
            }
        val countingFlag = HybridFlagImpl("rank", counting, [])

        val inv = testInvocation("-rank", "guest")
        val result = inv.processElement(countingFlag)

        assertEquals("guest", assertIs<HybridFlagResult.Value<String>>(result.expectSuccessValue()).value)
        assertEquals(2, inv.consumedArgs)
        assertEquals(1, resolves)
    }

    @Test
    fun `parse consumes what variable-width parameter took`() {
        val varying =
            object : Parameter.Bounded<TestEnv, Unit, String>(Size.between(1, 2), "v", "") {
                context(validationContext: ValidationContext<TestEnv, Unit>)
                override fun resolve(args: List<String>): ConsumingResult<String> = ParsingResult.success(args[0]).consuming(1)
            }
        val varyingFlag = HybridFlagImpl("boost", varying, [])

        val inv = testInvocation("-boost", "a", "b")
        val result = inv.processElement(varyingFlag)

        assertEquals("a", assertIs<HybridFlagResult.Value<String>>(result.expectSuccessValue()).value)
        assertEquals(2, inv.consumedArgs)
    }

    @Test
    fun `empty args fails with TypeNotMatchedInternal`() {
        val result = withInvocation { flag.parse([]) }
        assertIs<ParsingResult.TypeNotMatchedInternal>(result)
    }

    @Test
    fun `matches with no trailing value returns Present`() {
        val result = withInvocation { flag.parse(["-boost"]) }
        assertIs<HybridFlagResult.Present<Int>>(result.expectSuccessValue())
    }

    @Test
    fun `matches with trailing value parses value`() {
        val result = withInvocation { flag.parse(["-boost", "5"]) }

        val value = result.expectSuccessValue()
        assertIs<HybridFlagResult.Value<Int>>(value)
        assertEquals(5, value.value)
    }

    @Test
    fun `match with no trailing value claims label`() {
        assertEquals(MatchResult.matchedAtLeast(1), withValidationContext { flag.match(["-boost"]) })
    }

    @Test
    fun `match with trailing value claims label and value`() {
        assertEquals(MatchResult.matchedExactly(2), withValidationContext { flag.match(["-boost", "5"]) })
    }

    @Test
    fun `match unmatched fails with TypeNotMatchedInternal`() {
        val result = withValidationContext { flag.match(["-other"]) }
        assertSame(ParsingResult.TypeNotMatchedInternal, result.expectUnmatched())
    }

    @Test
    fun `parameter failure fails with TypeNotMatched`() {
        val result = withInvocation { flag.parse(["-boost", "not-a-number"]) }
        assertEquals(Feedback.TypeNotMatched("integer", "not-a-number"), result.expectFailure().feedback)
    }

    @Test
    fun `mismatch fails with TypeNotMatchedInternal`() {
        val result = withInvocation { flag.parse(["-other"]) }
        assertIs<ParsingResult.TypeNotMatchedInternal>(result)
    }

    @Test
    fun `default value is Absent`() {
        val defaultResult = flag.default(testInvocation())
        assertIs<HybridFlagResult.Absent<Int>>(defaultResult.expectSuccessValue())
    }

    @Test
    fun `getSyntax nests flag parameter syntax`() {
        val syntax = withValidationContext { flag.getSyntax() }
        assertEquals("[-boost [amount]]", syntax)
    }

    @Test
    fun `TransformedHybridFlag delegates parse to flag parameter`() {
        val invalidDefault = invalidSenderDefault<TestEnv, Int, HybridFlagResult<Int>>(HybridFlagResult.Absent())
        val transformed = TransformedHybridFlag(flag, { }, invalidDefault)

        val result = withInvocationSender(1) { transformed.parse(["-boost", "5"]) }

        val value = result.expectSuccessValue()
        assertIs<HybridFlagResult.Value<Int>>(value)
        assertEquals(5, value.value)
    }

    @Test
    fun `TransformedHybridFlag validateSender delegates to invalid sender default`() {
        var validated = false
        val invalidDefault =
            invalidSenderDefault<TestEnv, Int, HybridFlagResult<Int>>(HybridFlagResult.Absent()) {
                validated = true
                SenderValidationResult.success()
            }
        val transformed = TransformedHybridFlag(flag, { }, invalidDefault)

        val result = withValidationContext(1) { transformed.validateSender() }

        assertTrue(result.isSuccess())
        assertTrue(validated)
    }
}
