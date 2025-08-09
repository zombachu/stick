package com.zombachu.stick.paper

import com.zombachu.stick.Command
import com.zombachu.stick.CommandStructure
import com.zombachu.stick.impl.StructureContext
import com.zombachu.stick.impl.StructureElement
import com.zombachu.stick.requireIs
import org.bukkit.Bukkit
import org.bukkit.command.CommandMap
import org.bukkit.command.CommandSender
import org.bukkit.plugin.Plugin

class BukkitCommandBridge(
    val fallbackPrefix: String
) {
    constructor(plugin: Plugin) : this(plugin.name.lowercase())

    private val commandMap: CommandMap = Bukkit.getServer().commandMap

    inline fun <reified S : CommandSender> registerCommand(command: Command<S>) {
        val emptyContext = StructureContext.empty<CommandSender>()
        val structureElement: StructureElement<CommandSender, CommandStructure<CommandSender>> =
            if (S::class == CommandSender::class) {
                (command as Command<CommandSender>).structure
            } else {
                emptyContext.requireIs(S::class) { command.structure }
            }
        val structure = structureElement(emptyContext)
        registerStructure(structure)
    }

    fun registerStructure(structure: CommandStructure<CommandSender>) {
        commandMap.register(fallbackPrefix, BukkitCommandWrapper(structure))
    }
}
