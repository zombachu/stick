package com.zombachu.stick.integration.fixtures

import com.zombachu.stick.CommandResult
import com.zombachu.stick.CommandWrapper
import com.zombachu.stick.Environment
import com.zombachu.stick.Invocation
import com.zombachu.stick.element.Structure
import com.zombachu.stick.feedback.FailureHandler
import com.zombachu.stick.feedback.Feedback
import kotlin.test.fail

internal fun <E : Environment, S> Structure<E, S, *>.execute(env: E, sender: S, command: String) {
    val feedback = dispatch(env, sender, command)
    if (feedback != null) fail("Unexpected error: ${feedback.message}")
}

internal fun <E : Environment, S> Structure<E, S, *>.executeExpectingError(
    env: E,
    sender: S,
    command: String,
): Feedback = dispatch(env, sender, command) ?: fail("No error returned")

private fun <E : Environment, S> Structure<E, S, *>.dispatch(env: E, sender: S, command: String): Feedback? {
    clearMessages(env, sender)

    val handler = RecordingFailureHandler<E, S>()
    val wrapper =
        object : CommandWrapper<E, S> {
            override val env: E = env
            override val failureHandler: FailureHandler<E, S> = handler
            override val structure: Structure<E, S, *> = this@dispatch
        }

    val args = command.replaceFirst("/", "").split(" ")
    wrapper.execute(sender, args)
    return handler.feedback
}

internal fun <E : Environment, S> Structure<E, S, *>.executeWithHandler(
    handler: FailureHandler<E, S>,
    env: E,
    sender: S,
    command: String,
) {
    clearMessages(env, sender)

    val wrapper =
        object : CommandWrapper<E, S> {
            override val env: E = env
            override val failureHandler: FailureHandler<E, S> = handler
            override val structure: Structure<E, S, *> = this@executeWithHandler
        }

    val args = command.replaceFirst("/", "").split(" ")
    wrapper.execute(sender, args)
}

private fun clearMessages(env: Environment, sender: Any?) {
    if (env is SynergyServer) env.clearMessages()
    if (sender is Sender) sender.logs.clear()
}

private class RecordingFailureHandler<E : Environment, S> : FailureHandler<E, S> {
    var feedback: Feedback? = null

    context(inv: Invocation<E, S>)
    override fun <F : Feedback> onFailure(failure: CommandResult.Failure<F>) {
        feedback = failure.feedback
    }
}
