package com.zombachu.stick.structure

import com.zombachu.stick.SenderContext
import com.zombachu.stick.SenderValidationResult
import com.zombachu.stick.element.Group
import com.zombachu.stick.element.GroupImpl
import com.zombachu.stick.element.Groupable
import com.zombachu.stick.impl.SenderScope
import com.zombachu.stick.impl.StructureElement
import com.zombachu.stick.impl.StructureScope

fun <O, S : SenderContext<O>> SenderScope<O, S>.group(
    vararg elements: StructureElement<O, S, Groupable<O, S, *>>,
    description: String = "",
): StructureElement<O, S, Group<O, S>> = {
    val groupScope = StructureScope<O, S>(
        name = "${this.name}_group",
        aliases = setOf(),
        description = "",
        parent = this,
        requirement = requirement { SenderValidationResult.success() },
    )
    GroupImpl(
        id(groupScope.name),
        description,
        elements.map { it.invoke(groupScope) }.toList(),
    )
}
