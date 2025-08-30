package com.zombachu.stick.feedback

import com.zombachu.stick.Environment
import com.zombachu.stick.Invocation
import com.zombachu.stick.Result

interface FailureHandler<out E : Environment, S> {
    fun onFailure(inv: Invocation<@UnsafeVariance E, S>, result: Result.Failure<*>)
}
