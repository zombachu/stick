package com.zombachu.stick.element

import com.zombachu.stick.CommandResult
import com.zombachu.stick.Environment
import com.zombachu.stick.SenderValidator
import com.zombachu.stick.ValidationContext
import com.zombachu.stick.success

context(validationContext: ValidationContext<E, S>)
internal fun <E : Environment, S, T> Element<E, S, T>.validateSender(): CommandResult<Unit> {
    return if (this !is SenderValidator<*, *>) {
        success()
    } else {
        @Suppress("UNCHECKED_CAST") (this as SenderValidator<E, S>).validateSender()
    }
}

internal fun unusedValue(): Nothing {
    throw NotImplementedError("This shouldn't be called")
}
