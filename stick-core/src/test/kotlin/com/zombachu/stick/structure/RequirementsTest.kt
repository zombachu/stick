package com.zombachu.stick.structure

import com.zombachu.stick.SenderValidationResult
import com.zombachu.stick.TestEnv
import com.zombachu.stick.ValidationContext
import com.zombachu.stick.element.SenderValidator
import com.zombachu.stick.expectFailure
import com.zombachu.stick.feedback.Feedback
import com.zombachu.stick.isSuccess
import com.zombachu.stick.structureTest
import com.zombachu.stick.withValidationContext
import kotlin.test.Test
import kotlin.test.assertSame
import kotlin.test.assertTrue

class RequirementsTest {

    @Test
    fun `requirement from CommandResult lambda passes through`() = structureTest {
        val requirement = requirement { SenderValidationResult.failPermission() }
        val result = withValidationContext { requirement.validateSender() }
        assertSame(Feedback.InvalidPermission, result.expectFailure().feedback)
    }

    @Test
    fun `requirement from Boolean lambda defaults to failSender`() = structureTest {
        val requirement = requirement { false }
        val result = withValidationContext { requirement.validateSender() }
        assertSame(Feedback.InvalidSender, result.expectFailure().feedback)
    }

    @Test
    fun `requirement from Boolean lambda uses failureResult`() = structureTest {
        val requirement = requirement(failureResult = { SenderValidationResult.failPermission() }) { false }
        val result = withValidationContext { requirement.validateSender() }
        assertSame(Feedback.InvalidPermission, result.expectFailure().feedback)
    }

    @Test
    fun `requirement from Boolean lambda succeeds when true`() = structureTest {
        val requirement = requirement { true }
        assertTrue(withValidationContext { requirement.validateSender() }.isSuccess())
    }

    @Test
    fun `requirement from SenderValidator delegates to it`() = structureTest {
        val validator =
            object : SenderValidator<TestEnv, Unit> {
                context(validationContext: ValidationContext<TestEnv, Unit>)
                override fun validateSender() = SenderValidationResult.failPermission()
            }
        val requirement = requirement(validator)

        val result = withValidationContext { requirement.validateSender() }

        assertSame(Feedback.InvalidPermission, result.expectFailure().feedback)
    }
}
