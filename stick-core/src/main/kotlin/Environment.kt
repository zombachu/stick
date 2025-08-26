package com.zombachu.stick

interface Environment {
    val sender: Any

    fun forSender(sender: Any): Environment
}

fun <S : Environment, O : Any, O2 : Any> Environment.transformSender(transform: (O) -> O2): S {
    return this.forSender(transform(this.sender as O)) as S // TODO: Handle safer
}
