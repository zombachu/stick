package com.zombachu.stick.paper.dsl

import com.zombachu.stick.ContextualValue
import com.zombachu.stick.StructureScope
import com.zombachu.stick.paper.BukkitEnvironment
import com.zombachu.stick.success
import org.bukkit.command.CommandSender

fun <E : BukkitEnvironment, S : CommandSender, T> StructureScope<E, S>.permissionedValue(
    permission: String,
    value: T,
    fallback: T,
): ContextualValue<E, S, T> = { success(if (sender.hasPermission(permission)) value else fallback) }
