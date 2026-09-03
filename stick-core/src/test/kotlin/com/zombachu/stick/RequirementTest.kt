package com.zombachu.stick

import com.zombachu.stick.feedback.Feedback
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotSame
import kotlin.test.assertSame
import kotlin.test.assertTrue

class RequirementTest {

    @Test
    fun `success passes through`() {
        val requirement = Requirement<TestEnv, Unit> { SenderValidationResult.success() }
        val result = withValidationContext { requirement.validateSender() }
        assertTrue(result.isSuccess())
    }

    @Test
    fun `failure passes through`() {
        val requirement = Requirement<TestEnv, Unit> { SenderValidationResult.failSender() }
        val result = withValidationContext { requirement.validateSender() }
        assertSame(Feedback.InvalidSender, result.expectFailure().feedback)
    }

    @Test
    fun `plus runs validators in order`() {
        val callOrder = mutableListOf<String>()
        val a =
            Requirement<TestEnv, Unit> {
                callOrder += "a"
                SenderValidationResult.success()
            }
        val b =
            Requirement<TestEnv, Unit> {
                callOrder += "b"
                SenderValidationResult.success()
            }

        val result = withValidationContext { (a + b).validateSender() }

        assertTrue(result.isSuccess())
        assertEquals(["a", "b"], callOrder)
    }

    @Test
    fun `plus short-circuits on first failure`() {
        var bCalled = false
        val a = Requirement<TestEnv, Unit> { SenderValidationResult.failSender() }
        val b =
            Requirement<TestEnv, Unit> {
                bCalled = true
                SenderValidationResult.success()
            }

        val result = withValidationContext { (a + b).validateSender() }

        assertSame(Feedback.InvalidSender, result.expectFailure().feedback)
        assertFalse(bCalled)
    }

    @Test
    fun `plus does not mutate operands operands`() {
        var bCalled = false
        val a = Requirement<TestEnv, Unit> { SenderValidationResult.success() }
        val b =
            Requirement<TestEnv, Unit> {
                bCalled = true
                SenderValidationResult.success()
            }

        val combined = a + b

        assertNotSame(a, combined)
        assertTrue(withValidationContext { a.validateSender() }.isSuccess())
        assertFalse(bCalled)

        assertTrue(withValidationContext { combined.validateSender() }.isSuccess())
        assertTrue(bCalled)
    }
}
