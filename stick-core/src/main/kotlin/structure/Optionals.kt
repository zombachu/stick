package com.zombachu.stick.structure

import com.zombachu.stick.ContextualValue
import com.zombachu.stick.Environment
import com.zombachu.stick.SenderValidationResult
import com.zombachu.stick.element.OptionalParameter
import com.zombachu.stick.element.Parameter
import com.zombachu.stick.element.SignatureConstraint
import com.zombachu.stick.impl.Requirement
import com.zombachu.stick.impl.SenderScope
import com.zombachu.stick.impl.StructureElement
import com.zombachu.stick.impl.ValidatedDefault

fun <E : Environment, O, T : Any> SenderScope<E, O>.defaultValidated(
    value: ContextualValue<E, O, T>,
    requirement: Requirement<E, O> = requirement { SenderValidationResult.success() },
): ValidatedDefault<E, O, T> {
    return ValidatedDefault(value) { requirement.validateSender() }
}

inline fun <E : Environment, O : Any, reified O2 : O> SenderScope<E, O>.defaultSender(): ValidatedDefault<E, O, O2> {
    // TODO: Tell player it's not optional for them
    // TODO: Handle safer
    return defaultValidated({ sender as O2 }, requirement() { it.sender is O2 })
}

fun <E : Environment, O, T : Any> SenderScope<E, O>.optionally(
    validatedDefault: ValidatedDefault<E, O, T>,
    parameter: StructureElement<E, O, Parameter<E, O, T>>,
): StructureElement<E, O, SignatureConstraint.Terminating<E, O, T>> = {
    OptionalParameter(validatedDefault, parameter(this))
}

fun <E : Environment, O, T : Any> SenderScope<E, O>.optionally(
    default: T,
    parameter: StructureElement<E, O, Parameter<E, O, T>>,
): StructureElement<E, O, SignatureConstraint.Terminating<E, O, T>> =
    optionally(defaultValidated({ default }), parameter)
