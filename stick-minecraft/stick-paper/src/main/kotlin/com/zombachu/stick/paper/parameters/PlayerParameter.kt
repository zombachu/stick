package com.zombachu.stick.paper.parameters

import com.zombachu.stick.CommandResult
import com.zombachu.stick.Invocation
import com.zombachu.stick.TypedIdentifier
import com.zombachu.stick.element.Parameter
import com.zombachu.stick.paper.BukkitEnvironment
import org.bukkit.entity.Player

class PlayerParameter<S : Any>(
    id: TypedIdentifier<Player>,
    description: String,
) : Parameter.Size1<BukkitEnvironment, S, Player>(id, description) {

    context(inv: Invocation<BukkitEnvironment, S>)
    override fun parse(arg0: String): CommandResult<Player> {
        inv.env.server.onlinePlayers
        TODO("Not yet implemented")
    }
}
