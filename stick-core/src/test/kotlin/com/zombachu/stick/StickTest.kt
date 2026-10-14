package com.zombachu.stick

import com.zombachu.stick.dsl.command
import com.zombachu.stick.dsl.invoke
import com.zombachu.stick.dsl.structure
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
        val stick = TestStick()
        val structure = structure(TestEnv::class, String::class) { command("cmd")() {} }

        stick.withContext { register(structure) }

        assertTrue("registerCommand" in stick.calls)
    }

    @Test
    fun `onFirstRegistration runs once before first registerCommand`() {
        val stick = TestStick()
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
        val stick = TestStick(Executor {}.asCoroutineContext())
        val structure = structure(TestEnv::class, String::class) { command("cmd")(AsyncNameParameter()) {} }
        stick.withContext { register(structure) }

        val suggestions = stick.runner.suggestAsync("", "cmd", [""])
        stick.cancel()

        assertEquals([], suggestions.getNow(null))
    }

    private class TestStick(asyncContext: CoroutineContext = EmptyCoroutineContext) :
        Stick<TestEnv, String>(String::class, lazyOf(TestEnv), lazyOf(noopFailureHandler()), asyncContext) {
        val calls: MutableList<String> = mutableListOf()
        lateinit var runner: CommandRunner<*, String>

        context(env: E2, failureHandler: FailureHandler<E2, String>)
        override fun <E2 : TestEnv> registerCommand(
            structure: Structure<E2, String, *>,
            asyncContext: CoroutineContext,
        ) {
            calls += "registerCommand"
            runner = CommandRunner(env, failureHandler, structure, EmptyCoroutineContext, asyncContext)
        }

        override fun onFirstRegistration() {
            calls += "onFirstRegistration"
        }

        fun cancel() = cancelRuns()
    }
}
