package com.zombachu.stick.element

import com.zombachu.stick.Aliasable
import com.zombachu.stick.Arguments
import com.zombachu.stick.CommandResult
import com.zombachu.stick.ContextualValue
import com.zombachu.stick.Environment
import com.zombachu.stick.GroupResult
import com.zombachu.stick.HybridFlagResult
import com.zombachu.stick.Invocation
import com.zombachu.stick.Position
import com.zombachu.stick.SenderValidator
import com.zombachu.stick.Size
import com.zombachu.stick.ValidationContext

sealed interface Element<in E : Environment, S, out T> {
    val size: Size
    val type: ElementType

    context(inv: Invocation<E, S>)
    fun parse(args: List<String>): CommandResult<T>

    sealed interface Positioned<in E : Environment, S, out T, out P : Position> : Element<E, S, T>
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

    sealed interface Positioned<in E : Environment, S, out T, out P : Position> :
        Groupable<E, S, T>, Element.Positioned<E, S, T, P>
}

sealed interface Helper<in E : Environment, S, out T> : Element.Positioned<E, S, T, Position.Leading>

sealed interface Flag<in E : Environment, S, out T> :
    SyntaxElement<E, S, T>, Element.Positioned<E, S, T, Position.Anywhere> {
    val default: ContextualValue<E, S, T>

    sealed interface Validated<in E : Environment, S, out T> : Flag<E, S, T>, SenderValidator<E, S> {
        val invalidDefault: ContextualValue<E, S, T>
    }
}

sealed interface ValueFlag<in E : Environment, S, out T> : Flag<E, S, T>

sealed interface HybridFlag<in E : Environment, S, out T> : Flag<E, S, HybridFlagResult<T>>

sealed interface Group<in E : Environment, S, out G : GroupResult, out P : Position> : Groupable.Positioned<E, S, G, P>

sealed interface Structure<in E : Environment, S, out T_ : Arguments> :
    Groupable.Positioned<E, S, T_, Position.Last>, Aliasable, SenderValidator<E, S>

sealed interface ValidatedParameter<in E : Environment, S, out T, out P : Position> : Groupable.Positioned<E, S, T, P>

sealed interface OptionalParameter<in E : Environment, S, out T, out P : Position> :
    SyntaxElement<E, S, T>, Element.Positioned<E, S, T, P>
