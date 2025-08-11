package com.zombachu.stick.parameters

import com.zombachu.stick.ExecutionContext
import com.zombachu.stick.ParsingResult
import com.zombachu.stick.Result
import com.zombachu.stick.TypedIdentifier
import com.zombachu.stick.impl.Parameter
import java.util.*

open class UUIDParameter<S>(
    id: TypedIdentifier<UUID>,
    description: String,
) : Parameter.Size1<S, UUID>(id, description) {

    override fun parse(context: ExecutionContext<S>, arg0: String): Result<UUID> {
        try {
            val uuid = UUID.fromString(arg0)
            return ParsingResult.success(uuid)
        } catch (_: IllegalArgumentException) {
            return ParsingResult.failType()
        }
    }
}
