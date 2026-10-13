package com.zombachu.stick.paper

import com.zombachu.stick.Stick
import com.zombachu.stick.asCoroutineContext
import com.zombachu.stick.element.Structure
import com.zombachu.stick.failure.FailureHandler
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents
import java.util.concurrent.Executor
import kotlin.coroutines.CoroutineContext
import org.bukkit.Bukkit
import org.bukkit.command.CommandSender
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.server.PluginDisableEvent
import org.bukkit.plugin.Plugin

class PaperStick(
    private val plugin: Plugin,
    asyncContext: CoroutineContext = asyncScheduler(plugin),
) :
    Stick<PaperEnvironment, CommandSender>(
        CommandSender::class,
        lazy(LazyThreadSafetyMode.NONE) { BasicPaperEnvironment(plugin) },
        lazy(LazyThreadSafetyMode.NONE) { BasicPaperFailureHandler() },
        asyncContext,
    ) {

    context(env: E, failureHandler: FailureHandler<E, CommandSender>)
    override fun <E : PaperEnvironment> registerCommand(
        structure: Structure<E, CommandSender, *>,
        asyncContext: CoroutineContext,
    ) {
        val adapter = PaperCommandAdapter(env, failureHandler, structure, asyncContext)
        env.plugin.lifecycleManager.registerEventHandler(LifecycleEvents.COMMANDS) { event ->
            event.registrar().register(adapter.toBrigadierNode(), structure.description, structure.aliases)
        }
    }

    override fun onFirstRegistration() {
        plugin.server.pluginManager.registerEvents(PluginDisableListener(plugin, ::cancelRuns), plugin)
    }
}

internal class PluginDisableListener(private val plugin: Plugin, private val cancelRuns: () -> Unit) : Listener {
    @EventHandler fun onPluginDisable(event: PluginDisableEvent) = onDisable(event.plugin)

    fun onDisable(disabled: Plugin) {
        if (disabled == plugin) cancelRuns()
    }
}

private fun asyncScheduler(plugin: Plugin): CoroutineContext {
    val executor = Executor { task -> Bukkit.getScheduler().runTaskAsynchronously(plugin, task) }
    return executor.asCoroutineContext()
}
