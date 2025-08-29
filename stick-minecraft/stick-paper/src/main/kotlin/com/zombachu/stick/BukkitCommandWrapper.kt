package com.zombachu.stick.paper

import com.zombachu.stick.Invocation
import com.zombachu.stick.element.Structure
import com.zombachu.stick.feedback.ParsingFailureHandler
import com.zombachu.stick.isSuccess
import org.bukkit.Location
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.command.PluginIdentifiableCommand
import org.bukkit.plugin.Plugin

class BukkitCommandWrapper<E : BukkitEnvironment>(
    private val env: E,
    private val parsingFailureHandler: ParsingFailureHandler<E, CommandSender>,
    private val plugin: Plugin,
    private val structure: Structure<E, CommandSender>,
) : Command(
    structure.label,
    structure.description,
    "/${structure.label}",
    structure.aliases.toList()
), PluginIdentifiableCommand {

    override fun execute(sender: CommandSender, label: String, args: Array<String>): Boolean {
        val args = args.toMutableList()
        val inv: Invocation<E, CommandSender> = Invocation(sender, env, label, args, structure)
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

    override fun getPlugin(): Plugin = plugin
}
