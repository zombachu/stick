package com.zombachu.stick.element.parameters

import com.zombachu.stick.CommandResult
import com.zombachu.stick.Environment
import com.zombachu.stick.ParsingResult
import com.zombachu.stick.ValidationContext
import com.zombachu.stick.element.GroupableType
import com.zombachu.stick.element.Parameter

open class StringParameter<E : Environment, S>(name: String, description: String) :
    Parameter.Size1<E, S, String>(name, description) {

    override val type: GroupableType = GroupableType.Passthrough

    context(validationContext: ValidationContext<E, S>)
    override fun resolve(arg0: String): CommandResult<String> {
        return ParsingResult.success(arg0)
    }
}
