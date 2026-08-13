package com.zombachu.stick.element

import com.zombachu.stick.CommandResult
import com.zombachu.stick.Environment
import com.zombachu.stick.SenderValidationResult
import com.zombachu.stick.SenderValidator
import com.zombachu.stick.ValidationContext

context(validationContext: ValidationContext<E, S>)
internal fun <E : Environment, S, T> SyntaxElement<E, S, T>.validateSender(): CommandResult<Unit> {
    return if (this !is SenderValidator<*, *>) {
        SenderValidationResult.success()
    } else {
        @Suppress("UNCHECKED_CAST") (this as SenderValidator<E, S>).validateSender()
    }
}

internal fun unusedValue(): Nothing {
    throw NotImplementedError("This shouldn't be called")
}
