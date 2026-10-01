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
        val result = withInvocation { success("value") }
        assertTrue(result.isSuccess())
        assertEquals("value", result.value)
    }

    @Test
    fun `success without value wraps Unit`() {
        val result = withInvocation { success() }
        assertTrue(result.isSuccess())
        assertSame(Unit, result.value)
    }

    @Test
    fun `fail returns Error with reason`() {
        val result = withInvocation { fail(Reason.InvalidSender) }
        assertSame(Reason.InvalidSender, result.expectError().reason)
    }

    @Test
    fun `noMatch defaults to InvalidSyntax`() {
        val result = withInvocation { noMatch() }
        assertSame(Reason.InvalidSyntax, result.expectNoMatch().reason)
    }

    @Test
    fun `noMatch returns NoMatch with reason`() {
        val result = withInvocation { noMatch(Reason.InvalidSender) }
        assertSame(Reason.InvalidSender, result.expectNoMatch().reason)
    }

    @Test
    fun `handled returns Handled`() {
        val result = withInvocation { handled() }
        assertFalse(result.isSuccess())
        assertSame(CommandResult.Failure.Handled, result)
    }

    @Test
    fun `failType returns NoMatch wrapping type and arg`() {
        val result = withInvocation { failType("boolean", "xyz") }
        assertEquals(Reason.TypeNotMatched("boolean", "xyz"), result.expectNoMatch().reason)
    }

    @Test
    fun `failLiteral returns NoMatch wrapping valid values and arg`() {
        val result = withInvocation { failLiteral(["a", "b"], "c") }
        assertEquals(Reason.LiteralNotMatched(["a", "b"], "c"), result.expectNoMatch().reason)
    }

    @Test
    fun `failSyntax returns Error with InvalidSyntax`() {
        val result = withInvocation { failSyntax() }
        assertSame(Reason.InvalidSyntax, result.expectError().reason)
    }

    @Test
    fun `failRange returns Error wrapping min, max, and arg`() {
        val result = withInvocation { failRange("0", "10", "20") }
        assertEquals(Reason.OutOfRange("0", "10", "20"), result.expectError().reason)
    }

    @Test
    fun `sender failures return Error with expected reason`() {
        withInvocation {
            assertSame(Reason.InvalidSender, failSender().expectError().reason)
            assertSame(Reason.InvalidPermission, failPermission().expectError().reason)
            assertEquals(Reason.InvalidSenderType(Int::class), failSenderType(Int::class).expectError().reason)
        }
    }

    @Test
    fun `commit reports default NoMatch as InvalidSyntax`() {
        val result = withInvocation { noMatch() }.commit()
        assertSame(Reason.InvalidSyntax, result.expectError().reason)
    }

    @Test
    fun `commit keeps NoMatch reason`() {
        val result = withInvocation { failType("integer", "many") }.commit()
        assertEquals(Reason.TypeNotMatched("integer", "many"), result.expectError().reason)
    }

    @Test
    fun `propagateFailure does not invoke callback on success`() {
        var called = false
        val result = withInvocation { success("ok") }
        result.propagateFailure {
            called = true
            error("shouldn't be called")
        }

        assertFalse(called)
    }

    @Test
    fun `propagateFailure invokes callback inline`() {
        fun run(result: CommandResult<String>): String {
            result.propagateFailure {
                return "propagated"
            }
            return "success:${result.value}"
        }
        assertEquals("propagated", run(withInvocation { fail(Reason.Unknown()) }))
        assertEquals("success:ok", run(withInvocation { success("ok") }))
    }

    @Test
    fun `valueOrPropagateFailure returns value on success`() {
        val result = withInvocation { success("ok") }
        val value = result.valueOrPropagateFailure { error("shouldn't be called") }
        assertEquals("ok", value)
    }

    @Test
    fun `valueOrPropagateFailure propagates on failure without producing value`() {
        fun run(result: CommandResult<String>): String {
            val value = result.valueOrPropagateFailure {
                return "propagated"
            }
            return "success:$value"
        }
        assertEquals("propagated", run(withInvocation { fail(Reason.Unknown()) }))
    }

    @Test
    fun `consuming sets consumed count on success`() {
        val result = withInvocation { success("ok") }.consuming(5)

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
