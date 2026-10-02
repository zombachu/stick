package com.zombachu.stick.velocity

import com.velocitypowered.api.command.CommandManager
import com.velocitypowered.api.command.CommandSource
import com.velocitypowered.api.proxy.ProxyServer
import com.zombachu.stick.Stick
import com.zombachu.stick.asCoroutineContext
import com.zombachu.stick.element.Structure
import com.zombachu.stick.failure.FailureHandler
import java.util.concurrent.Executor
import kotlin.coroutines.CoroutineContext
import org.slf4j.Logger

class VelocityStick(
    private val plugin: Any,
    proxy: ProxyServer,
    logger: Logger,
    asyncContext: CoroutineContext = scheduler(plugin, proxy),
) :
    Stick<VelocityEnvironment, CommandSource>(
        CommandSource::class,
        lazy(LazyThreadSafetyMode.NONE) { BasicVelocityEnvironment(proxy) },
        lazy(LazyThreadSafetyMode.NONE) { BasicVelocityFailureHandler(logger) },
        asyncContext,
    ) {

    private val commandManager: CommandManager = proxy.commandManager

    context(env: E, failureHandler: FailureHandler<E, CommandSource>)
    override fun <E : VelocityEnvironment> registerCommand(
        structure: Structure<E, CommandSource, *>,
        asyncContext: CoroutineContext,
    ) {
        @Suppress("SpreadOperator")
        val commandMeta =
            commandManager.metaBuilder(structure.name).aliases(*structure.aliases.toTypedArray()).plugin(plugin).build()
        commandManager.register(commandMeta, VelocityCommandAdapter(env, failureHandler, structure, asyncContext))
    }
}

private fun scheduler(plugin: Any, proxy: ProxyServer): CoroutineContext {
    val executor = Executor { task -> proxy.scheduler.buildTask(plugin, task).schedule() }
    return executor.asCoroutineContext()
}
