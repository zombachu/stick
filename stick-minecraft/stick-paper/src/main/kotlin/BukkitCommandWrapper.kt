package com.zombachu.stick.paper

import com.zombachu.stick.Command
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
        // TODO: Error handling
        structure.parse(context, args)
        return true
    }
}
