package com.zombachu.stick.paper

import com.zombachu.stick.Invocation
import com.zombachu.stick.Result
import com.zombachu.stick.feedback.ParsingFailureHandler
import org.bukkit.command.CommandSender

class BukkitParsingFailureHandler : ParsingFailureHandler<BukkitEnvironment, CommandSender> {
    override fun onFailure(inv: Invocation<BukkitEnvironment, CommandSender>, result: Result.Failure<*>) {
        val message = result.feedback().format()
        if (message.isEmpty()) {
            return
        }
        inv.sender.sendMessage(message)
    }
}
