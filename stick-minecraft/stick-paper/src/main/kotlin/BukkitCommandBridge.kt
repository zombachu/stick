package com.zombachu.stick.paper

import com.zombachu.stick.Bridge
import com.zombachu.stick.element.Structure
import com.zombachu.stick.feedback.ParsingFailureHandler
import org.bukkit.Bukkit
import org.bukkit.command.CommandMap
import org.bukkit.command.CommandSender
import org.bukkit.plugin.Plugin

class BukkitCommandBridge(
    val fallbackPrefix: String,
    parsingFailureHandler: ParsingFailureHandler<CommandSender> = BukkitParsingFailureHandler()
) : Bridge<CommandSender>(CommandSender::class, parsingFailureHandler) {

    constructor(
        plugin: Plugin,
        parsingFailureHandler: ParsingFailureHandler<CommandSender> = BukkitParsingFailureHandler()
    ) : this(plugin.name.lowercase(), parsingFailureHandler)

    private val commandMap: CommandMap = Bukkit.getServer().commandMap

    override fun registerStructure(structure: Structure<CommandSender>) {
        commandMap.register(fallbackPrefix, BukkitCommandWrapper(structure, parsingFailureHandler))
    }
}
