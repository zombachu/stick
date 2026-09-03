package com.zombachu.stick.velocity

import com.velocitypowered.api.command.CommandManager
import com.velocitypowered.api.command.CommandSource
import com.velocitypowered.api.proxy.ProxyServer
import com.zombachu.stick.Stick
import com.zombachu.stick.element.Structure
import com.zombachu.stick.feedback.FailureHandler
import org.slf4j.Logger

class VelocityStick(private val plugin: Any, proxy: ProxyServer, logger: Logger) :
    Stick<VelocityEnvironment, CommandSource>(
        CommandSource::class,
        lazy(LazyThreadSafetyMode.NONE) { BasicVelocityEnvironment(proxy) },
        lazy(LazyThreadSafetyMode.NONE) { BasicVelocityFailureHandler(logger) },
    ) {

    private val commandManager: CommandManager = proxy.commandManager

    context(env: E, failureHandler: FailureHandler<E, CommandSource>)
    override fun <E : VelocityEnvironment> registerCommand(structure: Structure<E, CommandSource, *>) {
        @Suppress("SpreadOperator")
        val commandMeta =
            commandManager.metaBuilder(structure.name).aliases(*structure.aliases.toTypedArray()).plugin(plugin).build()
        commandManager.register(commandMeta, VelocityCommandWrapper(env, failureHandler, structure))
    }
}
