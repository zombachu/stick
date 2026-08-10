package com.zombachu.stick.velocity

import com.zombachu.stick.structure.command
import com.zombachu.stick.structure.invoke
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

class VelocityStickTest {

    @Test
    fun `registerCommand registers wrapper with command meta`() {
        val manager = FakeCommandManager()
        val plugin = Any()
        val stick = VelocityStick(plugin, FakeProxyServer(manager))
        val structure = velocityStructure { command("cmd", aliases = ["c"])() }

        stick.withContext { register(structure) }

        assertEquals(1, manager.registerCalls)
        assertEquals(["cmd", "c"], manager.registeredMeta?.aliases?.toList())
        assertEquals(plugin, manager.registeredMeta?.plugin)
        assertIs<VelocityCommandWrapper<*>>(manager.registeredCommand)
    }
}
