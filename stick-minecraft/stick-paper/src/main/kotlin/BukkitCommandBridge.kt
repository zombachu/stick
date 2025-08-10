package com.zombachu.stick.paper

import com.zombachu.stick.Bridge
import com.zombachu.stick.feedback.ParsingFailureHandler
import com.zombachu.stick.impl.Structure
import org.bukkit.Bukkit
import org.bukkit.command.CommandMap
import org.bukkit.command.CommandSender
import org.bukkit.plugin.Plugin

class BukkitCommandBridge(
    val fallbackPrefix: String,
    override val parsingFailureHandler: ParsingFailureHandler<CommandSender> = BukkitParsingFailureHandler()
) : Bridge<CommandSender> {
    constructor(plugin: Plugin) : this(plugin.name.lowercase())

    private val commandMap: CommandMap = Bukkit.getServer().commandMap

    override fun registerStructure(structure: Structure<CommandSender>) {
        commandMap.register(fallbackPrefix, BukkitCommandWrapper(structure, parsingFailureHandler))
    }
}
