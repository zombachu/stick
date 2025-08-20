package com.zombachu.stick.structure

import com.zombachu.stick.Command
import com.zombachu.stick.impl.Requirement
import com.zombachu.stick.impl.StructureScope
import com.zombachu.stick.lowercase

fun <S> Command<S>.command(
    name: String,
    aliases: Set<String> = setOf(),
    requirement: Requirement<S> = Requirement { true },
    description: String = "",
): StructureScope<S> {
    return StructureScope(
        name.lowercase(),
        aliases.lowercase(),
        description,
        parent = null,
        requirement,
    )
}

fun <S> StructureScope<S>.command(
    name: String,
    aliases: Set<String> = setOf(),
    requirement: Requirement<S> = Requirement { true },
    description: String = "",
): StructureScope<S> {
    return StructureScope(
        name.lowercase(),
        aliases.lowercase(),
        description,
        parent = if (this.parent == null) null else this,
        requirement,
    )
}
