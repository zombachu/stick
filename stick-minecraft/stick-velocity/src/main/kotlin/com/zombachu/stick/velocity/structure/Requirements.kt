package com.zombachu.stick.velocity.structure

import com.velocitypowered.api.command.CommandSource
import com.zombachu.stick.impl.Requirement
import com.zombachu.stick.impl.SenderScope
import com.zombachu.stick.structure.requirement
import com.zombachu.stick.velocity.VelocityEnvironment

// TODO: Permission message
fun <E : VelocityEnvironment, S : CommandSource> SenderScope<E, S>.permission(
    permission: String
): Requirement<E, S> = requirement {
    it.sender.hasPermission(permission)
}
