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

internal open class StructureImpl<S : Environment, O>(
    override val id: TypedIdentifier<out Unit>,
    override val aliases: Set<String>,
    override val description: String,
    internal val requirement: Requirement<S, O>,
    internal val signature: Signature<S, O>,
) : Structure<S, O>, SenderValidator<S, O> {

    override val label by id
    override val size: Size = Size.Deferred
    override val type: ElementType = ElementType.Literal

    context(env: S, inv: Invocation<S, O>)
    override fun parse(args: List<String>): Result<out Unit> {
        val peeked = (inv as InvocationImpl<S, O>).peek(Size.Companion(1))
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

    context(env: S)
    override fun getSyntax(): String {
        val signatureSyntax = signature.getSyntax()
        return if (signatureSyntax.isEmpty()) {
            id.name
        } else {
            "${id.name} $signatureSyntax"
        }
    }

    context(env: S)
    override fun validateSender(): Result<Unit> = requirement.validateSender()
}
