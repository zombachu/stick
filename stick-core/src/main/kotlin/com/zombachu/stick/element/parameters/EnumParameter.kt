package com.zombachu.stick.element.parameters

import com.zombachu.stick.AliasEntry
import com.zombachu.stick.Aliasable
import com.zombachu.stick.Environment
import com.zombachu.stick.Invocation
import com.zombachu.stick.ParsingResult
import com.zombachu.stick.Result
import com.zombachu.stick.TypedIdentifier
import com.zombachu.stick.ValidationContext
import com.zombachu.stick.element.ElementType
import com.zombachu.stick.element.Parameter
import com.zombachu.stick.lowercase

open class EnumParameter<E : Environment, S, T : Enum<T>>(
    id: TypedIdentifier<T>,
    description: String,
    val primaryValues: Map<String, T>,
    val aliasedValues: Map<String, T>,
) : Parameter.Size1<E, S, T>(id, description) {

    override val type: ElementType = ElementType.Literal

    context(inv: Invocation<E, S>)
    override fun parse(arg0: String): Result<T> {
        val enumValue = primaryValues[arg0]
            ?: aliasedValues[arg0]
            ?: return ParsingResult.failLiteral(primaryValues.keys.toList(), arg0)
        return ParsingResult.success(enumValue)
    }

    context(validationContext: ValidationContext<E, S>)
    override fun getSyntax(): String = "<${primaryValues.keys.joinToString("|")}>"
}

class EnumEntry<T : Enum<T>>(val enumValue: T, label: String, aliases: Set<String> = setOf()) : Aliasable {
    override val label: String = label.lowercase()
    override val aliases: Set<String> = aliases.lowercase()
}

infix fun <T : Enum<T>> T.by(label: String): EnumEntry<T> = EnumEntry(this, label, setOf())

infix fun <T : Enum<T>> T.by(entry: AliasEntry): EnumEntry<T> = EnumEntry(this, entry.label, entry.aliases)
