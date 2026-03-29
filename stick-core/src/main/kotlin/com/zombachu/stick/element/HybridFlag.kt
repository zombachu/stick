package com.zombachu.stick.element

import com.zombachu.stick.Aliasable
import com.zombachu.stick.CommandResult
import com.zombachu.stick.ContextualValue
import com.zombachu.stick.Environment
import com.zombachu.stick.HybridFlagResult
import com.zombachu.stick.Invocation
import com.zombachu.stick.ParsingResult
import com.zombachu.stick.ValidationContext
import com.zombachu.stick.element.Parameter.FixedSize
import com.zombachu.stick.impl.InvalidSenderDefault
import com.zombachu.stick.impl.InvocationImpl
import com.zombachu.stick.impl.Requirement
import com.zombachu.stick.impl.Size
import com.zombachu.stick.valueOrPropagateError

internal open class HybridFlagImpl<E : Environment, S, T>(
    override val name: String,
    private val parameter: FixedSize<E, S, T>,
    aliases: Set<String>,
): HybridFlag<E, S, T>, Aliasable {

    override val size: Size = Size.Unbounded
    override val type: ElementType = ElementType.Flag
    override val description: String = parameter.description
    override val default: ContextualValue<E, S, HybridFlagResult<T>> = { ParsingResult.success(HybridFlagResult.Absent()) }
    override val label: String = "-${name}"
    override val aliases: Set<String> = aliases.map { "-$it" }.toSet()

    context(inv: Invocation<E, S>)
    override fun parse(args: List<String>): CommandResult<HybridFlagResult<T>> {
        if (args.isEmpty()) return ParsingResult.failTypeInternal()
        if (matches(args.first().lowercase())) {
            if (args.size > 1) {
                val value = parameter.parse(args.subList(1, args.size)).valueOrPropagateError { return it }
                return ParsingResult.success(HybridFlagResult.Value(value))
            } else {
                return ParsingResult.success(HybridFlagResult.Present())
            }
        }
        return ParsingResult.failTypeInternal()
    }

    context(validationContext: ValidationContext<E, S>)
    override fun getSyntax(): String = "[$label [${parameter.getSyntax()}]]"
}

internal class TransformedHybridFlag<E : Environment, S, S2 : Any, T>(
    private val base: HybridFlag<E, S2, T>,
    private val transform: (S) -> S2,
    private val invalidSenderDefault: InvalidSenderDefault<E, S, HybridFlagResult<T>>,
) : HybridFlag<E, S, T>, Flag.Validated<E, S, HybridFlagResult<T>> {

    override val size: Size = base.size
    override val type: ElementType = ElementType.Flag
    override val name: String = base.name
    override val description: String = base.description
    override val invalidDefault: ContextualValue<E, S, HybridFlagResult<T>> = invalidSenderDefault.value
    override val default: ContextualValue<E, S, HybridFlagResult<T>> = { ParsingResult.success(HybridFlagResult.Absent()) }

    context(inv: Invocation<E, S>)
    override fun parse(args: List<String>): CommandResult<HybridFlagResult<T>> {
        val transformedInvocation = (inv as InvocationImpl).forSender(transform)
        context(transformedInvocation) {
            return base.parse(args)
        }
    }

    context(validationContext: ValidationContext<E, S>)
    override fun getSyntax(): String {
        val transformedValidationContext = validationContext.forSender(transform)
        context(transformedValidationContext) {
            return base.getSyntax()
        }
    }

    context(validationContext: ValidationContext<E, S>)
    override fun validateSender(): CommandResult<Unit> = invalidSenderDefault.validateSender()
}
