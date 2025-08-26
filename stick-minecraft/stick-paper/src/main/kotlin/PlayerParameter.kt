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

    context(env: BukkitEnvironment, inv: Invocation<BukkitEnvironment, S>)
    override fun parse(arg0: String): Result<out Player> {
        env.server.onlinePlayers
        TODO("Not yet implemented")
    }
}

fun <S : Any> SenderScope<BukkitEnvironment, S>.playerParameter(
    id: TypedIdentifier<Player>,
    description: String = "",
): StructureElement<BukkitEnvironment, S, PlayerParameter<S>> = {
    PlayerParameter(
        id,
        description,
    )
}
fun <S : Any> SenderScope<BukkitEnvironment, S>.playerParameter(
    name: String,
    description: String = "",
): StructureElement<BukkitEnvironment, S, PlayerParameter<S>> = playerParameter(id(name), description)
