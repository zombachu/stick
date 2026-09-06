package com.zombachu.stick.dsl

import com.zombachu.stick.Arguments2
import com.zombachu.stick.Arguments3
import com.zombachu.stick.Arguments4
import com.zombachu.stick.Arguments5
import com.zombachu.stick.Arguments6
import com.zombachu.stick.Arguments7
import com.zombachu.stick.Arguments8
import com.zombachu.stick.ContextualValue
import com.zombachu.stick.Environment
import com.zombachu.stick.ParsingResult
import com.zombachu.stick.Position
import com.zombachu.stick.Requirement
import com.zombachu.stick.SenderValidationResult
import com.zombachu.stick.StructureScope
import com.zombachu.stick.element.Element
import com.zombachu.stick.element.InvalidSenderDefault
import com.zombachu.stick.element.OptionalParameter
import com.zombachu.stick.element.OptionalParameterImpl
import com.zombachu.stick.element.Optionals2Impl
import com.zombachu.stick.element.Optionals3Impl
import com.zombachu.stick.element.Optionals4Impl
import com.zombachu.stick.element.Optionals5Impl
import com.zombachu.stick.element.Optionals6Impl
import com.zombachu.stick.element.Optionals7Impl
import com.zombachu.stick.element.Optionals8Impl
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

fun <E_ : Environment, S, A, B> StructureScope<E_, S>.optionals(
    elementA: Element.Positioned<E_, S, A, Position.Optional>,
    elementB: Element.Positioned<E_, S, B, Position.LastOptional>,
): Element.Positioned<E_, S, Arguments2<A, B>, Position.Last> = Optionals2Impl(elementA, elementB)

fun <E_ : Environment, S, A, B, C> StructureScope<E_, S>.optionals(
    elementA: Element.Positioned<E_, S, A, Position.Optional>,
    elementB: Element.Positioned<E_, S, B, Position.Optional>,
    elementC: Element.Positioned<E_, S, C, Position.LastOptional>,
): Element.Positioned<E_, S, Arguments3<A, B, C>, Position.Last> = Optionals3Impl(elementA, elementB, elementC)

fun <E_ : Environment, S, A, B, C, D> StructureScope<E_, S>.optionals(
    elementA: Element.Positioned<E_, S, A, Position.Optional>,
    elementB: Element.Positioned<E_, S, B, Position.Optional>,
    elementC: Element.Positioned<E_, S, C, Position.Optional>,
    elementD: Element.Positioned<E_, S, D, Position.LastOptional>,
): Element.Positioned<E_, S, Arguments4<A, B, C, D>, Position.Last> =
    Optionals4Impl(elementA, elementB, elementC, elementD)

fun <E_ : Environment, S, A, B, C, D, E> StructureScope<E_, S>.optionals(
    elementA: Element.Positioned<E_, S, A, Position.Optional>,
    elementB: Element.Positioned<E_, S, B, Position.Optional>,
    elementC: Element.Positioned<E_, S, C, Position.Optional>,
    elementD: Element.Positioned<E_, S, D, Position.Optional>,
    elementE: Element.Positioned<E_, S, E, Position.LastOptional>,
): Element.Positioned<E_, S, Arguments5<A, B, C, D, E>, Position.Last> =
    Optionals5Impl(elementA, elementB, elementC, elementD, elementE)

fun <E_ : Environment, S, A, B, C, D, E, F> StructureScope<E_, S>.optionals(
    elementA: Element.Positioned<E_, S, A, Position.Optional>,
    elementB: Element.Positioned<E_, S, B, Position.Optional>,
    elementC: Element.Positioned<E_, S, C, Position.Optional>,
    elementD: Element.Positioned<E_, S, D, Position.Optional>,
    elementE: Element.Positioned<E_, S, E, Position.Optional>,
    elementF: Element.Positioned<E_, S, F, Position.LastOptional>,
): Element.Positioned<E_, S, Arguments6<A, B, C, D, E, F>, Position.Last> =
    Optionals6Impl(elementA, elementB, elementC, elementD, elementE, elementF)

fun <E_ : Environment, S, A, B, C, D, E, F, G> StructureScope<E_, S>.optionals(
    elementA: Element.Positioned<E_, S, A, Position.Optional>,
    elementB: Element.Positioned<E_, S, B, Position.Optional>,
    elementC: Element.Positioned<E_, S, C, Position.Optional>,
    elementD: Element.Positioned<E_, S, D, Position.Optional>,
    elementE: Element.Positioned<E_, S, E, Position.Optional>,
    elementF: Element.Positioned<E_, S, F, Position.Optional>,
    elementG: Element.Positioned<E_, S, G, Position.LastOptional>,
): Element.Positioned<E_, S, Arguments7<A, B, C, D, E, F, G>, Position.Last> =
    Optionals7Impl(elementA, elementB, elementC, elementD, elementE, elementF, elementG)

fun <E_ : Environment, S, A, B, C, D, E, F, G, H> StructureScope<E_, S>.optionals(
    elementA: Element.Positioned<E_, S, A, Position.Optional>,
    elementB: Element.Positioned<E_, S, B, Position.Optional>,
    elementC: Element.Positioned<E_, S, C, Position.Optional>,
    elementD: Element.Positioned<E_, S, D, Position.Optional>,
    elementE: Element.Positioned<E_, S, E, Position.Optional>,
    elementF: Element.Positioned<E_, S, F, Position.Optional>,
    elementG: Element.Positioned<E_, S, G, Position.Optional>,
    elementH: Element.Positioned<E_, S, H, Position.LastOptional>,
): Element.Positioned<E_, S, Arguments8<A, B, C, D, E, F, G, H>, Position.Last> =
    Optionals8Impl(elementA, elementB, elementC, elementD, elementE, elementF, elementG, elementH)
