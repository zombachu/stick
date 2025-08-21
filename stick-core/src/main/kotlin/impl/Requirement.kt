package com.zombachu.stick.impl

import com.zombachu.stick.Result
import com.zombachu.stick.SenderValidationResult
import com.zombachu.stick.element.SenderValidator
import com.zombachu.stick.propagateError

class Requirement<S> internal constructor(validate: (S) -> Result<Unit>) : SenderValidator<S> {

    private val validations: MutableList<(S) -> Result<Unit>> = mutableListOf(validate)

    override fun validateSender(sender: S): Result<Unit> {
        validations.forEach { it(sender).propagateError { return it } }
        return SenderValidationResult.success()
    }

    operator fun plus(other: Requirement<S>): Requirement<S> {
        validations += other.validations
        return this
    }
}
