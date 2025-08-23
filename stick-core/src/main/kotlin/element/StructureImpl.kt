package com.zombachu.stick.element

import com.zombachu.stick.ExecutionContext
import com.zombachu.stick.ExecutionResult
import com.zombachu.stick.ParsingResult
import com.zombachu.stick.PeekingResult
import com.zombachu.stick.Result
import com.zombachu.stick.SenderContext
import com.zombachu.stick.TypedIdentifier
import com.zombachu.stick.impl.ExecutionContextImpl
import com.zombachu.stick.impl.Requirement
import com.zombachu.stick.impl.Size
import com.zombachu.stick.propagateError

internal open class StructureImpl<S>(
    override val id: TypedIdentifier<out Unit>,
    override val aliases: Set<String>,
    override val description: String,
    internal val requirement: Requirement<S>,
    internal val signature: Signature<S>,
) : Structure<S>, SenderValidator<S> {

    override val label by id
    override val size: Size = Size.Deferred
    override val type: ElementType = ElementType.Literal

    override fun parse(context: ExecutionContext<S>, args: List<String>): Result<out Unit> {
        val peeked = (context as ExecutionContextImpl<S>).peek(Size.Companion(1))
        if (peeked !is PeekingResult.Success) {
            return ParsingResult.failTypeSyntax(context.getSyntax())
        }

        val label = peeked.value.first().lowercase()
        if (!matches(label)) {
            return ParsingResult.failTypeSyntax(context.getSyntax())
        }
        peeked.consume()

        validateSender(context).propagateError { return it }
        signature.execute(context).propagateError { return it }
        return ExecutionResult.success()
    }

    override fun validateSender(context: SenderContext<S>): Result<Unit> = requirement.validateSender(context)

    override fun getSyntax(context: SenderContext<S>): String {
        val signatureSyntax = signature.getSyntax(context)
        return if (signatureSyntax.isEmpty()) {
            id.name
        } else {
            "${id.name} $signatureSyntax"
        }
    }
}
