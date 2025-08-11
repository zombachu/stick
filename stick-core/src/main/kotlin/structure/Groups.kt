package com.zombachu.stick.structure

import com.zombachu.stick.element.Group
import com.zombachu.stick.element.GroupImpl
import com.zombachu.stick.element.Groupable
import com.zombachu.stick.impl.SenderScope
import com.zombachu.stick.impl.StructureElement
import com.zombachu.stick.impl.StructureScope

fun <S> SenderScope<S>.group(
    vararg elements: StructureElement<S, Groupable<S, *>>,
    description: String = "",
): StructureElement<S, Group<S>> = {
    val groupScope = StructureScope<S>(
        name = "${this.name}_group",
        aliases = setOf(),
        description = "",
        parent = this,
        requirement = { true },
    )
    GroupImpl(
        id(groupScope.name),
        description,
        elements.map { it.invoke(groupScope) }.toList(),
    )
}
