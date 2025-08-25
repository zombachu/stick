package com.zombachu.stick.paper

import com.zombachu.stick.Bridge
import com.zombachu.stick.Command
import com.zombachu.stick.SenderContext
import com.zombachu.stick.element.Structure
import com.zombachu.stick.feedback.ParsingFailureHandler
import org.bukkit.Bukkit
import org.bukkit.Server
import org.bukkit.command.CommandMap
import org.bukkit.command.CommandSender
import org.bukkit.plugin.Plugin

interface BukkitContext : SenderContext {
    val server: Server
}

open class BukkitContextImpl(override val sender: Any) : BukkitContext {

    override fun forSender(sender: Any): SenderContext {
        TODO("Copy deps")
        return BukkitContextImpl(sender)
    }

    override val server: Server = Bukkit.getServer()
}

interface BukkitCommand<O : Any> : Command<BukkitContext, O> {
    override fun createSenderContext(sender: Any): BukkitContext {
        return BukkitContextImpl(sender)
    }
}

class BukkitCommandBridge(
    val fallbackPrefix: String,
    parsingFailureHandler: ParsingFailureHandler<BukkitContext, CommandSender> = BukkitParsingFailureHandler()
) : Bridge<BukkitContext, CommandSender>(CommandSender::class, parsingFailureHandler) {

    constructor(
        plugin: Plugin,
        parsingFailureHandler: ParsingFailureHandler<BukkitContext, CommandSender> = BukkitParsingFailureHandler()
    ) : this(plugin.name.lowercase(), parsingFailureHandler)

    private val commandMap: CommandMap = Bukkit.getServer().commandMap

    override fun registerCommand(
        structure: Structure<BukkitContext, CommandSender>,
        createSenderContext: (CommandSender) -> BukkitContext,
    ) {
        commandMap.register(fallbackPrefix, BukkitCommandWrapper(structure, createSenderContext, parsingFailureHandler))
    }
}
