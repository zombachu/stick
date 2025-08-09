package com.zombachu.stick

interface Aliasable {
    val label: String
    val aliases: Set<String>

    fun matches(arg: String): Boolean = arg == label || aliases.contains(arg)
}

fun Set<String>.lowercase(): Set<String> = this.map { it.lowercase() }.toSet()
