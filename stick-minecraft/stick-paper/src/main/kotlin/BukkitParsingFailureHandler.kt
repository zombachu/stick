package com.zombachu.stick.paper

import com.zombachu.stick.Invocation
import com.zombachu.stick.Result
import com.zombachu.stick.feedback.ParsingFailureHandler
import org.bukkit.command.CommandSender

class BukkitParsingFailureHandler : ParsingFailureHandler<BukkitContext, CommandSender> {
    override fun onFailure(context: Invocation<BukkitContext, CommandSender>, result: Result.Failure<*>) {
        val message = result.feedback().format()
        if (message.isEmpty()) {
            return
        }
        context.sender.sendMessage(message)
    }
}
