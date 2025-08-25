package com.zombachu.stick.paper

class Logger

context(logger: Logger)
fun info(s: String) {}

fun noContextInfo(logger: Logger, s: String) {}

fun logMessage(
    f: context(Logger) (String) -> Unit,
) {}

fun noLambdaLogMessage(
    f: context(Logger) (String) -> Unit,
    secondParam: Boolean,
) {}

fun main() {
//    logMessage(::info, false)
    noLambdaLogMessage(::noContextInfo, false)

    logMessage { s: String ->
//        val num = contextOf<Number>()
//        print(it)
    }

    logMessage(::noContextInfo)
}
