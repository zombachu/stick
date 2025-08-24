package com.zombachu.stick.element

import com.zombachu.stick.Aliasable
import com.zombachu.stick.ExecutionContext
import com.zombachu.stick.GroupResult
import com.zombachu.stick.Result
import com.zombachu.stick.SenderContext
import com.zombachu.stick.TypedIdentifier
import com.zombachu.stick.impl.Size

sealed interface Element<O, S : SenderContext, out T> {
    val size: Size
    val type: ElementType
}

sealed interface SyntaxElement<O, S : SenderContext, out T : Any> : Element<O, S, T> {
    val id: TypedIdentifier<out T>
    val description: String
    fun parse(context: ExecutionContext<O, S>, args: List<String>): Result<out T>
    fun getSyntax(senderContext: S): String
}

sealed interface SignatureConstraint<O, S : SenderContext, out T> : Element<O, S, T> {
    sealed interface NonTerminating<O, S : SenderContext, out T> : SignatureConstraint<O, S, T>, Terminating<O, S, T>
    sealed interface Terminating<O, S : SenderContext, out T> : SignatureConstraint<O, S, T>
}

sealed interface Groupable<O, S : SenderContext, T : Any> : SyntaxElement<O, S, T> {
    fun getGroupedSyntax(senderContext: S): String = id.name
}

sealed interface Helper<O, S : SenderContext, T : Any> : SignatureConstraint.NonTerminating<O, S, T>
sealed interface Flag<O, S : SenderContext, T : Any> : SyntaxElement<O, S, T>, SignatureConstraint.NonTerminating<O, S, T>
sealed interface Group<O, S : SenderContext> : SyntaxElement<O, S, GroupResult<*>>, SignatureConstraint.Terminating<O, S, GroupResult<*>>
sealed interface Structure<O, S : SenderContext> : Groupable<O, S, Unit>, SignatureConstraint.Terminating<O, S, Unit>, Aliasable
sealed interface ValidatedParameter<O, S : SenderContext, T : Any> : Groupable<O, S, T>
