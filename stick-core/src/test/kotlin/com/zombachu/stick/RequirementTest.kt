package com.zombachu.stick

import com.zombachu.stick.failure.Reason
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotSame
import kotlin.test.assertSame
import kotlin.test.assertTrue

class RequirementTest {

    @Test
    fun `success passes through`() {
        val requirement = Requirement<TestEnv, Unit> { success() }
        val result = withValidationContext { requirement.validateSender() }
        assertTrue(result.isSuccess())
    }

    @Test
    fun `failure passes through`() {
        val requirement = Requirement<TestEnv, Unit> { failSender() }
        val result = withValidationContext { requirement.validateSender() }
        assertSame(Reason.InvalidSender, result.expectReason())
    }

    @Test
    fun `plus runs validators in order`() {
        val callOrder = mutableListOf<String>()
        val a =
            Requirement<TestEnv, Unit> {
                callOrder += "a"
                success()
            }
        val b =
            Requirement<TestEnv, Unit> {
                callOrder += "b"
                success()
            }

        val result = withValidationContext { (a + b).validateSender() }

        assertTrue(result.isSuccess())
        assertEquals(["a", "b"], callOrder)
    }

    @Test
    fun `plus short-circuits on first failure`() {
        var bCalled = false
        val a = Requirement<TestEnv, Unit> { failSender() }
        val b =
            Requirement<TestEnv, Unit> {
                bCalled = true
                success()
            }

        val result = withValidationContext { (a + b).validateSender() }

        assertSame(Reason.InvalidSender, result.expectReason())
        assertFalse(bCalled)
    }

    @Test
    fun `plus does not mutate operands operands`() {
        var bCalled = false
        val a = Requirement<TestEnv, Unit> { success() }
        val b =
            Requirement<TestEnv, Unit> {
                bCalled = true
                success()
            }

        val combined = a + b

        assertNotSame(a, combined)
        assertTrue(withValidationContext { a.validateSender() }.isSuccess())
        assertFalse(bCalled)

        assertTrue(withValidationContext { combined.validateSender() }.isSuccess())
        assertTrue(bCalled)
    }
}
