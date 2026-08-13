package com.zombachu.stick.paper.dsl

import com.zombachu.stick.StructureScope
import com.zombachu.stick.paper.BukkitEnvironment
import com.zombachu.stick.paper.parameters.PlayerParameter
import org.bukkit.command.CommandSender

fun <E : BukkitEnvironment, S : CommandSender> StructureScope<E, S>.playerParameter(
    name: String,
    description: String = "",
): PlayerParameter<E, S> = PlayerParameter(name, description)
