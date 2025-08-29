package com.zombachu.stick.impl

import com.zombachu.stick.ContextualValue
import com.zombachu.stick.Environment
import com.zombachu.stick.Result
import com.zombachu.stick.ValidationContext
import com.zombachu.stick.element.SenderValidator

class ValidatedDefault<E : Environment, S, T : Any>(
    val value: ContextualValue<E, S, T>,
    val validate: context(ValidationContext<E, S>) () -> Result<Unit>,
): SenderValidator<E, S> {
    context(validationContext: ValidationContext<E, S>)
    override fun validateSender(): Result<Unit> = this.validate()
}
