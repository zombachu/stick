package com.zombachu.stick.impl

import com.zombachu.stick.ContextualValue
import com.zombachu.stick.Result
import com.zombachu.stick.element.SenderValidator

class ValidatedDefault<S, T : Any>(
    val value: ContextualValue<S, T>,
    val validate: (S) -> Result<Unit>,
): SenderValidator<S> {
    override fun validateSender(sender: S): Result<Unit> = this.validate(sender)
}
