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

open class BukkitEnvironmentImpl() : BukkitEnvironment {
    override val server: Server = Bukkit.getServer()
}

interface BukkitCommand<S : Any> : Command<BukkitEnvironment, S> {
    override fun createEnvironment(): BukkitEnvironment {
        return BukkitEnvironmentImpl()
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
        createEnvironment: () -> BukkitEnvironment,
    ) {
        commandMap.register(fallbackPrefix, BukkitCommandWrapper(structure, createEnvironment, parsingFailureHandler))
    }
}
