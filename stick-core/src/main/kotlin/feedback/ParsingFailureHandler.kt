package com.zombachu.stick.feedback

import com.zombachu.stick.ExecutionContext
import com.zombachu.stick.Result
import com.zombachu.stick.SenderContext

interface ParsingFailureHandler<O, S : SenderContext<O>> {
    fun onFailure(context: ExecutionContext<O, S>, result: Result.Failure<*>)
}
