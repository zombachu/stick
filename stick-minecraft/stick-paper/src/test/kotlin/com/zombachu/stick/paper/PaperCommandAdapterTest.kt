package com.zombachu.stick.paper

import com.mojang.brigadier.Command
import com.zombachu.stick.Arguments
import com.zombachu.stick.AsyncNameParameter
import com.zombachu.stick.StructureScope
import com.zombachu.stick.SuspendingNameParameter
import com.zombachu.stick.asCoroutineContext
import com.zombachu.stick.dsl.command
import com.zombachu.stick.dsl.flag
import com.zombachu.stick.dsl.invoke
import com.zombachu.stick.dsl.literalParameter
import com.zombachu.stick.dsl.requireSender
import com.zombachu.stick.dsl.textParameter
import com.zombachu.stick.element.Structure
import com.zombachu.stick.noopFailureHandler
import com.zombachu.stick.paper.dsl.permission
import com.zombachu.stick.success
import org.bukkit.command.CommandSender
import java.util.concurrent.Executor
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class PaperCommandAdapterTest {

    @Test
    fun `run joins label and args and returns single success`() {
        var text: String? = null
        val registrar = register {
            command("cmd")(textParameter("")) { text = it }
        }

        val result = registrar.execute("cmd hello world")

        assertEquals(Command.SINGLE_SUCCESS, result)
        assertEquals("hello world", text)
    }

    @Test
    fun `run collapses consecutive spaces`() {
        var text: String? = null
        val registrar = register {
            command("cmd")(textParameter("")) { text = it }
        }

        val ignored = registrar.execute("cmd hello   world")

        assertEquals("hello world", text)
    }

    @Test
    fun `run drops leading and trailing spaces`() {
        var text: String? = null
        val registrar = register {
            command("cmd")(textParameter("")) { text = it }
        }

        val ignored = registrar.execute("cmd   hello world  ")

        assertEquals("hello world", text)
    }

    @Test
    fun `run with no args runs command`() {
        var executed = false
        val registrar = register {
            command("cmd")() { executed = true }
        }

        val ignored = registrar.execute("cmd")

        assertTrue(executed)
    }

    @Test
    fun `run accepts namespaced label`() {
        var text: String? = null
        val registrar = register {
            command("cmd")(textParameter("")) { text = it }
        }

        val ignored = registrar.execute("fake-plugin:cmd hello world")

        assertEquals("hello world", text)
    }

    @Test
    fun `getSuggestions completes arg`() {
        val registrar = register {
            command("hello")(
                literalParameter("there")
            ) { }
        }

        assertEquals(["hello there"], registrar.complete("hello "))
        assertEquals(["hello there"], registrar.complete("hello the"))
        assertEquals([], registrar.complete("hello general"))
    }

    @Test
    fun `getSuggestions includes suspending parameter suggestions`() {
        val registrar = register {
            command("hello")(
                SuspendingNameParameter(),
                flag("loud"),
            ) { _, _ -> }
        }

        assertEquals(["hello -loud", "hello there"], registrar.complete("hello "))
    }

    @Test
    fun `getSuggestions completes arg for namespaced label`() {
        val registrar = register {
            command("hello")(
                literalParameter("there")
            ) { }
        }

        assertEquals(["fake-plugin:hello there"], registrar.complete("fake-plugin:hello the"))
    }

    @Test
    fun `getSuggestions ignores consecutive spaces`() {
        val registrar = register {
            command("hello")(
                literalParameter("there")
            ) { }
        }

        assertEquals(["hello  there"], registrar.complete("hello  the"))
    }

    @Test
    fun `getSuggestions abandons sender's previous suggestions`() {
        val registrar = register(Executor {}.asCoroutineContext()) {
            command("hello")(AsyncNameParameter()) { }
        }

        val first = registrar.suggestions("hello ")
        val second = registrar.suggestions("hello t")

        assertEquals([], first.getNow(null))
        assertFalse(second.isDone)
    }

    @Test
    fun `test delegates to sender validation`() {
        val adapter = adapter {
            command("cmd", requirement = permission("stick.cmd"))() { }
        }

        assertTrue(adapter.test(FakeCommandSourceStack(FakeCommandSender(["stick.cmd"]))))
        assertFalse(adapter.test(FakeCommandSourceStack(FakeCommandSender())))
    }

    @Test
    fun `test sees base permission of a sender-narrowed command`() {
        val adapter = adapter {
            requireSender(FakeCommandSender::class) {
                command("cmd", requirement = permission("stick.cmd"))() { }
            }
        }

        assertTrue(adapter.test(FakeCommandSourceStack(FakeCommandSender(["stick.cmd"]))))
        assertFalse(adapter.test(FakeCommandSourceStack(FakeCommandSender())))
    }

    private fun <T_ : Arguments> register(
        asyncContext: CoroutineContext = EmptyCoroutineContext,
        block: StructureScope<PaperEnvironment, CommandSender>.() -> Structure<PaperEnvironment, CommandSender, T_>,
    ): FakeRegistrar {
        val registrar = FakeRegistrar()
        registrar.register(adapter(asyncContext, block).toBrigadierNode())
        return registrar
    }

    private fun <T_ : Arguments> adapter(
        asyncContext: CoroutineContext = EmptyCoroutineContext,
        block: StructureScope<PaperEnvironment, CommandSender>.() -> Structure<PaperEnvironment, CommandSender, T_>,
    ): PaperCommandAdapter<PaperEnvironment> =
        PaperCommandAdapter(FakePaperEnvironment(), noopFailureHandler(), paperStructure(block), asyncContext)
}
