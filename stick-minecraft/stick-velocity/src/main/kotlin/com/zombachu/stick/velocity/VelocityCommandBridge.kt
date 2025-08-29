package com.zombachu.stick.velocity

import com.velocitypowered.api.command.CommandManager
import com.velocitypowered.api.command.CommandSource
import com.velocitypowered.api.proxy.ProxyServer
import com.zombachu.stick.Bridge
import com.zombachu.stick.Command
import com.zombachu.stick.Environment
import com.zombachu.stick.element.Structure
import com.zombachu.stick.feedback.FailureHandler

interface VelocityEnvironment : Environment {
    val proxy: ProxyServer
}

open class VelocityEnvironmentImpl(override val proxy: ProxyServer) : VelocityEnvironment

interface VelocityCommand<S : Any> : Command<VelocityEnvironment, S>

class VelocityCommandBridge(
    private val plugin: Any,
    proxy: ProxyServer,
) : Bridge<VelocityEnvironment, CommandSource>(CommandSource::class) {

    private val commandManager: CommandManager = proxy.commandManager

    context(env: E, failureHandler: FailureHandler<E, CommandSource>)
    override fun <E : VelocityEnvironment> registerCommand(
        structure: Structure<E, CommandSource>,
    ) {
        val commandMeta = commandManager
            .metaBuilder(structure.id.name)
            .aliases(*structure.aliases.toTypedArray())
            .plugin(plugin)
            .build()
        commandManager.register(commandMeta, VelocityCommandWrapper(env, failureHandler, structure))
    }
}
