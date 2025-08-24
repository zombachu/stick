package com.zombachu.stick.impl

import com.zombachu.stick.ContextualValue
import com.zombachu.stick.Result
import com.zombachu.stick.SenderContext
import com.zombachu.stick.element.SenderValidator

class ValidatedDefault<S : SenderContext, O, T : Any>(
    val value: ContextualValue<S, O, T>,
    val validate: (S) -> Result<Unit>,
): SenderValidator<S, O> {
    override fun validateSender(senderContext: S): Result<Unit> = this.validate(senderContext)
}
