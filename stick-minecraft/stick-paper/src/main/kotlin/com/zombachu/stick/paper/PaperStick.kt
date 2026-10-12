package com.zombachu.stick.paper

import com.zombachu.stick.Stick
import com.zombachu.stick.asCoroutineContext
import com.zombachu.stick.element.Structure
import com.zombachu.stick.failure.FailureHandler
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents
import org.bukkit.Bukkit
import org.bukkit.command.CommandSender
import org.bukkit.plugin.Plugin
import java.util.concurrent.Executor
import kotlin.coroutines.CoroutineContext

class PaperStick(
    plugin: Plugin,
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
}

private fun asyncScheduler(plugin: Plugin): CoroutineContext {
    val executor = Executor { task -> Bukkit.getScheduler().runTaskAsynchronously(plugin, task) }
    return executor.asCoroutineContext()
}
