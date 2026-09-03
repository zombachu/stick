package com.zombachu.stick

class Requirement<E : Environment, S>
private constructor(private val validations: List<(env: ValidationContext<E, S>) -> CommandResult<Unit>>) :
    SenderValidator<E, S> {

    @PublishedApi
    internal constructor(validate: (env: ValidationContext<E, S>) -> CommandResult<Unit>) : this([validate])

    context(validationContext: ValidationContext<E, S>)
    override fun validateSender(): CommandResult<Unit> {
        validations.forEach {
            it(validationContext).propagateError {
                return it
            }
        }
        return SenderValidationResult.success()
    }

    operator fun plus(other: Requirement<E, S>): Requirement<E, S> = Requirement(validations + other.validations)
}
