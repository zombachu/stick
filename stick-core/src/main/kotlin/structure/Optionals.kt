package com.zombachu.stick.structure

import com.zombachu.stick.ContextualValue
import com.zombachu.stick.SenderContext
import com.zombachu.stick.SenderValidationResult
import com.zombachu.stick.element.OptionalParameter
import com.zombachu.stick.element.Parameter
import com.zombachu.stick.element.SignatureConstraint
import com.zombachu.stick.impl.Requirement
import com.zombachu.stick.impl.SenderScope
import com.zombachu.stick.impl.StructureElement
import com.zombachu.stick.impl.ValidatedDefault
import com.zombachu.stick.transformSender

fun <O, S : SenderContext, T : Any> SenderScope<O, S>.defaultValidated(
    value: ContextualValue<O, S, T>,
    requirement: Requirement<O, S> = requirement { SenderValidationResult.success() },
): ValidatedDefault<O, S, T> {
    return ValidatedDefault(value, requirement::validateSender)
}

inline fun <O : Any, S : SenderContext, reified O2 : O> SenderScope<O, S>.defaultSender(): ValidatedDefault<O, S, S> {
    // TODO: Tell player it's not optional for them
    // TODO: Handle safer
    return defaultValidated({ senderContext.transformSender<O, O2, S> { it as O2 } }, requirement() { it.sender is O2 })
}

fun <O, S : SenderContext, T : Any> SenderScope<O, S>.optionally(
    validatedDefault: ValidatedDefault<O, S, T>,
    parameter: StructureElement<O, S, Parameter<O, S, T>>,
): StructureElement<O, S, SignatureConstraint.Terminating<O, S, T>> = {
    OptionalParameter(validatedDefault, parameter(this))
}

fun <O, S : SenderContext, T : Any> SenderScope<O, S>.optionally(
    default: T,
    parameter: StructureElement<O, S, Parameter<O, S, T>>,
): StructureElement<O, S, SignatureConstraint.Terminating<O, S, T>> =
    optionally(defaultValidated({ default }), parameter)
