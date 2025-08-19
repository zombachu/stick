package com.zombachu.stick.element.parameters

import com.zombachu.stick.ExecutionContext
import com.zombachu.stick.ParsingResult
import com.zombachu.stick.Result
import com.zombachu.stick.TypedIdentifier
import com.zombachu.stick.element.Parameter

open class BooleanParameter<S>(
    id: TypedIdentifier<Boolean>,
    description: String,
) : Parameter.Size1<S, Boolean>(id, description) {

    override fun parse(context: ExecutionContext<S>, arg0: String): Result<Boolean> {
        val bool = arg0.toBooleanStrictOrNull() ?: return ParsingResult.failType("boolean", arg0)
        return ParsingResult.success(bool)
    }
}
