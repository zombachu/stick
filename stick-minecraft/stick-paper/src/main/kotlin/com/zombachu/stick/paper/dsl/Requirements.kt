package com.zombachu.stick.paper.dsl

import com.zombachu.stick.CommandResult
import com.zombachu.stick.Requirement
import com.zombachu.stick.SenderValidationResult
import com.zombachu.stick.StructureScope
import com.zombachu.stick.dsl.requirement
import com.zombachu.stick.paper.BukkitEnvironment
import org.bukkit.command.CommandSender

fun <E : BukkitEnvironment, S : CommandSender> StructureScope<E, S>.permission(
    permission: String,
    failureResult: () -> CommandResult.Failure<*> = SenderValidationResult::failPermission,
): Requirement<E, S> = requirement(failureResult) { it.sender.hasPermission(permission) }
