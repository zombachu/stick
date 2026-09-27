package com.zombachu.stick.dsl

import com.zombachu.stick.Invocation
import com.zombachu.stick.SenderValidator
import com.zombachu.stick.TestEnv
import com.zombachu.stick.expectReason
import com.zombachu.stick.failPermission
import com.zombachu.stick.failure.Reason
import com.zombachu.stick.isSuccess
import com.zombachu.stick.structureTest
import com.zombachu.stick.withInvocation
import kotlin.test.Test
import kotlin.test.assertSame
import kotlin.test.assertTrue

class RequirementsTest {

    @Test
    fun `requirement from CommandResult lambda passes through`() = structureTest {
        val requirement = requirement { failPermission() }
        val result = withInvocation { requirement.validateSender() }
        assertSame(Reason.InvalidPermission, result.expectReason())
    }

    @Test
    fun `requirement from Boolean lambda defaults to failSender`() = structureTest {
        val requirement = requirement { false }
        val result = withInvocation { requirement.validateSender() }
        assertSame(Reason.InvalidSender, result.expectReason())
    }

    @Test
    fun `requirement from Boolean lambda uses failureResult`() = structureTest {
        val requirement = requirement(failureResult = { failPermission() }) { false }
        val result = withInvocation { requirement.validateSender() }
        assertSame(Reason.InvalidPermission, result.expectReason())
    }

    @Test
    fun `requirement from Boolean lambda succeeds when true`() = structureTest {
        val requirement = requirement { true }
        assertTrue(withInvocation { requirement.validateSender() }.isSuccess())
    }

    @Test
    fun `requirement from SenderValidator delegates to it`() = structureTest {
        val validator =
            object : SenderValidator<TestEnv, Unit> {
                context(inv: Invocation<TestEnv, Unit>)
                override fun validateSender() = failPermission()
            }
        val requirement = requirement(validator)

        val result = withInvocation { requirement.validateSender() }

        assertSame(Reason.InvalidPermission, result.expectReason())
    }
}
