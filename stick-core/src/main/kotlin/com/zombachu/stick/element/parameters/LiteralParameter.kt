package com.zombachu.stick.element.parameters

import com.zombachu.stick.Aliasable
import com.zombachu.stick.CommandResult
import com.zombachu.stick.Environment
import com.zombachu.stick.Invocation
import com.zombachu.stick.ParsingResult
import com.zombachu.stick.TypedIdentifier
import com.zombachu.stick.element.ElementType
import com.zombachu.stick.element.Parameter

open class LiteralParameter<E : Environment, S>(
    id: TypedIdentifier<String>,
    override val aliases: Set<String>,
    description: String,
) : Parameter.Size1<E, S, String>(id, description), Aliasable {

    override val label by id
    override val type: ElementType = ElementType.Literal

    context(inv: Invocation<E, S>)
    override fun parse(arg0: String): CommandResult<String> {
        if (!matches(arg0.lowercase())) {
            return ParsingResult.failLiteral(listOf(label), arg0)
        }
        return ParsingResult.success(arg0)
    }
}
