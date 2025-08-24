package com.zombachu.stick

typealias ContextualValue<S, O, T> = ExecutionContext<S, O>.() -> T
