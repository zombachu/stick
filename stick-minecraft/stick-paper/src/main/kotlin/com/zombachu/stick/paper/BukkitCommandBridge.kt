package com.zombachu.stick.paper

import com.zombachu.stick.Bridge
import com.zombachu.stick.element.Structure
import com.zombachu.stick.feedback.FailureHandler
import org.bukkit.Bukkit
import org.bukkit.command.CommandMap
import org.bukkit.command.CommandSender

class BukkitCommandBridge : Bridge<BukkitEnvironment, CommandSender>(CommandSender::class) {

    private val commandMap: CommandMap = Bukkit.getServer().commandMap

    context(env: E, failureHandler: FailureHandler<E, CommandSender>)
    override fun <E : BukkitEnvironment> registerCommand(
        structure: Structure<E, CommandSender>
    ) {
        val fallbackPrefix = env.plugin.name.lowercase()
        commandMap.register(fallbackPrefix, BukkitCommandWrapper(env, failureHandler, structure))
    }
}
