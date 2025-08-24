package com.zombachu.stick.paper

import com.zombachu.stick.ContextualValue
import com.zombachu.stick.impl.SenderScope
import org.bukkit.command.CommandSender

fun <O : CommandSender, S : BukkitContext<O>, T> SenderScope<O, S>.permissionedValue(
    permission: String,
    default: T,
    fallback: T
): ContextualValue<O, S, T> = {
    if (senderContext.sender.hasPermission(permission)) default else fallback
}
