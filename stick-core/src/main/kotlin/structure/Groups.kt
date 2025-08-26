package com.zombachu.stick.structure

import com.zombachu.stick.Environment
import com.zombachu.stick.SenderValidationResult
import com.zombachu.stick.element.Group
import com.zombachu.stick.element.GroupImpl
import com.zombachu.stick.element.Groupable
import com.zombachu.stick.impl.SenderScope
import com.zombachu.stick.impl.StructureElement
import com.zombachu.stick.impl.StructureScope

fun <S : Environment, O> SenderScope<S, O>.group(
    vararg elements: StructureElement<S, O, Groupable<S, O, *>>,
    description: String = "",
): StructureElement<S, O, Group<S, O>> = {
    val groupScope = StructureScope<S, O>(
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
