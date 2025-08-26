package com.zombachu.stick.paper

import com.zombachu.stick.ContextualValue
import com.zombachu.stick.impl.SenderScope
import org.bukkit.command.CommandSender

fun <O : CommandSender, T> SenderScope<BukkitEnvironment, O>.permissionedValue(
    permission: String,
    default: T,
    fallback: T
): ContextualValue<BukkitEnvironment, O, T> = {
    if (sender.hasPermission(permission)) default else fallback
}
