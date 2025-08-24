package com.zombachu.stick

interface SenderContext {
    val sender: Any

    fun forSender(sender: Any): SenderContext
}

fun <S : SenderContext, O : Any, O2 : Any> SenderContext.transformSender(transform: (O) -> O2): S {
    return this.forSender(transform(this.sender as O)) as S // TODO: Handle safer
}
