package com.zombachu.stick.element.parameters

import com.zombachu.stick.Invocation
import com.zombachu.stick.ParsingResult
import com.zombachu.stick.Result
import com.zombachu.stick.Environment
import com.zombachu.stick.TypedIdentifier
import com.zombachu.stick.element.ElementType
import com.zombachu.stick.element.Parameter

open class EnumParameter<E : Environment, O, T : Enum<T>>(
    id: TypedIdentifier<T>,
    description: String,
    val primaryValues: Map<String, T>,
    val aliasedValues: Map<String, T>,
) : Parameter.Size1<E, O, T>(id, description) {

    override val type: ElementType = ElementType.Literal

    context(env: E, inv: Invocation<E, O>)
    override fun parse(arg0: String): Result<out T> {
        val enumValue = primaryValues[arg0] ?: aliasedValues[arg0] ?: return ParsingResult.failLiteral(primaryValues.keys.toList(), arg0)
        return ParsingResult.success(enumValue)
    }

    context(env: E)
    override fun getSyntax(): String = "<${primaryValues.keys.joinToString("|")}>"
}
