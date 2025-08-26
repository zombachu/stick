package com.zombachu.stick.element.parameters

import com.zombachu.stick.Invocation
import com.zombachu.stick.ParsingResult
import com.zombachu.stick.Result
import com.zombachu.stick.Environment
import com.zombachu.stick.TypedIdentifier
import com.zombachu.stick.element.ElementType
import com.zombachu.stick.element.Parameter

open class StringParameter<E : Environment, S>(
    id: TypedIdentifier<String>,
    description: String,
) : Parameter.Size1<E, S, String>(id, description) {

    override val type: ElementType = ElementType.Passthrough

    context(env: E, inv: Invocation<E, S>)
    override fun parse(arg0: String): Result<out String> {
        return ParsingResult.success(arg0)
    }
}
