package com.zombachu.stick.element

import com.zombachu.stick.HybridFlagResult
import com.zombachu.stick.ParsingResult
import com.zombachu.stick.SenderValidationResult
import com.zombachu.stick.TestEnv
import com.zombachu.stick.element.parameters.IntParameter
import com.zombachu.stick.expectFailure
import com.zombachu.stick.expectSuccessValue
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
import kotlin.test.assertTrue

class HybridFlagTest {

    private val parameter = IntParameter<TestEnv, Unit>("amount", "", Int.MIN_VALUE, Int.MAX_VALUE)
    private val flag = HybridFlagImpl("boost", parameter, [])

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
