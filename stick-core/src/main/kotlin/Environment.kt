package com.zombachu.stick

interface Environment {
    val sender: Any

    fun forSender(sender: Any): Environment
}

fun <E : Environment, S : Any, S2 : Any> Environment.transformSender(transform: (S) -> S2): E {
    return this.forSender(transform(this.sender as S)) as E // TODO: Handle safer
}
