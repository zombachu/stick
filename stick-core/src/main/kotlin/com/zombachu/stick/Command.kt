package com.zombachu.stick

import com.zombachu.stick.element.Structure
import com.zombachu.stick.impl.CommandScope
import com.zombachu.stick.impl.StructureElement

interface Command<in E : Environment, S> : CommandScope<@UnsafeVariance E, S> {
    val structure: StructureElement<@UnsafeVariance E, S, Structure<@UnsafeVariance E, S>>
}
