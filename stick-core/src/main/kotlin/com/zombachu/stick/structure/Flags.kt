package com.zombachu.stick.structure

import com.zombachu.stick.ContextualValue
import com.zombachu.stick.Environment
import com.zombachu.stick.HybridFlagResult
import com.zombachu.stick.ParsingResult
import com.zombachu.stick.TypedIdentifier
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
    id: TypedIdentifier<Boolean>,
    aliases: Set<String> = setOf(),
    description: String = "",
): StructureElement<E, S, ValueFlag<E, S, Boolean>> = {
    ValueFlagImpl(
        { ParsingResult.success(false) },
        FlagParameter.PresenceFlagParameter(id, { ParsingResult.success(true) }, aliases.lowercase(), description)
    )
}
fun <E : Environment, S> BuilderScope<E, S>.flag(
    name: String,
    aliases: Set<String> = setOf(),
    description: String = "",
): StructureElement<E, S, ValueFlag<E, S, Boolean>> = flag(id(name), aliases, description)

fun <E : Environment, S, T> BuilderScope<E, S>.flag(
    id: TypedIdentifier<T>,
    default: ContextualValue<E, S, T>,
    presentValue: ContextualValue<E, S, T>,
    aliases: Set<String> = setOf(),
    description: String = "",
): StructureElement<E, S, ValueFlag<E, S, T>> = {
    ValueFlagImpl(
        default,
        FlagParameter.PresenceFlagParameter(id, presentValue, aliases.lowercase(), description)
    )
}
inline fun <E : Environment, S, reified T> BuilderScope<E, S>.flag(
    name: String,
    noinline default: ContextualValue<E, S, T>,
    noinline presentValue: ContextualValue<E, S, T>,
    aliases: Set<String> = setOf(),
    description: String = "",
): StructureElement<E, S, ValueFlag<E, S, T>> = flag(id(name), default, presentValue, aliases, description)

fun <E : Environment, S, T> BuilderScope<E, S>.valueFlag(
    id: TypedIdentifier<T>,
    default: ContextualValue<E, S, T>,
    parameter: StructureElement<E, S, Parameter.FixedSize<E, S, T>>,
    aliases: Set<String> = setOf(),
    description: String = "",
): StructureElement<E, S, ValueFlag<E, S, T>> = {
    ValueFlagImpl(
        default,
        FlagParameter.ParameterFlagParameter(id, parameter(this), aliases.lowercase(), description)
    )
}
inline fun <E : Environment, S, reified T> BuilderScope<E, S>.valueFlag(
    name: String,
    noinline default: ContextualValue<E, S, T>,
    noinline parameter: StructureElement<E, S, Parameter.FixedSize<E, S, T>>,
    aliases: Set<String> = setOf(),
    description: String = "",
): StructureElement<E, S, ValueFlag<E, S, T>> = valueFlag(id<T>(name), default, parameter, aliases, description)

fun <E : Environment, S, T> BuilderScope<E, S>.valueFlag(
    id: TypedIdentifier<T>,
    default: T,
    parameter: StructureElement<E, S, Parameter.FixedSize<E, S, T>>,
    aliases: Set<String> = setOf(),
    description: String = "",
): StructureElement<E, S, ValueFlag<E, S, T>> =
    valueFlag(id, { ParsingResult.success(default) }, parameter, aliases.lowercase(), description)
inline fun <E : Environment, S, reified T> BuilderScope<E, S>.valueFlag(
    name: String,
    default: T,
    noinline parameter: StructureElement<E, S, Parameter.FixedSize<E, S, T>>,
    aliases: Set<String> = setOf(),
    description: String = "",
): StructureElement<E, S, ValueFlag<E, S, T>> = valueFlag(id(name), default, parameter, aliases, description)

fun <E : Environment, S, T> BuilderScope<E, S>.nullableValueFlag(
    id: TypedIdentifier<T?>,
    parameter: StructureElement<E, S, Parameter.FixedSize<E, S, out T>>,
    aliases: Set<String> = setOf(),
    description: String = "",
): StructureElement<E, S, ValueFlag<E, S, T?>> = {
    @Suppress("UNCHECKED_CAST")
    ValueFlagImpl(
        { ParsingResult.success(null) },
        FlagParameter.ParameterFlagParameter(id, parameter(this) as Parameter.FixedSize<E, S, T?>, aliases.lowercase(), description)
    )
}
inline fun <E : Environment, S, reified T> BuilderScope<E, S>.nullableValueFlag(
    name: String,
    noinline parameter: StructureElement<E, S, Parameter.FixedSize<E, S, out T>>,
    aliases: Set<String> = setOf(),
    description: String = "",
): StructureElement<E, S, ValueFlag<E, S, T?>> = nullableValueFlag(id<T?>(name), parameter, aliases, description)

fun <E : Environment, S, T : Enum<T>> BuilderScope<E, S>.enumFlag(
    default: ContextualValue<E, S, T>,
    from: StructureElement<E, S, EnumParameter<E, S, T>>,
): StructureElement<E, S, ValueFlag<E, S, T>> = {
    ValueFlagImpl(default, FlagParameter.MultiFlagParameter(from(this)))
}
fun <E : Environment, S, T : Enum<T>> BuilderScope<E, S>.enumFlag(
    default: T,
    from: StructureElement<E, S, EnumParameter<E, S, T>>,
): StructureElement<E, S, ValueFlag<E, S, T>> = enumFlag({ ParsingResult.success(default) }, from)

fun <E : Environment, S, T> BuilderScope<E, S>.hybridFlag(
    id: TypedIdentifier<HybridFlagResult<T>>,
    parameter: StructureElement<E, S, Parameter.FixedSize<E, S, T>>,
    aliases: Set<String> = setOf(),
    description: String = "",
): StructureElement<E, S, HybridFlag<E, S, T>> = {
    HybridFlagImpl(id, description, parameter(this), aliases)
}
fun <E : Environment, S, T> BuilderScope<E, S>.hybridFlag(
    name: String,
    parameter: StructureElement<E, S, Parameter.FixedSize<E, S, T>>,
    aliases: Set<String> = setOf(),
    description: String = "",
): StructureElement<E, S, HybridFlag<E, S, T>> = hybridFlag(id(name), parameter, aliases, description)
