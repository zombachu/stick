package com.zombachu.stick.dsl

import com.zombachu.stick.ContextualValue
import com.zombachu.stick.Environment
import com.zombachu.stick.ParsingResult
import com.zombachu.stick.Position
import com.zombachu.stick.Requirement
import com.zombachu.stick.SenderValidationResult
import com.zombachu.stick.StructureScope
import com.zombachu.stick.element.InvalidSenderDefault
import com.zombachu.stick.element.OptionalParameter
import com.zombachu.stick.element.OptionalParameterImpl
import com.zombachu.stick.element.Parameter
import com.zombachu.stick.element.ValidSenderDefault
import com.zombachu.stick.element.ValidatedDefaultImpl

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
    parameter: Parameter<E, S, T, Position.Leading>,
): OptionalParameter<E, S, T, Position.Optional> =
    OptionalParameterImpl(requirementDefault = ifInvalid, presenceDefault = ifAbsent, parameter = parameter)

@JvmName("optionallyLast")
fun <E : Environment, S, T> StructureScope<E, S>.optionally(
    ifInvalid: InvalidSenderDefault<E, S, T>,
    ifAbsent: ValidSenderDefault<E, S, T>,
    parameter: Parameter<E, S, T, Position.Last>,
): OptionalParameter<E, S, T, Position.LastOptional> =
    OptionalParameterImpl(requirementDefault = ifInvalid, presenceDefault = ifAbsent, parameter = parameter)

fun <E : Environment, S, T> StructureScope<E, S>.optionally(
    ifAbsent: ValidSenderDefault<E, S, T>,
    parameter: Parameter<E, S, T, Position.Leading>,
): OptionalParameter<E, S, T, Position.Optional> =
    optionally(invalidDefault({ ifAbsent.value(this) }), ifAbsent, parameter)

@JvmName("optionallyLast")
fun <E : Environment, S, T> StructureScope<E, S>.optionally(
    ifAbsent: ValidSenderDefault<E, S, T>,
    parameter: Parameter<E, S, T, Position.Last>,
): OptionalParameter<E, S, T, Position.LastOptional> =
    optionally(invalidDefault({ ifAbsent.value(this) }), ifAbsent, parameter)

fun <E : Environment, S, T> StructureScope<E, S>.optionallyNullable(
    parameter: Parameter<E, S, T, Position.Leading>
): OptionalParameter<E, S, T?, Position.Optional> = optionally(invalidDefault(null), default(null), parameter)

@JvmName("optionallyNullableLast")
fun <E : Environment, S, T> StructureScope<E, S>.optionallyNullable(
    parameter: Parameter<E, S, T, Position.Last>
): OptionalParameter<E, S, T?, Position.LastOptional> = optionally(invalidDefault(null), default(null), parameter)
