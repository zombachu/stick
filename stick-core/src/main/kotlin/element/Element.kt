package com.zombachu.stick.element

import com.zombachu.stick.Aliasable
import com.zombachu.stick.GroupResult
import com.zombachu.stick.Invocation
import com.zombachu.stick.Result
import com.zombachu.stick.Environment
import com.zombachu.stick.TypedIdentifier
import com.zombachu.stick.impl.Size

sealed interface Element<E : Environment, O, out T> {
    val size: Size
    val type: ElementType
}

sealed interface SyntaxElement<E : Environment, O, out T : Any> : Element<E, O, T> {
    val id: TypedIdentifier<out T>
    val description: String

    context(env: E, inv: Invocation<E, O>)
    fun parse(args: List<String>): Result<out T>

    context(env: E)
    fun getSyntax(): String
}

sealed interface SignatureConstraint<E : Environment, O, out T> : Element<E, O, T> {
    sealed interface NonTerminating<E : Environment, O, out T> : SignatureConstraint<E, O, T>, Terminating<E, O, T>
    sealed interface Terminating<E : Environment, O, out T> : SignatureConstraint<E, O, T>
}

sealed interface Groupable<E : Environment, O, T : Any> : SyntaxElement<E, O, T> {

    context(env: E)
    fun getGroupedSyntax(): String = id.name
}

sealed interface Helper<E : Environment, O, T : Any> : SignatureConstraint.NonTerminating<E, O, T>
sealed interface Flag<E : Environment, O, T : Any> : SyntaxElement<E, O, T>, SignatureConstraint.NonTerminating<E, O, T>
sealed interface Group<E : Environment, O> : SyntaxElement<E, O, GroupResult<*>>, SignatureConstraint.Terminating<E, O, GroupResult<*>>
sealed interface Structure<E : Environment, O> : Groupable<E, O, Unit>, SignatureConstraint.Terminating<E, O, Unit>, Aliasable
sealed interface ValidatedParameter<E : Environment, O, T : Any> : Groupable<E, O, T>
