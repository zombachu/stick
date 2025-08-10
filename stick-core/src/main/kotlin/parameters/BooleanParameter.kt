package com.zombachu.stick.parameters

import com.zombachu.stick.ExecutionContext
import com.zombachu.stick.ParsingResult
import com.zombachu.stick.TypedIdentifier
import com.zombachu.stick.impl.Parameter

open class BooleanParameter<S>(
    id: TypedIdentifier<Boolean>,
    description: String,
) : Parameter.Size1<S, Boolean>(id, description) {

    override fun parse(context: ExecutionContext<S>, arg0: String): ParsingResult<out Boolean> {
        val bool = arg0.toBooleanStrictOrNull() ?: return ParsingResult.failType()
        return ParsingResult.success(bool)
    }
}
