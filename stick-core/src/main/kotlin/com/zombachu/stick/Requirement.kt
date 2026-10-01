package com.zombachu.stick

class Requirement<E : Environment, S>
private constructor(private val validations: List<Invocation<E, S>.() -> CommandResult<Unit>>) : SenderValidator<E, S> {

    @PublishedApi internal constructor(validate: Invocation<E, S>.() -> CommandResult<Unit>) : this([validate])

    context(inv: Invocation<E, S>)
    override fun validateSender(): CommandResult<Unit> {
        validations.forEach {
            it(inv).propagateFailure {
                return it
            }
        }
        return success()
    }

    operator fun plus(other: Requirement<E, S>): Requirement<E, S> = Requirement(validations + other.validations)
}
