package com.zombachu.stick.paper.dsl

import com.zombachu.stick.CommandResult
import com.zombachu.stick.Invocation
import com.zombachu.stick.Requirement
import com.zombachu.stick.StructureScope
import com.zombachu.stick.dsl.requirement
import com.zombachu.stick.failPermission
import com.zombachu.stick.paper.PaperEnvironment
import org.bukkit.command.CommandSender

fun <E : PaperEnvironment, S : CommandSender> StructureScope<E, S>.permission(
    permission: String,
    failureResult: Invocation<E, S>.() -> CommandResult.Failure = { failPermission() },
): Requirement<E, S> = requirement({ sender.hasPermission(permission) }, failureResult)
