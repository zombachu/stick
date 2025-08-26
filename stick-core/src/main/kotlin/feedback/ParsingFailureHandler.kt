package com.zombachu.stick.feedback

import com.zombachu.stick.Invocation
import com.zombachu.stick.Result
import com.zombachu.stick.Environment

interface ParsingFailureHandler<E : Environment, O> {
    fun onFailure(context: Invocation<E, O>, result: Result.Failure<*>)
}
