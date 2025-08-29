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

open class BukkitEnvironmentImpl : BukkitEnvironment {
    override val server: Server = Bukkit.getServer()
}

interface BukkitCommand<S : Any> : Command<BukkitEnvironment, S>

class BukkitCommandBridge(
    val plugin: Plugin,
    val fallbackPrefix: String = plugin.name.lowercase(),
) : Bridge<BukkitEnvironment, CommandSender>(CommandSender::class) {

    private val commandMap: CommandMap = Bukkit.getServer().commandMap

    context(env: E, parsingFailureHandler: ParsingFailureHandler<E, CommandSender>)
    override fun <E : BukkitEnvironment> registerCommand(
        structure: Structure<E, CommandSender>
    ) {
        commandMap.register(fallbackPrefix, BukkitCommandWrapper(env, parsingFailureHandler, plugin, structure))
    }
}
