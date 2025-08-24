package com.zombachu.stick.impl

import com.zombachu.stick.ContextualValue
import com.zombachu.stick.Result
import com.zombachu.stick.SenderContext
import com.zombachu.stick.element.SenderValidator

class ValidatedDefault<O, S : SenderContext, T : Any>(
    val value: ContextualValue<O, S, T>,
    val validate: (S) -> Result<Unit>,
): SenderValidator<O, S> {
    override fun validateSender(senderContext: S): Result<Unit> = this.validate(senderContext)
}
