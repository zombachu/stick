@file:OptIn(ExperimentalContracts::class)

package com.zombachu.stick

import com.zombachu.stick.feedback.ErrorMessages
import com.zombachu.stick.feedback.Feedback
import com.zombachu.stick.feedback.Feedback0
import com.zombachu.stick.feedback.Feedback1
import com.zombachu.stick.feedback.Feedback2
import com.zombachu.stick.feedback.Feedback3
import com.zombachu.stick.impl.Array2
import com.zombachu.stick.impl.Tuple
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract

sealed interface Result<T> {
    sealed interface Success<T> : Result<T> {
        val value: T
    }

    sealed interface InternalFailure<T> : Result<T>

    sealed interface Failure<T> : InternalFailure<T> {
        val feedback: () -> Feedback<out Tuple<String>>
    }
}

sealed interface ParsingResult<T> : Result<T> {
    class Success<T> internal constructor(override val value: T) : ParsingResult<T>, Result.Success<T>

//    class UnknownError<T> internal constructor(override val feedback: () -> Feedback0) : ParsingResult<T>, Result.Failure<T>

    class HandledError<T> internal constructor() : ParsingResult<T>, Result.InternalFailure<T>

    class TypeNotMatchedInternal<T> internal constructor() : ParsingResult<T>, Result.InternalFailure<T>
    class TypeNotMatchedError<T> internal constructor(override val feedback: () -> Feedback2) : ParsingResult<T>, Result.Failure<T>
    class TypeNotMatchedSyntaxError<T> internal constructor(override val feedback: () -> Feedback1) : ParsingResult<T>, Result.Failure<T>
    class LiteralNotMatchedError<T> internal constructor(override val feedback: () -> Feedback2) : ParsingResult<T>, Result.Failure<T>

    class InvalidSyntaxError<T> internal constructor(override val feedback: () -> Feedback1) : ParsingResult<T>, Result.Failure<T>

    class OutOfRangeError<T> internal constructor(override val feedback: () -> Feedback3) : ParsingResult<T>, Result.Failure<T>

    interface CustomError<T> : ParsingResult<T>, Result.Failure<T>

    companion object {
        fun <T> success(value: T): ParsingResult<T> = Success(value)
//        fun <T> failUnknown(): ParsingResult<T> = UnknownError { ErrorMessages.Unknown }
        fun <T> failHandled(): ParsingResult<T> = HandledError()
        internal fun <T> failTypeInternal(): ParsingResult<T> = TypeNotMatchedInternal()
        fun <T> failType(type: String, arg: String): ParsingResult<T> = TypeNotMatchedError { ErrorMessages.NotAType.with(type, arg) }
        fun <T> failTypeSyntax(syntax: String): ParsingResult<T> = TypeNotMatchedSyntaxError { ErrorMessages.InvalidSyntax.with(syntax) }
        fun <T> failLiteral(valid: List<String>, arg: String): ParsingResult<T> = LiteralNotMatchedError { ErrorMessages.InvalidLiteral.with(Array2(valid.joinToString(","), arg), valid) }
        fun <T> failSyntax(syntax: String): ParsingResult<T> = InvalidSyntaxError { ErrorMessages.InvalidSyntax.with(syntax) }
        fun <T> failRange(min: String, max: String, arg: String): ParsingResult<T> = OutOfRangeError { ErrorMessages.OutOfRange.with(min, max, arg) }
    }
}

sealed interface ExecutionResult : Result<Unit> {
    class Success internal constructor() : ExecutionResult, Result.Success<Unit> {
        override val value: Unit = Unit
    }
    class Failure internal constructor() : ExecutionResult, Result.InternalFailure<Unit>

    companion object {
        fun success(): ExecutionResult = Success()
        fun error(feedback: Feedback<*> = ErrorMessages.Empty): ExecutionResult = Failure()
    }
}

sealed interface SenderValidationResult: Result<Unit> {
    class Success internal constructor() : SenderValidationResult, Result.Success<Unit> {
        override val value: Unit = Unit
    }

    class InvalidSenderError internal constructor(override val feedback: () -> Feedback0): SenderValidationResult, Result.Failure<Unit>
    class InvalidSenderTypeError internal constructor(override val feedback: () -> Feedback0): SenderValidationResult, Result.Failure<Unit>

    companion object {
        fun success(): SenderValidationResult = Success()
        fun failSender(): SenderValidationResult = InvalidSenderError { ErrorMessages.InvalidSender }
        fun failSenderType(): SenderValidationResult = InvalidSenderTypeError { ErrorMessages.InvalidSenderType }
    }
}

internal sealed interface PeekingResult : Result<List<String>> {
    class Success internal constructor(private val mutableArgs: MutableList<String>): PeekingResult, Result.Success<List<String>> {
        override val value: List<String> = mutableArgs
        fun consume() {
            mutableArgs.clear()
        }
    }
    class InvalidSizeError internal constructor() : PeekingResult, Result.InternalFailure<List<String>>

    companion object {
        fun success(mutableArgs: MutableList<String>): PeekingResult = Success(mutableArgs)
        fun failSize(): PeekingResult = InvalidSizeError()
    }
}

internal inline fun <T, R> Result<T>.handleInternal(
    onSuccess: (Result.Success<T>) -> R,
    onFailure: (Result.InternalFailure<T>) -> R
): R {
    return if (isSuccess()) onSuccess(this) else onFailure(this)
}

inline fun <T2> Result<*>.propagateError(onFailure: (Result.Failure<T2>) -> Nothing) {
    contract {
        callsInPlace(onFailure, InvocationKind.AT_MOST_ONCE)
    }

    if (isSuccess()) {
        return
    }
    onFailure(unsafeCast())
}

inline fun <T, T2> Result<T>.valueOrPropagateError(onFailure: (Result.Failure<T2>) -> Nothing): T {
    contract {
        callsInPlace(onFailure, InvocationKind.AT_MOST_ONCE)
    }

    if (isSuccess()) {
        return value
    }
    onFailure(unsafeCast())
}

fun <T> Result<T>.isSuccess(): Boolean {
    contract {
        returns(true) implies (this@isSuccess is Result.Success)
        returns(false) implies (this@isSuccess is Result.InternalFailure)
    }
    return this is Result.Success
}

@Suppress("UNCHECKED_CAST")
fun <T2> Result.InternalFailure<*>.unsafeCast(): Result.Failure<T2> = this as Result.Failure<T2>
