package com.zombachu.stick.paper

import com.zombachu.stick.Bridge
import com.zombachu.stick.SenderContext
import com.zombachu.stick.element.Structure
import com.zombachu.stick.feedback.ParsingFailureHandler
import org.bukkit.Bukkit
import org.bukkit.command.CommandMap
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.plugin.Plugin

open class BukkitContext(override val sender: CommandSender) : SenderContext
open class PlayerContext(override val sender: Player) : BukkitContext(sender)

class BukkitCommandBridge(
    val fallbackPrefix: String,
    parsingFailureHandler: ParsingFailureHandler<BukkitContext> = BukkitParsingFailureHandler()
) : Bridge<BukkitContext>(BukkitContext::class, parsingFailureHandler) {

    constructor(
        plugin: Plugin,
        parsingFailureHandler: ParsingFailureHandler<BukkitContext> = BukkitParsingFailureHandler()
    ) : this(plugin.name.lowercase(), parsingFailureHandler)

    private val commandMap: CommandMap = Bukkit.getServer().commandMap

    override fun registerStructure(structure: Structure<BukkitContext>) {
        commandMap.register(fallbackPrefix, BukkitCommandWrapper(structure, parsingFailureHandler, ::createSenderContext))
    }

    companion object {
        fun createSenderContext(sender: CommandSender): BukkitContext {
            return when (sender) {
                is Player -> PlayerContext(sender)
                else -> BukkitContext(sender)
            }
        }
    }
}
