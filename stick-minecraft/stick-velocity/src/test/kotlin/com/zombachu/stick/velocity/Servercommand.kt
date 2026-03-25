package com.zombachu.stick.velocity

import com.velocitypowered.api.command.CommandSource
import com.zombachu.stick.structure.command
import com.zombachu.stick.structure.id
import com.zombachu.stick.structure.intParameter
import com.zombachu.stick.structure.invoke
import com.zombachu.stick.structure.nullableValueFlag
import com.zombachu.stick.structure.optionallyNullable
import com.zombachu.stick.structure.store
import com.zombachu.stick.structure.stringParameter

class ServerCommand : VelocityCommand<CommandSource> {

    val nullableIntId = id<Int?>("null")
    val nonNullIntId = id<Int>("notnull")

    override val structure =
        command("test")(
            nullableValueFlag(
                "int",
                parameter = intParameter("intValue"),
            ).store(nullableIntId),
            optionallyNullable(stringParameter("someString"))
        ) { flag: Int?, optional: String? ->
            val result: Int? = this.get(nullableIntId)
        }

}
