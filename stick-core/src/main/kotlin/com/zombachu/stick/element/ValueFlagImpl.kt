package com.zombachu.stick.element

import com.zombachu.stick.Aliasable
import com.zombachu.stick.CommandResult
import com.zombachu.stick.ContextualValue
import com.zombachu.stick.Environment
import com.zombachu.stick.Invocation
import com.zombachu.stick.ParsingResult
import com.zombachu.stick.ParsingResult.LiteralNotMatchedError
import com.zombachu.stick.Size
import com.zombachu.stick.ValidationContext
import com.zombachu.stick.element.parameters.EnumParameter
import com.zombachu.stick.withSize

internal open class ValueFlagImpl<E : Environment, S, T>(
    override val name: String,
    override val default: ContextualValue<E, S, T>,
    private val flagParameter: FlagParameter<E, S, T>,
) : ValueFlag<E, S, T> {

    override val size: Size.Fixed = flagParameter.size
    override val type: ElementType = ElementType.Flag
    override val description: String = flagParameter.description

    context(inv: Invocation<E, S>)
    override fun parse(args: List<String>): CommandResult<T> {
        return flagParameter.parse(args).withSize(size)
    }

    context(validationContext: ValidationContext<E, S>)
    override fun getSyntax(): String = flagParameter.getSyntax()
}

internal sealed class FlagParameter<E : Environment, S, T>(
    size: Size.Fixed,
    name: String,
    aliases: Set<String>,
    description: String,
) : Parameter.FixedSize<E, S, T>(size, name, description), Aliasable {

    override val label: String = "-${name.lowercase()}"
    override val aliases: Set<String> = aliases.map { "-$it" }.toSet()

    internal class PresenceFlagParameter<E : Environment, S, T>(
        name: String,
        private val presentValue: ContextualValue<E, S, T>,
        aliases: Set<String>,
        description: String,
    ) : FlagParameter<E, S, T>(Size(1), name, aliases, description) {

        context(inv: Invocation<E, S>)
        override fun parse(args: List<String>): CommandResult<T> {
            if (matches(args.first().lowercase())) {
                return inv.presentValue()
            }
            return ParsingResult.failTypeInternal()
        }

        context(validationContext: ValidationContext<E, S>)
        override fun getSyntax(): String = "[$label]"
    }

    internal class ParameterFlagParameter<E : Environment, S, T>(
        name: String,
        private val parameter: FixedSize<E, S, T>,
        aliases: Set<String>,
    ) : FlagParameter<E, S, T>(Size(1) + parameter.size, name, aliases, parameter.description) {

        context(inv: Invocation<E, S>)
        override fun parse(args: List<String>): CommandResult<T> {
            if (matches(args.first().lowercase())) {
                return parameter.parse(args.subList(1, args.size)).withSize(size)
            }
            return ParsingResult.failTypeInternal()
        }

        context(validationContext: ValidationContext<E, S>)
        override fun getSyntax(): String = "[$label ${parameter.getSyntax()}]"
    }

    internal class EnumFlagParameter<E : Environment, S, T : Enum<T>>(
        private val enumParameter: EnumParameter<E, S, T>
    ) :
        FlagParameter<E, S, T>(
            enumParameter.size,
            enumParameter.name,
            enumParameter.primaryValues.keys + enumParameter.aliasedValues.keys,
            enumParameter.description,
        ) {
        private val primaryValues = enumParameter.primaryValues.keys.toList().map { "-$it" }

        context(inv: Invocation<E, S>)
        override fun parse(args: List<String>): CommandResult<T> {
            if (args.isEmpty()) return ParsingResult.failTypeInternal()

            // Ignore the - before passing it to the enum parameter
            val arg = args.first().substring(1)
            val result = enumParameter.parse(arg)
            if (result is LiteralNotMatchedError) {
                return ParsingResult.failTypeInternal()
            }
            return result
        }

        context(validationContext: ValidationContext<E, S>)
        override fun getSyntax(): String = "[${primaryValues.joinToString("|")}]"
    }
}
