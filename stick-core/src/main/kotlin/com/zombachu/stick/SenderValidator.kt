package com.zombachu.stick

interface SenderValidator<in E : Environment, S> {
    context(validationContext: ValidationContext<E, S>)
    fun validateSender(): CommandResult<Unit>
}
