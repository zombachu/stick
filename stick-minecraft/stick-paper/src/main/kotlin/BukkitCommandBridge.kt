package com.zombachu.stick.paper

import com.zombachu.stick.Bridge
import com.zombachu.stick.SenderContext
import com.zombachu.stick.element.Structure
import com.zombachu.stick.feedback.ParsingFailureHandler
import org.bukkit.Bukkit
import org.bukkit.command.CommandMap
import org.bukkit.command.CommandSender
import org.bukkit.plugin.Plugin

class BukkitContext<O>(override val sender: O) : SenderContext<O> {
    override fun <O2> forSender(sender: O2): BukkitContext<O2> {
        TODO("Copy deps")
        return BukkitContext(sender)
    }
}

class BukkitCommandBridge(
    val fallbackPrefix: String,
    parsingFailureHandler: ParsingFailureHandler<CommandSender, BukkitContext<CommandSender>> = BukkitParsingFailureHandler()
) : Bridge<CommandSender, BukkitContext<CommandSender>>(CommandSender::class, parsingFailureHandler) {

    constructor(
        plugin: Plugin,
        parsingFailureHandler: ParsingFailureHandler<CommandSender, BukkitContext<CommandSender>> = BukkitParsingFailureHandler()
    ) : this(plugin.name.lowercase(), parsingFailureHandler)

    private val commandMap: CommandMap = Bukkit.getServer().commandMap

    override fun registerStructure(structure: Structure<CommandSender, BukkitContext<CommandSender>>) {
        commandMap.register(fallbackPrefix, BukkitCommandWrapper(structure, parsingFailureHandler))
    }
}
