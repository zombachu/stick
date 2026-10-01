package com.zombachu.stick.paper

import com.zombachu.stick.Stick
import com.zombachu.stick.element.Structure
import com.zombachu.stick.failure.FailureHandler
import org.bukkit.Bukkit
import org.bukkit.command.CommandMap
import org.bukkit.command.CommandSender
import org.bukkit.plugin.Plugin

class PaperStick(plugin: Plugin) :
    Stick<PaperEnvironment, CommandSender>(
        CommandSender::class,
        lazy(LazyThreadSafetyMode.NONE) { BasicPaperEnvironment(plugin) },
        lazy(LazyThreadSafetyMode.NONE) { BasicPaperFailureHandler() },
    ) {

    private val commandMap: CommandMap = Bukkit.getServer().commandMap

    context(env: E, failureHandler: FailureHandler<E, CommandSender>)
    override fun <E : PaperEnvironment> registerCommand(structure: Structure<E, CommandSender, *>) {
        val fallbackPrefix = env.plugin.name.lowercase()
        commandMap.register(fallbackPrefix, PaperCommandAdapter(env, failureHandler, structure))
    }
}
