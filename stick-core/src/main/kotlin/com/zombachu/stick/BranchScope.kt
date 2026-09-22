package com.zombachu.stick

import com.zombachu.stick.element.Parameter

class BranchScope<E : Environment, S, A>
internal constructor(internal val leadingParameter: Parameter<E, S, A, Position.Leading>)
