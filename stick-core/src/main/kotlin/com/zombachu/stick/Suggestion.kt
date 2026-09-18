package com.zombachu.stick

interface Suggestion {

    val value: String

    val index: Int

    val isAlias: Boolean

    fun withIndex(index: Int): Suggestion
}

data class SimpleSuggestion(
    override val value: String,
    override val index: Int = 0,
    override val isAlias: Boolean = false,
) : Suggestion {
    override fun withIndex(index: Int): Suggestion = SimpleSuggestion(value, index, isAlias)
}

fun Iterable<String>.toSuggestions(isAlias: Boolean = false): List<Suggestion> = map {
    SimpleSuggestion(it, isAlias = isAlias)
}

internal fun Aliasable.suggestAliases(): List<Suggestion> =
    aliases.toSuggestions(isAlias = true) + SimpleSuggestion(label)
