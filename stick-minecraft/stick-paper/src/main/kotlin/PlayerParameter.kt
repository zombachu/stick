package com.zombachu.stick.paper

import com.zombachu.stick.Invocation
import com.zombachu.stick.Result
import com.zombachu.stick.TypedIdentifier
import com.zombachu.stick.element.Parameter
import com.zombachu.stick.impl.SenderScope
import com.zombachu.stick.impl.StructureElement
import com.zombachu.stick.structure.id
import org.bukkit.entity.Player

class PlayerParameter<O : Any>(
    id: TypedIdentifier<Player>,
    description: String,
) : Parameter.Size1<BukkitEnvironment, O, Player>(id, description) {

    context(env: BukkitEnvironment, inv: Invocation<BukkitEnvironment, O>)
    override fun parse(arg0: String): Result<out Player> {
        env.server.onlinePlayers
        TODO("Not yet implemented")
    }
}

fun <O : Any> SenderScope<BukkitEnvironment, O>.playerParameter(
    id: TypedIdentifier<Player>,
    description: String = "",
): StructureElement<BukkitEnvironment, O, PlayerParameter<O>> = {
    PlayerParameter(
        id,
        description,
    )
}
fun <O : Any> SenderScope<BukkitEnvironment, O>.playerParameter(
    name: String,
    description: String = "",
): StructureElement<BukkitEnvironment, O, PlayerParameter<O>> = playerParameter(id(name), description)
