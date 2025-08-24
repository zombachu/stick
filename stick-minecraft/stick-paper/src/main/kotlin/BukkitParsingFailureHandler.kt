package com.zombachu.stick.paper

import com.zombachu.stick.ExecutionContext
import com.zombachu.stick.Result
import com.zombachu.stick.feedback.ParsingFailureHandler

class BukkitParsingFailureHandler : ParsingFailureHandler<BukkitContext> {
    override fun onFailure(context: ExecutionContext<BukkitContext>, result: Result.Failure<*>) {
        val message = result.feedback().format()
        if (message.isEmpty()) {
            return
        }
        context.senderContext.sender.sendMessage(message)
    }
}
