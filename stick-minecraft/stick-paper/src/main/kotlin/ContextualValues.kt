package com.zombachu.stick.paper

import com.zombachu.stick.ContextualValue
import com.zombachu.stick.impl.SenderScope
import org.bukkit.command.CommandSender

fun <T> SenderScope<CommandSender>.permissionedValue(
    permission: String,
    default: T,
    fallback: T
): ContextualValue<CommandSender, T> = {
    if (sender.hasPermission(permission)) default else fallback
}
