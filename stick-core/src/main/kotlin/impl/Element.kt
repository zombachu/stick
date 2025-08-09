package com.zombachu.stick.impl

import com.zombachu.stick.ExecutionContext
import com.zombachu.stick.ParsingResult
import com.zombachu.stick.TypedIdentifier

sealed interface Element<S, out T> {
    val size: Size
    val type: ElementType
}

sealed interface SyntaxElement<S, out T : Any> : Element<S, T> {
    val id: TypedIdentifier<out T>
    val description: String
    fun parse(context: ExecutionContext<S>, args: List<String>): ParsingResult<out T>
    fun getSyntax(sender: S): String
}

sealed interface SignatureConstraint<S, out T> : Element<S, T> {
    sealed interface NonTerminating<S, out T> : SignatureConstraint<S, T>, Terminating<S, T>
    sealed interface Terminating<S, out T> : SignatureConstraint<S, T>
}

sealed interface Groupable<S, T : Any> : SyntaxElement<S, T> {
    fun getGroupedSyntax(sender: S): String = id.name
}
