package com.zombachu.stick.paper.structure

import com.zombachu.stick.TypedIdentifier
import com.zombachu.stick.impl.BuilderScope
import com.zombachu.stick.impl.StructureElement
import com.zombachu.stick.paper.BukkitEnvironment
import com.zombachu.stick.paper.parameters.PlayerParameter
import com.zombachu.stick.structure.id
import org.bukkit.entity.Player

fun <E : BukkitEnvironment, S : Any> BuilderScope<E, S>.playerParameter(
    id: TypedIdentifier<Player>,
    description: String = "",
): StructureElement<E, S, PlayerParameter<S>> = {
    PlayerParameter(id, description)
}
fun <E : BukkitEnvironment, S : Any> BuilderScope<E, S>.playerParameter(
    name: String,
    description: String = "",
): StructureElement<E, S, PlayerParameter<S>> = playerParameter(id(name), description)
