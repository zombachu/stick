package com.zombachu.stick.paper

import com.zombachu.stick.Execution
import com.zombachu.stick.failure.FailureHandler
import com.zombachu.stick.failure.FailureOrigin
import com.zombachu.stick.failure.Reason
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.command.CommandSender
import java.util.logging.Level

interface PaperFailureHandler<E : PaperEnvironment> : FailureHandler<E, CommandSender>

open class BasicPaperFailureHandler : PaperFailureHandler<PaperEnvironment> {
    context(ex: Execution<PaperEnvironment, CommandSender>)
    override fun onFailure(reason: Reason, origin: FailureOrigin) {
        if (reason is Reason.Unknown && reason.cause != null) {
            ex.env.plugin.logger.log(Level.SEVERE, "Command /${ex.label} threw", reason.cause)
        }
        val message = reason.message(origin)
        if (message.isEmpty()) {
            return
        }
        ex.sender.sendMessage(Component.text(message, NamedTextColor.RED))
    }
}
