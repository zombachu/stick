package com.zombachu.stick.structure

import com.zombachu.stick.Environment
import com.zombachu.stick.SenderValidationResult
import com.zombachu.stick.element.Group
import com.zombachu.stick.element.GroupImpl
import com.zombachu.stick.element.Groupable
import com.zombachu.stick.impl.SenderScope
import com.zombachu.stick.impl.StructureElement
import com.zombachu.stick.impl.StructureScope

fun <E : Environment, S> SenderScope<E, S>.group(
    vararg elements: StructureElement<E, S, Groupable<E, S, out Any>>,
    description: String = "",
): StructureElement<E, S, Group<E, S>> = {
    val groupScope = StructureScope<E, S>(
        name = "${this.name}_group",
        aliases = setOf(),
        description = "",
        parent = this,
        requirement = requirement { SenderValidationResult.success() },
    )
    GroupImpl(
        id(groupScope.name),
        description,
        elements.map {
            @Suppress("UNCHECKED_CAST")
            it.invoke(groupScope) as Groupable<E, S, Any>
        }.toList(),
    )
}
