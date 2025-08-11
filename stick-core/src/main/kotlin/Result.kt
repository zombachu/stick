@file:OptIn(ExperimentalContracts::class)

package com.zombachu.stick

import com.zombachu.stick.Result.Failure
import com.zombachu.stick.Result.Success
import com.zombachu.stick.feedback.Feedback
import com.zombachu.stick.feedback.Feedback0
import com.zombachu.stick.feedback.Feedback1
import com.zombachu.stick.impl.Tuple
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract

sealed interface Result<T> {
    sealed interface Success<T> : Result<T> {
        val value: T
    }

    sealed interface Failure<T> : Result<T> {
        val feedback: Feedback<out Tuple<String>>
    }
}

sealed interface ParsingResult<T> : Result<T> {
    class Success<T> internal constructor(override val value: T) : ParsingResult<T>, Result.Success<T>

    // TODO: Document potentially ignored
    class UnknownTypeError<T> internal constructor(override val feedback: Feedback1) : ParsingResult<T>, Failure<T>

    // TODO: Document causes syntax printing
    interface SyntaxError<T> : ParsingResult<T>, Failure<T>
    class InvalidSyntaxError<T> internal constructor(override val feedback: Feedback1) : SyntaxError<T>
    class InvalidArgumentError<T> internal constructor(override val feedback: Feedback1) : SyntaxError<T>

    interface StateError<T> : ParsingResult<T>, Failure<T>
    class IllegalStateError<T> internal constructor(override val feedback: Feedback1) : StateError<T>
    class UnknownError<T> internal constructor(override val feedback: Feedback1) : StateError<T>

    companion object {
        fun <T> success(value: T): ParsingResult<T> = Success(value)
        fun <T> failType(): ParsingResult<T> = UnknownTypeError(TODO())
        fun <T> failSyntax(): ParsingResult<T> = InvalidSyntaxError(TODO())
        fun <T> failArgument(): ParsingResult<T> = InvalidArgumentError(TODO())
        fun <T> failState(): ParsingResult<T> = IllegalStateError(TODO())
        fun <T> failUnknown(): ParsingResult<T> = UnknownError(TODO())
    }
}

sealed interface ExecutionResult : Result<Unit> {
    class Success internal constructor() : ExecutionResult, Result.Success<Unit> {
        override val value: Unit = Unit
    }
    class Failure internal constructor(override val feedback: Feedback<out Tuple<String>>) : ExecutionResult, Result.Failure<Unit>

    companion object {
        fun success(): ExecutionResult = Success()
        fun error(feedback: Feedback<*> = Feedback.empty()): ExecutionResult = Failure(feedback)
    }
}

sealed interface SenderValidationResult: Result<Unit> {
    class Success internal constructor() : SenderValidationResult, Result.Success<Unit> {
        override val value: Unit = Unit
    }

    interface Failure : SenderValidationResult, Result.Failure<Unit> {}
    class InvalidSenderError internal constructor(override val feedback: Feedback0): Failure

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
    class InvalidSizeError internal constructor(override val feedback: Feedback1) : PeekingResult, Result.Failure<List<String>>

    companion object {
        fun success(mutableArgs: MutableList<String>): PeekingResult = Success(mutableArgs)
        fun failSize(): InvalidSizeError = InvalidSizeError(TODO())
    }
}

fun <T> Result<T>.isSuccess(): Boolean {
    contract {
        returns(true) implies (this@isSuccess is Success<T>)
        returns(false) implies (this@isSuccess is Failure<T>)
    }
    return this is Success<T>
}

internal fun <T2> Failure<*>.cast(): Failure<T2> {
    // TODO: Handle safer
    return this as Failure<T2>
}
