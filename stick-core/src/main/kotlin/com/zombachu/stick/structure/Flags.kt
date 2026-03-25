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
import com.zombachu.stick.impl.BuilderScope
import com.zombachu.stick.impl.StructureElement
import com.zombachu.stick.lowercase

fun <E : Environment, S> BuilderScope<E, S>.flag(
    name: String,
    aliases: Set<String> = setOf(),
    description: String = "",
): StructureElement<E, S, ValueFlag<E, S, Boolean>> = {
    ValueFlagImpl(
        name,
        { ParsingResult.success(false) },
        FlagParameter.PresenceFlagParameter(name, { ParsingResult.success(true) }, aliases.lowercase(), description)
    )
}

fun <E : Environment, S, T> BuilderScope<E, S>.flag(
    name: String,
    default: ContextualValue<E, S, T>,
    presentValue: ContextualValue<E, S, T>,
    aliases: Set<String> = setOf(),
    description: String = "",
): StructureElement<E, S, ValueFlag<E, S, T>> = {
    ValueFlagImpl(
        name,
        default,
        FlagParameter.PresenceFlagParameter(name, presentValue, aliases.lowercase(), description)
    )
}

fun <E : Environment, S, T> BuilderScope<E, S>.valueFlag(
    name: String,
    default: ContextualValue<E, S, T>,
    parameter: StructureElement<E, S, Parameter.FixedSize<E, S, T>>,
    aliases: Set<String> = setOf(),
): StructureElement<E, S, ValueFlag<E, S, T>> = {
    ValueFlagImpl(
        name,
        default,
        FlagParameter.ParameterFlagParameter(parameter(this), aliases.lowercase())
    )
}

fun <E : Environment, S, T> BuilderScope<E, S>.valueFlag(
    name: String,
    default: T,
    parameter: StructureElement<E, S, Parameter.FixedSize<E, S, T>>,
    aliases: Set<String> = setOf(),
): StructureElement<E, S, ValueFlag<E, S, T>> =
    valueFlag(name, { ParsingResult.success(default) }, parameter, aliases.lowercase())

fun <E : Environment, S, T> BuilderScope<E, S>.nullableValueFlag(
    name: String,
    parameter: StructureElement<E, S, Parameter.FixedSize<E, S, out T>>,
    aliases: Set<String> = setOf(),
): StructureElement<E, S, ValueFlag<E, S, T?>> = {
    @Suppress("UNCHECKED_CAST")
    ValueFlagImpl(
        name,
        { ParsingResult.success(null) },
        FlagParameter.ParameterFlagParameter(parameter(this) as Parameter.FixedSize<E, S, T?>, aliases.lowercase())
    )
}

fun <E : Environment, S, T : Enum<T>> BuilderScope<E, S>.enumFlag(
    default: ContextualValue<E, S, T>,
    from: StructureElement<E, S, EnumParameter<E, S, T>>,
): StructureElement<E, S, ValueFlag<E, S, T>> = {
    val enumParameter = from(this)
    ValueFlagImpl(enumParameter.name, default, FlagParameter.MultiFlagParameter(enumParameter))
}
fun <E : Environment, S, T : Enum<T>> BuilderScope<E, S>.enumFlag(
    default: T,
    from: StructureElement<E, S, EnumParameter<E, S, T>>,
): StructureElement<E, S, ValueFlag<E, S, T>> = enumFlag({ ParsingResult.success(default) }, from)

fun <E : Environment, S, T> BuilderScope<E, S>.hybridFlag(
    parameter: StructureElement<E, S, Parameter.FixedSize<E, S, T>>,
    aliases: Set<String> = setOf(),
): StructureElement<E, S, HybridFlag<E, S, T>> = {
    HybridFlagImpl(parameter(this), aliases)
}
