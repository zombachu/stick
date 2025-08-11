package com.zombachu.stick.structure

import com.zombachu.stick.ContextualValue
import com.zombachu.stick.element.OptionalParameter
import com.zombachu.stick.element.Parameter
import com.zombachu.stick.element.SignatureConstraint
import com.zombachu.stick.impl.SenderScope
import com.zombachu.stick.impl.StructureElement
import com.zombachu.stick.impl.ValidatedDefault

fun <S, T : Any> SenderScope<S>.defaultValidated(
    value: ContextualValue<S, T>,
    validate: (S) -> Boolean = { true },
): ValidatedDefault<S, T> {
    return ValidatedDefault(value, validate)
}

inline fun <S, reified S2 : Any> SenderScope<S>.defaultSender(): ValidatedDefault<S, S2> {
    return defaultValidated({ sender as S2 }, { it is S2 })
}

fun <S, T : Any> SenderScope<S>.optionally(
    validatedDefault: ValidatedDefault<S, T>,
    parameter: StructureElement<S, Parameter<S, T>>,
): StructureElement<S, SignatureConstraint.Terminating<S, T>> = {
    OptionalParameter(validatedDefault, parameter(this))
}

fun <S, T : Any> SenderScope<S>.optionally(
    default: T,
    parameter: StructureElement<S, Parameter<S, T>>,
): StructureElement<S, SignatureConstraint.Terminating<S, T>> =
    optionally(defaultValidated({ default }), parameter)
