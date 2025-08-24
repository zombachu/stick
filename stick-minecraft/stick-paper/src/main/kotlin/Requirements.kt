package com.zombachu.stick.paper

import com.zombachu.stick.impl.Requirement
import com.zombachu.stick.impl.SenderScope
import com.zombachu.stick.structure.requirement
import org.bukkit.command.CommandSender

// TODO: Permission message
fun <O : CommandSender, S : BukkitContext<O>> SenderScope<O, S>.permission(permission: String): Requirement<O, S> = requirement {
    it.sender.hasPermission(permission)
}
