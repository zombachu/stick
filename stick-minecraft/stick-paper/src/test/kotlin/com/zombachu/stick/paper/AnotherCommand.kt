package com.zombachu.stick.paper

import com.zombachu.stick.element.parameters.by
import com.zombachu.stick.structure.command
import com.zombachu.stick.structure.enumParameter
import com.zombachu.stick.structure.invoke
import org.bukkit.command.CommandSender

class AnotherCommand : BukkitCommand<CommandSender> {

    override val structure = command("testingEnums")(
        enumParameter("someEnum", listOf(
            Block.Dirt by "dirt",
            Block.Grass by "grass" + setOf("mycellium", "podzol"),
        ))
    )
}

private enum class Block {
    Dirt, Grass, Stone
}
