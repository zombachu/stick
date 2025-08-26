package com.zombachu.stick.impl

import com.zombachu.stick.Result
import com.zombachu.stick.Environment
import com.zombachu.stick.SenderValidationResult
import com.zombachu.stick.element.SenderValidator
import com.zombachu.stick.propagateError

class Requirement<S : Environment, O> internal constructor(validate: (env: S) -> Result<Unit>) : SenderValidator<S, O> {

    private val validations: MutableList<(env: S) -> Result<Unit>> = mutableListOf(validate)

    context(env: S)
    override fun validateSender(): Result<Unit> {
        validations.forEach { it(env).propagateError { return it } }
        return SenderValidationResult.success()
    }

    operator fun plus(other: Requirement<S, O>): Requirement<S, O> {
        validations += other.validations
        return this
    }
}
