@file:OptIn(ExperimentalContracts::class)

package com.zombachu.stick

import com.zombachu.stick.feedback.Feedback
import com.zombachu.stick.impl.Size
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract

sealed interface CommandResult<out T> {
    interface Success<out T> : CommandResult<T> {
        val value: T
        val consumed: Size.Fixed
    }

    sealed interface InternalFailure : CommandResult<Nothing>

    sealed interface Failure : InternalFailure {
        val feedback: Feedback
    }
}

sealed interface ParsingResult<out T> : CommandResult<T> {
    class Success<out T> internal constructor(override val value: T, override val consumed: Size.Fixed) : ParsingResult<T>, CommandResult.Success<T>


    class UnknownError internal constructor(override val feedback: Feedback.Unknown) : ParsingResult<Nothing>, CommandResult.Failure
    sealed interface InternalFailure : ParsingResult<Nothing>, CommandResult.InternalFailure
    sealed interface Failure : ParsingResult<Nothing>, CommandResult.Failure

    object HandledError : InternalFailure
    object TypeNotMatchedInternal : InternalFailure

    class TypeNotMatchedError internal constructor(override val feedback: Feedback.TypeNotMatched) : Failure
    class LiteralNotMatchedError internal constructor(override val feedback: Feedback.LiteralNotMatched) : Failure

    class InvalidSyntaxError internal constructor(override val feedback: Feedback.InvalidSyntax) : Failure

    class OutOfRangeError internal constructor(override val feedback: Feedback.OutOfRange) : Failure

    interface CustomError : Failure

    companion object {
        fun <T> success(value: T, consumed: Size.Fixed = Size(0)): Success<T> = Success(value, consumed)
        fun failUnknown(): UnknownError = UnknownError(Feedback.Unknown)
        fun failHandled(): HandledError = HandledError
        internal fun failTypeInternal(): TypeNotMatchedInternal = TypeNotMatchedInternal
        fun failType(type: String, arg: String): TypeNotMatchedError = TypeNotMatchedError(Feedback.TypeNotMatched(type, arg))
        fun failLiteral(valid: List<String>, arg: String): LiteralNotMatchedError = LiteralNotMatchedError(Feedback.LiteralNotMatched(valid, arg))
        fun failSyntax(syntax: String): InvalidSyntaxError = InvalidSyntaxError(Feedback.InvalidSyntax(syntax))
        fun failRange(min: String, max: String, arg: String): OutOfRangeError = OutOfRangeError(Feedback.OutOfRange(min, max, arg))
    }
}

sealed interface SenderValidationResult {
    object Success : SenderValidationResult, CommandResult.Success<Unit> {
        override val value: Unit = Unit
        override val consumed: Size.Fixed = Size(0)
    }

    sealed interface Failure : SenderValidationResult, CommandResult.Failure

    class InvalidSenderError internal constructor(override val feedback: Feedback.InvalidSender): Failure
    class InvalidSenderPermissionError internal constructor(override val feedback: Feedback.InvalidPermission): Failure
    class InvalidSenderTypeError internal constructor(override val feedback: Feedback.InvalidSenderType): Failure

    companion object {
        fun success(): Success = Success
        fun failSender(): InvalidSenderError = InvalidSenderError(Feedback.InvalidSender)
        fun failPermission(): InvalidSenderPermissionError = InvalidSenderPermissionError(Feedback.InvalidPermission)
        fun failSenderType(): InvalidSenderTypeError = InvalidSenderTypeError(Feedback.InvalidSenderType)
    }
}

internal sealed interface PeekingResult {
    data class Success internal constructor(private val mutableArgs: MutableList<String>): PeekingResult, CommandResult.Success<List<String>> {
        override val value: List<String> = mutableArgs
        override val consumed: Size.Fixed = Size(0)
        fun consume(count: Int) {
            mutableArgs.subList(0, count).clear()
        }
    }
    object InvalidSizeError : PeekingResult, CommandResult.InternalFailure

    companion object {
        fun success(mutableArgs: MutableList<String>): Success = Success(mutableArgs)
        fun failSize(): InvalidSizeError = InvalidSizeError
    }
}

internal inline fun <T, R> CommandResult<T>.handleInternal(
    onSuccess: (CommandResult.Success<T>) -> R,
    onFailure: (CommandResult.InternalFailure) -> R
): R {
    return if (isSuccess()) onSuccess(this) else onFailure(this)
}

@OptIn(ExperimentalContracts::class)
inline fun <T> CommandResult<T>.propagateError(onFailure: (CommandResult.InternalFailure) -> Nothing) {
    contract {
        returns() implies (this@propagateError is CommandResult.Success)
        callsInPlace(onFailure, InvocationKind.AT_MOST_ONCE)
    }
    if (isSuccess()) return
    onFailure(this)
}

inline fun <T> CommandResult<T>.valueOrPropagateError(onFailure: (CommandResult.InternalFailure) -> Nothing): T {
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
        returns(false) implies (this@isSuccess is CommandResult.InternalFailure)
    }
    return this is CommandResult.Success
}

fun <T> CommandResult<T>.withSize(size: Size.Fixed): CommandResult<T> {
    this.propagateError { return it }
    return ParsingResult.success(this.value, size)
}
