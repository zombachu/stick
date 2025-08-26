package com.zombachu.stick.paper

import com.zombachu.stick.Bridge
import com.zombachu.stick.Command
import com.zombachu.stick.Environment
import com.zombachu.stick.element.Structure
import com.zombachu.stick.feedback.ParsingFailureHandler
import org.bukkit.Bukkit
import org.bukkit.Server
import org.bukkit.command.CommandMap
import org.bukkit.command.CommandSender
import org.bukkit.plugin.Plugin

interface BukkitEnvironment : Environment {
    val server: Server
}

open class BukkitEnvironmentImpl(override val sender: Any) : BukkitEnvironment {

    override fun forSender(sender: Any): Environment {
        TODO("Copy deps")
        return BukkitEnvironmentImpl(sender)
    }

    override val server: Server = Bukkit.getServer()
}

interface BukkitCommand<O : Any> : Command<BukkitEnvironment, O> {
    override fun createEnvironment(sender: Any): BukkitEnvironment {
        return BukkitEnvironmentImpl(sender)
    }
}

class BukkitCommandBridge(
    val fallbackPrefix: String,
    parsingFailureHandler: ParsingFailureHandler<BukkitEnvironment, CommandSender> = BukkitParsingFailureHandler()
) : Bridge<BukkitEnvironment, CommandSender>(CommandSender::class, parsingFailureHandler) {

    constructor(
        plugin: Plugin,
        parsingFailureHandler: ParsingFailureHandler<BukkitEnvironment, CommandSender> = BukkitParsingFailureHandler()
    ) : this(plugin.name.lowercase(), parsingFailureHandler)

    private val commandMap: CommandMap = Bukkit.getServer().commandMap

    override fun registerCommand(
        structure: Structure<BukkitEnvironment, CommandSender>,
        createEnvironment: (CommandSender) -> BukkitEnvironment,
    ) {
        commandMap.register(fallbackPrefix, BukkitCommandWrapper(structure, createEnvironment, parsingFailureHandler))
    }
}
