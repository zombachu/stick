package com.zombachu.stick.element

import com.zombachu.stick.Aliasable
import com.zombachu.stick.Arguments
import com.zombachu.stick.CommandResult
import com.zombachu.stick.ConsumingResult
import com.zombachu.stick.ContextualValue
import com.zombachu.stick.Environment
import com.zombachu.stick.GroupResult
import com.zombachu.stick.HybridFlagResult
import com.zombachu.stick.Invocation
import com.zombachu.stick.MatchResult
import com.zombachu.stick.Position
import com.zombachu.stick.SenderValidator
import com.zombachu.stick.Size
import com.zombachu.stick.Suggestion
import com.zombachu.stick.ValidationContext

sealed interface Element<in E : Environment, S, out T>

internal sealed interface InternalElement<in E : Environment, S, out T> : Element<E, S, T> {
    context(inv: Invocation<E, S>)
    fun parse(args: List<String>): CommandResult<T>
}

context(inv: Invocation<E, S>)
internal fun <E : Environment, S, T> Element<E, S, T>.parse(args: List<String>): CommandResult<T> =
    when (this) {
        is Parameter<E, S, out T, *> -> parse(args)
        is InternalElement -> parse(args)
    }

sealed interface SignatureElement<in E : Environment, S, out T, out P : Position> : Element<E, S, T>

sealed interface SyntaxElement<in E : Environment, S, out T> : Element<E, S, T> {
    val size: Size
    val name: String
    val description: String

    context(validationContext: ValidationContext<E, S>)
    fun match(args: List<String>): MatchResult

    context(validationContext: ValidationContext<E, S>)
    fun suggest(preceding: List<String>, partial: String): List<Suggestion> = []

    context(validationContext: ValidationContext<E, S>)
    fun getSyntax(): String
}

sealed interface ConsumingElement<in E : Environment, S, out T> : SyntaxElement<E, S, T>

internal sealed interface InternalConsumingElement<in E : Environment, S, out T> :
    InternalElement<E, S, T>, ConsumingElement<E, S, T> {
    context(inv: Invocation<E, S>)
    override fun parse(args: List<String>): ConsumingResult<T>
}

context(inv: Invocation<E, S>)
internal fun <E : Environment, S, T> ConsumingElement<E, S, T>.parse(args: List<String>): ConsumingResult<T> =
    when (this) {
        is Parameter<E, S, out T, *> -> parse(args)
        is InternalConsumingElement -> parse(args)
    }

sealed interface Groupable<in E : Environment, S, T, out P : Position> : SyntaxElement<E, S, T> {
    val type: GroupableType

    context(validationContext: ValidationContext<E, S>)
    fun getGroupedSyntax(): String = name
}

sealed interface Helper<in E : Environment, S, out T> : SignatureElement<E, S, T, Position.Leading>

sealed interface Flag<in E : Environment, S, out T> :
    SignatureElement<E, S, T, Position.Anywhere>, ConsumingElement<E, S, T>, SenderValidator<E, S> {
    override val size: Size.Bounded
    val default: ContextualValue<E, S, T>
}

sealed interface ValueFlag<in E : Environment, S, out T> : Flag<E, S, T>

sealed interface HybridFlag<in E : Environment, S, out T> : Flag<E, S, HybridFlagResult<T>>

sealed interface Group<in E : Environment, S, G, out P : Position> : Groupable<E, S, G, P>, SignatureElement<E, S, G, P>

sealed interface Structure<in E : Environment, S, T_ : Arguments> : Branch<E, S, T_>, Aliasable, SenderValidator<E, S>

sealed interface Branch<in E : Environment, S, T_ : Arguments> : Groupable<E, S, T_, Position.Last>

internal interface InternalBranch<in E : Environment, S, T_ : Arguments> : Branch<E, S, T_>, InternalElement<E, S, T_> {

    context(validationContext: ValidationContext<E, S>)
    fun suggestBranch(preceding: List<String>, partial: String, leadingParameterMatch: MatchResult?): List<Suggestion>

    context(validationContext: ValidationContext<E, S>)
    override fun suggest(preceding: List<String>, partial: String): List<Suggestion> =
        suggestBranch(preceding, partial, null)
}

sealed interface ValidatedParameter<in E : Environment, S, T, out P : Position> :
    Groupable<E, S, T, P>, ConsumingElement<E, S, T>

sealed interface OptionalParameter<in E : Environment, S, out T, out P : Position> :
    SignatureElement<E, S, T, P>, ConsumingElement<E, S, T>

sealed interface OptionalGroup<in E : Environment, S, out G : GroupResult?, out P : Position> :
    SignatureElement<E, S, G, P>, SyntaxElement<E, S, G>
