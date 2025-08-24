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

fun <S : SenderContext, T : Any> SenderScope<S>.defaultValidated(
    value: ContextualValue<S, T>,
    requirement: Requirement<S> = requirement { SenderValidationResult.success() },
): ValidatedDefault<S, T> {
    return ValidatedDefault(value, requirement::validateSender)
}

inline fun <S : SenderContext, reified S2 : Any> SenderScope<S>.defaultSender(): ValidatedDefault<S, S2> {
    // TODO: Tell player it's not optional for them
    return defaultValidated({ senderContext as S2 }, requirement() { it is S2 })
}

fun <S : SenderContext, T : Any> SenderScope<S>.optionally(
    validatedDefault: ValidatedDefault<S, T>,
    parameter: StructureElement<S, Parameter<S, T>>,
): StructureElement<S, SignatureConstraint.Terminating<S, T>> = {
    OptionalParameter(validatedDefault, parameter(this))
}

fun <S : SenderContext, T : Any> SenderScope<S>.optionally(
    default: T,
    parameter: StructureElement<S, Parameter<S, T>>,
): StructureElement<S, SignatureConstraint.Terminating<S, T>> =
    optionally(defaultValidated({ default }), parameter)
