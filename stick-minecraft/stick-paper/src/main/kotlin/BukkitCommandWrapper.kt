package com.zombachu.stick.paper

import com.zombachu.stick.Command
import com.zombachu.stick.ExecutionContext
import com.zombachu.stick.element.Structure
import com.zombachu.stick.feedback.ParsingFailureHandler
import com.zombachu.stick.isSuccess
import org.bukkit.Location
import org.bukkit.command.CommandSender
import org.bukkit.command.PluginIdentifiableCommand
import org.bukkit.entity.Player
import org.bukkit.plugin.Plugin

typealias BukkitCommand = Command<CommandSender>
typealias PlayerCommand = Command<Player>

class BukkitCommandWrapper(
    val structure: Structure<CommandSender>,
    val parsingFailureHandler: ParsingFailureHandler<CommandSender>,
) : org.bukkit.command.Command(
    structure.label,
    structure.description,
    "/${structure.label}",
    structure.aliases.toList()
), PluginIdentifiableCommand {

    override fun execute(sender: CommandSender, label: String, args: Array<String>): Boolean {
        val args = args.toMutableList()
        val context = ExecutionContext(sender, label, args)
        args.addFirst(label)

        val result = structure.parse(context, args)
        if (!result.isSuccess()) {
            parsingFailureHandler.onFailure(context, result)
        }
        return true
    }

    override fun tabComplete(
        sender: CommandSender,
        alias: String,
        args: Array<out String>?,
    ): List<String?> {
        TODO("Not yet implemented")
    }

    override fun tabComplete(
        sender: CommandSender,
        alias: String,
        args: Array<out String>?,
        location: Location?,
    ): List<String?> {
        TODO("Not yet implemented")
    }

    override fun testPermissionSilent(target: CommandSender): Boolean {
        TODO("Not yet implemented")
    }

    override fun getPlugin(): Plugin {
        TODO("Not yet implemented")
    }
}
