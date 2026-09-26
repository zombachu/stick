package com.zombachu.stick.velocity.dsl

import com.velocitypowered.api.command.CommandSource
import com.zombachu.stick.ValidationContext
import com.zombachu.stick.expectReason
import com.zombachu.stick.failSender
import com.zombachu.stick.failure.Reason
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
        assertSame(Reason.InvalidPermission, result.expectReason())
    }

    @Test
    fun `permission uses failureResult`() = structureTest<VelocityEnvironment, CommandSource> {
        val requirement = permission("stick.perm", failureResult = { failSender() })
        val result = context(validationContext([])) { requirement.validateSender() }
        assertSame(Reason.InvalidSender, result.expectReason())
    }

    private fun validationContext(permissions: Set<String>): ValidationContext<VelocityEnvironment, CommandSource> =
        ValidationContext(env, FakeCommandSource(permissions))
}
