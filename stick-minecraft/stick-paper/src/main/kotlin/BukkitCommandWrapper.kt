package com.zombachu.stick.paper

import com.zombachu.stick.Command
import com.zombachu.stick.CommandStructure
import com.zombachu.stick.ExecutionContext
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

typealias BukkitCommand = Command<CommandSender>
typealias PlayerCommand = Command<Player>

class BukkitCommandWrapper(val structure: CommandStructure<CommandSender>) : org.bukkit.command.Command(
    structure.label,
    structure.description,
    "/${structure.label}",
    structure.aliases.toList()
) {
    override fun execute(sender: CommandSender, label: String, args: Array<String>): Boolean {
        val args = args.toMutableList()
        val context = ExecutionContext(sender, label, args)

        args.addFirst(label)
        // TODO: Error handling
        structure.parse(context, args)
        return true
    }
}
