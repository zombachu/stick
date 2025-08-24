package com.zombachu.stick.element.parameters

import com.zombachu.stick.ExecutionContext
import com.zombachu.stick.ParsingResult
import com.zombachu.stick.Result
import com.zombachu.stick.SenderContext
import com.zombachu.stick.TypedIdentifier
import com.zombachu.stick.element.Parameter
import java.util.*

open class UUIDParameter<S : SenderContext, O>(
    id: TypedIdentifier<UUID>,
    description: String,
) : Parameter.Size1<S, O, UUID>(id, description) {

    context(senderContext: S, executionContext: ExecutionContext<S, O>)
    override fun parse(arg0: String): Result<out UUID> {
        try {
            val uuid = UUID.fromString(arg0)
            return ParsingResult.success(uuid)
        } catch (_: IllegalArgumentException) {
            return ParsingResult.failType("UUID", arg0)
        }
    }
}
