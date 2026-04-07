package com.zombachu.stick.paper.structure

import com.zombachu.stick.impl.StructureScope
import com.zombachu.stick.paper.BukkitEnvironment
import com.zombachu.stick.paper.parameters.PlayerParameter
import org.bukkit.command.CommandSender

fun <E : BukkitEnvironment, S : CommandSender> StructureScope<E, S>.playerParameter(
    name: String,
    description: String = "",
): PlayerParameter<E, S> = PlayerParameter(name, description)
