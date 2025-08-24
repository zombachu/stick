package com.zombachu.stick

import com.zombachu.stick.element.Structure
import com.zombachu.stick.impl.SenderScope
import com.zombachu.stick.impl.StructureElement

interface Command<O, S : SenderContext> : SenderScope<O, S> {
    val structure: StructureElement<O, S, Structure<O, S>>
}
