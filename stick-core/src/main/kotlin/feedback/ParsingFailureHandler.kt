package com.zombachu.stick.feedback

import com.zombachu.stick.Invocation
import com.zombachu.stick.Result
import com.zombachu.stick.SenderContext

interface ParsingFailureHandler<S : SenderContext, O> {
    fun onFailure(context: Invocation<S, O>, result: Result.Failure<*>)
}
