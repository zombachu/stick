package com.zombachu.stick.impl

import com.zombachu.stick.ContextualValue
import com.zombachu.stick.Result
import com.zombachu.stick.SenderContext
import com.zombachu.stick.element.SenderValidator

class ValidatedDefault<S, T : Any>(
    val value: ContextualValue<S, T>,
    val validate: (SenderContext<S>) -> Result<Unit>,
): SenderValidator<S> {
    override fun validateSender(context: SenderContext<S>): Result<Unit> = this.validate(context)
}
