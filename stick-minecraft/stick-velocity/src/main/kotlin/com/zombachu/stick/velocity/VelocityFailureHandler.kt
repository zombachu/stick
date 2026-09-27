package com.zombachu.stick.velocity

import com.velocitypowered.api.command.CommandSource
import com.zombachu.stick.Execution
import com.zombachu.stick.failure.FailureHandler
import com.zombachu.stick.failure.FailureOrigin
import com.zombachu.stick.failure.Reason
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import org.slf4j.Logger

interface VelocityFailureHandler<E : VelocityEnvironment> : FailureHandler<E, CommandSource>

open class BasicVelocityFailureHandler(private val logger: Logger) : VelocityFailureHandler<VelocityEnvironment> {
    context(ex: Execution<VelocityEnvironment, CommandSource>)
    override fun onFailure(reason: Reason, origin: FailureOrigin) {
        if (reason is Reason.Unknown && reason.cause != null) {
            logger.error("Command /${ex.label} threw", reason.cause)
        }
        val message = reason.message(origin)
        if (message.isEmpty()) {
            return
        }
        ex.sender.sendMessage(Component.text(message, NamedTextColor.RED))
    }
}
