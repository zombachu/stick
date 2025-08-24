package com.zombachu.stick.paper

import com.zombachu.stick.ExecutionContext
import com.zombachu.stick.Result
import com.zombachu.stick.SenderContext
import com.zombachu.stick.TypedIdentifier
import com.zombachu.stick.element.Parameter
import com.zombachu.stick.impl.SenderScope
import com.zombachu.stick.impl.StructureElement
import com.zombachu.stick.structure.id
import org.bukkit.entity.Player

class PlayerParameter<O, S : BukkitContext<O>>(
    id: TypedIdentifier<Player>,
    description: String,
) : Parameter.Size1<O, S, Player>(id, description) {

    override fun parse(context: ExecutionContext<O, S>, arg0: String): Result<Player> {
        TODO("Not yet implemented")
    }
}

fun <O, S : BukkitContext<O>> SenderScope<O, S>.playerParameter(
    id: TypedIdentifier<Player>,
    description: String = "",
): StructureElement<O, S, PlayerParameter<O, S>> = {
    PlayerParameter(
        id,
        description,
    )
}
fun <O, S : BukkitContext<O>> SenderScope<O, S>.playerParameter(
    name: String,
    description: String = "",
): StructureElement<O, S, PlayerParameter<O, S>> = playerParameter(id(name), description)
