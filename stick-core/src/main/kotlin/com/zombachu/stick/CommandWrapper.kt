package com.zombachu.stick

import com.zombachu.stick.element.Structure
import com.zombachu.stick.feedback.FailureHandler

interface CommandWrapper<E : Environment, S> {
    val env: E
    val failureHandler: FailureHandler<E, S>
    val structure: Structure<E, S, *>

    @Suppress("TooGenericExceptionCaught")
    fun execute(sender: S, fullArgs: List<String>) {
        val inv = Invocation(sender, env, fullArgs.first(), fullArgs, structure)
        context(env, inv) {
            val result =
                try {
                    val validationResult = structure.validateSender()
                    if (validationResult.isSuccess()) structure.parse(fullArgs) else validationResult
                } catch (e: Exception) {
                    ParsingResult.failUnknown(e)
                }
            // Ignore InternalFailures
            if (result is CommandResult.Failure<*>) {
                failureHandler.onFailure(result)
            }
        }
    }
}
