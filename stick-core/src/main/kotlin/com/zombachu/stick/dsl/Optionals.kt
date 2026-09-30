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
import com.zombachu.stick.Position
import com.zombachu.stick.StructureScope
import com.zombachu.stick.element.GatedDefault
import com.zombachu.stick.element.Group
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
import com.zombachu.stick.success

fun <E : Environment, S, T> StructureScope<E, S>.optionally(
    parameter: Parameter<E, S, T, Position.Leading>,
    default: T,
): OptionalParameter<E, S, T, Position.Optional> = OptionalParameterImpl(parameter, { success(default) }, null)

fun <E : Environment, S, T> StructureScope<E, S>.optionally(
    parameter: Parameter<E, S, T, Position.Leading>,
    default: ContextualValue<E, S, T>,
): OptionalParameter<E, S, T, Position.Optional> = OptionalParameterImpl(parameter, default, null)

fun <E : Environment, S, T> StructureScope<E, S>.optionally(
    parameter: Parameter<E, S, T, Position.Leading>,
    default: Nothing?,
): OptionalParameter<E, S, T?, Position.Optional> = OptionalParameterImpl(parameter, { success(default) }, null)

fun <E : Environment, S, T> StructureScope<E, S>.optionally(
    parameter: Parameter<E, S, T, Position.Leading>,
    default: GatedDefault<E, S, T>,
): OptionalParameter<E, S, T, Position.Optional> = OptionalParameterImpl(parameter, default.value, default)

@JvmName("optionallyLast")
fun <E : Environment, S, T> StructureScope<E, S>.optionally(
    parameter: Parameter<E, S, T, Position.Last>,
    default: T,
): OptionalParameter<E, S, T, Position.LastOptional> = OptionalParameterImpl(parameter, { success(default) }, null)

@JvmName("optionallyLast")
fun <E : Environment, S, T> StructureScope<E, S>.optionally(
    parameter: Parameter<E, S, T, Position.Last>,
    default: ContextualValue<E, S, T>,
): OptionalParameter<E, S, T, Position.LastOptional> = OptionalParameterImpl(parameter, default, null)

@JvmName("optionallyLast")
fun <E : Environment, S, T> StructureScope<E, S>.optionally(
    parameter: Parameter<E, S, T, Position.Last>,
    default: Nothing?,
): OptionalParameter<E, S, T?, Position.LastOptional> = OptionalParameterImpl(parameter, { success(default) }, null)

@JvmName("optionallyLast")
fun <E : Environment, S, T> StructureScope<E, S>.optionally(
    parameter: Parameter<E, S, T, Position.Last>,
    default: GatedDefault<E, S, T>,
): OptionalParameter<E, S, T, Position.LastOptional> = OptionalParameterImpl(parameter, default.value, default)

fun <E : Environment, S, G : GroupResult> StructureScope<E, S>.optionally(
    group: Group<E, S, G, Position.Leading>,
    default: G,
): OptionalGroup<E, S, G, Position.Optional> = OptionalGroupImpl(group, { success(default) }, null)

fun <E : Environment, S, G : GroupResult> StructureScope<E, S>.optionally(
    group: Group<E, S, G, Position.Leading>,
    default: ContextualValue<E, S, G>,
): OptionalGroup<E, S, G, Position.Optional> = OptionalGroupImpl(group, default, null)

fun <E : Environment, S, G : GroupResult> StructureScope<E, S>.optionally(
    group: Group<E, S, G, Position.Leading>,
    default: Nothing?,
): OptionalGroup<E, S, G?, Position.Optional> = OptionalGroupImpl(group, { success(default) }, null)

fun <E : Environment, S, G : GroupResult> StructureScope<E, S>.optionally(
    group: Group<E, S, G, Position.Leading>,
    default: GatedDefault<E, S, G>,
): OptionalGroup<E, S, G, Position.Optional> = OptionalGroupImpl(group, default.value, default)

@JvmName("optionallyLastGroup")
fun <E : Environment, S, G : GroupResult> StructureScope<E, S>.optionally(
    group: Group<E, S, G, Position.Last>,
    default: G,
): OptionalGroup<E, S, G, Position.LastOptional> = OptionalGroupImpl(group, { success(default) }, null)

@JvmName("optionallyLastGroup")
fun <E : Environment, S, G : GroupResult> StructureScope<E, S>.optionally(
    group: Group<E, S, G, Position.Last>,
    default: ContextualValue<E, S, G>,
): OptionalGroup<E, S, G, Position.LastOptional> = OptionalGroupImpl(group, default, null)

@JvmName("optionallyLastGroup")
fun <E : Environment, S, G : GroupResult> StructureScope<E, S>.optionally(
    group: Group<E, S, G, Position.Last>,
    default: Nothing?,
): OptionalGroup<E, S, G?, Position.LastOptional> = OptionalGroupImpl(group, { success(default) }, null)

@JvmName("optionallyLastGroup")
fun <E : Environment, S, G : GroupResult> StructureScope<E, S>.optionally(
    group: Group<E, S, G, Position.Last>,
    default: GatedDefault<E, S, G>,
): OptionalGroup<E, S, G, Position.LastOptional> = OptionalGroupImpl(group, default.value, default)

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
