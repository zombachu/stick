package com.zombachu.stick.paper.parameters

import com.zombachu.stick.CommandResult
import com.zombachu.stick.Invocation
import com.zombachu.stick.element.Parameter
import com.zombachu.stick.paper.PaperEnvironment
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class PlayerParameter<E : PaperEnvironment, S : CommandSender>(name: String, description: String) :
    Parameter.Size1<E, S, Player>(name, description) {

    context(inv: Invocation<E, S>)
    override fun resolve(arg0: String): CommandResult<Player> {
        inv.env.server.onlinePlayers
        TODO("Not yet implemented")
    }
}
