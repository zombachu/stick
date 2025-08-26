package com.zombachu.stick.impl

import com.zombachu.stick.ContextualValue
import com.zombachu.stick.Result
import com.zombachu.stick.Environment
import com.zombachu.stick.element.SenderValidator

class ValidatedDefault<E : Environment, S, T : Any>(
    val value: ContextualValue<E, S, T>,
    val validate: context(E) () -> Result<Unit>,
): SenderValidator<E, S> {
    context(env: E)
    override fun validateSender(): Result<Unit> = this.validate()
}
