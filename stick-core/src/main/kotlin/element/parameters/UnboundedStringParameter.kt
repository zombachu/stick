package com.zombachu.stick.element.parameters

import com.zombachu.stick.ExecutionContext
import com.zombachu.stick.ParsingResult
import com.zombachu.stick.Result
import com.zombachu.stick.SenderContext
import com.zombachu.stick.TypedIdentifier
import com.zombachu.stick.element.ElementType
import com.zombachu.stick.element.Parameter
import com.zombachu.stick.impl.Size

open class UnboundedStringParameter<O, S : SenderContext<O>>(
    id: TypedIdentifier<String>,
    description: String,
) : Parameter.UnknownSize<O, S, String>(Size.Unbounded, id, description) {

    override val type: ElementType = ElementType.Passthrough

    override fun parse(context: ExecutionContext<O, S>, args: List<String>): Result<String> {
        return ParsingResult.success(args.joinToString(" "))
    }
}
