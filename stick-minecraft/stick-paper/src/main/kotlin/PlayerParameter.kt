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
) : Parameter.Size1<O, BukkitContext, Player>(id, description) {

    override fun parse(context: ExecutionContext<O, BukkitContext>, arg0: String): Result<Player> {
        TODO("Not yet implemented")
    }
}

fun <O : Any> SenderScope<O, BukkitContext>.playerParameter(
    id: TypedIdentifier<Player>,
    description: String = "",
): StructureElement<O, BukkitContext, PlayerParameter<O>> = {
    PlayerParameter(
        id,
        description,
    )
}
fun <O : Any> SenderScope<O, BukkitContext>.playerParameter(
    name: String,
    description: String = "",
): StructureElement<O, BukkitContext, PlayerParameter<O>> = playerParameter(id(name), description)
