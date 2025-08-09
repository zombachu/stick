package com.zombachu.stick.impl

import com.zombachu.stick.ExecutionContext
import com.zombachu.stick.ExecutionResult
import com.zombachu.stick.ParsingResult
import com.zombachu.stick.PeekingResult
import com.zombachu.stick.Result
import com.zombachu.stick.TypedIdentifier

interface Structure<S> : Groupable<S, Unit>, SignatureConstraint.Terminating<S, Unit>, Validator<S>, Aliasable

internal open class StructureImpl<S>(
    override val id: TypedIdentifier<out Unit>,
    override val aliases: Set<String>,
    override val description: String,
    override val validate: (S) -> Boolean,
    internal val signature: Signature<S>,
) : Structure<S> {

    override val label by id
    override val size: Size = Size.Deferred
    override val type: ElementType = ElementType.Literal

    override fun parse(context: ExecutionContext<S>, args: List<String>): ParsingResult<out Unit> {
        val peeked = context.peek(Size(1))
        if (peeked !is PeekingResult.Success) {
            return ParsingResult.failType()
        }

        val label = peeked.args.first().lowercase()
        if (!matches(label)) {
            return ParsingResult.failType()
        }
        peeked.consume()

        if (!validate(context.sender)) {
            return ParsingResult.failSyntax()
        }

        val result = signature.execute(context)
        when (result) {
            is Result.Success -> return ParsingResult.success()
            is ParsingResult.Failure<*> -> return result.cast()
            is ExecutionResult.Failure -> return result.toParsingResult()
        }
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
