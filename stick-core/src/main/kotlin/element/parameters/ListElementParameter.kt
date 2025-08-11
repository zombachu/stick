package com.zombachu.stick.element.parameters

import com.zombachu.stick.ContextualValue
import com.zombachu.stick.ExecutionContext
import com.zombachu.stick.ParsingResult
import com.zombachu.stick.Result
import com.zombachu.stick.TypedIdentifier
import com.zombachu.stick.element.Parameter

open class ListElementParameter<S, T : Any>(
    id: TypedIdentifier<T>,
    description: String,
    val list: ContextualValue<S, List<T>>,
) : Parameter.Size1<S, T>(id, description) {

    override fun parse(context: ExecutionContext<S>, arg0: String): Result<T> {
        val index = arg0.toIntOrNull() ?: return ParsingResult.failType()
        val list = list(context)

        if (list.isEmpty()) {
            // TODO: Give a better error
            return ParsingResult.failUnknown()
        }

        // If the given number is not in the valid range then give the sender an error
        if (index !in 0..list.size) {
            return ParsingResult.failArgument()
        }

        return ParsingResult.success(list[index])
    }
}
