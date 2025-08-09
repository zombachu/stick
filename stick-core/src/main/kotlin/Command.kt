package com.zombachu.stick

import com.zombachu.stick.impl.SenderScope
import com.zombachu.stick.impl.Structure
import com.zombachu.stick.impl.StructureElement

interface Command<S> : SenderScope<S> {
    val structure: StructureElement<S, Structure<S>>
}
