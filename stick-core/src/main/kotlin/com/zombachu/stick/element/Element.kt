package com.zombachu.stick.element

import com.zombachu.stick.Aliasable
import com.zombachu.stick.Arguments
import com.zombachu.stick.CommandResult
import com.zombachu.stick.ContextualValue
import com.zombachu.stick.Environment
import com.zombachu.stick.GroupResult
import com.zombachu.stick.HybridFlagResult
import com.zombachu.stick.Invocation
import com.zombachu.stick.SenderValidator
import com.zombachu.stick.Size
import com.zombachu.stick.ValidationContext

sealed interface Element<in E : Environment, S, out T> {
    val size: Size
    val type: ElementType

    context(inv: Invocation<E, S>)
    fun parse(args: List<String>): CommandResult<T>

    sealed interface NonTerminating<in E : Environment, S, out T> : Element<E, S, T>
}

sealed interface SyntaxElement<in E : Environment, S, out T> : Element<E, S, T> {
    val name: String
    val description: String

    context(validationContext: ValidationContext<E, S>)
    fun getSyntax(): String
}

sealed interface Groupable<in E : Environment, S, out T> : SyntaxElement<E, S, T> {
    context(validationContext: ValidationContext<E, S>)
    fun getGroupedSyntax(): String = name

    sealed interface NonTerminating<in E : Environment, S, out T> : Groupable<E, S, T>, Element.NonTerminating<E, S, T>
}

sealed interface Helper<in E : Environment, S, out T> : Element.NonTerminating<E, S, T>

sealed interface Flag<in E : Environment, S, out T> : SyntaxElement<E, S, T> {
    val default: ContextualValue<E, S, T>

    sealed interface Validated<in E : Environment, S, out T> : Flag<E, S, T>, SenderValidator<E, S> {
        val invalidDefault: ContextualValue<E, S, T>
    }
}

sealed interface ValueFlag<in E : Environment, S, out T> : Flag<E, S, T>, Element.NonTerminating<E, S, T>

sealed interface HybridFlag<in E : Environment, S, out T> : Flag<E, S, HybridFlagResult<T>>

sealed interface Group<in E : Environment, S, out G : GroupResult> : SyntaxElement<E, S, G> {
    sealed interface UnknownSize<in E : Environment, S, out G : GroupResult> : Group<E, S, G>

    sealed interface FiniteSize<in E : Environment, S, out G : GroupResult> :
        Group<E, S, G>, Element.NonTerminating<E, S, G>
}

sealed interface Structure<in E : Environment, S, out T_ : Arguments> :
    Groupable<E, S, T_>, Aliasable, SenderValidator<E, S>

sealed interface ValidatedParameter<in E : Environment, S, out T> : Groupable<E, S, T> {
    sealed interface UnknownSize<in E : Environment, S, out T> : ValidatedParameter<E, S, T>, Groupable<E, S, T>

    sealed interface FixedSize<in E : Environment, S, out T> :
        ValidatedParameter<E, S, T>, Groupable.NonTerminating<E, S, T>
}

sealed interface OptionalParameter<in E : Environment, S, out T> : SyntaxElement<E, S, T>
