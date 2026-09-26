package com.zombachu.stick.paper.dsl

import com.zombachu.stick.ValidationContext
import com.zombachu.stick.expectReason
import com.zombachu.stick.failSender
import com.zombachu.stick.failure.Reason
import com.zombachu.stick.isSuccess
import com.zombachu.stick.paper.BukkitEnvironment
import com.zombachu.stick.paper.FakeBukkitEnvironment
import com.zombachu.stick.paper.FakeCommandSender
import com.zombachu.stick.structureTest
import org.bukkit.command.CommandSender
import kotlin.test.Test
import kotlin.test.assertSame
import kotlin.test.assertTrue

class RequirementsTest {

    private val env = FakeBukkitEnvironment()

    @Test
    fun `permission succeeds when granted`() = structureTest<BukkitEnvironment, CommandSender> {
        val requirement = permission("stick.perm")
        val result = context(validationContext(["stick.perm"])) { requirement.validateSender() }
        assertTrue(result.isSuccess())
    }

    @Test
    fun `permission fails with InvalidPermission when denied`() = structureTest<BukkitEnvironment, CommandSender> {
        val requirement = permission("stick.perm")
        val result = context(validationContext([])) { requirement.validateSender() }
        assertSame(Reason.InvalidPermission, result.expectReason())
    }

    @Test
    fun `permission uses failureResult`() = structureTest<BukkitEnvironment, CommandSender> {
        val requirement = permission("stick.perm", failureResult = { failSender() })
        val result = context(validationContext([])) { requirement.validateSender() }
        assertSame(Reason.InvalidSender, result.expectReason())
    }

    private fun validationContext(permissions: Set<String>): ValidationContext<BukkitEnvironment, CommandSender> =
        ValidationContext(env, FakeCommandSender(permissions))
}
