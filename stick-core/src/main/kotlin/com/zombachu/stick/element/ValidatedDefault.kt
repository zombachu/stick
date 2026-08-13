package com.zombachu.stick.element

import com.zombachu.stick.CommandResult
import com.zombachu.stick.ContextualValue
import com.zombachu.stick.Environment
import com.zombachu.stick.SenderValidator
import com.zombachu.stick.ValidationContext

sealed interface ValidatedDefault<in E : Environment, S, out T> : SenderValidator<E, S> {
    val value: ContextualValue<E, S, T>
}

sealed interface ValidSenderDefault<in E : Environment, S, out T> : ValidatedDefault<E, S, T>

sealed interface InvalidSenderDefault<in E : Environment, S, out T> : ValidatedDefault<E, S, T>

internal class ValidatedDefaultImpl<E : Environment, S, T>(
    override val value: ContextualValue<E, S, T>,
    private val validate: context(ValidationContext<E, S>) () -> CommandResult<Unit>,
) : ValidSenderDefault<E, S, T>, InvalidSenderDefault<E, S, T> {
    context(validationContext: ValidationContext<E, S>)
    override fun validateSender(): CommandResult<Unit> = this.validate()
}
