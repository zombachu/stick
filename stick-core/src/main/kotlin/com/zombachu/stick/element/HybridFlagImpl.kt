package com.zombachu.stick.element

import com.zombachu.stick.Aliasable
import com.zombachu.stick.CommandResult
import com.zombachu.stick.ContextualValue
import com.zombachu.stick.Environment
import com.zombachu.stick.HybridFlagResult
import com.zombachu.stick.Invocation
import com.zombachu.stick.ParsingResult
import com.zombachu.stick.Size
import com.zombachu.stick.ValidationContext
import com.zombachu.stick.element.Parameter.FixedSize
import com.zombachu.stick.propagateError

internal open class HybridFlagImpl<E : Environment, S, T>(
    override val name: String,
    private val parameter: FixedSize<E, S, T>,
    aliases: Set<String>,
) : HybridFlag<E, S, T>, Aliasable {

    override val size: Size = Size.Deferred
    override val type: ElementType = ElementType.Flag
    override val description: String = parameter.description
    override val default: ContextualValue<E, S, HybridFlagResult<T>> = {
        ParsingResult.success(HybridFlagResult.Absent())
    }
    override val label: String = "-${name.lowercase()}"
    override val aliases: Set<String> = aliases.map { "-$it" }.toSet()

    context(inv: Invocation<E, S>)
    override fun parse(args: List<String>): CommandResult<HybridFlagResult<T>> {
        if (args.isEmpty()) return ParsingResult.failTypeInternal()
        if (matches(args.first().lowercase())) {
            if (args.size == 1) {
                return ParsingResult.success(HybridFlagResult.Present(), Size(1))
            } else {
                val result = parameter.parse(args.subList(1, args.size))
                result.propagateError {
                    return it
                }
                return ParsingResult.success(HybridFlagResult.Value(result.value), Size(1) + result.consumed)
            }
        }
        return ParsingResult.failTypeInternal()
    }

    context(validationContext: ValidationContext<E, S>)
    override fun getSyntax(): String = "[$label [${parameter.getGroupedSyntax()}]]"
}
