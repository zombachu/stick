package com.zombachu.stick.paper

import com.zombachu.stick.Environment
import org.bukkit.Bukkit
import org.bukkit.Server
import org.bukkit.plugin.Plugin

interface PaperEnvironment : Environment {
    val plugin: Plugin
    val server: Server
}

open class BasicPaperEnvironment(override val plugin: Plugin) : PaperEnvironment {
    override val server: Server = Bukkit.getServer()
}
