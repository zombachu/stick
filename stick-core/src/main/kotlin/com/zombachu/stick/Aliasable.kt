package com.zombachu.stick

interface Aliasable {
    val label: String
    val aliases: Set<String>

    fun matches(arg: String): Boolean = arg == label || aliases.contains(arg)
}

class AliasEntry(override val label: String, override val aliases: Set<String> = setOf()) : Aliasable

fun Set<String>.lowercase(): Set<String> = this.map { it.lowercase() }.toSet()

operator fun String.plus(aliases: Set<String>): AliasEntry = AliasEntry(this, aliases)
