package com.zombachu.stick

import com.zombachu.stick.feedback.Feedback
import com.zombachu.stick.impl.Size
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertSame
import kotlin.test.assertTrue

class CommandResultTest {

    @Test
    fun `success wraps value and consumes`() {
        val result = ParsingResult.success("value", Size(3))

        assertTrue(result.isSuccess())
        assertEquals("value", result.value)
        assertEquals(3, result.consumed.size)
    }

    @Test
    fun `success consumed defaults to zero`() {
        val result = ParsingResult.success("value")
        assertTrue(result.isSuccess())
        assertEquals(0, result.consumed.size)
    }

    @Test
    fun `failUnknown returns Unknown singleton`() {
        val result = ParsingResult.failUnknown()
        assertFalse(result.isSuccess())
        assertSame(Feedback.Unknown, result.feedback)
    }

    @Test
    fun `failHandled returns HandledError singleton`() {
        val result = ParsingResult.failHandled()
        assertFalse(result.isSuccess())
        assertSame(ParsingResult.HandledError, result)
    }

    @Test
    fun `failTypeInternal returns TypeNotMatchedInternal singleton`() {
        val result = ParsingResult.failTypeInternal()
        assertFalse(result.isSuccess())
        assertSame(ParsingResult.TypeNotMatchedInternal, result)
    }

    @Test
    fun `failType feedback wraps type and arg`() {
        val result = ParsingResult.failType("boolean", "xyz")
        assertFalse(result.isSuccess())
        assertEquals(Feedback.TypeNotMatched("boolean", "xyz"), result.feedback)
    }

    @Test
    fun `failLiteral feedback wraps valid values and arg`() {
        val result = ParsingResult.failLiteral(listOf("a", "b"), "c")
        assertFalse(result.isSuccess())
        assertEquals(Feedback.LiteralNotMatched(listOf("a", "b"), "c"), result.feedback)
    }

    @Test
    fun `failSyntax feedback wraps usage string`() {
        val result = ParsingResult.failSyntax("/foo <bar>")
        assertFalse(result.isSuccess())
        assertEquals(Feedback.InvalidSyntax("/foo <bar>"), result.feedback)
    }

    @Test
    fun `failRange feedback wraps min max and arg`() {
        val result = ParsingResult.failRange("0", "10", "20")
        assertFalse(result.isSuccess())
        assertEquals(Feedback.OutOfRange("0", "10", "20"), result.feedback)
    }

    @Test
    fun `senderValidationResult returns Success singleton`() {
        val result = SenderValidationResult.success()
        assertTrue(result.isSuccess())
        assertSame(SenderValidationResult.Success, result)
    }

    @Test
    fun `senderValidationResult wraps expected feedback`() {
        assertEquals(Feedback.InvalidSender, SenderValidationResult.failSender().feedback)
        assertEquals(Feedback.InvalidPermission, SenderValidationResult.failPermission().feedback)
        assertEquals(Feedback.InvalidSenderType, SenderValidationResult.failSenderType().feedback)
    }

    @Test
    fun `propagateError does not invoke callback on success`() {
        var called = false
        val result = ParsingResult.success("ok")
        result.propagateError {
            called = true
            error("shouldn't be called")
        }

        assertFalse(called)
    }

    @Test
    fun `propagateError invokes callback inline`() {
        fun run(result: CommandResult<String>): String {
            result.propagateError {
                return "propagated"
            }
            return "success:${result.value}"
        }
        assertEquals("propagated", run(ParsingResult.failUnknown()))
        assertEquals("success:ok", run(ParsingResult.success("ok")))
    }

    @Test
    fun `valueOrPropagateError returns value on success`() {
        val result = ParsingResult.success("ok")
        val value = result.valueOrPropagateError { error("shouldn't be called") }
        assertEquals("ok", value)
    }

    @Test
    fun `valueOrPropagateError propagates on failure without producing value`() {
        fun run(result: CommandResult<String>): String {
            val value = result.valueOrPropagateError {
                return "propagated"
            }
            return "success:$value"
        }
        assertEquals("propagated", run(ParsingResult.failUnknown()))
    }

    @Test
    fun `handleInternal dispatches based on success`() {
        val successResult: CommandResult<String> = ParsingResult.success("ok")
        val failureResult: CommandResult<String> = ParsingResult.failUnknown()

        assertEquals("s", successResult.handleInternal(onSuccess = { "s" }, onFailure = { "f" }))
        assertEquals("f", failureResult.handleInternal(onSuccess = { "s" }, onFailure = { "f" }))
    }

    @Test
    fun `withSize sets result size on success`() {
        val result = ParsingResult.success("ok", Size(1)).withSize(Size(5))

        assertTrue(result.isSuccess())
        assertEquals("ok", result.value)
        assertEquals(5, result.consumed.size)
    }

    @Test
    fun `withSize passes through failures unchanged`() {
        val failure = ParsingResult.failSyntax("usage")
        val result = failure.withSize(Size(5))
        assertSame(failure, result)
    }

    @Test
    fun `handle runs block with feedback as receiver`() {
        val failure = ParsingResult.failType("boolean", "xyz")
        val message = failure.handle { message }
        assertEquals("The argument provided is not a boolean: xyz.", message)
    }

    @Test
    fun `PeekingResult success shares mutable backing list with consume`() {
        val backing = mutableListOf("a", "b", "c")
        val peeked = PeekingResult.success(backing)
        val valueRef = peeked.value

        peeked.consume(2)

        assertEquals(["c"], valueRef)
        assertEquals(["c"], backing)
    }

    @Test
    fun `PeekingResult failSize is a singleton internal failure`() {
        val result = PeekingResult.failSize()
        assertFalse(result.isSuccess())
        assertSame(PeekingResult.InvalidSizeError, result)
    }
}
