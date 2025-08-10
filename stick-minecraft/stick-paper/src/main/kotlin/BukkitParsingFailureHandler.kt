package com.zombachu.stick.paper

import com.zombachu.stick.ParsingResult
import com.zombachu.stick.feedback.ParsingFailureHandler
import org.bukkit.command.CommandSender

class BukkitParsingFailureHandler : ParsingFailureHandler<CommandSender> {
    override fun handleFailure(sender: CommandSender, result: ParsingResult.Failure<*>) {
        val message = result.feedback.format()
        if (message.isEmpty()) {
            return
        }
        sender.sendMessage(message)
    }
}
