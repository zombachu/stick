package com.zombachu.stick.element

import com.zombachu.stick.CommandResult
import com.zombachu.stick.Environment
import com.zombachu.stick.Invocation
import com.zombachu.stick.InvocationImpl
import com.zombachu.stick.Requirement
import com.zombachu.stick.SenderValidator
import com.zombachu.stick.Size
import com.zombachu.stick.ValidationContext

internal class TransformedParameter<E : Environment, S : Any, S2 : Any, T>(
    val base: Parameter<E, S2, T>,
    val transform: (S) -> S2,
    val requirement: Requirement<E, S>,
) : ValidatedParameter.UnknownSize<E, S, T>, ValidatedParameter.FixedSize<E, S, T>, SenderValidator<E, S> {

    override val size: Size = base.size
    override val type: ElementType = base.type
    override val name: String = base.name
    override val description: String = base.description

    context(inv: Invocation<E, S>)
    override fun parse(args: List<String>): CommandResult<T> {
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
