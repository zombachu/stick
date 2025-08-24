package com.zombachu.stick.paper

import com.zombachu.stick.ContextualValue
import com.zombachu.stick.impl.SenderScope
import org.bukkit.command.CommandSender

fun <O : CommandSender, T> SenderScope<O, BukkitContext>.permissionedValue(
    permission: String,
    default: T,
    fallback: T
): ContextualValue<O, BukkitContext, T> = {
    // TODO: Cast in context
    if ((senderContext.sender as CommandSender).hasPermission(permission)) default else fallback
}
