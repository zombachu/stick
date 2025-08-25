package com.zombachu.stick

import com.zombachu.stick.element.Structure
import com.zombachu.stick.impl.ExecutionContextImpl
import com.zombachu.stick.impl.SenderScope

// TODO: Shouldn't extend platformcontext
interface ExecutionContext<S : SenderContext, O> : SenderScope<S, O> {
    val sender: O
    val senderContext: S
    val label: String
    val args: List<String>

    fun <T : Any> get(id: TypedIdentifier<T>): T
    fun <T : Any> getOrPut(id: TypedIdentifier<T>, defaultValue: () -> T): T
    fun getSyntax(): String

    companion object {
        operator fun <S : SenderContext, O> invoke(senderContext: S, label: String, args: List<String>, structure: Structure<S, O>): ExecutionContext<S, O> {
            return ExecutionContextImpl(senderContext, label, args, structure, parent = null)
        }
    }
}
