package com.zombachu.stick.failure

import kotlin.reflect.KClass

sealed interface Reason {
    fun message(origin: FailureOrigin): String

    data class Unknown(val cause: Throwable? = null) : Reason {
        override fun message(origin: FailureOrigin) = "An unknown error has occurred."
    }

    data class TypeNotMatched(val expectedType: String, val provided: String) : Reason {
        override fun message(origin: FailureOrigin) = "The argument provided is not a $expectedType: $provided."
    }

    data object InvalidSyntax : Reason {
        override fun message(origin: FailureOrigin) = "Invalid syntax. Correct usage: ${origin.usage}."
    }

    data class OutOfRange(val min: String, val max: String, val provided: String) : Reason {
        override fun message(origin: FailureOrigin) =
            "The number provided is not in the valid range of $min to $max: $provided."
    }

    data class LiteralNotMatched(val validValues: List<String>, val provided: String) : Reason {
        override fun message(origin: FailureOrigin) =
            "The value provided is not one of ${validValues.joinToString(", ")}: $provided."
    }

    object InvalidSender : Reason {
        override fun message(origin: FailureOrigin) = "You are unable to use this command."
    }

    data class InvalidSenderType(val required: KClass<*>) : Reason {
        override fun message(origin: FailureOrigin) = "You are unable to use this command."
    }

    object InvalidPermission : Reason {
        override fun message(origin: FailureOrigin) = "You do not have permission to use this command."
    }
}

interface CustomReason : Reason
