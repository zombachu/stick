package com.zombachu.stick

internal class ValidationContextImpl<E : Environment, S>(override val env: E, override val sender: S) :
    ValidationContext<E, S>

internal fun <E : Environment, S, S2 : Any> ValidationContext<E, S>.forSender(
    transform: (S) -> S2
): ValidationContext<E, S2> =
    when (this) {
        is InvocationImpl -> forSender(transform)
        is ValidationContextImpl -> ValidationContextImpl(env, transform(sender))
    }
