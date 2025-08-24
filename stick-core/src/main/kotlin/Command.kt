package com.zombachu.stick

import com.zombachu.stick.element.Structure
import com.zombachu.stick.impl.SenderScope
import com.zombachu.stick.impl.StructureElement

interface Command<S : SenderContext> : SenderScope<S> {
    val structure: StructureElement<S, Structure<S>>
}
