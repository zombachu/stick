package com.zombachu.stick.element.parameters

import com.zombachu.stick.Environment
import com.zombachu.stick.Invocation
import com.zombachu.stick.ParsingResult
import com.zombachu.stick.Result
import com.zombachu.stick.TypedIdentifier
import com.zombachu.stick.element.Parameter
import java.util.*

open class UUIDParameter<E : Environment, S>(
    id: TypedIdentifier<UUID>,
    description: String,
) : Parameter.Size1<E, S, UUID>(id, description) {

    context(inv: Invocation<E, S>)
    override fun parse(arg0: String): Result<out UUID> {
        try {
            val uuid = UUID.fromString(arg0)
            return ParsingResult.success(uuid)
        } catch (_: IllegalArgumentException) {
            return ParsingResult.failType("UUID", arg0)
        }
    }
}
