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
import com.zombachu.stick.GroupResult
import com.zombachu.stick.ParsingResult
import com.zombachu.stick.Position
import com.zombachu.stick.Requirement
import com.zombachu.stick.SenderValidationResult
import com.zombachu.stick.StructureScope
import com.zombachu.stick.element.Group
import com.zombachu.stick.element.InvalidSenderDefault
import com.zombachu.stick.element.OptionalGroup
import com.zombachu.stick.element.OptionalGroupImpl
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
import com.zombachu.stick.element.SignatureElement
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
): OptionalParameter<E, S, T?, Position.Optional> =
    OptionalParameterImpl(invalidDefault(null), default(null), parameter)

@JvmName("optionallyNullableLast")
fun <E : Environment, S, T> StructureScope<E, S>.optionallyNullable(
    parameter: Parameter<E, S, T, Position.Last>
): OptionalParameter<E, S, T?, Position.LastOptional> =
    OptionalParameterImpl(invalidDefault(null), default(null), parameter)

fun <E : Environment, S, T> StructureScope<E, S>.optionallyNullable(
    parameter: Parameter<E, S, T, Position.Leading>,
    requirement: Requirement<E, S>,
): OptionalParameter<E, S, T?, Position.Optional> =
    OptionalParameterImpl(invalidDefault(null, requirement), default(null), parameter)

@JvmName("optionallyNullableLast")
fun <E : Environment, S, T> StructureScope<E, S>.optionallyNullable(
    parameter: Parameter<E, S, T, Position.Last>,
    requirement: Requirement<E, S>,
): OptionalParameter<E, S, T?, Position.LastOptional> =
    OptionalParameterImpl(invalidDefault(null, requirement), default(null), parameter)

fun <E : Environment, S, G : GroupResult> StructureScope<E, S>.optionally(
    ifInvalid: InvalidSenderDefault<E, S, G>,
    ifAbsent: ValidSenderDefault<E, S, G>,
    group: Group<E, S, G, Position.Leading>,
): OptionalGroup<E, S, G, Position.Optional> = OptionalGroupImpl(ifInvalid, ifAbsent, group)

@JvmName("optionallyLastGroup")
fun <E : Environment, S, G : GroupResult> StructureScope<E, S>.optionally(
    ifInvalid: InvalidSenderDefault<E, S, G>,
    ifAbsent: ValidSenderDefault<E, S, G>,
    group: Group<E, S, G, Position.Last>,
): OptionalGroup<E, S, G, Position.LastOptional> = OptionalGroupImpl(ifInvalid, ifAbsent, group)

fun <E : Environment, S, G : GroupResult> StructureScope<E, S>.optionally(
    ifAbsent: ValidSenderDefault<E, S, G>,
    group: Group<E, S, G, Position.Leading>,
): OptionalGroup<E, S, G, Position.Optional> = optionally(invalidDefault({ ifAbsent.value(this) }), ifAbsent, group)

@JvmName("optionallyLastGroup")
fun <E : Environment, S, G : GroupResult> StructureScope<E, S>.optionally(
    ifAbsent: ValidSenderDefault<E, S, G>,
    group: Group<E, S, G, Position.Last>,
): OptionalGroup<E, S, G, Position.LastOptional> = optionally(invalidDefault({ ifAbsent.value(this) }), ifAbsent, group)

fun <E : Environment, S, G : GroupResult> StructureScope<E, S>.optionallyNullable(
    group: Group<E, S, G, Position.Leading>
): OptionalGroup<E, S, G?, Position.Optional> = OptionalGroupImpl(invalidDefault(null), default(null), group)

@JvmName("optionallyNullableLastGroup")
fun <E : Environment, S, G : GroupResult> StructureScope<E, S>.optionallyNullable(
    group: Group<E, S, G, Position.Last>
): OptionalGroup<E, S, G?, Position.LastOptional> = OptionalGroupImpl(invalidDefault(null), default(null), group)

fun <E : Environment, S, G : GroupResult> StructureScope<E, S>.optionallyNullable(
    group: Group<E, S, G, Position.Leading>,
    requirement: Requirement<E, S>,
): OptionalGroup<E, S, G?, Position.Optional> =
    OptionalGroupImpl(invalidDefault(null, requirement), default(null), group)

@JvmName("optionallyNullableLastGroup")
fun <E : Environment, S, G : GroupResult> StructureScope<E, S>.optionallyNullable(
    group: Group<E, S, G, Position.Last>,
    requirement: Requirement<E, S>,
): OptionalGroup<E, S, G?, Position.LastOptional> =
    OptionalGroupImpl(invalidDefault(null, requirement), default(null), group)

fun <E_ : Environment, S, A, B> StructureScope<E_, S>.optionals(
    elementA: SignatureElement<E_, S, A, Position.Optional>,
    elementB: SignatureElement<E_, S, B, Position.LastOptional>,
): SignatureElement<E_, S, Arguments2<A, B>, Position.Last> = Optionals2Impl(elementA, elementB)

fun <E_ : Environment, S, A, B, C> StructureScope<E_, S>.optionals(
    elementA: SignatureElement<E_, S, A, Position.Optional>,
    elementB: SignatureElement<E_, S, B, Position.Optional>,
    elementC: SignatureElement<E_, S, C, Position.LastOptional>,
): SignatureElement<E_, S, Arguments3<A, B, C>, Position.Last> = Optionals3Impl(elementA, elementB, elementC)

fun <E_ : Environment, S, A, B, C, D> StructureScope<E_, S>.optionals(
    elementA: SignatureElement<E_, S, A, Position.Optional>,
    elementB: SignatureElement<E_, S, B, Position.Optional>,
    elementC: SignatureElement<E_, S, C, Position.Optional>,
    elementD: SignatureElement<E_, S, D, Position.LastOptional>,
): SignatureElement<E_, S, Arguments4<A, B, C, D>, Position.Last> =
    Optionals4Impl(elementA, elementB, elementC, elementD)

fun <E_ : Environment, S, A, B, C, D, E> StructureScope<E_, S>.optionals(
    elementA: SignatureElement<E_, S, A, Position.Optional>,
    elementB: SignatureElement<E_, S, B, Position.Optional>,
    elementC: SignatureElement<E_, S, C, Position.Optional>,
    elementD: SignatureElement<E_, S, D, Position.Optional>,
    elementE: SignatureElement<E_, S, E, Position.LastOptional>,
): SignatureElement<E_, S, Arguments5<A, B, C, D, E>, Position.Last> =
    Optionals5Impl(elementA, elementB, elementC, elementD, elementE)

fun <E_ : Environment, S, A, B, C, D, E, F> StructureScope<E_, S>.optionals(
    elementA: SignatureElement<E_, S, A, Position.Optional>,
    elementB: SignatureElement<E_, S, B, Position.Optional>,
    elementC: SignatureElement<E_, S, C, Position.Optional>,
    elementD: SignatureElement<E_, S, D, Position.Optional>,
    elementE: SignatureElement<E_, S, E, Position.Optional>,
    elementF: SignatureElement<E_, S, F, Position.LastOptional>,
): SignatureElement<E_, S, Arguments6<A, B, C, D, E, F>, Position.Last> =
    Optionals6Impl(elementA, elementB, elementC, elementD, elementE, elementF)

fun <E_ : Environment, S, A, B, C, D, E, F, G> StructureScope<E_, S>.optionals(
    elementA: SignatureElement<E_, S, A, Position.Optional>,
    elementB: SignatureElement<E_, S, B, Position.Optional>,
    elementC: SignatureElement<E_, S, C, Position.Optional>,
    elementD: SignatureElement<E_, S, D, Position.Optional>,
    elementE: SignatureElement<E_, S, E, Position.Optional>,
    elementF: SignatureElement<E_, S, F, Position.Optional>,
    elementG: SignatureElement<E_, S, G, Position.LastOptional>,
): SignatureElement<E_, S, Arguments7<A, B, C, D, E, F, G>, Position.Last> =
    Optionals7Impl(elementA, elementB, elementC, elementD, elementE, elementF, elementG)

fun <E_ : Environment, S, A, B, C, D, E, F, G, H> StructureScope<E_, S>.optionals(
    elementA: SignatureElement<E_, S, A, Position.Optional>,
    elementB: SignatureElement<E_, S, B, Position.Optional>,
    elementC: SignatureElement<E_, S, C, Position.Optional>,
    elementD: SignatureElement<E_, S, D, Position.Optional>,
    elementE: SignatureElement<E_, S, E, Position.Optional>,
    elementF: SignatureElement<E_, S, F, Position.Optional>,
    elementG: SignatureElement<E_, S, G, Position.Optional>,
    elementH: SignatureElement<E_, S, H, Position.LastOptional>,
): SignatureElement<E_, S, Arguments8<A, B, C, D, E, F, G, H>, Position.Last> =
    Optionals8Impl(elementA, elementB, elementC, elementD, elementE, elementF, elementG, elementH)
