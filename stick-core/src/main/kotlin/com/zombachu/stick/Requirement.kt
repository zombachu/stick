package com.zombachu.stick

class Requirement<E : Environment, S>
@PublishedApi
internal constructor(validate: (env: ValidationContext<E, S>) -> CommandResult<Unit>) : SenderValidator<E, S> {

    private val validations: MutableList<(env: ValidationContext<E, S>) -> CommandResult<Unit>> = [validate]

    context(validationContext: ValidationContext<E, S>)
    override fun validateSender(): CommandResult<Unit> {
        validations.forEach {
            it(validationContext).propagateError {
                return it
            }
        }
        return SenderValidationResult.success()
    }

    operator fun plus(other: Requirement<E, S>): Requirement<E, S> {
        validations += other.validations
        return this
    }
}
