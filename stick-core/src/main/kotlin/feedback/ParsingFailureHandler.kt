package com.zombachu.stick.feedback

import com.zombachu.stick.Invocation
import com.zombachu.stick.Result
import com.zombachu.stick.Environment

interface ParsingFailureHandler<S : Environment, O> {
    fun onFailure(context: Invocation<S, O>, result: Result.Failure<*>)
}
