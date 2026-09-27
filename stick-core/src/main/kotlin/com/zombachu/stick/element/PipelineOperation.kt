package com.zombachu.stick.element

import com.zombachu.stick.CommandResult
import com.zombachu.stick.Execution

typealias PipelineOperation<E, S, A, B> = Execution<E, S>.(A) -> CommandResult<B>
