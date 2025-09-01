@file:OptIn(ExperimentalContracts::class)

package com.zombachu.stick

import com.zombachu.stick.feedback.ErrorMessages
import com.zombachu.stick.feedback.Feedback
import com.zombachu.stick.feedback.Feedback0
import com.zombachu.stick.feedback.Feedback1
import com.zombachu.stick.feedback.Feedback2
import com.zombachu.stick.feedback.Feedback3
import com.zombachu.stick.feedback.PreformattedFeedback
import com.zombachu.stick.impl.Array2
import com.zombachu.stick.impl.Tuple
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract

sealed interface CommandResult<T> {
    sealed interface Success<T> : CommandResult<T> {
        val value: T
    }

    sealed interface InternalFailure<T> : CommandResult<T>

    sealed interface Failure<T> : InternalFailure<T> {
        val feedback: Feedback<out Tuple<String>>
    }
}

sealed interface ParsingResult<T> : CommandResult<T> {
    class Success<T> internal constructor(override val value: T) : ParsingResult<T>, CommandResult.Success<T>

//    class UnknownError<T> internal constructor(override val feedback: Feedback0) : ParsingResult<T>, Result.Failure<T>

    class HandledError<T> internal constructor() : ParsingResult<T>, CommandResult.InternalFailure<T>

    class TypeNotMatchedInternal<T> internal constructor() : ParsingResult<T>, CommandResult.InternalFailure<T>
    class TypeNotMatchedError<T> internal constructor(override val feedback: Feedback2) : ParsingResult<T>, CommandResult.Failure<T>
    class TypeNotMatchedSyntaxError<T> internal constructor(override val feedback: Feedback1) : ParsingResult<T>, CommandResult.Failure<T>
    class LiteralNotMatchedError<T> internal constructor(override val feedback: PreformattedFeedback<Array2<String>>) : ParsingResult<T>, CommandResult.Failure<T>

    class InvalidSyntaxError<T> internal constructor(override val feedback: Feedback1) : ParsingResult<T>, CommandResult.Failure<T>

    class OutOfRangeError<T> internal constructor(override val feedback: Feedback3) : ParsingResult<T>, CommandResult.Failure<T>

    interface CustomError<T> : ParsingResult<T>, CommandResult.Failure<T>

    companion object {
        fun <T> success(value: T): ParsingResult<T> = Success(value)
//        fun <T> failUnknown(): ParsingResult<T> = UnknownError { ErrorMessages.Unknown }
        fun <T> failHandled(): ParsingResult<T> = HandledError()
        internal fun <T> failTypeInternal(): ParsingResult<T> = TypeNotMatchedInternal()
        fun <T> failType(type: String, arg: String): ParsingResult<T> = TypeNotMatchedError(ErrorMessages.NotAType.with(type, arg))
        fun <T> failTypeSyntax(syntax: String): ParsingResult<T> = TypeNotMatchedSyntaxError(ErrorMessages.InvalidSyntax.with(syntax))
        fun <T> failLiteral(valid: List<String>, arg: String): ParsingResult<T> = LiteralNotMatchedError(ErrorMessages.InvalidLiteral.with(Array2(valid.joinToString(", "), arg), valid))
        fun <T> failSyntax(syntax: String): ParsingResult<T> = InvalidSyntaxError(ErrorMessages.InvalidSyntax.with(syntax))
        fun <T> failRange(min: String, max: String, arg: String): ParsingResult<T> = OutOfRangeError(ErrorMessages.OutOfRange.with(min, max, arg))
    }
}

sealed interface SenderValidationResult: CommandResult<Unit> {
    class Success internal constructor() : SenderValidationResult, CommandResult.Success<Unit> {
        override val value: Unit = Unit
    }

    class InvalidSenderError internal constructor(override val feedback: Feedback0): SenderValidationResult, CommandResult.Failure<Unit>
    class InvalidSenderTypeError internal constructor(override val feedback: Feedback0): SenderValidationResult, CommandResult.Failure<Unit>

    companion object {
        fun success(): SenderValidationResult = Success()
        fun failSender(): SenderValidationResult = InvalidSenderError(ErrorMessages.InvalidSender)
        fun failSenderType(): SenderValidationResult = InvalidSenderTypeError(ErrorMessages.InvalidSenderType)
    }
}

internal sealed interface PeekingResult : CommandResult<List<String>> {
    class Success internal constructor(private val mutableArgs: MutableList<String>): PeekingResult, CommandResult.Success<List<String>> {
        override val value: List<String> = mutableArgs
        fun consume() {
            mutableArgs.clear()
        }
    }
    class InvalidSizeError internal constructor() : PeekingResult, CommandResult.InternalFailure<List<String>>

    companion object {
        fun success(mutableArgs: MutableList<String>): PeekingResult = Success(mutableArgs)
        fun failSize(): PeekingResult = InvalidSizeError()
    }
}

internal inline fun <T, R> CommandResult<T>.handleInternal(
    onSuccess: (CommandResult.Success<T>) -> R,
    onFailure: (CommandResult.InternalFailure<T>) -> R
): R {
    return if (isSuccess()) onSuccess(this) else onFailure(this)
}

inline fun <T2> CommandResult<*>.propagateError(onFailure: (CommandResult.InternalFailure<T2>) -> Nothing) {
    contract {
        callsInPlace(onFailure, InvocationKind.AT_MOST_ONCE)
    }

    if (isSuccess()) {
        return
    }
    onFailure(unsafeCast())
}

inline fun <T, T2> CommandResult<T>.valueOrPropagateError(onFailure: (CommandResult.InternalFailure<T2>) -> Nothing): T {
    contract {
        callsInPlace(onFailure, InvocationKind.AT_MOST_ONCE)
    }

    if (isSuccess()) {
        return value
    }
    onFailure(unsafeCast())
}

fun <T> CommandResult<T>.isSuccess(): Boolean {
    contract {
        returns(true) implies (this@isSuccess is CommandResult.Success)
        returns(false) implies (this@isSuccess is CommandResult.InternalFailure)
    }
    return this is CommandResult.Success
}

@Suppress("UNCHECKED_CAST")
fun <T2> CommandResult.InternalFailure<*>.unsafeCast(): CommandResult.InternalFailure<T2> = this as CommandResult.InternalFailure<T2>
