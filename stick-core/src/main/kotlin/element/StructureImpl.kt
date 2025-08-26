package com.zombachu.stick.element

import com.zombachu.stick.ExecutionResult
import com.zombachu.stick.Invocation
import com.zombachu.stick.ParsingResult
import com.zombachu.stick.PeekingResult
import com.zombachu.stick.Result
import com.zombachu.stick.Environment
import com.zombachu.stick.TypedIdentifier
import com.zombachu.stick.impl.InvocationImpl
import com.zombachu.stick.impl.Requirement
import com.zombachu.stick.impl.Size
import com.zombachu.stick.propagateError

internal open class StructureImpl<E : Environment, O>(
    override val id: TypedIdentifier<out Unit>,
    override val aliases: Set<String>,
    override val description: String,
    internal val requirement: Requirement<E, O>,
    internal val signature: Signature<E, O>,
) : Structure<E, O>, SenderValidator<E, O> {

    override val label by id
    override val size: Size = Size.Deferred
    override val type: ElementType = ElementType.Literal

    context(env: E, inv: Invocation<E, O>)
    override fun parse(args: List<String>): Result<out Unit> {
        val peeked = (inv as InvocationImpl<E, O>).peek(Size.Companion(1))
        if (peeked !is PeekingResult.Success) {
            return ParsingResult.failTypeSyntax(inv.getSyntax())
        }

        val label = peeked.value.first().lowercase()
        if (!matches(label)) {
            return ParsingResult.failTypeSyntax(inv.getSyntax())
        }
        peeked.consume()

        validateSender().propagateError { return it }
        signature.execute().propagateError { return it }
        return ExecutionResult.success()
    }

    context(env: E)
    override fun getSyntax(): String {
        val signatureSyntax = signature.getSyntax()
        return if (signatureSyntax.isEmpty()) {
            id.name
        } else {
            "${id.name} $signatureSyntax"
        }
    }

    context(env: E)
    override fun validateSender(): Result<Unit> = requirement.validateSender()
}
