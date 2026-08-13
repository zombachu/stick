package com.zombachu.stick.velocity.dsl

import com.velocitypowered.api.command.CommandSource
import com.zombachu.stick.Invocation
import com.zombachu.stick.dsl.command
import com.zombachu.stick.dsl.invoke
import com.zombachu.stick.expectSuccessValue
import com.zombachu.stick.structureTest
import com.zombachu.stick.velocity.BasicVelocityEnvironment
import com.zombachu.stick.velocity.FakeCommandSource
import com.zombachu.stick.velocity.FakeProxyServer
import com.zombachu.stick.velocity.VelocityEnvironment
import com.zombachu.stick.velocity.velocityStructure
import kotlin.test.Test
import kotlin.test.assertEquals

class ContextualValuesTest {

    private val env = BasicVelocityEnvironment(FakeProxyServer())
    private val structure = velocityStructure { command("cmd")() }

    @Test
    fun `permissionedValue resolves permitted value`() = structureTest<VelocityEnvironment, CommandSource> {
        val permissionedValue = permissionedValue("stick.perm", "yes", "no")
        val allowed = Invocation(FakeCommandSource(["stick.perm"]), env, "cmd", ["cmd"], structure)
        assertEquals("yes", permissionedValue(allowed).expectSuccessValue())
    }

    @Test
    fun `permissionedValue resolves fallback value`() = structureTest<VelocityEnvironment, CommandSource> {
        val permissionedValue = permissionedValue("stick.perm", "yes", "no")
        val denied = Invocation(FakeCommandSource([]), env, "cmd", ["cmd"], structure)
        assertEquals("no", permissionedValue(denied).expectSuccessValue())
    }
}
