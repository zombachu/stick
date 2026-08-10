package com.zombachu.stick.paper.structure

import com.zombachu.stick.Invocation
import com.zombachu.stick.expectSuccessValue
import com.zombachu.stick.paper.BukkitEnvironment
import com.zombachu.stick.paper.FakeBukkitEnvironment
import com.zombachu.stick.paper.FakeCommandSender
import com.zombachu.stick.paper.bukkitStructure
import com.zombachu.stick.structure.command
import com.zombachu.stick.structure.invoke
import com.zombachu.stick.structureTest
import org.bukkit.command.CommandSender
import kotlin.test.Test
import kotlin.test.assertEquals

class ContextualValuesTest {

    private val env = FakeBukkitEnvironment()
    private val struct = bukkitStructure { command("cmd")() }

    @Test
    fun `permissionedValue resolves permitted value`() = structureTest<BukkitEnvironment, CommandSender> {
        val permissionedValue = permissionedValue("stick.perm", "yes", "no")
        val allowed = Invocation(FakeCommandSender(["stick.perm"]), env, "cmd", ["cmd"], struct)
        assertEquals("yes", permissionedValue(allowed).expectSuccessValue())
    }

    @Test
    fun `permissionedValue resolves fallback value`() = structureTest<BukkitEnvironment, CommandSender> {
        val permissionedValue = permissionedValue("stick.perm", "yes", "no")
        val denied = Invocation(FakeCommandSender([]), env, "cmd", ["cmd"], struct)
        assertEquals("no", permissionedValue(denied).expectSuccessValue())
    }
}
