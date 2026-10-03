package com.zombachu.stick.paper

import com.zombachu.stick.CommandResult
import com.zombachu.stick.Invocation
import com.zombachu.stick.MatchResult
import com.zombachu.stick.Suggestion
import com.zombachu.stick.dsl.command
import com.zombachu.stick.dsl.flag
import com.zombachu.stick.dsl.invoke
import com.zombachu.stick.dsl.literalParameter
import com.zombachu.stick.dsl.requireSender
import com.zombachu.stick.dsl.textParameter
import com.zombachu.stick.element.AsyncParameter
import com.zombachu.stick.noopFailureHandler
import com.zombachu.stick.paper.dsl.permission
import com.zombachu.stick.success
import com.zombachu.stick.toSuggestions
import kotlin.coroutines.EmptyCoroutineContext
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertSame
import kotlin.test.assertTrue
import org.bukkit.command.CommandSender

class PaperCommandAdapterTest {

    @Test
    fun `execute joins label and args and returns true`() {
        var text: String? = null
        val structure =
            paperStructure {
                command("cmd")(textParameter("")) { text = it }
            }
        val adapter =
            PaperCommandAdapter(FakePaperEnvironment(), noopFailureHandler(), structure, EmptyCoroutineContext)

        val result = adapter.execute(FakeCommandSender(), "cmd", arrayOf("hello", "world"))

        assertTrue(result)
        assertEquals("hello world", text)
    }

    @Test
    fun `execute with no args runs command`() {
        var executed = false
        val structure = paperStructure {
            command("cmd")() { executed = true }
        }
        val adapter =
            PaperCommandAdapter(FakePaperEnvironment(), noopFailureHandler(), structure, EmptyCoroutineContext)

        val result = adapter.execute(FakeCommandSender(), "cmd", arrayOf())

        assertTrue(result)
        assertTrue(executed)
    }

    @Test
    fun `execute accepts a namespaced label`() {
        var text: String? = null
        val structure =
            paperStructure {
                command("cmd")(textParameter("")) { text = it }
            }
        val adapter =
            PaperCommandAdapter(FakePaperEnvironment(), noopFailureHandler(), structure, EmptyCoroutineContext)

        val result = adapter.execute(FakeCommandSender(), "fake-plugin:cmd", arrayOf("hello", "world"))

        assertTrue(result)
        assertEquals("hello world", text)
    }

    @Test
    fun `tabComplete completes arg`() {
        val structure = paperStructure {
            command("hello")(
                literalParameter("there")
            ) { }
        }
        val adapter =
            PaperCommandAdapter(FakePaperEnvironment(), noopFailureHandler(), structure, EmptyCoroutineContext)

        assertEquals(["there"], adapter.tabComplete(FakeCommandSender(), "hello", arrayOf("")))
        assertEquals(["there"], adapter.tabComplete(FakeCommandSender(), "hello", arrayOf("the")))
        assertEquals([], adapter.tabComplete(FakeCommandSender(), "hello", arrayOf("general")))
    }

    @Test
    fun `tabComplete omits async parameter suggestions`() {
        val structure = paperStructure {
            command("hello")(
                AsyncNameParameter(),
                flag("loud"),
            ) { _, _ -> }
        }
        val adapter =
            PaperCommandAdapter(FakePaperEnvironment(), noopFailureHandler(), structure, EmptyCoroutineContext)

        assertEquals(["-loud"], adapter.tabComplete(FakeCommandSender(), "hello", arrayOf("")))
    }

    @Test
    fun `tabComplete completes arg for a namespaced label`() {
        val structure = paperStructure {
            command("hello")(
                literalParameter("there")
            ) { }
        }
        val adapter =
            PaperCommandAdapter(FakePaperEnvironment(), noopFailureHandler(), structure, EmptyCoroutineContext)

        assertEquals(["there"], adapter.tabComplete(FakeCommandSender(), "fake-plugin:hello", arrayOf("the")))
    }

    @Test
    fun `tabComplete ignores consecutive spaces`() {
        val structure = paperStructure {
            command("hello")(
                literalParameter("there")
            ) { }
        }
        val adapter =
            PaperCommandAdapter(FakePaperEnvironment(), noopFailureHandler(), structure, EmptyCoroutineContext)

        assertEquals(["there"], adapter.tabComplete(FakeCommandSender(), "hello", arrayOf("", "the")))
    }

    @Test
    fun `testPermissionSilent delegates to sender validation`() {
        val structure = paperStructure {
            command("cmd", requirement = permission("stick.cmd"))() { }
        }
        val adapter =
            PaperCommandAdapter(FakePaperEnvironment(), noopFailureHandler(), structure, EmptyCoroutineContext)

        assertTrue(adapter.testPermissionSilent(FakeCommandSender(["stick.cmd"])))
        assertFalse(adapter.testPermissionSilent(FakeCommandSender()))
    }

    @Test
    fun `testPermissionSilent sees base permission of a sender-narrowed command`() {
        val structure = paperStructure {
            requireSender(FakeCommandSender::class) {
                command("cmd", requirement = permission("stick.cmd"))() { }
            }
        }
        val adapter =
            PaperCommandAdapter(FakePaperEnvironment(), noopFailureHandler(), structure, EmptyCoroutineContext)

        assertTrue(adapter.testPermissionSilent(FakeCommandSender(["stick.cmd"])))
        assertFalse(adapter.testPermissionSilent(FakeCommandSender()))
    }

    @Test
    fun `getPlugin returns environment plugin`() {
        val structure = paperStructure { command("cmd")() }
        val adapter =
            PaperCommandAdapter(
                FakePaperEnvironment(FakePlugin),
                noopFailureHandler(),
                structure,
                EmptyCoroutineContext,
            )

        assertSame(FakePlugin, adapter.getPlugin())
    }

    private class AsyncNameParameter : AsyncParameter.Size1<PaperEnvironment, CommandSender, String>("name", "") {
        context(inv: Invocation<PaperEnvironment, CommandSender>)
        override fun match(arg0: String): MatchResult = MatchResult.matchedExactly(1)

        context(inv: Invocation<PaperEnvironment, CommandSender>)
        override suspend fun suggest(preceding: List<String>, partial: String): List<Suggestion> =
            ["there"].toSuggestions()

        context(inv: Invocation<PaperEnvironment, CommandSender>)
        override suspend fun resolve(arg0: String): CommandResult<String> = success(arg0)
    }
}
