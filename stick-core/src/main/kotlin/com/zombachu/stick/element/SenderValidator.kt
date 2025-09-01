package com.zombachu.stick.element

import com.zombachu.stick.CommandResult
import com.zombachu.stick.Environment
import com.zombachu.stick.SenderValidationResult
import com.zombachu.stick.ValidationContext

interface SenderValidator<E : Environment, S> {
    context(validationContext: ValidationContext<E, S>)
    fun validateSender(): CommandResult<Unit>
}

context(validationContext: ValidationContext<E, S>)
internal fun <E : Environment, S, T : Any> SyntaxElement<E, S, T>.validateSender(): CommandResult<Unit> {
    return if (this !is SenderValidator<*, *>) {
        SenderValidationResult.success()
    } else {
        @Suppress("UNCHECKED_CAST")
        (this as SenderValidator<E, S>).validateSender()
    }
}

internal fun unusedValue(): Nothing {
    throw NotImplementedError("This shouldn't be called")
}
