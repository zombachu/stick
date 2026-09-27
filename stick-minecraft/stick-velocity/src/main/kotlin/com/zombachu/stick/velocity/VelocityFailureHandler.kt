package com.zombachu.stick.velocity

import com.velocitypowered.api.command.CommandSource
import com.zombachu.stick.Invocation
import com.zombachu.stick.failure.FailureHandler
import com.zombachu.stick.failure.FailureOrigin
import com.zombachu.stick.failure.Reason
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import org.slf4j.Logger

interface VelocityFailureHandler<E : VelocityEnvironment> : FailureHandler<E, CommandSource>

open class BasicVelocityFailureHandler(private val logger: Logger) : VelocityFailureHandler<VelocityEnvironment> {
    context(inv: Invocation<VelocityEnvironment, CommandSource>)
    override fun onFailure(reason: Reason, origin: FailureOrigin) {
        if (reason is Reason.Unknown && reason.cause != null) {
            logger.error("Command /${inv.label} threw", reason.cause)
        }
        val message = reason.message(origin)
        if (message.isEmpty()) {
            return
        }
        inv.sender.sendMessage(Component.text(message, NamedTextColor.RED))
    }
}
