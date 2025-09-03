package com.zombachu.stick.velocity

import com.velocitypowered.api.command.CommandSource
import com.zombachu.stick.structure.command
import com.zombachu.stick.structure.intParameter
import com.zombachu.stick.structure.invoke
import com.zombachu.stick.structure.nullableValueFlag
import com.zombachu.stick.structure.optionallyNullable
import com.zombachu.stick.structure.stringParameter

class ServerCommand : VelocityCommand<CommandSource> {
    override val structure =
        command("test")(
            nullableValueFlag(
                name = "hey",
                parameter = intParameter("someInt"),
            ),
            optionallyNullable(stringParameter("someString"))
        ) { flag: Int?, optional: String? ->

        }

}
