package com.zombachu.stick.element.parameters

import com.zombachu.stick.ExecutionContext
import com.zombachu.stick.ParsingResult
import com.zombachu.stick.Result
import com.zombachu.stick.SenderContext
import com.zombachu.stick.TypedIdentifier
import com.zombachu.stick.element.ElementType
import com.zombachu.stick.element.Parameter
import com.zombachu.stick.impl.Size

open class UnboundedStringParameter<S : SenderContext>(
    id: TypedIdentifier<String>,
    description: String,
) : Parameter.UnknownSize<S, String>(Size.Unbounded, id, description) {

    override val type: ElementType = ElementType.Passthrough

    override fun parse(context: ExecutionContext<S>, args: List<String>): Result<String> {
        return ParsingResult.success(args.joinToString(" "))
    }
}
