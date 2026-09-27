@file:OptIn(ExperimentalContracts::class)

package com.zombachu.stick

import com.zombachu.stick.failure.FailureOrigin
import com.zombachu.stick.failure.Reason
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract
import kotlin.reflect.KClass

sealed interface CommandResult<out T> {
    interface Success<out T> : CommandResult<T> {
        val value: T
    }

    sealed class Failure : ConsumingResult<Nothing> {
        sealed class Unhandled(val reason: Reason, internal val origin: FailureOrigin) : Failure()

        class NoMatch internal constructor(reason: Reason, origin: FailureOrigin) : Unhandled(reason, origin)

        class Error internal constructor(reason: Reason, origin: FailureOrigin) : Unhandled(reason, origin)

        data object Handled : Failure()
    }
}

sealed interface ConsumingResult<out T> : CommandResult<T> {
    interface Success<out T> : ConsumingResult<T>, CommandResult.Success<T> {
        val consumed: Int
        val canConsumeMore: Boolean
    }
}

private class ValueSuccess<out T>(override val value: T) : CommandResult.Success<T>

private class ConsumingSuccess<out T>(
    override val value: T,
    override val consumed: Int,
    override val canConsumeMore: Boolean,
) : ConsumingResult.Success<T>

context(_: ValidationContext<*, *>)
fun <T> success(value: T): CommandResult.Success<T> = ValueSuccess(value)

context(_: ValidationContext<*, *>)
fun success(): CommandResult.Success<Unit> = ValueSuccess(Unit)

context(validationContext: ValidationContext<*, *>)
fun fail(reason: Reason): CommandResult.Failure.Error =
    CommandResult.Failure.Error(reason, validationContext.createFailureOrigin())

context(validationContext: ValidationContext<*, *>)
fun noMatch(reason: Reason = Reason.InvalidSyntax): CommandResult.Failure.NoMatch =
    CommandResult.Failure.NoMatch(reason, validationContext.createFailureOrigin())

context(_: ValidationContext<*, *>)
fun handled(): CommandResult.Failure.Handled = CommandResult.Failure.Handled

context(_: ValidationContext<*, *>)
fun failType(type: String, arg: String): CommandResult.Failure.NoMatch = noMatch(Reason.TypeNotMatched(type, arg))

context(_: ValidationContext<*, *>)
fun failLiteral(valid: List<String>, arg: String): CommandResult.Failure.NoMatch =
    noMatch(Reason.LiteralNotMatched(valid, arg))

context(_: ValidationContext<*, *>)
fun failSyntax(): CommandResult.Failure.Error = fail(Reason.InvalidSyntax)

context(_: ValidationContext<*, *>)
fun failRange(min: String, max: String, arg: String): CommandResult.Failure.Error =
    fail(Reason.OutOfRange(min, max, arg))

context(_: ValidationContext<*, *>)
fun failSender(): CommandResult.Failure.Error = fail(Reason.InvalidSender)

context(_: ValidationContext<*, *>)
fun failPermission(): CommandResult.Failure.Error = fail(Reason.InvalidPermission)

context(_: ValidationContext<*, *>)
fun failSenderType(required: KClass<*>): CommandResult.Failure.Error = fail(Reason.InvalidSenderType(required))

@OptIn(ExperimentalContracts::class)
inline fun <T> CommandResult<T>.propagateError(onFailure: (CommandResult.Failure) -> Nothing) {
    contract {
        returns() implies (this@propagateError is CommandResult.Success)
        callsInPlace(onFailure, InvocationKind.AT_MOST_ONCE)
    }
    if (isSuccess()) return
    onFailure(this)
}

@OptIn(ExperimentalContracts::class)
inline fun <T> ConsumingResult<T>.propagateError(onFailure: (CommandResult.Failure) -> Nothing) {
    contract {
        returns() implies (this@propagateError is ConsumingResult.Success)
        callsInPlace(onFailure, InvocationKind.AT_MOST_ONCE)
    }
    when (this) {
        is ConsumingResult.Success -> return
        is CommandResult.Failure -> onFailure(this)
    }
}

inline fun <T> CommandResult<T>.valueOrPropagateError(onFailure: (CommandResult.Failure) -> Nothing): T {
    contract {
        returns() implies (this@valueOrPropagateError is CommandResult.Success)
        callsInPlace(onFailure, InvocationKind.AT_MOST_ONCE)
    }
    if (isSuccess()) return value
    onFailure(this)
}

fun <T> CommandResult<T>.isSuccess(): Boolean {
    contract {
        returns(true) implies (this@isSuccess is CommandResult.Success)
        returns(false) implies (this@isSuccess is CommandResult.Failure)
    }
    return this is CommandResult.Success
}

internal fun CommandResult.Failure.commit(): CommandResult.Failure =
    if (this is CommandResult.Failure.NoMatch) CommandResult.Failure.Error(reason, origin) else this

fun <T> CommandResult<T>.consuming(consumed: Int, canConsumeMore: Boolean = true): ConsumingResult<T> {
    this.propagateError {
        return it
    }
    return ConsumingSuccess(this.value, consumed, canConsumeMore)
}

typealias ContextualValue<E, S, T> = Execution<E, S>.() -> CommandResult<T>
