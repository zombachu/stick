package com.zombachu.stick.element.parameters

import com.zombachu.stick.CommandResult
import com.zombachu.stick.Environment
import com.zombachu.stick.Invocation
import com.zombachu.stick.ParsingResult
import com.zombachu.stick.TypedIdentifier
import com.zombachu.stick.element.ElementType
import com.zombachu.stick.element.Parameter
import com.zombachu.stick.impl.Size

open class TextParameter<E : Environment, S>(
    id: TypedIdentifier<String>,
    description: String,
) : Parameter.UnknownSize<E, S, String>(Size.Unbounded, id, description) {

    override val type: ElementType = ElementType.Passthrough

    context(inv: Invocation<E, S>)
    override fun parse(args: List<String>): CommandResult<String> {
        return ParsingResult.success(args.joinToString(" "))
    }
}
