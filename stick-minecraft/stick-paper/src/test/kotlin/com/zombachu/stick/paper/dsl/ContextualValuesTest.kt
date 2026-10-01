package com.zombachu.stick.paper.dsl

import com.zombachu.stick.expectSuccessValue
import com.zombachu.stick.paper.FakeCommandSender
import com.zombachu.stick.paper.FakePaperEnvironment
import com.zombachu.stick.paper.PaperEnvironment
import com.zombachu.stick.structureTest
import com.zombachu.stick.testExecution
import org.bukkit.command.CommandSender
import kotlin.test.Test
import kotlin.test.assertEquals

class ContextualValuesTest {

    private val env = FakePaperEnvironment()

    @Test
    fun `permissionedValue resolves permitted value`() = structureTest<PaperEnvironment, CommandSender> {
        val permissionedValue = permissionedValue("stick.perm", "yes", "no")
        val allowed = testExecution<PaperEnvironment, CommandSender>(env, FakeCommandSender(["stick.perm"]))
        assertEquals("yes", permissionedValue(allowed).expectSuccessValue())
    }

    @Test
    fun `permissionedValue resolves fallback value`() = structureTest<PaperEnvironment, CommandSender> {
        val permissionedValue = permissionedValue("stick.perm", "yes", "no")
        val denied = testExecution<PaperEnvironment, CommandSender>(env, FakeCommandSender([]))
        assertEquals("no", permissionedValue(denied).expectSuccessValue())
    }
}
