package com.zombachu.stick.element

import com.zombachu.stick.Arguments
import com.zombachu.stick.CommandResult
import com.zombachu.stick.Environment
import com.zombachu.stick.Invocation
import com.zombachu.stick.InvocationImpl
import com.zombachu.stick.ParsingResult
import com.zombachu.stick.PeekingResult
import com.zombachu.stick.Requirement
import com.zombachu.stick.Size
import com.zombachu.stick.ValidationContext
import com.zombachu.stick.propagateError
import com.zombachu.stick.valueOrPropagateError

internal open class StructureImpl<E : Environment, S, T_ : Arguments>(
    override val name: String,
    override val aliases: Set<String>,
    override val description: String,
    internal val requirement: Requirement<E, S>,
    internal val signature: Signature<E, S, T_>,
) : Structure<E, S, T_> {

    override val size: Size = Size.atLeast(1)
    override val type: ElementType = ElementType.Literal
    override val label: String = name

    context(inv: Invocation<E, S>)
    override fun parse(args: List<String>): CommandResult<T_> {
        val peeked = (inv as InvocationImpl).peek(Size(1))
        if (peeked !is PeekingResult.Success) {
            return ParsingResult.failTypeInternal()
        }
        val label = peeked.value.first().lowercase()
        if (!matches(label)) {
            return ParsingResult.failTypeInternal()
        }
        peeked.consume(1)
        validateSender().propagateError {
            return it
        }
        val parsedValuesTuple =
            signature.execute().valueOrPropagateError {
                return it
            }
        return ParsingResult.success(parsedValuesTuple)
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
