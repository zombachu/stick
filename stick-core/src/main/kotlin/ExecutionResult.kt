@file:OptIn(ExperimentalContracts::class)

package com.zombachu.stick

import com.zombachu.stick.feedback.Feedback
import com.zombachu.stick.feedback.Feedback0
import com.zombachu.stick.feedback.Feedback1
import com.zombachu.stick.impl.Tuple
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract

sealed interface Result {
    sealed interface Success : Result
    sealed interface Failure : Result {
        val feedback: Feedback<out Tuple<String>>
    }
}

sealed class SenderValidationResult: Result {
    open class Success internal constructor() : SenderValidationResult(), Result.Success
    open class Failure internal constructor(override val feedback: Feedback<out Tuple<String>>) : SenderValidationResult(), Result.Failure {
        class InvalidSenderError internal constructor(feedback: Feedback0): Failure(feedback)
    }

    companion object {
        fun success(): SenderValidationResult = Success()
        fun failSender(): SenderValidationResult = Failure.InvalidSenderError(TODO())
    }
}

sealed interface ExecutionResult : ParsingResult<Unit> {
    class Success internal constructor() : ExecutionResult, ParsingResult.Success<Unit> {
        override val value: Unit = Unit
    }
    class Failure internal constructor(override val feedback: Feedback<out Tuple<String>>) : ExecutionResult, ParsingResult.Failure<Unit>

    companion object {
        fun success(): ExecutionResult = Success()
        fun error(feedback: Feedback<*> = Feedback.empty()): ExecutionResult = Failure(feedback)
    }
}

sealed interface ParsingResult<T> : Result {
    sealed interface Success<T> : ParsingResult<T>, Result.Success {
        val value: T
    }
    class ParsingSuccess<T> internal constructor(override val value: T) : Success<T>

    sealed interface Failure<T> : ParsingResult<T>, Result.Failure {
        fun <T2> cast(): Failure<T2> {
            // TODO: Handle safer
            return this as Failure<T2>
        }
    }
    // TODO: Document potentially ignored
    class UnknownTypeError<T> internal constructor(override val feedback: Feedback1) : Failure<T>

    // TODO: Document causes syntax printing
    sealed interface SyntaxError<T> : Failure<T>
    class InvalidSyntaxError<T> internal constructor(override val feedback: Feedback1) : SyntaxError<T>
    class InvalidArgumentError<T> internal constructor(override val feedback: Feedback1) : SyntaxError<T>

    sealed interface StateError<T> : Failure<T>
    class IllegalStateError<T> internal constructor(override val feedback: Feedback1) : StateError<T>
    class UnknownError<T> internal constructor(override val feedback: Feedback1) : StateError<T>

    companion object {
        fun <T> success(value: T): ParsingResult<T> = ParsingSuccess(value)
        fun <T> failType(): ParsingResult<T> = UnknownTypeError(TODO())
        fun <T> failSyntax(): ParsingResult<T> = InvalidSyntaxError(TODO())
        fun <T> failArgument(): ParsingResult<T> = InvalidArgumentError(TODO())
        fun <T> failState(): ParsingResult<T> = IllegalStateError(TODO())
        fun <T> failUnknown(): ParsingResult<T> = UnknownError(TODO())

        fun <T> ParsingResult<T>.isSuccess(): Boolean {
            contract {
                returns(true) implies (this@isSuccess is Success<T>)
                returns(false) implies (this@isSuccess is Failure<T>)
            }
            return this is Success<T>
        }
    }
}

internal sealed interface PeekingResult : Result {
    class Success internal constructor(private val mutableArgs: MutableList<String>): PeekingResult, Result.Success {
        val args: List<String> = mutableArgs
        fun consume() {
            mutableArgs.clear()
        }
    }
    class InvalidSizeError internal constructor(override val feedback: Feedback1) : PeekingResult, Result.Failure

    companion object {
        fun success(mutableArgs: MutableList<String>): Success = Success(mutableArgs)
        fun failSize(): InvalidSizeError = InvalidSizeError(TODO())
    }
}
