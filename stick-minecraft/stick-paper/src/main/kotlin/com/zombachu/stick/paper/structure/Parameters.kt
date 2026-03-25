package com.zombachu.stick.paper.structure

import com.zombachu.stick.impl.BuilderScope
import com.zombachu.stick.impl.StructureElement
import com.zombachu.stick.paper.BukkitEnvironment
import com.zombachu.stick.paper.parameters.PlayerParameter
import org.bukkit.command.CommandSender

fun <E : BukkitEnvironment, S : CommandSender> BuilderScope<E, S>.playerParameter(
    name: String,
    description: String = "",
): StructureElement<E, S, PlayerParameter<E, S>> = {
    PlayerParameter(name, description)
}
