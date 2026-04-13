package com.zombachu.stick.impl

import com.zombachu.stick.CommandResult
import com.zombachu.stick.ContextualValue
import com.zombachu.stick.Environment
import com.zombachu.stick.ValidationContext
import com.zombachu.stick.element.SenderValidator

sealed interface ValidatedDefault<E : Environment, S, T> : SenderValidator<E, S> {
    val value: ContextualValue<E, S, T>
}

sealed interface ValidSenderDefault<E : Environment, S, T> : ValidatedDefault<E, S, T>

sealed interface InvalidSenderDefault<E : Environment, S, T> : ValidatedDefault<E, S, T>

internal class ValidatedDefaultImpl<E : Environment, S, T>(
    override val value: ContextualValue<E, S, T>,
    private val validate:
        context(ValidationContext<E, S>)
        () -> CommandResult<Unit>,
) : ValidSenderDefault<E, S, T>, InvalidSenderDefault<E, S, T> {
    context(validationContext: ValidationContext<E, S>)
    override fun validateSender(): CommandResult<Unit> = this.validate()
}
