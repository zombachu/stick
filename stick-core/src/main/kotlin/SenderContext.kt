package com.zombachu.stick

interface SenderContext<out O> {
    val sender: O

    fun <O2> forSender(sender: O2): SenderContext<O2>
}

fun <O, O2, S2 : SenderContext<O2>> SenderContext<O>.transformSender(transform: (O) -> O2): S2 {
    return this.forSender(transform(this.sender)) as S2 // TODO: Handle safer
}
