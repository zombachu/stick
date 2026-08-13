package com.zombachu.stick

import com.zombachu.stick.dsl.command
import com.zombachu.stick.dsl.invoke
import com.zombachu.stick.dsl.structure
import com.zombachu.stick.element.Structure
import com.zombachu.stick.feedback.FailureHandler
import kotlin.test.Test
import kotlin.test.assertTrue

class StickTest {

    @Test
    fun `withContext register calls registerCommand`() {
        val stick =
            object : Stick<TestEnv, String>(String::class, lazyOf(TestEnv), lazyOf(noopFailureHandler())) {
                var registered = false

                context(env: E2, failureHandler: FailureHandler<E2, String>)
                override fun <E2 : TestEnv> registerCommand(structure: Structure<E2, String, *>) {
                    registered = true
                }
            }
        val structure = structure(TestEnv::class, String::class) { command("cmd")() {} }

        stick.withContext { register(structure) }

        assertTrue(stick.registered)
    }
}
