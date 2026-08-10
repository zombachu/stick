package com.zombachu.stick.element

import com.zombachu.stick.SenderValidationResult
import com.zombachu.stick.TestEnv
import com.zombachu.stick.element.parameters.StringParameter
import com.zombachu.stick.impl.Requirement
import com.zombachu.stick.isSuccess
import com.zombachu.stick.withValidationContext
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class SenderValidatorTest {

    @Test
    fun `validateSender on non-SenderValidator element succeeds`() {
        val parameter = StringParameter<TestEnv, Unit>("", "")
        val result = withValidationContext { parameter.validateSender() }
        assertTrue(result.isSuccess())
    }

    @Test
    fun `SenderValidator fails invalid sender`() {
        val requirement = Requirement<TestEnv, Unit> { SenderValidationResult.failSender() }
        val parameter = TransformedParameter(StringParameter("", ""), { it }, requirement)

        val result = withValidationContext { parameter.validateSender() }

        assertFalse(result.isSuccess())
    }

    @Test
    fun `SenderValidator passes valid sender`() {
        val requirement = Requirement<TestEnv, Unit> { SenderValidationResult.success() }
        val parameter = TransformedParameter(StringParameter("", ""), { it }, requirement)

        val result = withValidationContext { parameter.validateSender() }

        assertTrue(result.isSuccess())
    }
}
