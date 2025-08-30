package com.zombachu.stick.paper

import com.zombachu.stick.Environment
import org.bukkit.Bukkit
import org.bukkit.Server
import org.bukkit.plugin.Plugin

interface BukkitEnvironment : Environment {
    val plugin: Plugin
    val server: Server
}

open class BasicBukkitEnvironment(
    override val plugin: Plugin
) : BukkitEnvironment {
    override val server: Server = Bukkit.getServer()
}
