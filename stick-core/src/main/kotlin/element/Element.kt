package com.zombachu.stick.element

import com.zombachu.stick.Aliasable
import com.zombachu.stick.GroupResult
import com.zombachu.stick.Invocation
import com.zombachu.stick.Result
import com.zombachu.stick.Environment
import com.zombachu.stick.TypedIdentifier
import com.zombachu.stick.impl.Size

sealed interface Element<S : Environment, O, out T> {
    val size: Size
    val type: ElementType
}

sealed interface SyntaxElement<S : Environment, O, out T : Any> : Element<S, O, T> {
    val id: TypedIdentifier<out T>
    val description: String

    context(env: S, inv: Invocation<S, O>)
    fun parse(args: List<String>): Result<out T>

    context(env: S)
    fun getSyntax(): String
}

sealed interface SignatureConstraint<S : Environment, O, out T> : Element<S, O, T> {
    sealed interface NonTerminating<S : Environment, O, out T> : SignatureConstraint<S, O, T>, Terminating<S, O, T>
    sealed interface Terminating<S : Environment, O, out T> : SignatureConstraint<S, O, T>
}

sealed interface Groupable<S : Environment, O, T : Any> : SyntaxElement<S, O, T> {

    context(env: S)
    fun getGroupedSyntax(): String = id.name
}

sealed interface Helper<S : Environment, O, T : Any> : SignatureConstraint.NonTerminating<S, O, T>
sealed interface Flag<S : Environment, O, T : Any> : SyntaxElement<S, O, T>, SignatureConstraint.NonTerminating<S, O, T>
sealed interface Group<S : Environment, O> : SyntaxElement<S, O, GroupResult<*>>, SignatureConstraint.Terminating<S, O, GroupResult<*>>
sealed interface Structure<S : Environment, O> : Groupable<S, O, Unit>, SignatureConstraint.Terminating<S, O, Unit>, Aliasable
sealed interface ValidatedParameter<S : Environment, O, T : Any> : Groupable<S, O, T>
