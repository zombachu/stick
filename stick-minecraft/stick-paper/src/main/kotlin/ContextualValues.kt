package com.zombachu.stick.paper

import com.zombachu.stick.ContextualValue
import com.zombachu.stick.impl.SenderScope
import org.bukkit.command.CommandSender

fun <O : CommandSender, T> SenderScope<BukkitContext, O>.permissionedValue(
    permission: String,
    default: T,
    fallback: T
): ContextualValue<BukkitContext, O, T> = {
    if (sender.hasPermission(permission)) default else fallback
}
