package com.zombachu.stick.paper.dsl

import com.zombachu.stick.expectSuccessValue
import com.zombachu.stick.paper.BukkitEnvironment
import com.zombachu.stick.paper.FakeBukkitEnvironment
import com.zombachu.stick.paper.FakeCommandSender
import com.zombachu.stick.structureTest
import com.zombachu.stick.testInvocation
import org.bukkit.command.CommandSender
import kotlin.test.Test
import kotlin.test.assertEquals

class ContextualValuesTest {

    private val env = FakeBukkitEnvironment()

    @Test
    fun `permissionedValue resolves permitted value`() = structureTest<BukkitEnvironment, CommandSender> {
        val permissionedValue = permissionedValue("stick.perm", "yes", "no")
        val allowed = testInvocation<BukkitEnvironment, CommandSender>(env, FakeCommandSender(["stick.perm"]))
        assertEquals("yes", permissionedValue(allowed).expectSuccessValue())
    }

    @Test
    fun `permissionedValue resolves fallback value`() = structureTest<BukkitEnvironment, CommandSender> {
        val permissionedValue = permissionedValue("stick.perm", "yes", "no")
        val denied = testInvocation<BukkitEnvironment, CommandSender>(env, FakeCommandSender([]))
        assertEquals("no", permissionedValue(denied).expectSuccessValue())
    }
}
