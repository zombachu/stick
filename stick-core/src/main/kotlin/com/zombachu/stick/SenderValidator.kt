package com.zombachu.stick

interface SenderValidator<in E : Environment, S> {
    context(inv: Invocation<E, S>)
    fun validateSender(): CommandResult<Unit>
}
