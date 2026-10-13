package com.zombachu.stick

import com.zombachu.stick.dsl.command
import com.zombachu.stick.dsl.invoke
import com.zombachu.stick.dsl.structure
import com.zombachu.stick.element.Parameter
import com.zombachu.stick.element.Structure
import com.zombachu.stick.failure.FailureHandler
import java.util.concurrent.Executor
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class StickTest {

    @Test
    fun `withContext register calls registerCommand`() {
        val stick =
            object :
                Stick<TestEnv, String>(
                    String::class,
                    lazyOf(TestEnv),
                    lazyOf(noopFailureHandler()),
                    EmptyCoroutineContext,
                ) {
                var registered = false

                context(env: E2, failureHandler: FailureHandler<E2, String>)
                override fun <E2 : TestEnv> registerCommand(
                    structure: Structure<E2, String, *>,
                    asyncContext: CoroutineContext,
                ) {
                    registered = true
                }
            }
        val structure = structure(TestEnv::class, String::class) { command("cmd")() {} }

        stick.withContext { register(structure) }

        assertTrue(stick.registered)
    }

    @Test
    fun `onFirstRegistration runs once before first registerCommand`() {
        val stick =
            object :
                Stick<TestEnv, String>(
                    String::class,
                    lazyOf(TestEnv),
                    lazyOf(noopFailureHandler()),
                    EmptyCoroutineContext,
                ) {
                val calls: MutableList<String> = mutableListOf()

                context(env: E2, failureHandler: FailureHandler<E2, String>)
                override fun <E2 : TestEnv> registerCommand(
                    structure: Structure<E2, String, *>,
                    asyncContext: CoroutineContext,
                ) {
                    calls += "registerCommand"
                }

                override fun onFirstRegistration() {
                    calls += "onFirstRegistration"
                }
            }
        val first = structure(TestEnv::class, String::class) { command("first")() {} }
        val second = structure(TestEnv::class, String::class) { command("second")() {} }

        stick.withContext {
            register(first)
            register(second)
        }

        assertEquals(["onFirstRegistration", "registerCommand", "registerCommand"], stick.calls)
    }

    @Test
    fun `cancelRuns completes open suggestions empty`() {
        val stick =
            object :
                Stick<TestEnv, String>(
                    String::class,
                    lazyOf(TestEnv),
                    lazyOf(noopFailureHandler()),
                    Executor {}.asCoroutineContext(),
                ) {
                lateinit var runner: CommandRunner<*, String>

                context(env: E2, failureHandler: FailureHandler<E2, String>)
                override fun <E2 : TestEnv> registerCommand(
                    structure: Structure<E2, String, *>,
                    asyncContext: CoroutineContext,
                ) {
                    runner = CommandRunner(env, failureHandler, structure, EmptyCoroutineContext, asyncContext)
                }

                fun cancel() = cancelRuns()
            }
        val structure = structure(TestEnv::class, String::class) { command("cmd")(AsyncNameParameter()) {} }
        stick.withContext { register(structure) }

        val suggestions = stick.runner.suggestAsync("", "cmd", [""])
        stick.cancel()

        assertEquals([], suggestions.getNow(null))
    }

    private class AsyncNameParameter : Parameter.Size1<TestEnv, String, String>("", "") {
        context(inv: Invocation<TestEnv, String>)
        override suspend fun suggest(preceding: List<String>, partial: String): List<Suggestion> =
            withAsyncContext { ["there"].toSuggestions() }

        context(inv: Invocation<TestEnv, String>)
        override fun resolve(arg0: String): CommandResult<String> = success(arg0)
    }
}
