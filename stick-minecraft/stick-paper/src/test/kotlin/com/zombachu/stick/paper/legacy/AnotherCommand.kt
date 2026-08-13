package com.zombachu.stick.paper.legacy

import com.zombachu.stick.dsl.command
import com.zombachu.stick.dsl.enumParameter
import com.zombachu.stick.dsl.invoke
import com.zombachu.stick.dsl.structure
import com.zombachu.stick.element.parameters.by
import com.zombachu.stick.paper.BukkitCommand
import org.bukkit.command.CommandSender

class AnotherCommand : BukkitCommand<CommandSender> {

    override val structure = structure {
        command("testingEnums")(
            enumParameter("someEnum", [
                Block.Dirt by "dirt",
                Block.Grass by "grass" + ["mycellium", "podzol"],
            ])
        )
    }
}

enum class Block {
    Dirt, Grass, Stone
}
