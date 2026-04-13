package com.zombachu.stick.structure

import com.zombachu.stick.ContextualValue
import com.zombachu.stick.Environment
import com.zombachu.stick.ParsingResult
import com.zombachu.stick.SenderValidationResult
import com.zombachu.stick.element.OptionalParameter
import com.zombachu.stick.element.OptionalParameterImpl
import com.zombachu.stick.element.Parameter
import com.zombachu.stick.impl.InvalidSenderDefault
import com.zombachu.stick.impl.Requirement
import com.zombachu.stick.impl.StructureScope
import com.zombachu.stick.impl.ValidSenderDefault
import com.zombachu.stick.impl.ValidatedDefaultImpl

fun <E : Environment, S, T> StructureScope<E, S>.default(
    value: ContextualValue<E, S, T>,
    requirement: Requirement<E, S> = requirement { SenderValidationResult.success() },
): ValidSenderDefault<E, S, T> = ValidatedDefaultImpl(value) { requirement.validateSender() }

fun <E : Environment, S, T> StructureScope<E, S>.default(
    value: T,
    requirement: Requirement<E, S> = requirement { SenderValidationResult.success() },
): ValidSenderDefault<E, S, T> = ValidatedDefaultImpl({ ParsingResult.success(value) }) { requirement.validateSender() }

fun <E : Environment, S, T> StructureScope<E, S>.invalidDefault(
    value: ContextualValue<E, S, T>,
    requirement: Requirement<E, S> = requirement { SenderValidationResult.success() },
): InvalidSenderDefault<E, S, T> = ValidatedDefaultImpl(value) { requirement.validateSender() }

fun <E : Environment, S, T> StructureScope<E, S>.invalidDefault(
    value: T,
    requirement: Requirement<E, S> = requirement { SenderValidationResult.success() },
): InvalidSenderDefault<E, S, T> =
    ValidatedDefaultImpl({ ParsingResult.success(value) }) { requirement.validateSender() }

inline fun <E : Environment, S : Any, reified S2 : S> StructureScope<E, S>.defaultSender():
    ValidSenderDefault<E, S, S2> = default({ ParsingResult.success(sender as S2) }, requirement { it.sender is S2 })

fun <E : Environment, S, T> StructureScope<E, S>.optionally(
    ifInvalid: InvalidSenderDefault<E, S, T>,
    ifAbsent: ValidSenderDefault<E, S, T>,
    parameter: Parameter<E, S, T>,
): OptionalParameter<E, S, T> =
    OptionalParameterImpl(requirementDefault = ifInvalid, presenceDefault = ifAbsent, parameter = parameter)

fun <E : Environment, S, T> StructureScope<E, S>.optionally(
    ifAbsent: ValidSenderDefault<E, S, T>,
    parameter: Parameter<E, S, T>,
): OptionalParameter<E, S, T> =
    OptionalParameterImpl(
        requirementDefault = invalidDefault({ ifAbsent.value(this) }),
        presenceDefault = ifAbsent,
        parameter = parameter,
    )

fun <E : Environment, S, T> StructureScope<E, S>.optionallyNullable(
    parameter: Parameter<E, S, out T>
): OptionalParameter<E, S, T?> =
    @Suppress("UNCHECKED_CAST")
    OptionalParameterImpl(
        requirementDefault = invalidDefault(null),
        presenceDefault = default(null),
        parameter = parameter as Parameter<E, S, T?>,
    )
