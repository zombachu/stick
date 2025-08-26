package com.zombachu.stick

import com.zombachu.stick.element.Structure
import com.zombachu.stick.impl.SenderScope
import com.zombachu.stick.impl.StructureElement

interface Command<E : Environment, O> : SenderScope<E, O> {
    val structure: StructureElement<E, O, Structure<E, O>>
    fun createEnvironment(sender: Any): E
}
