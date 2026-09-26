package com.zombachu.stick.paper

import com.zombachu.stick.Invocation
import com.zombachu.stick.failure.FailureHandler
import com.zombachu.stick.failure.Reason
import java.util.logging.Level
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.command.CommandSender

interface BukkitFailureHandler<E : BukkitEnvironment> : FailureHandler<E, CommandSender>

open class BasicBukkitFailureHandler : BukkitFailureHandler<BukkitEnvironment> {
    context(inv: Invocation<BukkitEnvironment, CommandSender>)
    override fun onFailure(reason: Reason) {
        if (reason is Reason.Unknown && reason.cause != null) {
            inv.env.plugin.logger.log(Level.SEVERE, "Command /${inv.label} threw", reason.cause)
        }
        val message = reason.message()
        if (message.isEmpty()) {
            return
        }
        inv.sender.sendMessage(Component.text(message, NamedTextColor.RED))
    }
}
