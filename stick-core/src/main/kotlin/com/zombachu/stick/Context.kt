package com.zombachu.stick

interface Environment

interface SenderContext<S> {
    val sender: S
}

typealias ContextualValue<E, S, T> = Invocation<E, S>.() -> CommandResult<T>
