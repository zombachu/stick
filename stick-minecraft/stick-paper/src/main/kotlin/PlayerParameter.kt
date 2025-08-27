package com.zombachu.stick.paper

import com.zombachu.stick.Invocation
import com.zombachu.stick.Result
import com.zombachu.stick.TypedIdentifier
import com.zombachu.stick.element.Parameter
import com.zombachu.stick.impl.SenderScope
import com.zombachu.stick.impl.StructureElement
import com.zombachu.stick.structure.id
import org.bukkit.entity.Player

class PlayerParameter<S : Any>(
    id: TypedIdentifier<Player>,
    description: String,
) : Parameter.Size1<BukkitEnvironment, S, Player>(id, description) {

    context(inv: Invocation<BukkitEnvironment, S>)
    override fun parse(arg0: String): Result<out Player> {
        inv.env.server.onlinePlayers
        TODO("Not yet implemented")
    }
}

fun <E : BukkitEnvironment, S : Any> SenderScope<E, S>.playerParameter(
    id: TypedIdentifier<Player>,
    description: String = "",
): StructureElement<E, S, PlayerParameter<S>> = {
    PlayerParameter(
        id,
        description,
    )
}
fun <E : BukkitEnvironment, S : Any> SenderScope<E, S>.playerParameter(
    name: String,
    description: String = "",
): StructureElement<E, S, PlayerParameter<S>> = playerParameter(id(name), description)
