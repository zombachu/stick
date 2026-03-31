package com.zombachu.stick.feedback

sealed interface Feedback {
    val message: String

    object Unknown : Feedback {
        override val message get() = "An unknown error has occurred."
    }

    data class TypeNotMatched(val expectedType: String, val provided: String) : Feedback {
        override val message get() = "The argument provided is not a $expectedType: $provided."
    }

    data class InvalidSyntax(val usage: String) : Feedback {
        override val message get() = "Invalid syntax. Correct usage: $usage."
    }

    data class OutOfRange(val min: String, val max: String, val provided: String) : Feedback {
        override val message get() = "The number provided is not in the valid range of $min to $max: $provided."
    }

    data class LiteralNotMatched(val validValues: List<String>, val provided: String) : Feedback {
        override val message get() = "The value provided is not one of ${validValues.joinToString(", ")}: $provided."
    }

    object InvalidSender : Feedback {
        override val message get() = "You are unable to use this command."
    }

    object InvalidSenderType : Feedback {
        override val message get() = "You are unable to use this command."
    }

    object InvalidPermission : Feedback {
        override val message get() = "You do not have permission to use this command."
    }
}

open class CustomFeedback(private val _message: () -> String) : Feedback {
    override val message get() = _message()
}
