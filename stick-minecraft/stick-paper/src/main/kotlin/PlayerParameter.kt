package com.zombachu.stick.paper

import com.zombachu.stick.ExecutionContext
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
) : Parameter.Size1<BukkitContext, O, Player>(id, description) {

    context(senderContext: BukkitContext, executionContext: ExecutionContext<BukkitContext, O>)
    override fun parse(arg0: String): Result<out Player> {
        senderContext.server.onlinePlayers
        TODO("Not yet implemented")
    }
}

fun <O : Any> SenderScope<BukkitContext, O>.playerParameter(
    id: TypedIdentifier<Player>,
    description: String = "",
): StructureElement<BukkitContext, O, PlayerParameter<O>> = {
    PlayerParameter(
        id,
        description,
    )
}
fun <O : Any> SenderScope<BukkitContext, O>.playerParameter(
    name: String,
    description: String = "",
): StructureElement<BukkitContext, O, PlayerParameter<O>> = playerParameter(id(name), description)
