package com.zombachu.stick.impl

import com.zombachu.stick.Result
import com.zombachu.stick.SenderContext
import com.zombachu.stick.SenderValidationResult
import com.zombachu.stick.element.SenderValidator
import com.zombachu.stick.propagateError

class Requirement<S : SenderContext, O> internal constructor(validate: (senderContext: S) -> Result<Unit>) : SenderValidator<S, O> {

    private val validations: MutableList<(senderContext: S) -> Result<Unit>> = mutableListOf(validate)

    context(senderContext: S)
    override fun validateSender(): Result<Unit> {
        validations.forEach { it(senderContext).propagateError { return it } }
        return SenderValidationResult.success()
    }

    operator fun plus(other: Requirement<S, O>): Requirement<S, O> {
        validations += other.validations
        return this
    }
}
