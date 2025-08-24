package com.zombachu.stick.feedback

import com.zombachu.stick.ExecutionContext
import com.zombachu.stick.Result
import com.zombachu.stick.SenderContext

interface ParsingFailureHandler<S : SenderContext, O> {
    fun onFailure(context: ExecutionContext<S, O>, result: Result.Failure<*>)
}
