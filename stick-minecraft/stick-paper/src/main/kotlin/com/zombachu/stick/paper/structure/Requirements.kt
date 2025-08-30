package com.zombachu.stick.paper.structure

import com.zombachu.stick.impl.BuilderScope
import com.zombachu.stick.impl.Requirement
import com.zombachu.stick.paper.BukkitEnvironment
import com.zombachu.stick.structure.requirement
import org.bukkit.command.CommandSender

// TODO: Permission message
fun <E : BukkitEnvironment, S : CommandSender> BuilderScope<E, S>.permission(
    permission: String
): Requirement<E, S> = requirement {
    it.sender.hasPermission(permission)
}
