package com.zombachu.stick.element

import com.zombachu.stick.Aliasable
import com.zombachu.stick.Environment
import com.zombachu.stick.GroupResult
import com.zombachu.stick.Invocation
import com.zombachu.stick.Result
import com.zombachu.stick.TypedIdentifier
import com.zombachu.stick.ValidationContext
import com.zombachu.stick.impl.Size

sealed interface Element<E : Environment, S, out T> {
    val size: Size
    val type: ElementType
}

sealed interface SyntaxElement<E : Environment, S, out T : Any> : Element<E, S, T> {
    val id: TypedIdentifier<out T>
    val description: String

    context(inv: Invocation<E, S>)
    fun parse(args: List<String>): Result<out T>

    context(validationContext: ValidationContext<E, S>)
    fun getSyntax(): String
}

sealed interface SignatureConstraint<E : Environment, S, out T> : Element<E, S, T> {
    sealed interface NonTerminating<E : Environment, S, out T> : SignatureConstraint<E, S, T>, Terminating<E, S, T>
    sealed interface Terminating<E : Environment, S, out T> : SignatureConstraint<E, S, T>
}

sealed interface Groupable<E : Environment, S, T : Any> : SyntaxElement<E, S, T> {

    context(validationContext: ValidationContext<E, S>)
    fun getGroupedSyntax(): String = id.name
}

sealed interface Helper<E : Environment, S, T : Any> : SignatureConstraint.NonTerminating<E, S, T>
sealed interface Flag<E : Environment, S, T : Any> : SyntaxElement<E, S, T>, SignatureConstraint.NonTerminating<E, S, T>
sealed interface Group<E : Environment, S> : SyntaxElement<E, S, GroupResult<*>>, SignatureConstraint.Terminating<E, S, GroupResult<*>>
sealed interface Structure<E : Environment, S> : Groupable<E, S, Unit>, SignatureConstraint.Terminating<E, S, Unit>, Aliasable
sealed interface ValidatedParameter<E : Environment, S, T : Any> : Groupable<E, S, T>
