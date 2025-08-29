package com.zombachu.stick.velocity

import com.velocitypowered.api.command.CommandSource
import com.zombachu.stick.Invocation
import com.zombachu.stick.Result
import com.zombachu.stick.feedback.ParsingFailureHandler
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer

class VelocityParsingFailureHandler : ParsingFailureHandler<VelocityEnvironment, CommandSource> {

    private val textComponentSerializer: PlainTextComponentSerializer = PlainTextComponentSerializer.plainText()

    override fun onFailure(inv: Invocation<VelocityEnvironment, CommandSource>, result: Result.Failure<*>) {
        val message = result.feedback().format()
        if (message.isEmpty()) {
            return
        }
        inv.sender.sendMessage(textComponentSerializer.deserialize(message))
    }
}
