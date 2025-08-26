package com.zombachu.stick

interface Environment {
    val sender: Any

    fun forSender(sender: Any): Environment
}

fun <E : Environment, O : Any, O2 : Any> Environment.transformSender(transform: (O) -> O2): E {
    return this.forSender(transform(this.sender as O)) as E // TODO: Handle safer
}
