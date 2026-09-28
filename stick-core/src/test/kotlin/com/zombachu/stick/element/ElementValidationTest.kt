package com.zombachu.stick.element

import com.zombachu.stick.Position
import com.zombachu.stick.Requirement
import com.zombachu.stick.TestEnv
import com.zombachu.stick.element.parameters.StringParameter
import com.zombachu.stick.failSender
import com.zombachu.stick.isSuccess
import com.zombachu.stick.success
import com.zombachu.stick.withInvocation
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class ElementValidationTest {

    @Test
    fun `validateSender on non-SenderValidator element succeeds`() {
        val parameter = StringParameter<TestEnv, Unit>("", "")
        val result = withInvocation { parameter.validateSender() }
        assertTrue(result.isSuccess())
    }

    @Test
    fun `SenderValidator fails invalid sender`() {
        val requirement = Requirement<TestEnv, Unit> { failSender() }
        val parameter = gatedParameter(requirement)

        val result = withInvocation { parameter.validateSender() }

        assertFalse(result.isSuccess())
    }

    @Test
    fun `SenderValidator passes valid sender`() {
        val requirement = Requirement<TestEnv, Unit> { success() }
        val parameter = gatedParameter(requirement)

        val result = withInvocation { parameter.validateSender() }

        assertTrue(result.isSuccess())
    }

    private fun gatedParameter(requirement: Requirement<TestEnv, Unit>) =
        GatedParameterImpl<TestEnv, Unit, String, Position.Leading>(StringParameter("", ""), requirement)
}
