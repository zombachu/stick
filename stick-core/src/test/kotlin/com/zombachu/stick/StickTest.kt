package com.zombachu.stick

import com.zombachu.stick.element.Structure
import com.zombachu.stick.feedback.FailureHandler
import com.zombachu.stick.feedback.Feedback
import com.zombachu.stick.structure.command
import com.zombachu.stick.structure.invoke
import com.zombachu.stick.structure.structure
import kotlin.test.Test
import kotlin.test.assertTrue

class StickTest {

    @Test
    fun `withContext register calls registerCommand`() {
        val failureHandler =
            object : FailureHandler<TestEnv, String> {
                context(inv: Invocation<TestEnv, String>)
                override fun <F : Feedback> onFailure(failure: CommandResult.Failure<F>) {}
            }
        val stick =
            object : Stick<TestEnv, String>(String::class, lazyOf(TestEnv), lazyOf(failureHandler)) {
                var registered = false

                context(env: E2, failureHandler: FailureHandler<E2, String>)
                override fun <E2 : TestEnv> registerCommand(structure: Structure<E2, String, *>) {
                    registered = true
                }
            }
        val testStructure = structure(TestEnv::class, String::class) { command("cmd")() {} }

        stick.withContext { register(testStructure) }

        assertTrue(stick.registered)
    }
}
