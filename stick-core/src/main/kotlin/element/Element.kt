package com.zombachu.stick.element

import com.zombachu.stick.Aliasable
import com.zombachu.stick.ExecutionContext
import com.zombachu.stick.GroupResult
import com.zombachu.stick.Result
import com.zombachu.stick.SenderContext
import com.zombachu.stick.TypedIdentifier
import com.zombachu.stick.impl.Size

sealed interface Element<S, out T> {
    val size: Size
    val type: ElementType
}

sealed interface SyntaxElement<S, out T : Any> : Element<S, T> {
    val id: TypedIdentifier<out T>
    val description: String
    fun parse(context: ExecutionContext<S>, args: List<String>): Result<out T>
    fun getSyntax(context: SenderContext<S>): String
}

sealed interface SignatureConstraint<S, out T> : Element<S, T> {
    sealed interface NonTerminating<S, out T> : SignatureConstraint<S, T>, Terminating<S, T>
    sealed interface Terminating<S, out T> : SignatureConstraint<S, T>
}

sealed interface Groupable<S, T : Any> : SyntaxElement<S, T> {
    fun getGroupedSyntax(context: SenderContext<S>): String = id.name
}

sealed interface Helper<S, T : Any> : SignatureConstraint.NonTerminating<S, T>
sealed interface Flag<S, T : Any> : SyntaxElement<S, T>, SignatureConstraint.NonTerminating<S, T>
sealed interface Group<S> : SyntaxElement<S, GroupResult<*>>, SignatureConstraint.Terminating<S, GroupResult<*>>
sealed interface Structure<S> : Groupable<S, Unit>, SignatureConstraint.Terminating<S, Unit>, Aliasable
sealed interface ValidatedParameter<S, T : Any> : Groupable<S, T>
