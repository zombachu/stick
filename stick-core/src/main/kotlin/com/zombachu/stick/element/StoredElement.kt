package com.zombachu.stick.element

import com.zombachu.stick.CommandResult
import com.zombachu.stick.Environment
import com.zombachu.stick.HybridFlagResult
import com.zombachu.stick.Invocation
import com.zombachu.stick.TypedIdentifier
import com.zombachu.stick.ValidationContext
import com.zombachu.stick.valueOrPropagateError

internal class StoredFixedSizeParameter<E : Environment, S, T>(
    private val base: FixedSize<E, S, T>,
    private val id: TypedIdentifier<T>,
) : Parameter.FixedSize<E, S, T>(base.size, base.name, base.description) {

    context(inv: Invocation<E, S>)
    override fun parse(args: List<String>): CommandResult<T> = parseAndStore(base, id, args)

    context(validationContext: ValidationContext<E, S>)
    override fun getSyntax(): String = base.getSyntax()
}

internal class StoredUnknownSizeParameter<E : Environment, S, T>(
    private val base: UnknownSize<E, S, T>,
    private val id: TypedIdentifier<T>,
) : Parameter.UnknownSize<E, S, T>(base.size, base.name, base.description) {

    context(inv: Invocation<E, S>)
    override fun parse(args: List<String>): CommandResult<T> = parseAndStore(base, id, args)

    context(validationContext: ValidationContext<E, S>)
    override fun getSyntax(): String = base.getSyntax()
}

internal class StoredValueFlag<E : Environment, S, T>(
    private val base: ValueFlag<E, S, T>,
    private val id: TypedIdentifier<T>,
) : ValueFlag<E, S, T> by base {

    context(inv: Invocation<E, S>)
    override fun parse(args: List<String>): CommandResult<T> = parseAndStore(base, id, args)

    context(validationContext: ValidationContext<E, S>)
    override fun getSyntax(): String = base.getSyntax()
}

internal class StoredHybridFlag<E : Environment, S, T>(
    private val base: HybridFlag<E, S, T>,
    private val id: TypedIdentifier<HybridFlagResult<T>>,
) : HybridFlag<E, S, T> by base {

    context(inv: Invocation<E, S>)
    override fun parse(args: List<String>): CommandResult<HybridFlagResult<T>> = parseAndStore(base, id, args)

    context(validationContext: ValidationContext<E, S>)
    override fun getSyntax(): String = base.getSyntax()
}

internal class StoredOptionalParameter<E : Environment, S, T>(
    private val base: OptionalParameter<E, S, T>,
    private val id: TypedIdentifier<T>,
) : OptionalParameter<E, S, T> by base {

    context(inv: Invocation<E, S>)
    override fun parse(args: List<String>): CommandResult<T> = parseAndStore(base, id, args)

    context(validationContext: ValidationContext<E, S>)
    override fun getSyntax(): String = base.getSyntax()
}

context(inv: Invocation<E, S>)
private fun <E : Environment, S, T> parseAndStore(
    base: SyntaxElement<E, S, T>,
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
