package com.zombachu.stick.paper.dsl

import com.zombachu.stick.ContextualValue
import com.zombachu.stick.ParsingResult
import com.zombachu.stick.StructureScope
import com.zombachu.stick.paper.BukkitEnvironment
import org.bukkit.command.CommandSender

fun <E : BukkitEnvironment, S : CommandSender, T> StructureScope<E, S>.permissionedValue(
    permission: String,
    default: T,
    fallback: T,
): ContextualValue<E, S, T> = { ParsingResult.success(if (sender.hasPermission(permission)) default else fallback) }
