package com.zombachu.stick.paper

import com.zombachu.stick.ContextualValue
import com.zombachu.stick.impl.SenderScope
import org.bukkit.command.CommandSender

fun <S : CommandSender, T> SenderScope<BukkitEnvironment, S>.permissionedValue(
    permission: String,
    default: T,
    fallback: T
): ContextualValue<BukkitEnvironment, S, T> = {
    if (sender.hasPermission(permission)) default else fallback
}
