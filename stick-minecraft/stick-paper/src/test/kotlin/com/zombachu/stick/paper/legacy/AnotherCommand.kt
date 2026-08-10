package com.zombachu.stick.paper.legacy

import com.zombachu.stick.element.parameters.by
import com.zombachu.stick.paper.BukkitCommand
import com.zombachu.stick.structure.command
import com.zombachu.stick.structure.enumParameter
import com.zombachu.stick.structure.invoke
import com.zombachu.stick.structure.structure
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
