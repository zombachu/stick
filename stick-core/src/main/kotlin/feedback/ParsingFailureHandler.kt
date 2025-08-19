package com.zombachu.stick.feedback

import com.zombachu.stick.ExecutionContext
import com.zombachu.stick.Result

interface ParsingFailureHandler<S> {
    fun onFailure(context: ExecutionContext<S>, result: Result.Failure<*>)
}
