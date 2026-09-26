package com.zombachu.stick

import com.zombachu.stick.failure.Reason
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertIs
import kotlin.test.assertSame
import kotlin.test.assertTrue

class CommandResultTest {

    @Test
    fun `success wraps value`() {
        val result = withValidationContext { success("value") }
        assertTrue(result.isSuccess())
        assertEquals("value", result.value)
    }

    @Test
    fun `success without value wraps Unit`() {
        val result = withValidationContext { success() }
        assertTrue(result.isSuccess())
        assertSame(Unit, result.value)
    }

    @Test
    fun `fail returns Error with reason`() {
        val result = withValidationContext { fail(Reason.InvalidSender) }
        assertSame(Reason.InvalidSender, result.expectError().reason)
    }

    @Test
    fun `noMatch defaults to InvalidSyntax`() {
        val inv = testInvocation()
        val result = context(inv) { noMatch() }
        assertEquals(Reason.InvalidSyntax(inv.getSyntax()), result.expectNoMatch().reason)
    }

    @Test
    fun `noMatch returns NoMatch with reason`() {
        val result = withValidationContext { noMatch(Reason.InvalidSender) }
        assertSame(Reason.InvalidSender, result.expectNoMatch().reason)
    }

    @Test
    fun `handled returns Handled`() {
        val result = withValidationContext { handled() }
        assertFalse(result.isSuccess())
        assertSame(CommandResult.Failure.Handled, result)
    }

    @Test
    fun `failType returns NoMatch wrapping type and arg`() {
        val result = withValidationContext { failType("boolean", "xyz") }
        assertEquals(Reason.TypeNotMatched("boolean", "xyz"), result.expectNoMatch().reason)
    }

    @Test
    fun `failLiteral returns NoMatch wrapping valid values and arg`() {
        val result = withValidationContext { failLiteral(["a", "b"], "c") }
        assertEquals(Reason.LiteralNotMatched(["a", "b"], "c"), result.expectNoMatch().reason)
    }

    @Test
    fun `failSyntax returns Error with InvalidSyntax`() {
        val inv = testInvocation()
        val result = context(inv) { failSyntax() }
        assertEquals(Reason.InvalidSyntax(inv.getSyntax()), result.expectError().reason)
    }

    @Test
    fun `failRange returns Error wrapping min, max, and arg`() {
        val result = withValidationContext { failRange("0", "10", "20") }
        assertEquals(Reason.OutOfRange("0", "10", "20"), result.expectError().reason)
    }

    @Test
    fun `sender failures return Error with expected reason`() {
        withValidationContext {
            assertSame(Reason.InvalidSender, failSender().expectError().reason)
            assertSame(Reason.InvalidPermission, failPermission().expectError().reason)
            assertEquals(Reason.InvalidSenderType(Int::class), failSenderType(Int::class).expectError().reason)
        }
    }

    @Test
    fun `commit reports default NoMatch as InvalidSyntax`() {
        val inv = testInvocation()
        val result = context(inv) { noMatch().commit() }
        assertEquals(Reason.InvalidSyntax(inv.getSyntax()), result.expectError().reason)
    }

    @Test
    fun `commit keeps NoMatch reason`() {
        val result = withValidationContext { failType("integer", "many").commit() }
        assertEquals(Reason.TypeNotMatched("integer", "many"), result.expectError().reason)
    }

    @Test
    fun `propagateError does not invoke callback on success`() {
        var called = false
        val result = withValidationContext { success("ok") }
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
        assertEquals("propagated", run(withValidationContext { fail(Reason.Unknown()) }))
        assertEquals("success:ok", run(withValidationContext { success("ok") }))
    }

    @Test
    fun `valueOrPropagateError returns value on success`() {
        val result = withValidationContext { success("ok") }
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
        assertEquals("propagated", run(withValidationContext { fail(Reason.Unknown()) }))
    }

    @Test
    fun `consuming sets consumed count on success`() {
        val result = withValidationContext { success("ok") }.consuming(5)

        assertIs<ConsumingResult.Success<String>>(result)
        assertEquals("ok", result.value)
        assertEquals(5, result.consumed)
    }

    @Test
    fun `consuming passes through failures unchanged`() {
        val failure = withInvocation { failSyntax() }
        val result = failure.consuming(5)
        assertSame(failure, result)
    }
}
