package com.zombachu.stick.structure

import com.zombachu.stick.Environment
import com.zombachu.stick.SenderValidationResult
import com.zombachu.stick.impl.CommandScope
import com.zombachu.stick.impl.Requirement
import com.zombachu.stick.impl.StructureScope
import com.zombachu.stick.lowercase

fun <E : Environment, S> CommandScope<E, S>.command(
    name: String,
    aliases: Set<String> = setOf(),
    requirement: Requirement<E, S> = requirement { SenderValidationResult.success() },
    description: String = "",
): StructureScope<E, S> {
    return StructureScope(
        name.lowercase(),
        aliases.lowercase(),
        description,
        parent = null,
        requirement,
    )
}

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
