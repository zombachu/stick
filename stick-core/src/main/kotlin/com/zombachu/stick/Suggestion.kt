package com.zombachu.stick

interface Suggestion {

    val value: String

    val index: Int

    fun withIndex(index: Int): Suggestion
}

data class SimpleSuggestion(override val value: String, override val index: Int = 0) : Suggestion {
    override fun withIndex(index: Int): Suggestion = SimpleSuggestion(value, index)
}

fun Iterable<String>.toSuggestions(): List<Suggestion> = map { SimpleSuggestion(it) }

internal fun Aliasable.suggestAliases(): List<Suggestion> = (aliases + label).toSuggestions()
