package com.zombachu.stick

import com.zombachu.stick.element.Structure
import com.zombachu.stick.impl.CommandScope

interface Command<E : Environment, S> : CommandScope<E, S> {
    val structure: Structure<E, S, *>
}
