@file:OptIn(ExperimentalContracts::class)

package com.zombachu.stick

import com.zombachu.stick.Result.Failure
import com.zombachu.stick.Result.Success
import com.zombachu.stick.feedback.ErrorMessages
import com.zombachu.stick.feedback.Feedback
import com.zombachu.stick.feedback.Feedback0
import com.zombachu.stick.feedback.Feedback1
import com.zombachu.stick.feedback.Feedback2
import com.zombachu.stick.feedback.Feedback3
import com.zombachu.stick.impl.Array2
import com.zombachu.stick.impl.Tuple
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract

sealed interface Result<T> {
    sealed interface Success<T> : Result<T> {
        val value: T
    }

    sealed interface Failure<T> : Result<T> {
        val feedback: () -> Feedback<out Tuple<String>>
    }
}

sealed interface ParsingResult<T> : Result<T> {
    class Success<T> internal constructor(override val value: T) : ParsingResult<T>, Result.Success<T>

    class UnknownError<T> internal constructor(override val feedback: () -> Feedback0) : ParsingResult<T>, Failure<T>

    interface TypeMatchError<T> : ParsingResult<T>, Failure<T>
    class TypeNotMatchedInternal<T> internal constructor(override val feedback: () -> Feedback0) : TypeMatchError<T>
    class TypeNotMatchedError<T> internal constructor(override val feedback: () -> Feedback2) : TypeMatchError<T>
    class TypeNotMatchedSyntaxError<T> internal constructor(override val feedback: () -> Feedback1) : TypeMatchError<T>
    class LiteralNotMatchedError<T> internal constructor(override val feedback: () -> Feedback2) : TypeMatchError<T>

    interface SyntaxError<T> : ParsingResult<T>, Failure<T>
    class InvalidSyntaxError<T> internal constructor(override val feedback: () -> Feedback1) : SyntaxError<T>

    interface ArgumentError<T> : ParsingResult<T>, Failure<T>
    class OutOfRangeError<T> internal constructor(override val feedback: () -> Feedback3) : ArgumentError<T>

    interface StateError<T> : ParsingResult<T>, Failure<T>
    class IllegalStateError<T> internal constructor(override val feedback: () -> Feedback1) : StateError<T>

    companion object {
        fun <T> success(value: T): ParsingResult<T> = Success(value)
        fun <T> failUnknown(): ParsingResult<T> = UnknownError { ErrorMessages.Unknown }
        internal fun <T> failTypeInternal(): ParsingResult<T> = TypeNotMatchedInternal { ErrorMessages.Empty }
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
    class Failure internal constructor(override val feedback: () -> Feedback<out Tuple<String>>) : ExecutionResult, Result.Failure<Unit>

    companion object {
        fun success(): ExecutionResult = Success()
        fun error(feedback: Feedback<*> = ErrorMessages.Empty): ExecutionResult = Failure { feedback }
    }
}

sealed interface SenderValidationResult: Result<Unit> {
    class Success internal constructor() : SenderValidationResult, Result.Success<Unit> {
        override val value: Unit = Unit
    }

    interface Failure : SenderValidationResult, Result.Failure<Unit> {}
    class InvalidSenderError internal constructor(override val feedback: () -> Feedback0): Failure

    companion object {
        fun success(): SenderValidationResult = Success()
        fun failSender(): SenderValidationResult = InvalidSenderError(TODO())
    }
}

internal sealed interface PeekingResult : Result<List<String>> {
    class Success internal constructor(private val mutableArgs: MutableList<String>): PeekingResult, Result.Success<List<String>> {
        override val value: List<String> = mutableArgs
        fun consume() {
            mutableArgs.clear()
        }
    }
    class InvalidSizeError internal constructor(override val feedback: () -> Feedback0) : PeekingResult, Failure<List<String>>

    companion object {
        fun success(mutableArgs: MutableList<String>): PeekingResult = Success(mutableArgs)
        fun failSize(): PeekingResult = InvalidSizeError { ErrorMessages.Empty }
    }
}

inline fun <T, T2> Result<T>.valueOrPropagate(onFailure: (Failure<T2>) -> Nothing): T {
    if (isSuccess()) {
        return value
    }
    onFailure(unsafeCast())
}

fun <T> Result<T>.isSuccess(): Boolean {
    contract {
        returns(true) implies (this@isSuccess is Success<T>)
        returns(false) implies (this@isSuccess is Failure<T>)
    }
    return this is Success<T>
}

fun <T2> Failure<*>.unsafeCast(): Failure<T2> {
    // TODO: Handle safer
    return this as Failure<T2>
}
