package com.zombachu.stick.feedback

import com.zombachu.stick.ParsingResult

interface ParsingFailureHandler<S> {
    fun handleFailure(sender: S, result: ParsingResult.Failure<*>)
}
