package com.zombachu.stick

import com.zombachu.stick.element.Structure

interface Command<in E : Environment, S> : CommandScope<E, S> {
    val structure: Structure<E, S, *>
}
