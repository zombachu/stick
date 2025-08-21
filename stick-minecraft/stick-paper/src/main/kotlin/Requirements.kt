package com.zombachu.stick.paper

import com.zombachu.stick.impl.Requirement
import com.zombachu.stick.impl.SenderScope
import com.zombachu.stick.structure.requirement
import org.bukkit.command.CommandSender

// TODO: Permission message
fun SenderScope<CommandSender>.permission(permission: String): Requirement<CommandSender> = requirement {
    it.hasPermission(permission)
}
