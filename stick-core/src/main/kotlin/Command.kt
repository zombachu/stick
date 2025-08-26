package com.zombachu.stick

import com.zombachu.stick.element.Structure
import com.zombachu.stick.impl.SenderScope
import com.zombachu.stick.impl.StructureElement

interface Command<E : Environment, S> : SenderScope<E, S> {
    val structure: StructureElement<E, S, Structure<E, S>>
    fun createEnvironment(): E
}
