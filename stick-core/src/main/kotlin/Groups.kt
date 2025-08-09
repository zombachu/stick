package com.zombachu.stick

import com.zombachu.stick.impl.Group
import com.zombachu.stick.impl.Groupable
import com.zombachu.stick.impl.SenderScope
import com.zombachu.stick.impl.StructureContext
import com.zombachu.stick.impl.StructureElement

data class GroupResult<T : Any>(
    val id: TypedIdentifier<out T>,
    val value: T,
) {
    inline fun <T2 : Any> on(id: TypedIdentifier<T2>, handler: (T2) -> Unit) {
        if (id == this.id) {
            handler(value as T2)
        }
    }
}

fun <S> SenderScope<S>.group(
    vararg elements: StructureElement<S, Groupable<S, *>>,
    description: String = "",
): StructureElement<S, Group<S>> = {
    val groupScope = StructureContext<S>(
        name = "${this.name}_group",
        aliases = setOf(),
        description = "",
        parent = this,
        requirement = { true },
    )
    Group(
        id(groupScope.name),
        description,
        elements.map { it.invoke(groupScope) }.toList(),
    )
}
