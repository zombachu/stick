package com.zombachu.stick.velocity.structure

import com.velocitypowered.api.command.CommandSource
import com.zombachu.stick.CommandResult
import com.zombachu.stick.SenderValidationResult
import com.zombachu.stick.impl.Requirement
import com.zombachu.stick.impl.StructureScope
import com.zombachu.stick.structure.requirement
import com.zombachu.stick.velocity.VelocityEnvironment

fun <E : VelocityEnvironment, S : CommandSource> StructureScope<E, S>.permission(
    permission: String,
    failureResult: () -> CommandResult.Failure<*> = SenderValidationResult::failPermission,
): Requirement<E, S> = requirement(failureResult) { it.sender.hasPermission(permission) }
