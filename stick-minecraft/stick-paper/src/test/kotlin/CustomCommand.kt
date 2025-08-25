package com.zombachu.stick.paper

import com.zombachu.stick.Command
import com.zombachu.stick.ExecutionContext
import com.zombachu.stick.ExecutionResult
import com.zombachu.stick.structure.command
import com.zombachu.stick.structure.invoke
import com.zombachu.stick.structure.stringParameter
import org.bukkit.command.CommandSender

class CustomCommand : Command<CustomBukkitContext, CommandSender> {
    override val structure = command("hi")(
        ExecutionContext<CustomBukkitContext, CommandSender>::doSomething,
        stringParameter("no")
    )

    override fun createSenderContext(sender: Any): CustomBukkitContext {
        return CustomBukkitContext(sender)
    }
}

private fun ExecutionContext<CustomBukkitContext, CommandSender>.doSomething(string: String): ExecutionResult {
    this.sender.sendMessage(string)
    return ExecutionResult.success()
}


class CustomBukkitContext(sender: Any) : BukkitContextImpl(sender) {
    fun translateMessage(string: String): String {
        TODO()
    }
}
