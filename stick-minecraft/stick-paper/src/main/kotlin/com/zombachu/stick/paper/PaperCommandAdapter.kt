package com.zombachu.stick.paper

import com.zombachu.stick.CommandRunner
import com.zombachu.stick.element.Structure
import com.zombachu.stick.failure.FailureHandler
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.command.PluginIdentifiableCommand
import org.bukkit.plugin.Plugin

internal class PaperCommandAdapter<E : PaperEnvironment>(
    private val env: E,
    failureHandler: FailureHandler<E, CommandSender>,
    structure: Structure<E, CommandSender, *>,
) :
    Command(structure.label, structure.description, "/${structure.label}", structure.aliases.toList()),
    PluginIdentifiableCommand {

    private val runner = CommandRunner(env, failureHandler, structure)

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
