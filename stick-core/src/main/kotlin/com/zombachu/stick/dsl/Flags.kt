package com.zombachu.stick.dsl

import com.zombachu.stick.CommandResult
import com.zombachu.stick.ContextualValue
import com.zombachu.stick.Environment
import com.zombachu.stick.Invocation
import com.zombachu.stick.StructureScope
import com.zombachu.stick.element.FlagParameter
import com.zombachu.stick.element.HybridFlag
import com.zombachu.stick.element.HybridFlagImpl
import com.zombachu.stick.element.Parameter
import com.zombachu.stick.element.ValueFlag
import com.zombachu.stick.element.ValueFlagImpl
import com.zombachu.stick.element.parameters.EnumParameter
import com.zombachu.stick.lowercase
import com.zombachu.stick.success

fun <E : Environment, S> StructureScope<E, S>.flag(
    name: String,
    aliases: Set<String> = [],
    description: String = "",
): ValueFlag<E, S, Boolean> =
    ValueFlagImpl(
        name,
        { success(false) },
        FlagParameter.PresenceFlagParameter(name, { success(true) }, aliases.lowercase(), description),
    )

fun <E : Environment, S, T> StructureScope<E, S>.flag(
    name: String,
    present: Invocation<E, S>.() -> CommandResult<T>,
    default: ContextualValue<E, S, T>,
    aliases: Set<String> = [],
    description: String = "",
): ValueFlag<E, S, T> =
    ValueFlagImpl(
        name,
        default,
        FlagParameter.PresenceFlagParameter(name, present, aliases.lowercase(), description),
    )

fun <E : Environment, S, T> StructureScope<E, S>.valueFlag(
    name: String,
    parameter: Parameter.Bounded<E, S, T>,
    default: ContextualValue<E, S, T>,
    aliases: Set<String> = [],
): ValueFlag<E, S, T> =
    ValueFlagImpl(name, default, FlagParameter.ParameterFlagParameter(name, parameter, aliases.lowercase()))

fun <E : Environment, S, T> StructureScope<E, S>.valueFlag(
    name: String,
    parameter: Parameter.Bounded<E, S, T>,
    default: T,
    aliases: Set<String> = [],
): ValueFlag<E, S, T> = valueFlag(name, parameter, { success(default) }, aliases.lowercase())

fun <E : Environment, S, T> StructureScope<E, S>.valueFlag(
    name: String,
    parameter: Parameter.Bounded<E, S, T>,
    default: Nothing?,
    aliases: Set<String> = [],
): ValueFlag<E, S, T?> =
    ValueFlagImpl(
        name,
        { success(default) },
        FlagParameter.ParameterFlagParameter(name, parameter, aliases.lowercase()),
    )

fun <E : Environment, S, T : Enum<T>> StructureScope<E, S>.enumFlag(
    parameter: EnumParameter<E, S, T>,
    default: ContextualValue<E, S, T>,
): ValueFlag<E, S, T> = ValueFlagImpl(parameter.name, default, FlagParameter.EnumFlagParameter(parameter))

fun <E : Environment, S, T : Enum<T>> StructureScope<E, S>.enumFlag(
    parameter: EnumParameter<E, S, T>,
    default: T,
): ValueFlag<E, S, T> = enumFlag(parameter, { success(default) })

fun <E : Environment, S, T : Enum<T>> StructureScope<E, S>.enumFlag(
    parameter: EnumParameter<E, S, T>,
    default: Nothing?,
): ValueFlag<E, S, T?> = ValueFlagImpl(parameter.name, { success(default) }, FlagParameter.EnumFlagParameter(parameter))

fun <E : Environment, S, T> StructureScope<E, S>.hybridFlag(
    name: String,
    parameter: Parameter.Bounded<E, S, T>,
    aliases: Set<String> = [],
): HybridFlag<E, S, T> = HybridFlagImpl(name, parameter, aliases.lowercase())
