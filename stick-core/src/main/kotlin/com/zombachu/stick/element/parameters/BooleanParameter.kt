package com.zombachu.stick.element.parameters

import com.zombachu.stick.CommandResult
import com.zombachu.stick.Environment
import com.zombachu.stick.ParsingResult
import com.zombachu.stick.Suggestion
import com.zombachu.stick.ValidationContext
import com.zombachu.stick.element.Parameter
import com.zombachu.stick.toSuggestions

open class BooleanParameter<E : Environment, S>(name: String, description: String) :
    Parameter.Size1<E, S, Boolean>(name, description) {

    context(validationContext: ValidationContext<E, S>)
    override fun suggest(preceding: List<String>, partial: String): List<Suggestion> = ["true", "false"].toSuggestions()

    context(validationContext: ValidationContext<E, S>)
    override fun resolve(arg0: String): CommandResult<Boolean> {
        val bool = arg0.lowercase().toBooleanStrictOrNull() ?: return ParsingResult.failType("boolean", arg0)
        return ParsingResult.success(bool)
    }
}
