package com.zombachu.stick.element

import com.zombachu.stick.Aliasable
import com.zombachu.stick.CommandResult
import com.zombachu.stick.ContextualValue
import com.zombachu.stick.Environment
import com.zombachu.stick.GroupResult
import com.zombachu.stick.HybridFlagResult
import com.zombachu.stick.Invocation
import com.zombachu.stick.ValidationContext
import com.zombachu.stick.impl.Size

sealed interface Element<E : Environment, S, out T> {
    val size: Size
    val type: ElementType

    context(inv: Invocation<E, S>)
    fun parse(args: List<String>): CommandResult<T>
}

sealed interface SyntaxElement<E : Environment, S, out T> : Element<E, S, T> {
    val name: String
    val description: String

    context(validationContext: ValidationContext<E, S>)
    fun getSyntax(): String
}

sealed interface SignatureConstraint<E : Environment, S, out T> : Element<E, S, T> {
    sealed interface NonTerminating<E : Environment, S, out T> : SignatureConstraint<E, S, T>, Terminating<E, S, T>
    sealed interface Terminating<E : Environment, S, out T> : SignatureConstraint<E, S, T>
}

sealed interface Groupable<E : Environment, S, out T> : SyntaxElement<E, S, T>, SignatureConstraint<E, S, T> {
    context(validationContext: ValidationContext<E, S>)
    fun getGroupedSyntax(): String = name
 
    sealed interface NonTerminating<E : Environment, S, out T> : Groupable<E, S, T>, SignatureConstraint.NonTerminating<E, S, T>, Terminating<E, S, T>
    sealed interface Terminating<E : Environment, S, out T> : Groupable<E, S, T>, SignatureConstraint.Terminating<E, S, T>
}

sealed interface Helper<E : Environment, S, out T> : SignatureConstraint.NonTerminating<E, S, T>

sealed interface Flag<E : Environment, S, out T> : SyntaxElement<E, S, T> {
    val default: ContextualValue<E, S, T>
 
    sealed interface Validated<E : Environment, S, out T>: Flag<E, S, T>, SenderValidator<E, S> {
        val invalidDefault: ContextualValue<E, S, T>
    }
}
sealed interface ValueFlag<E : Environment, S, out T> : Flag<E, S, T>, SignatureConstraint.NonTerminating<E, S, T>
sealed interface HybridFlag<E : Environment, S, out T> : Flag<E, S, HybridFlagResult<T>>, SignatureConstraint.Terminating<E, S, HybridFlagResult<T>>
 
sealed interface Group<E : Environment, S, out G : GroupResult> : SyntaxElement<E, S, G>, SignatureConstraint<E, S, G> {
    sealed interface UnknownSize<E : Environment, S, out G : GroupResult> : Group<E, S, G>, SignatureConstraint.Terminating<E, S, G>
    sealed interface FiniteSize<E : Environment, S, out G : GroupResult> : Group<E, S, G>, SignatureConstraint.NonTerminating<E, S, G>
}
sealed interface Structure<E : Environment, S> : Groupable.Terminating<E, S, Unit>, Aliasable, SenderValidator<E, S>
sealed interface ValidatedParameter<E : Environment, S, out T> : Groupable<E, S, T> {
    sealed interface UnknownSize<E : Environment, S, out T> : ValidatedParameter<E, S, T>, Groupable.Terminating<E, S, T>
    sealed interface FixedSize<E : Environment, S, out T> : ValidatedParameter<E, S, T>, Groupable.NonTerminating<E, S, T>
}
sealed interface OptionalParameter<E : Environment, S, out T> : SyntaxElement<E, S, T>, SignatureConstraint.Terminating<E, S, T>
