package com.zombachu.stick.element

import com.zombachu.stick.CommandResult
import com.zombachu.stick.Environment
import com.zombachu.stick.Invocation
import com.zombachu.stick.ParsingResult
import com.zombachu.stick.PeekingResult
import com.zombachu.stick.ValidationContext
import com.zombachu.stick.impl.Arguments
import com.zombachu.stick.impl.InvocationImpl
import com.zombachu.stick.impl.Requirement
import com.zombachu.stick.impl.Size
import com.zombachu.stick.propagateError
import com.zombachu.stick.valueOrPropagateError

internal open class StructureImpl<E : Environment, S, T_ : Arguments>(
    override val name: String,
    override val aliases: Set<String>,
    override val description: String,
    internal val requirement: Requirement<E, S>,
    internal val signature: Signature<E, S, T_>,
) : Structure<E, S, T_>  {

    override val size: Size = Size.Deferred
    override val type: ElementType = ElementType.Literal
    override val label: String = name

    context(inv: Invocation<E, S>)
    override fun parse(args: List<String>): CommandResult<T_> {
        val peeked = (inv as InvocationImpl).peek(Size(1))
        if (peeked !is PeekingResult.Success) {
            return ParsingResult.failTypeSyntax(inv.getSyntax())
        }
        val label = peeked.value.first().lowercase()
        if (!matches(label)) {
            return ParsingResult.failTypeSyntax(inv.getSyntax())
        }
        peeked.consume(1)
        validateSender().propagateError { return it }
        val parsedValuesTuple = signature.execute().valueOrPropagateError { return it }
        return ParsingResult.success(parsedValuesTuple, Size(args.size - 1))
    }

    context(validationContext: ValidationContext<E, S>)
    override fun getSyntax(): String {
        val signatureSyntax = signature.getSyntax()
        return if (signatureSyntax.isEmpty()) {
            name
        } else {
            "${name} $signatureSyntax"
        }
    }

    context(validationContext: ValidationContext<E, S>)
    override fun validateSender(): CommandResult<Unit> = requirement.validateSender()
}

internal class TransformedStructure<E : Environment, S, S2 : Any, T_ : Arguments>(
    private val base: Structure<E, S2, T_>,
    private val transform: (S) -> S2,
    private val requirement: Requirement<E, S>,
) : Structure<E, S, T_> {

    override val name: String = base.name
    override val aliases: Set<String> = base.aliases
    override val description: String = base.description
    override val size: Size = base.size
    override val type: ElementType = base.type
    override val label: String = base.label

    context(inv: Invocation<E, S>)
    override fun parse(args: List<String>): CommandResult<T_> {
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
    override fun validateSender(): CommandResult<Unit> = requirement.validateSender()
}
