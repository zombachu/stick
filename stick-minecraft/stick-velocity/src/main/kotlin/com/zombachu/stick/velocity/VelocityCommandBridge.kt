package com.zombachu.stick.velocity

import com.velocitypowered.api.command.CommandSource
import com.velocitypowered.api.plugin.PluginManager
import com.velocitypowered.api.proxy.ProxyServer
import com.zombachu.stick.Bridge
import com.zombachu.stick.Command
import com.zombachu.stick.Environment
import com.zombachu.stick.element.Structure
import com.zombachu.stick.feedback.ParsingFailureHandler

interface VelocityEnvironment : Environment {
    val proxy: ProxyServer
}

open class VelocityEnvironmentImpl(override val proxy: ProxyServer) : VelocityEnvironment

interface VelocityCommand<S : Any> : Command<VelocityEnvironment, S>

class VelocityCommandBridge(
    val proxy: ProxyServer,
    val plugin: Any,
) : Bridge<VelocityEnvironment, CommandSource>(CommandSource::class) {

    private val pluginManager: PluginManager = proxy.pluginManager

    context(env: E, parsingFailureHandler: ParsingFailureHandler<E, CommandSource>)
    override fun <E : VelocityEnvironment> registerCommand(
        structure: Structure<E, CommandSource>,
    ) {
        val commandMeta = proxy.commandManager
            .metaBuilder(structure.id.name)
            .aliases(*structure.aliases.toTypedArray())
            .plugin(plugin)
            .build()
        proxy.commandManager.register(commandMeta, VelocityCommandWrapper(env, parsingFailureHandler, structure))
    }
}
