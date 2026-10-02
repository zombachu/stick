package com.zombachu.stick.paper

import com.zombachu.stick.CommandRunner
import com.zombachu.stick.asCoroutineContext
import com.zombachu.stick.element.Structure
import com.zombachu.stick.failure.FailureHandler
import java.util.concurrent.Executor
import kotlin.coroutines.CoroutineContext
import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.command.PluginIdentifiableCommand
import org.bukkit.plugin.Plugin

internal class PaperCommandAdapter<E : PaperEnvironment>(
    private val env: E,
    failureHandler: FailureHandler<E, CommandSender>,
    structure: Structure<E, CommandSender, *>,
    asyncContext: CoroutineContext,
) :
    Command(structure.label, structure.description, "/${structure.label}", structure.aliases.toList()),
    PluginIdentifiableCommand {

    private val runner = CommandRunner(env, failureHandler, structure, mainThread(env.plugin), asyncContext)

    override fun execute(sender: CommandSender, label: String, args: Array<String>): Boolean {
        runner.execute(sender, label.stripNamespace(), args.asList())
        return true
    }

    override fun tabComplete(sender: CommandSender, alias: String, args: Array<String>): List<String> =
        runner.suggest(sender, alias.stripNamespace(), args.asList())

    override fun testPermissionSilent(target: CommandSender): Boolean = runner.canUse(target)

    override fun getPlugin(): Plugin = env.plugin
}

private fun String.stripNamespace(): String = substringAfterLast(':')

private fun mainThread(plugin: Plugin): CoroutineContext {
    val executor = Executor { task ->
        if (Bukkit.isPrimaryThread()) {
            task.run()
        } else if (plugin.isEnabled) {
            Bukkit.getScheduler().runTask(plugin, task)
        }
    }
    return executor.asCoroutineContext()
}
