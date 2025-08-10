package com.zombachu.stick.feedback

object ErrorMessages {
    object NotANumber : FeedbackMessage1("The argument provided is not a number: %0.")
    object OutOfRange : FeedbackMessage3("The number provided is not in the valid range of %0 to %1: %2.")
}
