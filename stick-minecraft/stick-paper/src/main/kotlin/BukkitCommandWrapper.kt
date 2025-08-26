package com.zombachu.stick.paper

import com.zombachu.stick.Invocation
import com.zombachu.stick.element.Structure
import com.zombachu.stick.feedback.ParsingFailureHandler
import com.zombachu.stick.isSuccess
import org.bukkit.Location
import org.bukkit.command.CommandSender
import org.bukkit.command.PluginIdentifiableCommand
import org.bukkit.plugin.Plugin

class BukkitCommandWrapper(
    val structure: Structure<BukkitEnvironment, CommandSender>,
    val createEnvironment: (CommandSender) -> BukkitEnvironment,
    val parsingFailureHandler: ParsingFailureHandler<BukkitEnvironment, CommandSender>,
) : org.bukkit.command.Command(
    structure.label,
    structure.description,
    "/${structure.label}",
    structure.aliases.toList()
), PluginIdentifiableCommand {

    override fun execute(sender: CommandSender, label: String, args: Array<String>): Boolean {
        val args = args.toMutableList()
        val env = createEnvironment(sender)
        val inv = Invocation(env, label, args, structure)
        args.addFirst(label)

        context(env, inv) {
            val result = structure.parse(args)
            if (!result.isSuccess()) {
                parsingFailureHandler.onFailure(inv, result)
            }
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
