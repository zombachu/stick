package com.zombachu.stick.element

import com.zombachu.stick.CommandResult
import com.zombachu.stick.ContextualValue
import com.zombachu.stick.Environment
import com.zombachu.stick.HybridFlagResult
import com.zombachu.stick.Invocation
import com.zombachu.stick.Position
import com.zombachu.stick.TypedIdentifier
import com.zombachu.stick.valueOrPropagateError

internal class StoredHelper<E : Environment, S, T>(
    private val base: Helper<E, S, T>,
    private val id: TypedIdentifier<T>,
) : Helper<E, S, T> by base {

    context(inv: Invocation<E, S>)
    override fun parse(args: List<String>): CommandResult<T> = parseAndStore(base, id, args)
}

internal class StoredParameter<E : Environment, S, T, P : Position>(
    private val base: Parameter<E, S, T, P>,
    private val id: TypedIdentifier<T>,
) : Parameter<E, S, T, P>(base.size, base.name, base.description) {

    context(inv: Invocation<E, S>)
    override fun parse(args: List<String>): CommandResult<T> = parseAndStore(base, id, args)
}

internal class StoredValueFlag<E : Environment, S, T>(
    private val base: ValueFlag<E, S, T>,
    private val id: TypedIdentifier<T>,
) : ValueFlag<E, S, T> by base {

    override val default: ContextualValue<E, S, T> = { defaultAndStore(base.default, id) }

    context(inv: Invocation<E, S>)
    override fun parse(args: List<String>): CommandResult<T> = parseAndStore(base, id, args)
}

internal class StoredHybridFlag<E : Environment, S, T>(
    private val base: HybridFlag<E, S, T>,
    private val id: TypedIdentifier<HybridFlagResult<T>>,
) : HybridFlag<E, S, T> by base {

    override val default: ContextualValue<E, S, HybridFlagResult<T>> = { defaultAndStore(base.default, id) }

    context(inv: Invocation<E, S>)
    override fun parse(args: List<String>): CommandResult<HybridFlagResult<T>> = parseAndStore(base, id, args)
}

internal class StoredOptionalParameter<E : Environment, S, T, P : Position>(
    private val base: OptionalParameter<E, S, T, P>,
    private val id: TypedIdentifier<T>,
) : OptionalParameter<E, S, T, P> by base {

    context(inv: Invocation<E, S>)
    override fun parse(args: List<String>): CommandResult<T> = parseAndStore(base, id, args)
}

private fun <E : Environment, S, T> Invocation<E, S>.defaultAndStore(
    default: ContextualValue<E, S, T>,
    id: TypedIdentifier<T>,
): CommandResult<T> {
    val result = default(this)
    val value = result.valueOrPropagateError {
        return it
    }
    put(id, value)
    return result
}

context(inv: Invocation<E, S>)
private fun <E : Environment, S, T> parseAndStore(
    base: Element<E, S, T>,
    id: TypedIdentifier<T>,
    args: List<String>,
): CommandResult<T> {
    val result = base.parse(args)
    val value = result.valueOrPropagateError {
        return it
    }
    inv.put(id, value)
    return result
}
