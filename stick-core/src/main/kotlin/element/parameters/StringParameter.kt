package com.zombachu.stick.element.parameters

import com.zombachu.stick.ExecutionContext
import com.zombachu.stick.ParsingResult
import com.zombachu.stick.Result
import com.zombachu.stick.SenderContext
import com.zombachu.stick.TypedIdentifier
import com.zombachu.stick.element.ElementType
import com.zombachu.stick.element.Parameter

open class StringParameter<S : SenderContext, O>(
    id: TypedIdentifier<String>,
    description: String,
) : Parameter.Size1<S, O, String>(id, description) {

    override val type: ElementType = ElementType.Passthrough

    context(senderContext: S, executionContext: ExecutionContext<S, O>)
    override fun parse(arg0: String): Result<out String> {
        return ParsingResult.success(arg0)
    }
}
