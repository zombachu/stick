@file:OptIn(ExperimentalContracts::class)

package com.zombachu.stick

import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract

sealed interface Result {
    sealed interface Success : Result
    sealed interface Failure : Result
//    sealed interface Failure : Result {
//        val feedback: Feedback
//    }
}

sealed class SenderValidationResult: Result {
    open class Success internal constructor() : SenderValidationResult(), Result.Success
    open class Failure internal constructor() : SenderValidationResult(), Result.Failure {
        class InvalidSenderError : Failure()
    }
//    open class Failure internal constructor(
//        override val feedback: Feedback
//    ) : SenderValidationResult(), Result.Failure {
//        class InvalidSenderError : Failure()
//    }

    companion object {
        fun success(): Success = Success()
        fun failSender(): Failure.InvalidSenderError = Failure.InvalidSenderError()
    }
}

sealed interface ExecutionResult : ParsingResult<Unit> {
    companion object {
        fun success(): ExecutionResult = ParsingResult.Success.ExecutionSuccess()
        fun error(): ExecutionResult = ParsingResult.Failure.ExecutionError()
    }
}

sealed interface ParsingResult<T> : Result {
    sealed interface Success<T> : ParsingResult<T>, Result.Success {
        val value: T

        class ParsingSuccess<T> internal constructor(override val value: T) : Success<T>
        class ExecutionSuccess internal constructor() : Success<Unit>, ExecutionResult {
            override val value: Unit = Unit
        }
    }
    sealed interface Failure<T> : ParsingResult<T>, Result.Failure {
        class UnknownError<T> internal constructor() : Failure<T>

        class ExecutionError internal constructor() : Failure<Unit>, ExecutionResult

        /**
         * Error when the argument is unable to be parsed.
         */
        class InvalidTypeError<T> internal constructor() : Failure<T>

        /**
         * Error when the command is unable to be resolved.
         */
        class InvalidSyntaxError<T> internal constructor() : Failure<T>

        /**
         * Error when the argument is parsable but invalid for the caller.
         */
        class InvalidArgumentError<T> internal constructor(val message: String) : Failure<T>

        fun <T2> cast(): Failure<T2> {
            // TODO: Handle safer
            return this as Failure<T2>
        }
    }

    companion object {
        fun <T> success(value: T): Success<T> = Success.ParsingSuccess(value)
        fun <T> failUnknown(): Failure.UnknownError<T> = Failure.UnknownError()
        fun <T> failType(): Failure.InvalidTypeError<T> = Failure.InvalidTypeError()
        fun <T> failSyntax(): Failure.InvalidSyntaxError<T> = Failure.InvalidSyntaxError()
        fun <T> failArgument(message: String): Failure.InvalidArgumentError<T> = Failure.InvalidArgumentError(message)

        fun <T> ParsingResult<T>.isSuccess(): Boolean {
            contract {
                returns(true) implies (this@isSuccess is Success<T>)
                returns(false) implies (this@isSuccess is Failure<T>)
            }
            return this is Success<T>
        }
    }
}

internal sealed class PeekingResult : Result {
    class Success internal constructor(private val mutableArgs: MutableList<String>): PeekingResult(), Result.Success {
        val args: List<String> = mutableArgs
        fun consume() {
            mutableArgs.clear()
        }
    }
    class InvalidSizeError internal constructor() : PeekingResult(), Result.Failure

    companion object {
        fun success(mutableArgs: MutableList<String>): Success = Success(mutableArgs)
        fun failSize(): InvalidSizeError = InvalidSizeError()
    }
}
