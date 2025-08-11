package com.zombachu.stick

import com.zombachu.stick.impl.ExecutionContextImpl
import com.zombachu.stick.impl.SenderScope

interface ExecutionContext<S> : SenderScope<S> {
    val sender: S
    val label: String
    val args: List<String>

    fun <T : Any> get(id: TypedIdentifier<T>): T
    fun <T : Any> getOrPut(id: TypedIdentifier<T>, defaultValue: () -> T): T

    companion object {
        operator fun <S> invoke(sender: S, label: String, args: List<String>): ExecutionContext<S> {
            return ExecutionContextImpl(sender, label, args)
        }
    }
}
