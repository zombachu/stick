package com.zombachu.stick.paper

import com.zombachu.stick.element.Structure
import com.zombachu.stick.feedback.FailureHandler
import com.zombachu.stick.impl.CommandWrapper
import org.bukkit.Location
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.command.PluginIdentifiableCommand
import org.bukkit.plugin.Plugin

class BukkitCommandWrapper<E : BukkitEnvironment>(
    override val env: E,
    override val failureHandler: FailureHandler<E, CommandSender>,
    override val structure: Structure<E, CommandSender>,
) : Command(
    structure.label,
    structure.description,
    "/${structure.label}",
    structure.aliases.toList()
), PluginIdentifiableCommand, CommandWrapper<E, CommandSender> {

    override fun execute(sender: CommandSender, label: String, args: Array<String>): Boolean {
        execute(sender, mutableListOf(label, *args))
        return true
    }

    override fun tabComplete(
        sender: CommandSender,
        alias: String,
        args: Array<String>,
    ): List<String> {
        TODO("Not yet implemented")
    }

    override fun tabComplete(
        sender: CommandSender,
        alias: String,
        args: Array<String>,
        location: Location?,
    ): List<String> {
        TODO("Not yet implemented")
    }

    override fun testPermissionSilent(target: CommandSender): Boolean {
        TODO("Not yet implemented")
    }

    override fun getPlugin(): Plugin = env.plugin
}
