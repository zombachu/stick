package com.zombachu.stick.paper

import com.zombachu.stick.impl.Requirement
import com.zombachu.stick.impl.SenderScope
import com.zombachu.stick.structure.requirement
import org.bukkit.command.CommandSender

// TODO: Permission message
fun <O : CommandSender> SenderScope<BukkitEnvironment, O>.permission(permission: String): Requirement<BukkitEnvironment, O> = requirement {
    // TODO: Cast in context
    (it.sender as CommandSender).hasPermission(permission)
}
