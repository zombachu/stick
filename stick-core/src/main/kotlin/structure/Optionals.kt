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

fun <S : SenderContext, O, T : Any> SenderScope<S, O>.defaultValidated(
    value: ContextualValue<S, O, T>,
    requirement: Requirement<S, O> = requirement { SenderValidationResult.success() },
): ValidatedDefault<S, O, T> {
    return ValidatedDefault(value) { requirement.validateSender() }
}

inline fun <S : SenderContext, O : Any, reified O2 : O> SenderScope<S, O>.defaultSender(): ValidatedDefault<S, O, O2> {
    // TODO: Tell player it's not optional for them
    // TODO: Handle safer
    return defaultValidated({ sender as O2 }, requirement() { it.sender is O2 })
}

fun <S : SenderContext, O, T : Any> SenderScope<S, O>.optionally(
    validatedDefault: ValidatedDefault<S, O, T>,
    parameter: StructureElement<S, O, Parameter<S, O, T>>,
): StructureElement<S, O, SignatureConstraint.Terminating<S, O, T>> = {
    OptionalParameter(validatedDefault, parameter(this))
}

fun <S : SenderContext, O, T : Any> SenderScope<S, O>.optionally(
    default: T,
    parameter: StructureElement<S, O, Parameter<S, O, T>>,
): StructureElement<S, O, SignatureConstraint.Terminating<S, O, T>> =
    optionally(defaultValidated({ default }), parameter)
