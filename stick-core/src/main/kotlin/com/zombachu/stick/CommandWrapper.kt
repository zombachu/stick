package com.zombachu.stick

import com.zombachu.stick.element.Structure
import com.zombachu.stick.feedback.FailureHandler

interface CommandWrapper<E : Environment, S> {
    val env: E
    val failureHandler: FailureHandler<E, S>
    val structure: Structure<E, S, *>

    fun execute(sender: S, fullArgs: List<String>) {
        val inv = Invocation(sender, env, fullArgs.first(), fullArgs, structure)
        context(env, inv) {
            val result = structure.parse(fullArgs)
            // Ignore InternalFailures
            if (result is CommandResult.Failure<*>) {
                failureHandler.onFailure(result)
            }
        }
    }
}
