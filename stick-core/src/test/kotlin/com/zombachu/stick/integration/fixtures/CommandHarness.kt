package com.zombachu.stick.integration.fixtures

import com.zombachu.stick.CommandRunner
import com.zombachu.stick.Environment
import com.zombachu.stick.Invocation
import com.zombachu.stick.element.Structure
import com.zombachu.stick.failure.FailureHandler
import com.zombachu.stick.failure.FailureOrigin
import com.zombachu.stick.failure.Reason
import kotlin.test.assertSame
import kotlin.test.fail

internal fun <E : Environment, S> Structure<E, S, *>.execute(env: E, sender: S, command: String) {
    val handler = dispatch(env, sender, command)
    val reason = handler.reason ?: return
    fail("Unexpected error: ${reason.message(handler.origin!!)}")
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
    assertSame(Reason.InvalidSyntax, handler.reason)
    return handler.origin!!.usage
}

private fun <E : Environment, S> Structure<E, S, *>.dispatch(
    env: E,
    sender: S,
    command: String,
): RecordingFailureHandler<E, S> {
    val handler = RecordingFailureHandler<E, S>()
    executeWithHandler(handler, env, sender, command)
    return handler
}

internal fun <E : Environment, S> Structure<E, S, *>.suggest(env: E, sender: S, command: String): List<String> {
    val runner = CommandRunner(env, RecordingFailureHandler(), this)

    val args = command.replaceFirst("/", "").split(" ")
    return runner.suggest(sender, args.first(), args.drop(1))
}

internal fun <E : Environment, S> Structure<E, S, *>.executeWithHandler(
    handler: FailureHandler<E, S>,
    env: E,
    sender: S,
    command: String,
) {
    clearMessages(env, sender)

    val runner = CommandRunner(env, handler, this)

    val args = command.replaceFirst("/", "").split(" ")
    runner.execute(sender, args.first(), args.drop(1))
}

private fun clearMessages(env: Environment, sender: Any?) {
    if (env is SynergyServer) env.clearMessages()
    if (sender is Sender) sender.logs.clear()
}

private class RecordingFailureHandler<E : Environment, S> : FailureHandler<E, S> {
    var reason: Reason? = null
    var origin: FailureOrigin? = null

    context(inv: Invocation<E, S>)
    override fun onFailure(reason: Reason, origin: FailureOrigin) {
        this.reason = reason
        this.origin = origin
    }
}
