package com.zombachu.stick.velocity.dsl

import com.velocitypowered.api.command.CommandSource
import com.zombachu.stick.expectSuccessValue
import com.zombachu.stick.structureTest
import com.zombachu.stick.testInvocation
import com.zombachu.stick.velocity.BasicVelocityEnvironment
import com.zombachu.stick.velocity.FakeCommandSource
import com.zombachu.stick.velocity.FakeProxyServer
import com.zombachu.stick.velocity.VelocityEnvironment
import kotlin.test.Test
import kotlin.test.assertEquals

class ContextualValuesTest {

    private val env = BasicVelocityEnvironment(FakeProxyServer())

    @Test
    fun `permissionedValue resolves permitted value`() = structureTest<VelocityEnvironment, CommandSource> {
        val permissionedValue = permissionedValue("stick.perm", "yes", "no")
        val allowed = testInvocation<VelocityEnvironment, CommandSource>(env, FakeCommandSource(["stick.perm"]))
        assertEquals("yes", permissionedValue(allowed).expectSuccessValue())
    }

    @Test
    fun `permissionedValue resolves fallback value`() = structureTest<VelocityEnvironment, CommandSource> {
        val permissionedValue = permissionedValue("stick.perm", "yes", "no")
        val denied = testInvocation<VelocityEnvironment, CommandSource>(env, FakeCommandSource([]))
        assertEquals("no", permissionedValue(denied).expectSuccessValue())
    }
}
