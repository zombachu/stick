package com.zombachu.stick.velocity.legacy

import com.velocitypowered.api.command.CommandSource
import com.zombachu.stick.dsl.command
import com.zombachu.stick.dsl.id
import com.zombachu.stick.dsl.intParameter
import com.zombachu.stick.dsl.invoke
import com.zombachu.stick.dsl.nullableValueFlag
import com.zombachu.stick.dsl.optionallyNullable
import com.zombachu.stick.dsl.store
import com.zombachu.stick.dsl.stringParameter
import com.zombachu.stick.dsl.structure
import com.zombachu.stick.velocity.VelocityCommand

class ServerCommand : VelocityCommand<CommandSource> {

    val nullableIntId = id<Int?>("null")
    val nonNullIntId = id<Int>("notnull")

    override val structure = structure {
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
}
