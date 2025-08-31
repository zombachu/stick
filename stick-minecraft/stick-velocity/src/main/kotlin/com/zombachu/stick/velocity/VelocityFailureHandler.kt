package com.zombachu.stick.velocity

import com.velocitypowered.api.command.CommandSource
import com.zombachu.stick.Invocation
import com.zombachu.stick.Result
import com.zombachu.stick.feedback.FailureHandler
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer

interface VelocityFailureHandler<E : VelocityEnvironment> : FailureHandler<E, CommandSource>

open class BasicVelocityFailureHandler : VelocityFailureHandler<VelocityEnvironment> {
    private val textComponentSerializer: PlainTextComponentSerializer = PlainTextComponentSerializer.plainText()

    context(inv: Invocation<VelocityEnvironment, CommandSource>)
    override fun onFailure(failure: Result.Failure<*>) {
        val message = failure.feedback.format()
        if (message.isEmpty()) { return }
        inv.sender.sendMessage(textComponentSerializer.deserialize(message))
    }
}
