package com.zombachu.stick

interface SenderContext<S> {
    val sender: S

    // TODO: Make internal
    fun <S2> forSender(transform: (S) -> S2): SenderContext<S2>
}
