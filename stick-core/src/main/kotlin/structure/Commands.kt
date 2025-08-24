package com.zombachu.stick.structure

import com.zombachu.stick.Command
import com.zombachu.stick.SenderContext
import com.zombachu.stick.SenderValidationResult
import com.zombachu.stick.impl.Requirement
import com.zombachu.stick.impl.StructureScope
import com.zombachu.stick.lowercase

fun <O, S : SenderContext> Command<O, S>.command(
    name: String,
    aliases: Set<String> = setOf(),
    requirement: Requirement<O, S> = requirement { SenderValidationResult.success() },
    description: String = "",
): StructureScope<O, S> {
    return StructureScope(
        name.lowercase(),
        aliases.lowercase(),
        description,
        parent = null,
        requirement,
    )
}

fun <O, S : SenderContext> StructureScope<O, S>.command(
    name: String,
    aliases: Set<String> = setOf(),
    requirement: Requirement<O, S> = requirement { SenderValidationResult.success() },
    description: String = "",
): StructureScope<O, S> {
    return StructureScope(
        name.lowercase(),
        aliases.lowercase(),
        description,
        parent = if (this.parent == null) null else this,
        requirement,
    )
}
