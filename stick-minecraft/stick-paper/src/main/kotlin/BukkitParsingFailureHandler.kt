package com.zombachu.stick.paper

import com.zombachu.stick.ExecutionContext
import com.zombachu.stick.Result
import com.zombachu.stick.feedback.ParsingFailureHandler
import org.bukkit.command.CommandSender

class BukkitParsingFailureHandler : ParsingFailureHandler<BukkitContext, CommandSender> {
    override fun onFailure(context: ExecutionContext<BukkitContext, CommandSender>, result: Result.Failure<*>) {
        val message = result.feedback().format()
        if (message.isEmpty()) {
            return
        }
        // TODO: Cast in context
        (context.senderContext.sender as CommandSender).sendMessage(message)
    }
}
