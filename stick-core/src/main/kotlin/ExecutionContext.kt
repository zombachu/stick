package com.zombachu.stick

import com.zombachu.stick.impl.SenderScope

interface ExecutionContext<S> : SenderScope<S> {
    val sender: S
    val label: String
    val args: List<String>

    fun <T : Any> get(id: TypedIdentifier<T>): T
    fun <T : Any> getOrPut(id: TypedIdentifier<T>, defaultValue: () -> T): T
}
