package com.zombachu.stick.element.parameters

import com.zombachu.stick.CommandResult
import com.zombachu.stick.Environment
import com.zombachu.stick.Invocation
import com.zombachu.stick.ParsingResult
import com.zombachu.stick.TypedIdentifier
import com.zombachu.stick.element.Parameter

open class BooleanParameter<E : Environment, S>(
    id: TypedIdentifier<Boolean>,
    description: String,
) : Parameter.Size1<E, S, Boolean>(id, description) {

    context(inv: Invocation<E, S>)
    override fun parse(arg0: String): CommandResult<Boolean> {
        val bool = arg0.toBooleanStrictOrNull() ?: return ParsingResult.failType("boolean", arg0)
        return ParsingResult.success(bool)
    }
}
