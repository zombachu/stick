package com.zombachu.stick.paper

import com.zombachu.stick.CommandResult
import com.zombachu.stick.Invocation
import com.zombachu.stick.feedback.FailureHandler
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.command.CommandSender

interface BukkitFailureHandler<E : BukkitEnvironment> : FailureHandler<E, CommandSender>

open class BasicBukkitFailureHandler : BukkitFailureHandler<BukkitEnvironment> {
    context(inv: Invocation<BukkitEnvironment, CommandSender>)
    override fun onFailure(failure: CommandResult.Failure<*>) {
        val message = failure.feedback.format()
        if (message.isEmpty()) { return }
        inv.sender.sendMessage(Component.text(message, NamedTextColor.RED))
    }
}
