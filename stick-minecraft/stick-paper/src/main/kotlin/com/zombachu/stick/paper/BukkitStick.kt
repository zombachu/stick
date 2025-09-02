package com.zombachu.stick.paper

import com.zombachu.stick.Stick
import com.zombachu.stick.element.Structure
import com.zombachu.stick.feedback.FailureHandler
import org.bukkit.Bukkit
import org.bukkit.command.CommandMap
import org.bukkit.command.CommandSender
import org.bukkit.plugin.Plugin

class BukkitStick(
    plugin: Plugin
) : Stick<BukkitEnvironment, CommandSender>(
    CommandSender::class,
    lazy(LazyThreadSafetyMode.NONE) { BasicBukkitEnvironment(plugin) },
    lazy(LazyThreadSafetyMode.NONE) { BasicBukkitFailureHandler() }
) {

    private val commandMap: CommandMap = Bukkit.getServer().commandMap

    context(env: E, failureHandler: FailureHandler<E, CommandSender>)
    override fun <E : BukkitEnvironment> registerCommand(
        structure: Structure<E, CommandSender>
    ) {
        val fallbackPrefix = env.plugin.name.lowercase()
        commandMap.register(fallbackPrefix, BukkitCommandWrapper(env, failureHandler, structure))
    }
}
