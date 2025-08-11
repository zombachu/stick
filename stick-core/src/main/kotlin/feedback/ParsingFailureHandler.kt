package com.zombachu.stick.feedback

import com.zombachu.stick.Result

interface ParsingFailureHandler<S> {
    fun handleFailure(sender: S, result: Result.Failure<*>)
}
