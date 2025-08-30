package com.zombachu.stick.structure

import com.zombachu.stick.ContextualValue
import com.zombachu.stick.Environment
import com.zombachu.stick.SenderValidationResult
import com.zombachu.stick.element.OptionalParameter
import com.zombachu.stick.element.Parameter
import com.zombachu.stick.element.SignatureConstraint
import com.zombachu.stick.impl.BuilderScope
import com.zombachu.stick.impl.Requirement
import com.zombachu.stick.impl.StructureElement
import com.zombachu.stick.impl.ValidatedDefault

fun <E : Environment, S, T : Any> BuilderScope<E, S>.defaultValidated(
    value: ContextualValue<E, S, T>,
    requirement: Requirement<E, S> = requirement { SenderValidationResult.success() },
): ValidatedDefault<E, S, T> {
    return ValidatedDefault(value) { requirement.validateSender() }
}

inline fun <E : Environment, S : Any, reified S2 : S> BuilderScope<E, S>.defaultSender(): ValidatedDefault<E, S, S2> {
    // TODO: Tell player it's not optional for them
    return defaultValidated({ sender as S2 }, requirement() { it.sender is S2 })
}

fun <E : Environment, S, T : Any> BuilderScope<E, S>.optionally(
    validatedDefault: ValidatedDefault<E, S, T>,
    parameter: StructureElement<E, S, Parameter<E, S, T>>,
): StructureElement<E, S, SignatureConstraint.Terminating<E, S, T>> = {
    OptionalParameter(validatedDefault, parameter(this))
}

fun <E : Environment, S, T : Any> BuilderScope<E, S>.optionally(
    default: T,
    parameter: StructureElement<E, S, Parameter<E, S, T>>,
): StructureElement<E, S, SignatureConstraint.Terminating<E, S, T>> =
    optionally(defaultValidated({ default }), parameter)
