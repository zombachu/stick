package com.zombachu.stick.paper

import com.zombachu.stick.ExecutionContext
import com.zombachu.stick.Result
import com.zombachu.stick.TypedIdentifier
import com.zombachu.stick.id
import com.zombachu.stick.impl.Parameter
import com.zombachu.stick.impl.SenderScope
import com.zombachu.stick.impl.StructureElement
import org.bukkit.entity.Player

class PlayerParameter<S>(
    id: TypedIdentifier<Player>,
    description: String,
) : Parameter.Size1<S, Player>(id, description) {

    override fun parse(context: ExecutionContext<S>, arg0: String): Result<Player> {
        TODO("Not yet implemented")
    }
}

fun <S> SenderScope<S>.playerParameter(
    id: TypedIdentifier<Player>,
    description: String = "",
): StructureElement<S, PlayerParameter<S>> = {
    PlayerParameter(
        id,
        description,
    )
}
fun <S> SenderScope<S>.playerParameter(
    name: String,
    description: String = "",
): StructureElement<S, PlayerParameter<S>> = playerParameter(id(name), description)
