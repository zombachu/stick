package com.zombachu.stick

import com.zombachu.stick.element.Structure
import com.zombachu.stick.impl.CommandScope
import com.zombachu.stick.impl.StructureElement

interface Command<E : Environment, S> : CommandScope<E, S> {
    val structure: StructureElement<E, S, Structure<E, S>>
}
