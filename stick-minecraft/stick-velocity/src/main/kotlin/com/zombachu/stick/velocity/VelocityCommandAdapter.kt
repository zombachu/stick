package com.zombachu.stick.velocity

import com.velocitypowered.api.command.CommandSource
import com.velocitypowered.api.command.RawCommand
import com.zombachu.stick.CommandRunner
import com.zombachu.stick.element.Structure
import com.zombachu.stick.failure.FailureHandler
import java.util.concurrent.CompletableFuture
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

internal class VelocityCommandAdapter<E : VelocityEnvironment>(
    env: E,
    failureHandler: FailureHandler<E, CommandSource>,
    structure: Structure<E, CommandSource, *>,
    asyncContext: CoroutineContext,
) : RawCommand {

    // Velocity doesn't have a main thread
    private val runner = CommandRunner(env, failureHandler, structure, EmptyCoroutineContext, asyncContext)

    override fun execute(invocation: RawCommand.Invocation) {
        val args = invocation.arguments().split(' ').filter { it.isNotEmpty() }
        runner.execute(invocation.source(), invocation.alias(), args)
    }

    override fun hasPermission(invocation: RawCommand.Invocation): Boolean = runner.canUse(invocation.source())

    override fun suggestAsync(invocation: RawCommand.Invocation): CompletableFuture<List<String>> {
        val args = invocation.arguments().split(' ')
        return runner.suggestAsync(invocation.source(), invocation.alias(), args)
    }
}
