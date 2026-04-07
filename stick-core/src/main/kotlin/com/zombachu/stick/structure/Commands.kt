package com.zombachu.stick.structure

import com.zombachu.stick.Environment
import com.zombachu.stick.SenderValidationResult
import com.zombachu.stick.element.Structure
import com.zombachu.stick.impl.Arguments
import com.zombachu.stick.impl.CommandScope
import com.zombachu.stick.impl.Requirement
import com.zombachu.stick.impl.StructureScope
import com.zombachu.stick.lowercase
import kotlin.properties.ReadOnlyProperty

fun <E : Environment, S, T_ : Arguments> CommandScope<E, S>.structure(
    structure: StructureScope<E, S>.() -> Structure<E, S, T_>,
): ReadOnlyProperty<Any?, Structure<E, S, T_>> =
    ReadOnlyProperty { _, _, -> structure(StructureScope.empty()) }

fun <E : Environment, S> StructureScope<E, S>.command(
    name: String,
    aliases: Set<String> = setOf(),
    requirement: Requirement<E, S> = requirement { SenderValidationResult.success() },
    description: String = "",
): StructureScope<E, S> {
    return StructureScope(
        name.lowercase(),
        aliases.lowercase(),
        description,
        parent = if (this.parent == null) null else this,
        requirement,
    )
}
