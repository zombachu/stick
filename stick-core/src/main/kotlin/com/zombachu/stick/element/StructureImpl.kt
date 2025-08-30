package com.zombachu.stick.element

import com.zombachu.stick.Environment
import com.zombachu.stick.ExecutionResult
import com.zombachu.stick.Invocation
import com.zombachu.stick.ParsingResult
import com.zombachu.stick.PeekingResult
import com.zombachu.stick.Result
import com.zombachu.stick.TypedIdentifier
import com.zombachu.stick.ValidationContext
import com.zombachu.stick.impl.InvocationImpl
import com.zombachu.stick.impl.Requirement
import com.zombachu.stick.impl.Size
import com.zombachu.stick.propagateError

internal open class StructureImpl<E : Environment, S>(
    override val id: TypedIdentifier<Unit>,
    override val aliases: Set<String>,
    override val description: String,
    internal val requirement: Requirement<E, S>,
    internal val signature: Signature<E, S>,
) : Structure<E, S>  {

    override val label by id
    override val size: Size = Size.Deferred
    override val type: ElementType = ElementType.Literal

    context(inv: Invocation<E, S>)
    override fun parse(args: List<String>): Result<Unit> {
        val peeked = (inv as InvocationImpl).peek(Size.Companion(1))
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

    context(validationContext: ValidationContext<E, S>)
    override fun getSyntax(): String {
        val signatureSyntax = signature.getSyntax()
        return if (signatureSyntax.isEmpty()) {
            id.name
        } else {
            "${id.name} $signatureSyntax"
        }
    }

    context(validationContext: ValidationContext<E, S>)
    override fun validateSender(): Result<Unit> = requirement.validateSender()
}

internal class TransformedStructure<E : Environment, S, S2 : Any>(
    val base: Structure<E, S2>,
    val transform: (S) -> S2,
    requirement: Requirement<E, S>,
) : StructureImpl<E, S>(
    base.id,
    base.aliases,
    base.description,
    requirement,
    Signature0(), // TransformedCommand forwards to signature of base command
) {
    context(inv: Invocation<E, S>)
    override fun parse(args: List<String>): Result<Unit> {
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
    override fun validateSender(): Result<Unit> {
        val transformedValidationContext = validationContext.forSender(transform)
        context(transformedValidationContext) {
            return base.validateSender()
        }
    }
}
