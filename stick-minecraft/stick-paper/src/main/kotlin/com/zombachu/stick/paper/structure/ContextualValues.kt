package com.zombachu.stick.paper.structure

import com.zombachu.stick.ContextualValue
import com.zombachu.stick.impl.BuilderScope
import com.zombachu.stick.paper.BukkitEnvironment
import org.bukkit.command.CommandSender

fun <E : BukkitEnvironment, S : CommandSender, T> BuilderScope<E, S>.permissionedValue(
    permission: String,
    default: T,
    fallback: T
): ContextualValue<E, S, T> = {
    if (sender.hasPermission(permission)) default else fallback
}
