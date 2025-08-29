package com.zombachu.stick.paper

import com.zombachu.stick.impl.Requirement
import com.zombachu.stick.impl.SenderScope
import com.zombachu.stick.structure.requirement
import org.bukkit.command.CommandSender

// TODO: Permission message
fun <E : BukkitEnvironment, S : CommandSender> SenderScope<E, S>.permission(
    permission: String
): Requirement<E, S> = requirement {
    it.sender.hasPermission(permission)
}
