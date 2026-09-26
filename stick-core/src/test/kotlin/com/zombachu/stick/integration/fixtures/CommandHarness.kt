package com.zombachu.stick.integration.fixtures

import com.zombachu.stick.CommandWrapper
import com.zombachu.stick.Environment
import com.zombachu.stick.Invocation
import com.zombachu.stick.element.Structure
import com.zombachu.stick.failure.FailureHandler
import com.zombachu.stick.failure.Reason
import kotlin.test.assertIs
import kotlin.test.fail

internal fun <E : Environment, S> Structure<E, S, *>.execute(env: E, sender: S, command: String) {
    val handler = dispatch(env, sender, command)
    val reason = handler.reason ?: return
    fail("Unexpected error: ${reason.message()}")
}

internal fun <E : Environment, S> Structure<E, S, *>.executeExpectingError(
    env: E,
    sender: S,
    command: String,
): Reason = dispatch(env, sender, command).reason ?: fail("No error returned")

internal fun <E : Environment, S> Structure<E, S, *>.executeExpectingInvalidSyntax(
    env: E,
    sender: S,
    command: String,
): String {
    val handler = dispatch(env, sender, command)
    return assertIs<Reason.InvalidSyntax>(handler.reason).usage
}

private fun <E : Environment, S> Structure<E, S, *>.dispatch(
    env: E,
    sender: S,
    command: String,
): RecordingFailureHandler<E, S> {
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
    return handler
}

internal fun <E : Environment, S> Structure<E, S, *>.suggest(env: E, sender: S, command: String): List<String> {
    val wrapper =
        object : CommandWrapper<E, S> {
            override val env: E = env
            override val failureHandler: FailureHandler<E, S> = RecordingFailureHandler()
            override val structure: Structure<E, S, *> = this@suggest
        }

    val args = command.replaceFirst("/", "").split(" ")
    return wrapper.suggest(sender, args.first(), args.drop(1))
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
    var reason: Reason? = null

    context(inv: Invocation<E, S>)
    override fun onFailure(reason: Reason) {
        this.reason = reason
    }
}
