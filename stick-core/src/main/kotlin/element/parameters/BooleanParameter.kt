package com.zombachu.stick.element.parameters

import com.zombachu.stick.ExecutionContext
import com.zombachu.stick.ParsingResult
import com.zombachu.stick.Result
import com.zombachu.stick.SenderContext
import com.zombachu.stick.TypedIdentifier
import com.zombachu.stick.element.Parameter

open class BooleanParameter<S : SenderContext, O>(
    id: TypedIdentifier<Boolean>,
    description: String,
) : Parameter.Size1<S, O, Boolean>(id, description) {

    context(senderContext: S, executionContext: ExecutionContext<S, O>)
    override fun parse(arg0: String): Result<out Boolean> {
        val bool = arg0.toBooleanStrictOrNull() ?: return ParsingResult.failType("boolean", arg0)
        return ParsingResult.success(bool)
    }
}
