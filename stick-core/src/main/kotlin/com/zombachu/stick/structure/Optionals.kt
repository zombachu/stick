package com.zombachu.stick.structure

import com.zombachu.stick.ContextualValue
import com.zombachu.stick.Environment
import com.zombachu.stick.ParsingResult
import com.zombachu.stick.SenderValidationResult
import com.zombachu.stick.element.OptionalParameter
import com.zombachu.stick.element.OptionalParameterImpl
import com.zombachu.stick.element.Parameter
import com.zombachu.stick.impl.BuilderScope
import com.zombachu.stick.impl.InvalidSenderDefault
import com.zombachu.stick.impl.Requirement
import com.zombachu.stick.impl.StructureElement
import com.zombachu.stick.impl.ValidSenderDefault
import com.zombachu.stick.impl.ValidatedDefaultImpl

fun <E : Environment, S, T> BuilderScope<E, S>.default(
    value: ContextualValue<E, S, T>,
    requirement: Requirement<E, S> = requirement { SenderValidationResult.success() },
): ValidSenderDefault<E, S, T> {
    return ValidatedDefaultImpl(value) { requirement.validateSender() }
}

fun <E : Environment, S, T> BuilderScope<E, S>.default(value: T): ValidSenderDefault<E, S, T> =
    default({ ParsingResult.success(value) })

fun <E : Environment, S, T> BuilderScope<E, S>.invalidDefault(
    value: ContextualValue<E, S, T>,
    requirement: Requirement<E, S> = requirement { SenderValidationResult.success() },
): InvalidSenderDefault<E, S, T> {
    return ValidatedDefaultImpl(value) { requirement.validateSender() }
}

fun <E : Environment, S, T> BuilderScope<E, S>.invalidDefault(value: T): InvalidSenderDefault<E, S, T> =
    invalidDefault({ ParsingResult.success(value) })

inline fun <E : Environment, S : Any, reified S2 : S> BuilderScope<E, S>.defaultSender(): ValidSenderDefault<E, S, S2> {
    // TODO: Tell player it's not optional for them
    return default({ ParsingResult.success(sender as S2) }, requirement { it.sender is S2 })
}

fun <E : Environment, S, T> BuilderScope<E, S>.optionally(
    ifInvalid: InvalidSenderDefault<E, S, T>,
    ifAbsent: ValidSenderDefault<E, S, T>,
    parameter: StructureElement<E, S, Parameter<E, S, T>>,
): StructureElement<E, S, OptionalParameter<E, S, T>> = {
    OptionalParameterImpl(
        requirementDefault = ifInvalid,
        presenceDefault = ifAbsent,
        parameter = parameter(this)
    )
}

fun <E : Environment, S, T> BuilderScope<E, S>.optionally(
    ifAbsent: ValidSenderDefault<E, S, T>,
    parameter: StructureElement<E, S, Parameter<E, S, T>>,
): StructureElement<E, S, OptionalParameter<E, S, T>> = {
    OptionalParameterImpl(
        requirementDefault = invalidDefault({ ifAbsent.value(this) }),
        presenceDefault = ifAbsent,
        parameter = parameter(this)
    )
}

fun <E : Environment, S, T> BuilderScope<E, S>.optionallyNullable(
    parameter: StructureElement<E, S, Parameter<E, S, out T>>,
): StructureElement<E, S, OptionalParameter<E, S, T?>> = {
    @Suppress("UNCHECKED_CAST")
    OptionalParameterImpl(
        requirementDefault = invalidDefault(null),
        presenceDefault = default(null),
        parameter = parameter(this) as Parameter<E, S, T?>)
}
