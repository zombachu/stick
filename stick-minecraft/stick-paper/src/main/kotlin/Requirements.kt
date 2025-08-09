package com.zombachu.stick.paper

import com.zombachu.stick.impl.Requirement
import com.zombachu.stick.impl.SenderScope
import org.bukkit.command.CommandSender

fun SenderScope<CommandSender>.permission(permission: String): Requirement<CommandSender> = Requirement {
    it.hasPermission(permission)
}
