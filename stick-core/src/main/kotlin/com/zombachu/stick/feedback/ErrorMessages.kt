package com.zombachu.stick.feedback

import com.zombachu.stick.impl.Array2

object ErrorMessages {
    val Empty = Feedback0("")
    val Unknown = Feedback0("An unknown error has occurred.")
    val NotAType = FeedbackMessage2("The argument provided is not a %0: %1.")
    val InvalidSyntax = FeedbackMessage1("Invalid syntax. Correct usage: %0.")
    val OutOfRange = FeedbackMessage3("The number provided is not in the valid range of %0 to %1: %2.")
    val InvalidLiteral = PreformattedFeedbackMessage<Array2<String>>("The value provided is not one of %0: %1.")
    val InvalidSender = Feedback0("You are unable to use this command.")
    val InvalidSenderType = Feedback0("You are unable to use this command.")
}
