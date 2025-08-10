package com.zombachu.stick.paper

import com.zombachu.stick.Command
import com.zombachu.stick.ParsingResult.Companion.isSuccess
import com.zombachu.stick.impl.ExecutionContextImpl
import com.zombachu.stick.impl.Structure
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

typealias BukkitCommand = Command<CommandSender>
typealias PlayerCommand = Command<Player>

class BukkitCommandWrapper(val structure: Structure<CommandSender>) : org.bukkit.command.Command(
    structure.label,
    structure.description,
    "/${structure.label}",
    structure.aliases.toList()
) {
    override fun execute(sender: CommandSender, label: String, args: Array<String>): Boolean {
        val args = args.toMutableList()
        val context = ExecutionContextImpl(sender, label, args)
        args.addFirst(label)

        val result = structure.parse(context, args)

        // If an error propagated up then send it to the user
        if (!result.isSuccess()) {
            val message = result.feedback.format()
            if (message.isNotEmpty()) {
                sender.sendMessage(message)
            }
        }

        return true
    }
}
