@file:OptIn(ExperimentalContracts::class)

package com.zombachu.stick

import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract

sealed interface Result {
    sealed interface Success : Result
    sealed interface Failure : Result
}

sealed class SenderValidationResult: Result {
    open class Success internal constructor() : SenderValidationResult(), Result.Success
    open class Failure internal constructor() : SenderValidationResult(), Result.Failure {
        class InvalidSenderError : Failure()
    }

    companion object {
        fun success(): Success = Success()
        fun failSender(): Failure.InvalidSenderError = Failure.InvalidSenderError()
    }
}

sealed class ExecutionResult : Result {
    class Success internal constructor() : ExecutionResult(), Result.Success
    class Failure internal constructor() : ExecutionResult(), Result.Failure {
        fun <T> toParsingResult(): ParsingResult.Failure<T> {
            return ParsingResult.Failure.ExecutionError(this)
        }
    }

    companion object {
        fun success(): Success = Success()
        fun error(): Failure = Failure()
    }
}

sealed class ParsingResult<T> : ExecutionResult() {
    class Success<T> internal constructor(val value: T): ParsingResult<T>(), Result.Success
    sealed class Failure<T> : ParsingResult<T>(), Result.Failure {
        class UnknownError<T> internal constructor() : Failure<T>()

        /**
         * Error when the argument is unable to be parsed.
         */
        class InvalidTypeError<T> internal constructor() : Failure<T>()

        /**
         * Error when the command is unable to be resolved.
         */
        class InvalidSyntaxError<T> internal constructor() : Failure<T>()

        /**
         * Error when the argument is parsable but invalid for the caller.
         */
        class InvalidArgumentError<T> internal constructor(val message: String) : Failure<T>()

        class ExecutionError<T> internal constructor(val executionResult: ExecutionResult.Failure) : Failure<T>()

        fun <T2> cast(): Failure<T2> {
            // TODO: Handle safer
            return this as Failure<T2>
        }
    }

    companion object {
        fun success(): Success<Unit> = Success(Unit)
        fun <T> success(value: T): Success<T> = Success(value)
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
