package com.zombachu.stick

interface ValidationContext<E : Environment, S> : SenderContext<S> {
    val env: E

    fun <S2 : Any> forSender(transform: (S) -> S2): ValidationContext<E, S2>
}
