package com.zombachu.stick.element.parameters

import com.zombachu.stick.Aliasable
import com.zombachu.stick.CommandResult
import com.zombachu.stick.Environment
import com.zombachu.stick.ParsingResult
import com.zombachu.stick.ValidationContext
import com.zombachu.stick.element.ElementType
import com.zombachu.stick.element.Parameter

open class LiteralParameter<E : Environment, S>(name: String, override val aliases: Set<String>, description: String) :
    Parameter.Size1<E, S, String>(name, description), Aliasable {

    override val label: String = name.lowercase()
    override val type: ElementType = ElementType.Literal

    context(validationContext: ValidationContext<E, S>)
    override fun resolve(arg0: String): CommandResult<String> {
        if (!matches(arg0.lowercase())) {
            return ParsingResult.failLiteral([label], arg0)
        }
        return ParsingResult.success(arg0)
    }
}
