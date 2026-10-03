package com.zombachu.stick.velocity

import com.velocitypowered.api.command.CommandSource
import com.zombachu.stick.CommandResult
import com.zombachu.stick.Invocation
import com.zombachu.stick.MatchResult
import com.zombachu.stick.Suggestion
import com.zombachu.stick.dsl.command
import com.zombachu.stick.dsl.flag
import com.zombachu.stick.dsl.invoke
import com.zombachu.stick.dsl.literalParameter
import com.zombachu.stick.dsl.textParameter
import com.zombachu.stick.element.AsyncParameter
import com.zombachu.stick.noopFailureHandler
import com.zombachu.stick.success
import com.zombachu.stick.toSuggestions
import com.zombachu.stick.velocity.dsl.permission
import kotlin.coroutines.EmptyCoroutineContext
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class VelocityCommandAdapterTest {

    @Test
    fun `execute collapses consecutive spaces`() {
        var text: String? = null
        val adapter = textAdapter { text = it }

        adapter.execute(FakeInvocation(FakeCommandSource(), "cmd", "hello   world"))

        assertEquals("hello world", text)
    }

    @Test
    fun `execute drops leading and trailing spaces`() {
        var text: String? = null
        val adapter = textAdapter { text = it }

        adapter.execute(FakeInvocation(FakeCommandSource(), "cmd", "  hello world  "))

        assertEquals("hello world", text)
    }

    @Test
    fun `execute with no args runs command`() {
        var executed = false
        val structure = velocityStructure {
            command("cmd")() { executed = true }
        }
        val adapter = VelocityCommandAdapter(environment(), noopFailureHandler(), structure, EmptyCoroutineContext)

        adapter.execute(FakeInvocation(FakeCommandSource(), "cmd", ""))

        assertTrue(executed)
    }

    @Test
    fun `suggest completes arg`() {
        val structure = velocityStructure {
            command("hello")(
                literalParameter("there")
            ) { }
        }
        val adapter = VelocityCommandAdapter(environment(), noopFailureHandler(), structure, EmptyCoroutineContext)

        assertEquals(["there"], adapter.suggestAsync(FakeInvocation(FakeCommandSource(), "hello", "")).join())
        assertEquals(["there"], adapter.suggestAsync(FakeInvocation(FakeCommandSource(), "hello", "the")).join())
        assertEquals([], adapter.suggestAsync(FakeInvocation(FakeCommandSource(), "hello", "general")).join())
    }

    @Test
    fun `suggest includes async parameter suggestions`() {
        val structure = velocityStructure {
            command("hello")(
                AsyncNameParameter(),
                flag("loud"),
            ) { _, _ -> }
        }
        val adapter = VelocityCommandAdapter(environment(), noopFailureHandler(), structure, EmptyCoroutineContext)

        assertEquals(["-loud", "there"], adapter.suggestAsync(FakeInvocation(FakeCommandSource(), "hello", "")).join())
    }

    @Test
    fun `suggest ignores consecutive spaces`() {
        val structure = velocityStructure {
            command("hello")(
                literalParameter("there")
            ) { }
        }
        val adapter = VelocityCommandAdapter(environment(), noopFailureHandler(), structure, EmptyCoroutineContext)

        assertEquals(["there"], adapter.suggestAsync(FakeInvocation(FakeCommandSource(), "hello", "  the")).join())
    }

    @Test
    fun `hasPermission delegates to sender validation`() {
        val structure = velocityStructure {
            command("cmd", requirement = permission("stick.cmd"))() { }
        }
        val adapter = VelocityCommandAdapter(environment(), noopFailureHandler(), structure, EmptyCoroutineContext)

        assertTrue(adapter.hasPermission(FakeInvocation(FakeCommandSource(["stick.cmd"]), "cmd", "")))
        assertFalse(adapter.hasPermission(FakeInvocation(FakeCommandSource(), "cmd", "")))
    }

    private fun environment(): VelocityEnvironment = BasicVelocityEnvironment(FakeProxyServer())

    private fun textAdapter(onExecute: (String) -> Unit): VelocityCommandAdapter<VelocityEnvironment> {
        val structure =
            velocityStructure {
                command("cmd")(textParameter("")) { rest -> onExecute(rest) }
            }
        return VelocityCommandAdapter(environment(), noopFailureHandler(), structure, EmptyCoroutineContext)
    }

    private class AsyncNameParameter : AsyncParameter.Size1<VelocityEnvironment, CommandSource, String>("name", "") {
        context(inv: Invocation<VelocityEnvironment, CommandSource>)
        override fun match(arg0: String): MatchResult = MatchResult.matchedExactly(1)

        context(inv: Invocation<VelocityEnvironment, CommandSource>)
        override suspend fun suggest(preceding: List<String>, partial: String): List<Suggestion> =
            ["there"].toSuggestions()

        context(inv: Invocation<VelocityEnvironment, CommandSource>)
        override suspend fun resolve(arg0: String): CommandResult<String> = success(arg0)
    }
}
