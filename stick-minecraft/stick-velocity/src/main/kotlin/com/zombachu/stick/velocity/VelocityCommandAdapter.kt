package com.zombachu.stick.velocity

import com.velocitypowered.api.command.CommandSource
import com.velocitypowered.api.command.RawCommand
import com.zombachu.stick.CommandRunner
import com.zombachu.stick.element.Structure
import com.zombachu.stick.failure.FailureHandler

internal class VelocityCommandAdapter<E : VelocityEnvironment>(
    env: E,
    failureHandler: FailureHandler<E, CommandSource>,
    structure: Structure<E, CommandSource, *>,
) : RawCommand {

    private val runner = CommandRunner(env, failureHandler, structure)

    override fun execute(invocation: RawCommand.Invocation) {
        val args = invocation.arguments().split(' ').filter { it.isNotEmpty() }
        runner.execute(invocation.source(), invocation.alias(), args)
    }

    override fun hasPermission(invocation: RawCommand.Invocation): Boolean = runner.canUse(invocation.source())

    override fun suggest(invocation: RawCommand.Invocation): List<String> {
        val args = invocation.arguments().split(' ')
        return runner.suggest(invocation.source(), invocation.alias(), args)
    }
}
