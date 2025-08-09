package com.zombachu.stick.parameters

import com.zombachu.stick.ExecutionContext
import com.zombachu.stick.ParsingResult
import com.zombachu.stick.TypedIdentifier
import com.zombachu.stick.impl.ElementType
import com.zombachu.stick.impl.Parameter

open class EnumParameter<S, T : Enum<T>>(
    id: TypedIdentifier<T>,
    description: String,
    val primaryValues: Map<String, T>,
    val aliasedValues: Map<String, T>,
) : Parameter.Size1<S, T>(id, description) {

    override val type: ElementType = ElementType.Literal

    override fun parse(context: ExecutionContext<S>, arg0: String): ParsingResult<out T> {
        val enumValue = primaryValues[arg0] ?: aliasedValues[arg0] ?: return ParsingResult.failType()
        return ParsingResult.success(enumValue)
    }

    override fun getSyntax(sender: S): String = "<${primaryValues.keys.joinToString("|")}>"
}
