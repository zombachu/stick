package com.zombachu.stick

import com.zombachu.stick.impl.Requirement
import com.zombachu.stick.impl.SenderScope
import com.zombachu.stick.impl.StructureContext

fun <S> SenderScope<S>.command(
    name: String,
    aliases: Set<String> = setOf(),
    requirement: Requirement<S> = Requirement { true },
    description: String = "",
): StructureContext<S> {
    return StructureContext(
        name.lowercase(),
        aliases.lowercase(),
        description,
        parent = null,
        requirement,
    )
}

fun <S> StructureContext<S>.command(
    name: String,
    aliases: Set<String> = setOf(),
    requirement: Requirement<S> = Requirement { true },
    description: String = "",
): StructureContext<S> {
    return StructureContext(
        name.lowercase(),
        aliases.lowercase(),
        description,
        parent = this,
        requirement,
    )
}
