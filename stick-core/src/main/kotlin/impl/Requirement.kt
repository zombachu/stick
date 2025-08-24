package com.zombachu.stick.impl

import com.zombachu.stick.Result
import com.zombachu.stick.SenderContext
import com.zombachu.stick.SenderValidationResult
import com.zombachu.stick.element.SenderValidator
import com.zombachu.stick.propagateError

class Requirement<S : SenderContext> internal constructor(validate: (senderContext: S) -> Result<Unit>) : SenderValidator<S> {

    private val validations: MutableList<(senderContext: S) -> Result<Unit>> = mutableListOf(validate)

    override fun validateSender(senderContext: S): Result<Unit> {
        validations.forEach { it(senderContext).propagateError { return it } }
        return SenderValidationResult.success()
    }

    operator fun plus(other: Requirement<S>): Requirement<S> {
        validations += other.validations
        return this
    }
}
