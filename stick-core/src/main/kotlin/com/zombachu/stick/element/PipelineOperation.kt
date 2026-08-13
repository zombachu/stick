package com.zombachu.stick.element

import com.zombachu.stick.CommandResult
import com.zombachu.stick.Invocation

typealias PipelineOperation<E, S, A, B> = Invocation<E, S>.(A) -> CommandResult<B>
