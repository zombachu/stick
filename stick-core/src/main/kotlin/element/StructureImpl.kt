package com.zombachu.stick.element

import com.zombachu.stick.ExecutionContext
import com.zombachu.stick.ExecutionResult
import com.zombachu.stick.ParsingResult
import com.zombachu.stick.PeekingResult
import com.zombachu.stick.Result
import com.zombachu.stick.TypedIdentifier
import com.zombachu.stick.cast
import com.zombachu.stick.impl.ExecutionContextImpl
import com.zombachu.stick.impl.Size
import com.zombachu.stick.isSuccess

internal open class StructureImpl<S>(
    override val id: TypedIdentifier<out Unit>,
    override val aliases: Set<String>,
    override val description: String,
    override val validate: (S) -> Boolean,
    internal val signature: Signature<S>,
) : Structure<S>, Validator<S> {

    override val label by id
    override val size: Size = Size.Deferred
    override val type: ElementType = ElementType.Literal

    override fun parse(context: ExecutionContext<S>, args: List<String>): Result<out Unit> {
        val peeked = (context as ExecutionContextImpl<S>).peek(Size.Companion(1))
        if (peeked !is PeekingResult.Success) {
            return ParsingResult.failType()
        }

        val label = peeked.value.first().lowercase()
        if (!matches(label)) {
            return ParsingResult.failType()
        }
        peeked.consume()

        if (!validate(context.sender)) {
            return ParsingResult.failSyntax()
        }

        val executionResult = signature.execute(context)
        if (!executionResult.isSuccess()) {
            return executionResult.cast()
        }
        return ExecutionResult.success()
    }

    override fun getSyntax(sender: S): String {
        val signatureSyntax = signature.getSyntax(sender)
        return if (signatureSyntax.isEmpty()) {
            id.name
        } else {
            "${id.name} $signatureSyntax"
        }
    }
}
