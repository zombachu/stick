package com.zombachu.stick.element

import com.zombachu.stick.CommandResult
import com.zombachu.stick.ContextualValue
import com.zombachu.stick.Environment
import com.zombachu.stick.Invocation
import com.zombachu.stick.Requirement
import com.zombachu.stick.SenderValidator

sealed interface GatedDefault<in E : Environment, S, out T> : SenderValidator<E, S> {
    val value: ContextualValue<E, S, T>
}

@PublishedApi
internal class GatedDefaultImpl<E : Environment, S, T>(
    override val value: ContextualValue<E, S, T>,
    private val requirement: Requirement<E, S>,
) : GatedDefault<E, S, T> {
    context(inv: Invocation<E, S>)
    override fun validateSender(): CommandResult<Unit> = requirement.validateSender()
}
