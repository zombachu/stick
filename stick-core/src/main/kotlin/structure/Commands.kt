package com.zombachu.stick.structure

import com.zombachu.stick.Command
import com.zombachu.stick.SenderContext
import com.zombachu.stick.SenderValidationResult
import com.zombachu.stick.impl.Requirement
import com.zombachu.stick.impl.StructureScope
import com.zombachu.stick.lowercase

fun <S : SenderContext, O> Command<S, O>.command(
    name: String,
    aliases: Set<String> = setOf(),
    requirement: Requirement<S, O> = requirement { SenderValidationResult.success() },
    description: String = "",
): StructureScope<S, O> {
    return StructureScope(
        name.lowercase(),
        aliases.lowercase(),
        description,
        parent = null,
        requirement,
    )
}

fun <S : SenderContext, O> StructureScope<S, O>.command(
    name: String,
    aliases: Set<String> = setOf(),
    requirement: Requirement<S, O> = requirement { SenderValidationResult.success() },
    description: String = "",
): StructureScope<S, O> {
    return StructureScope(
        name.lowercase(),
        aliases.lowercase(),
        description,
        parent = if (this.parent == null) null else this,
        requirement,
    )
}
