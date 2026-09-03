package com.zombachu.stick.velocity

import com.velocitypowered.api.command.CommandSource
import com.zombachu.stick.CommandResult
import com.zombachu.stick.Invocation
import com.zombachu.stick.feedback.FailureHandler
import com.zombachu.stick.feedback.Feedback
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import org.slf4j.Logger

interface VelocityFailureHandler<E : VelocityEnvironment> : FailureHandler<E, CommandSource>

open class BasicVelocityFailureHandler(private val logger: Logger) : VelocityFailureHandler<VelocityEnvironment> {
    context(inv: Invocation<VelocityEnvironment, CommandSource>)
    override fun <F : Feedback> onFailure(failure: CommandResult.Failure<F>) {
        val feedback = failure.feedback
        if (feedback is Feedback.Unknown && feedback.cause != null) {
            logger.error("Command /${inv.label} threw", feedback.cause)
        }
        val message = feedback.message
        if (message.isEmpty()) {
            return
        }
        inv.sender.sendMessage(Component.text(message, NamedTextColor.RED))
    }
}
