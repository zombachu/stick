package com.zombachu.stick.paper.structure

import com.zombachu.stick.CommandResult
import com.zombachu.stick.SenderValidationResult
import com.zombachu.stick.impl.BuilderScope
import com.zombachu.stick.impl.Requirement
import com.zombachu.stick.paper.BukkitEnvironment
import com.zombachu.stick.structure.requirement
import org.bukkit.command.CommandSender

fun <E : BukkitEnvironment, S : CommandSender> BuilderScope<E, S>.permission(
    permission: String,
    failureResult: () -> CommandResult.Failure = SenderValidationResult::failPermission,
): Requirement<E, S> = requirement(failureResult) {
    it.sender.hasPermission(permission)
}
