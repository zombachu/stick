package com.zombachu.stick.element

import com.zombachu.stick.Aliasable
import com.zombachu.stick.ExecutionContext
import com.zombachu.stick.GroupResult
import com.zombachu.stick.Result
import com.zombachu.stick.SenderContext
import com.zombachu.stick.TypedIdentifier
import com.zombachu.stick.impl.Size

sealed interface Element<S : SenderContext, O, out T> {
    val size: Size
    val type: ElementType
}

sealed interface SyntaxElement<S : SenderContext, O, out T : Any> : Element<S, O, T> {
    val id: TypedIdentifier<out T>
    val description: String
    fun parse(context: ExecutionContext<S, O>, args: List<String>): Result<out T>
    fun getSyntax(senderContext: S): String
}

sealed interface SignatureConstraint<S : SenderContext, O, out T> : Element<S, O, T> {
    sealed interface NonTerminating<S : SenderContext, O, out T> : SignatureConstraint<S, O, T>, Terminating<S, O, T>
    sealed interface Terminating<S : SenderContext, O, out T> : SignatureConstraint<S, O, T>
}

sealed interface Groupable<S : SenderContext, O, T : Any> : SyntaxElement<S, O, T> {
    fun getGroupedSyntax(senderContext: S): String = id.name
}

sealed interface Helper<S : SenderContext, O, T : Any> : SignatureConstraint.NonTerminating<S, O, T>
sealed interface Flag<S : SenderContext, O, T : Any> : SyntaxElement<S, O, T>, SignatureConstraint.NonTerminating<S, O, T>
sealed interface Group<S : SenderContext, O> : SyntaxElement<S, O, GroupResult<*>>, SignatureConstraint.Terminating<S, O, GroupResult<*>>
sealed interface Structure<S : SenderContext, O> : Groupable<S, O, Unit>, SignatureConstraint.Terminating<S, O, Unit>, Aliasable
sealed interface ValidatedParameter<S : SenderContext, O, T : Any> : Groupable<S, O, T>
