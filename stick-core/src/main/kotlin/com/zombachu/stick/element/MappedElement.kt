package com.zombachu.stick.element

import com.zombachu.stick.CommandResult
import com.zombachu.stick.ConsumingResult
import com.zombachu.stick.Environment
import com.zombachu.stick.Execution
import com.zombachu.stick.Invocation
import com.zombachu.stick.MatchResult
import com.zombachu.stick.Position
import com.zombachu.stick.Size
import com.zombachu.stick.Suggestion
import com.zombachu.stick.consuming
import com.zombachu.stick.propagateFailure
import com.zombachu.stick.valueOrPropagateFailure

internal class MappedParameter<E : Environment, S, A, T, P : Position>(
    private val base: Parameter<E, S, A, P>,
    private val transform: suspend Execution<E, S>.(A) -> CommandResult<T>,
) : Parameter<E, S, T, P>(base.size, base.name, base.description) {

    override val type: GroupableType = base.type

    context(inv: Invocation<E, S>)
    override fun match(args: List<String>): MatchResult = base.match(args)

    context(inv: Invocation<E, S>)
    override fun suggest(preceding: List<String>, partial: String): List<Suggestion> = base.suggest(preceding, partial)

    context(ex: Execution<E, S>)
    override suspend fun parse(args: List<String>): ConsumingResult<T> = parseMapped(args, base, transform)

    context(inv: Invocation<E, S>)
    override fun getSyntax(): String = base.getSyntax()

    context(inv: Invocation<E, S>)
    override fun getGroupedSyntax(): String = base.getGroupedSyntax()
}

internal class MappedValueFlag<E : Environment, S, A, T>(
    private val base: ValueFlag<E, S, A>,
    private val transform: suspend Execution<E, S>.(A) -> CommandResult<T>,
) : ValueFlag<E, S, T>, InternalConsumingElement<E, S, T> {

    override val size: Size.Bounded = base.size
    override val name: String = base.name
    override val description: String = base.description

    context(inv: Invocation<E, S>)
    override fun match(args: List<String>): MatchResult = base.match(args)

    context(inv: Invocation<E, S>)
    override fun suggest(preceding: List<String>, partial: String): List<Suggestion> = base.suggest(preceding, partial)

    context(ex: Execution<E, S>)
    override suspend fun parse(args: List<String>): ConsumingResult<T> = parseMapped(args, base, transform)

    context(inv: Invocation<E, S>)
    override fun getSyntax(): String = base.getSyntax()
}

internal class MappedOptionalParameter<E : Environment, S, A, T, P : Position>(
    private val base: OptionalParameter<E, S, A, P>,
    private val transform: suspend Execution<E, S>.(A) -> CommandResult<T>,
) : OptionalParameter<E, S, T, P>, InternalConsumingElement<E, S, T>, InternalOptional<E, S> {

    override val size: Size = base.size
    override val name: String = base.name
    override val description: String = base.description

    context(inv: Invocation<E, S>)
    override fun match(args: List<String>): MatchResult = base.match(args)

    context(inv: Invocation<E, S>)
    override fun suggest(preceding: List<String>, partial: String): List<Suggestion> = base.suggest(preceding, partial)

    context(ex: Execution<E, S>)
    override suspend fun parse(args: List<String>): ConsumingResult<T> = parseMapped(args, base, transform)

    context(inv: Invocation<E, S>)
    override fun getSyntax(): String = base.getSyntax()

    context(inv: Invocation<E, S>)
    override fun validateDefault(): CommandResult<Unit> = base.validateDefault()
}

internal class MappedHelper<E : Environment, S, A, T>(
    private val base: Helper<E, S, A>,
    private val transform: suspend Execution<E, S>.(A) -> CommandResult<T>,
) : Helper<E, S, T>, InternalElement<E, S, T> {

    context(ex: Execution<E, S>)
    override suspend fun parse(args: List<String>): CommandResult<T> {
        val value =
            base.parse(args).valueOrPropagateFailure {
                return it
            }
        return transform(ex, value)
    }
}

context(ex: Execution<E, S>)
private suspend fun <E : Environment, S, A, T> parseMapped(
    args: List<String>,
    base: ConsumingElement<E, S, A>,
    transform: suspend Execution<E, S>.(A) -> CommandResult<T>,
): ConsumingResult<T> {
    val baseResult = base.parse(args)
    baseResult.propagateFailure {
        return it
    }
    return transform(ex, baseResult.value).consuming(baseResult.consumed)
}
