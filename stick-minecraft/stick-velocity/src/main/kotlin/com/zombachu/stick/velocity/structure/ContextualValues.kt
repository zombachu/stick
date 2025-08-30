package com.zombachu.stick.velocity.structure

import com.velocitypowered.api.command.CommandSource
import com.zombachu.stick.ContextualValue
import com.zombachu.stick.impl.BuilderScope
import com.zombachu.stick.velocity.VelocityEnvironment

fun <E : VelocityEnvironment, S : CommandSource, T> BuilderScope<E, S>.permissionedValue(
    permission: String,
    default: T,
    fallback: T
): ContextualValue<E, S, T> = {
    if (sender.hasPermission(permission)) default else fallback
}
