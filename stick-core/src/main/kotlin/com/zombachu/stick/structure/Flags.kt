package com.zombachu.stick.structure

import com.zombachu.stick.ContextualValue
import com.zombachu.stick.Environment
import com.zombachu.stick.ParsingResult
import com.zombachu.stick.element.FlagParameter
import com.zombachu.stick.element.HybridFlag
import com.zombachu.stick.element.HybridFlagImpl
import com.zombachu.stick.element.Parameter
import com.zombachu.stick.element.ValueFlag
import com.zombachu.stick.element.ValueFlagImpl
import com.zombachu.stick.element.parameters.EnumParameter
import com.zombachu.stick.impl.StructureScope
import com.zombachu.stick.lowercase

fun <E : Environment, S> StructureScope<E, S>.flag(
    name: String,
    aliases: Set<String> = setOf(),
    description: String = "",
): ValueFlag<E, S, Boolean> =
    ValueFlagImpl(
        name,
        { ParsingResult.success(false) },
        FlagParameter.PresenceFlagParameter(name, { ParsingResult.success(true) }, aliases.lowercase(), description)
    )

fun <E : Environment, S, T> StructureScope<E, S>.flag(
    name: String,
    default: ContextualValue<E, S, T>,
    presentValue: ContextualValue<E, S, T>,
    aliases: Set<String> = setOf(),
    description: String = "",
): ValueFlag<E, S, T> =
    ValueFlagImpl(
        name,
        default,
        FlagParameter.PresenceFlagParameter(name, presentValue, aliases.lowercase(), description)
    )

fun <E : Environment, S, T> StructureScope<E, S>.valueFlag(
    name: String,
    default: ContextualValue<E, S, T>,
    parameter: Parameter.FixedSize<E, S, T>,
    aliases: Set<String> = setOf(),
): ValueFlag<E, S, T> =
    ValueFlagImpl(
        name,
        default,
        FlagParameter.ParameterFlagParameter(parameter, aliases.lowercase())
    )

fun <E : Environment, S, T> StructureScope<E, S>.valueFlag(
    name: String,
    default: T,
    parameter: Parameter.FixedSize<E, S, T>,
    aliases: Set<String> = setOf(),
): ValueFlag<E, S, T> =
    valueFlag(name, { ParsingResult.success(default) }, parameter, aliases.lowercase())

fun <E : Environment, S, T> StructureScope<E, S>.nullableValueFlag(
    name: String,
    parameter: Parameter.FixedSize<E, S, T>,
    aliases: Set<String> = setOf(),
): ValueFlag<E, S, T?> =
    @Suppress("UNCHECKED_CAST")
    ValueFlagImpl(
        name,
        { ParsingResult.success(null) },
        FlagParameter.ParameterFlagParameter(parameter, aliases.lowercase()) as FlagParameter.ParameterFlagParameter<E, S, T?>
    )

fun <E : Environment, S, T : Enum<T>> StructureScope<E, S>.enumFlag(
    default: ContextualValue<E, S, T>,
    from: EnumParameter<E, S, T>,
): ValueFlag<E, S, T> =
    ValueFlagImpl(from.name, default, FlagParameter.EnumFlagParameter(from))

fun <E : Environment, S, T : Enum<T>> StructureScope<E, S>.enumFlag(
    default: T,
    from: EnumParameter<E, S, T>,
): ValueFlag<E, S, T> =
    enumFlag({ ParsingResult.success(default) }, from)

fun <E : Environment, S, T : Enum<T>> StructureScope<E, S>.nullableEnumFlag(
    from: EnumParameter<E, S, T>,
): ValueFlag<E, S, T?> =
    @Suppress("UNCHECKED_CAST")
    ValueFlagImpl(
        from.name,
        { ParsingResult.success(null) },
        FlagParameter.EnumFlagParameter(from) as FlagParameter.EnumFlagParameter<E, S, T?>
    )

fun <E : Environment, S, T> StructureScope<E, S>.hybridFlag(
    name: String,
    parameter: Parameter.FixedSize<E, S, T>,
    aliases: Set<String> = setOf(),
): HybridFlag<E, S, T> =
    HybridFlagImpl(name, parameter, aliases)
