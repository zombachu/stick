package com.zombachu.stick.impl

import com.zombachu.stick.CommandResult
import com.zombachu.stick.ContextualValue
import com.zombachu.stick.Environment
import com.zombachu.stick.ValidationContext
import com.zombachu.stick.element.SenderValidator

class ValidatedDefault<E : Environment, S, T>(
    val value: ContextualValue<E, S, T>,
    val validate: context(ValidationContext<E, S>) () -> CommandResult<Unit>,
): SenderValidator<E, S> {
    context(validationContext: ValidationContext<E, S>)
    override fun validateSender(): CommandResult<Unit> = this.validate()
}
