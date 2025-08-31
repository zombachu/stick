package com.zombachu.stick.paper

import com.zombachu.stick.Invocation
import com.zombachu.stick.Result
import com.zombachu.stick.feedback.FailureHandler
import org.bukkit.command.CommandSender

interface BukkitFailureHandler<E : BukkitEnvironment> : FailureHandler<E, CommandSender>

open class BasicBukkitFailureHandler : BukkitFailureHandler<BukkitEnvironment> {
    override fun onFailure(inv: Invocation<BukkitEnvironment, CommandSender>, failure: Result.Failure<*>) {
        val message = failure.feedback.format()
        if (message.isEmpty()) {
            return
        }
        inv.sender.sendMessage(message)
    }
}
