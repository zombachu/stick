package com.zombachu.stick.dsl

import com.zombachu.stick.Requirement
import com.zombachu.stick.SenderValidationResult
import com.zombachu.stick.TestEnv
import com.zombachu.stick.element.OptionalParameter
import com.zombachu.stick.element.parameters.IntParameter
import com.zombachu.stick.expectFailure
import com.zombachu.stick.expectSuccessValue
import com.zombachu.stick.feedback.Feedback
import com.zombachu.stick.isSuccess
import com.zombachu.stick.structureTest
import com.zombachu.stick.testInvocation
import com.zombachu.stick.testInvocationSender
import com.zombachu.stick.withInvocationSender
import com.zombachu.stick.withValidationContext
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertSame
import kotlin.test.assertTrue

class OptionalsTest {

    private val intParameter = IntParameter<TestEnv, String>("", "", Int.MIN_VALUE, Int.MAX_VALUE)

    @Test
    fun `default evaluates to given value`() = structureTest {
        val default = default("x")
        assertEquals("x", default.value(testInvocation()).expectSuccessValue())
        assertTrue(withValidationContext { default.validateSender() }.isSuccess())
    }

    @Test
    fun `default uses requirement in sender validation`() = structureTest {
        val default = default("x", Requirement { SenderValidationResult.failSender() })
        val validationResult = withValidationContext { default.validateSender() }
        assertSame(Feedback.InvalidSender, validationResult.expectFailure().feedback)
    }

    @Test
    fun `invalidDefault evaluates to given value`() = structureTest {
        val invalidDefault = invalidDefault("x")
        assertEquals("x", invalidDefault.value(testInvocation()).expectSuccessValue())
    }

    @Test
    fun `defaultSender requires and casts sender type`() = structureTest<Any> {
        val sender = defaultSender<TestEnv, Any, String>()
        val stringSender: Any = "hello"
        val intSender: Any = 42

        val stringSenderResult = withValidationContext(stringSender) { sender.validateSender() }
        assertTrue(stringSenderResult.isSuccess())
        assertEquals("hello", sender.value(testInvocationSender(stringSender)).expectSuccessValue())

        val intSenderResult = withValidationContext(intSender) { sender.validateSender() }
        assertSame(Feedback.InvalidSender, intSenderResult.expectFailure().feedback)
    }

    @Test
    fun `optionally resolves defaults by sender validity`() = structureTest<String> {
        val invalidDefault = invalidDefault(-1, requirement { it.sender == "correct" })
        val optional = optionally(invalidDefault, default(0), intParameter)

        assertEquals(0, withInvocationSender("correct") { optional.parse([]) }.expectSuccessValue())
        assertEquals(99, withInvocationSender("correct", "99") { optional.parse(["99"]) }.expectSuccessValue())

        assertEquals(-1, withInvocationSender("incorrect") { optional.parse([]) }.expectSuccessValue())
        assertSame(
            Feedback.InvalidSender,
            withInvocationSender("incorrect", "5") { optional.parse(["5"]) }.expectFailure().feedback)
    }

    @Test
    fun `optionally defaults without ifInvalid`() = structureTest<String> {
        val optional = optionally(default(7), intParameter)
        assertEquals(7, withInvocationSender("sender") { optional.parse([]) }.expectSuccessValue())
    }

    @Test
    fun `optionally infers nullable type from null defaults`() = structureTest<String> {
        val optional =
            optionally(
                ifInvalid = invalidDefault(null, requirement { it.sender == "correct" }),
                ifAbsent = default(null),
                parameter = intParameter,
            )

        assertNull(withInvocationSender("correct") { optional.parse([]) }.expectSuccessValue())
        assertEquals(1, withInvocationSender("correct") { optional.parse(["1"]) }.expectSuccessValue())
    }

    @Test
    fun `optionallyNullable defaults to null`() = structureTest<String> {
        val optional = optionallyNullable(intParameter)
        assertNull(withInvocationSender("sender") { optional.parse([]) }.expectSuccessValue())
    }
}
