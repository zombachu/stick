package com.zombachu.stick.feedback

object ErrorMessages {
    val NotANumber = FeedbackMessage1("The argument provided is not a number: %0.")
    val OutOfRange = FeedbackMessage3("The number provided is not in the valid range of %0 to %1: %2.")
}

//fun example() {
//    val result = ParsingResult.error(Message.NotANumber, args)
//    result.message.format("optional other message")
//
//    ErrorMessages.NotANumber
//
//    when (result.message) {
//        Message.NotANumber -> result.format("alternate message")
//        Message.NotANumber -> result.format()
//    }
//}
