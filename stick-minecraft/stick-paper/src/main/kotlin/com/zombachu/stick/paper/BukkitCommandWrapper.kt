package com.zombachu.stick.paper

import com.zombachu.stick.CommandWrapper
import com.zombachu.stick.ValidationContext
import com.zombachu.stick.element.Structure
import com.zombachu.stick.feedback.FailureHandler
import com.zombachu.stick.isSuccess
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.command.PluginIdentifiableCommand
import org.bukkit.plugin.Plugin

class BukkitCommandWrapper<E : BukkitEnvironment>(
    override val env: E,
    override val failureHandler: FailureHandler<E, CommandSender>,
    override val structure: Structure<E, CommandSender, *>,
) :
    Command(structure.label, structure.description, "/${structure.label}", structure.aliases.toList()),
    PluginIdentifiableCommand,
    CommandWrapper<E, CommandSender> {

    override fun execute(sender: CommandSender, label: String, args: Array<String>): Boolean {
        val fullArgs =
            buildList(args.size + 1) {
                add(label.stripNamespace())
                addAll(args)
            }
        execute(sender, fullArgs)
        return true
    }

    override fun tabComplete(sender: CommandSender, alias: String, args: Array<String>): List<String> =
        suggest(sender, alias.stripNamespace(), args.asList())

    override fun testPermissionSilent(target: CommandSender): Boolean {
        val validationContext = ValidationContext(env, target)
        context(validationContext) {
            return structure.validateSender().isSuccess()
        }
    }

    override fun getPlugin(): Plugin = env.plugin
}

private fun String.stripNamespace(): String = substringAfterLast(':')
