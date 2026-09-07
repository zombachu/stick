package com.zombachu.stick.paper.parameters

import com.zombachu.stick.CommandResult
import com.zombachu.stick.ValidationContext
import com.zombachu.stick.element.Parameter
import com.zombachu.stick.paper.BukkitEnvironment
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class PlayerParameter<E : BukkitEnvironment, S : CommandSender>(name: String, description: String) :
    Parameter.Size1<E, S, Player>(name, description) {

    context(validationContext: ValidationContext<E, S>)
    override fun resolve(arg0: String): CommandResult<Player> {
        validationContext.env.server.onlinePlayers
        TODO("Not yet implemented")
    }
}
