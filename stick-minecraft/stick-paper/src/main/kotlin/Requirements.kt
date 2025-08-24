package com.zombachu.stick.paper

import com.zombachu.stick.impl.Requirement
import com.zombachu.stick.impl.SenderScope
import com.zombachu.stick.structure.requirement
import org.bukkit.command.CommandSender

// TODO: Permission message
fun <O : CommandSender> SenderScope<BukkitContext, O>.permission(permission: String): Requirement<BukkitContext, O> = requirement {
    // TODO: Cast in context
    (it.sender as CommandSender).hasPermission(permission)
}
