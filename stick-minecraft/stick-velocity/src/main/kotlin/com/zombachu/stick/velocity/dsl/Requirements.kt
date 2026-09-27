package com.zombachu.stick.velocity.dsl

import com.velocitypowered.api.command.CommandSource
import com.zombachu.stick.CommandResult
import com.zombachu.stick.Invocation
import com.zombachu.stick.Requirement
import com.zombachu.stick.StructureScope
import com.zombachu.stick.dsl.requirement
import com.zombachu.stick.failPermission
import com.zombachu.stick.velocity.VelocityEnvironment

fun <E : VelocityEnvironment, S : CommandSource> StructureScope<E, S>.permission(
    permission: String,
    failureResult: Invocation<E, S>.() -> CommandResult.Failure = { failPermission() },
): Requirement<E, S> = requirement(failureResult) { sender.hasPermission(permission) }
