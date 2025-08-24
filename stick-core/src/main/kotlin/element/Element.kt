package com.zombachu.stick.element

import com.zombachu.stick.Aliasable
import com.zombachu.stick.ExecutionContext
import com.zombachu.stick.GroupResult
import com.zombachu.stick.Result
import com.zombachu.stick.SenderContext
import com.zombachu.stick.TypedIdentifier
import com.zombachu.stick.impl.Size

sealed interface Element<S : SenderContext, out T> {
    val size: Size
    val type: ElementType
}

sealed interface SyntaxElement<S : SenderContext, out T : Any> : Element<S, T> {
    val id: TypedIdentifier<out T>
    val description: String
    fun parse(context: ExecutionContext<S>, args: List<String>): Result<out T>
    fun getSyntax(senderContext: S): String
}

sealed interface SignatureConstraint<S : SenderContext, out T> : Element<S, T> {
    sealed interface NonTerminating<S : SenderContext, out T> : SignatureConstraint<S, T>, Terminating<S, T>
    sealed interface Terminating<S : SenderContext, out T> : SignatureConstraint<S, T>
}

sealed interface Groupable<S : SenderContext, T : Any> : SyntaxElement<S, T> {
    fun getGroupedSyntax(senderContext: S): String = id.name
}

sealed interface Helper<S : SenderContext, T : Any> : SignatureConstraint.NonTerminating<S, T>
sealed interface Flag<S : SenderContext, T : Any> : SyntaxElement<S, T>, SignatureConstraint.NonTerminating<S, T>
sealed interface Group<S : SenderContext> : SyntaxElement<S, GroupResult<*>>, SignatureConstraint.Terminating<S, GroupResult<*>>
sealed interface Structure<S : SenderContext> : Groupable<S, Unit>, SignatureConstraint.Terminating<S, Unit>, Aliasable
sealed interface ValidatedParameter<S : SenderContext, T : Any> : Groupable<S, T>
