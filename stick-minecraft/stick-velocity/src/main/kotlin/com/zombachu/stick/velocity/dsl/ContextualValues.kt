package com.zombachu.stick.velocity.dsl

import com.velocitypowered.api.command.CommandSource
import com.zombachu.stick.ContextualValue
import com.zombachu.stick.StructureScope
import com.zombachu.stick.success
import com.zombachu.stick.velocity.VelocityEnvironment

fun <E : VelocityEnvironment, S : CommandSource, T> StructureScope<E, S>.permissionedValue(
    permission: String,
    default: T,
    fallback: T,
): ContextualValue<E, S, T> = { success(if (sender.hasPermission(permission)) default else fallback) }
