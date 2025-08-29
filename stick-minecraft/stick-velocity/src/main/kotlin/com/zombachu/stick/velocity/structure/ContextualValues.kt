package com.zombachu.stick.velocity.structure

import com.velocitypowered.api.command.CommandSource
import com.zombachu.stick.ContextualValue
import com.zombachu.stick.impl.SenderScope
import com.zombachu.stick.velocity.VelocityEnvironment

fun <E : VelocityEnvironment, S : CommandSource, T> SenderScope<E, S>.permissionedValue(
    permission: String,
    default: T,
    fallback: T
): ContextualValue<E, S, T> = {
    if (sender.hasPermission(permission)) default else fallback
}
