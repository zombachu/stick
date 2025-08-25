package com.zombachu.stick

import com.zombachu.stick.element.Structure
import com.zombachu.stick.impl.SenderScope
import com.zombachu.stick.impl.StructureElement

interface Command<S : SenderContext, O> : SenderScope<S, O> {
    val structure: StructureElement<S, O, Structure<S, O>>
    fun createSenderContext(sender: Any): S
}
