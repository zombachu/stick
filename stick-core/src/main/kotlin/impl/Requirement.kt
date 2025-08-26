package com.zombachu.stick.impl

import com.zombachu.stick.Result
import com.zombachu.stick.Environment
import com.zombachu.stick.SenderValidationResult
import com.zombachu.stick.element.SenderValidator
import com.zombachu.stick.propagateError

class Requirement<E : Environment, S> internal constructor(validate: (env: E) -> Result<Unit>) : SenderValidator<E, S> {

    private val validations: MutableList<(env: E) -> Result<Unit>> = mutableListOf(validate)

    context(env: E)
    override fun validateSender(): Result<Unit> {
        validations.forEach { it(env).propagateError { return it } }
        return SenderValidationResult.success()
    }

    operator fun plus(other: Requirement<E, S>): Requirement<E, S> {
        validations += other.validations
        return this
    }
}
