package com.zombachu.stick.velocity.structure

import com.velocitypowered.api.command.CommandSource
import com.zombachu.stick.SenderValidationResult
import com.zombachu.stick.ValidationContext
import com.zombachu.stick.expectFailure
import com.zombachu.stick.feedback.Feedback
import com.zombachu.stick.isSuccess
import com.zombachu.stick.structureTest
import com.zombachu.stick.velocity.BasicVelocityEnvironment
import com.zombachu.stick.velocity.FakeCommandSource
import com.zombachu.stick.velocity.FakeProxyServer
import com.zombachu.stick.velocity.VelocityEnvironment
import kotlin.test.Test
import kotlin.test.assertSame
import kotlin.test.assertTrue

class RequirementsTest {

    private val env = BasicVelocityEnvironment(FakeProxyServer())

    @Test
    fun `permission succeeds when granted`() = structureTest<VelocityEnvironment, CommandSource> {
        val requirement = permission("stick.perm")
        val result = context(validationContext(["stick.perm"])) { requirement.validateSender() }
        assertTrue(result.isSuccess())
    }

    @Test
    fun `permission fails with InvalidPermission when denied`() = structureTest<VelocityEnvironment, CommandSource> {
        val requirement = permission("stick.perm")
        val result = context(validationContext([])) { requirement.validateSender() }
        assertSame(Feedback.InvalidPermission, result.expectFailure().feedback)
    }

    @Test
    fun `permission uses failureResult`() = structureTest<VelocityEnvironment, CommandSource> {
        val requirement = permission("stick.perm", failureResult = { SenderValidationResult.failSender() })
        val result = context(validationContext([])) { requirement.validateSender() }
        assertSame(Feedback.InvalidSender, result.expectFailure().feedback)
    }

    private fun validationContext(permissions: Set<String>): ValidationContext<VelocityEnvironment, CommandSource> =
        ValidationContext(env, FakeCommandSource(permissions))
}
