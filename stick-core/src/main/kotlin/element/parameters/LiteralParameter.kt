package com.zombachu.stick.element.parameters

import com.zombachu.stick.Aliasable
import com.zombachu.stick.ExecutionContext
import com.zombachu.stick.ParsingResult
import com.zombachu.stick.Result
import com.zombachu.stick.TypedIdentifier
import com.zombachu.stick.element.ElementType
import com.zombachu.stick.element.Parameter

open class LiteralParameter<S>(
    id: TypedIdentifier<String>,
    override val aliases: Set<String>,
    description: String,
) : Parameter.Size1<S, String>(id, description), Aliasable {

    override val label by id
    override val type: ElementType = ElementType.Literal

    override fun parse(context: ExecutionContext<S>, arg0: String): Result<String> {
        if (!matches(arg0.lowercase())) {
            return ParsingResult.failType()
        }
        return ParsingResult.success(arg0)
    }
}
