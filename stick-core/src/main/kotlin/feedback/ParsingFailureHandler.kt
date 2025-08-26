package com.zombachu.stick.feedback

import com.zombachu.stick.Invocation
import com.zombachu.stick.Result
import com.zombachu.stick.Environment

interface ParsingFailureHandler<E : Environment, S> {
    fun onFailure(context: Invocation<E, S>, result: Result.Failure<*>)
}
