package com.zombachu.stick.element

import com.zombachu.stick.CommandResult
import com.zombachu.stick.ContextualValue
import com.zombachu.stick.Environment
import com.zombachu.stick.Invocation
import com.zombachu.stick.SenderValidator
import com.zombachu.stick.success

sealed interface ValidatedDefault<in E : Environment, S, out T> : SenderValidator<E, S> {
    val value: ContextualValue<E, S, T>
}

sealed interface ValidSenderDefault<in E : Environment, S, out T> : ValidatedDefault<E, S, T>

sealed interface InvalidSenderDefault<in E : Environment, S, out T> : ValidatedDefault<E, S, T>

@PublishedApi
internal class ValidatedDefaultImpl<E : Environment, S, T>(
    override val value: ContextualValue<E, S, T>,
    private val validate: context(Invocation<E, S>) () -> CommandResult<Unit> = { success() },
) : ValidSenderDefault<E, S, T>, InvalidSenderDefault<E, S, T> {
    context(inv: Invocation<E, S>)
    override fun validateSender(): CommandResult<Unit> = this.validate()
}
