package com.zombachu.stick

import com.zombachu.stick.impl.Flag
import com.zombachu.stick.impl.FlagImpl
import com.zombachu.stick.impl.FlagParameter
import com.zombachu.stick.impl.Parameter
import com.zombachu.stick.impl.SenderScope
import com.zombachu.stick.impl.StructureElement

fun <S> SenderScope<S>.flag(
    id: TypedIdentifier<Boolean>,
    aliases: Set<String> = setOf(),
    description: String = "",
): StructureElement<S, Flag<S, Boolean>> = {
    FlagImpl(
        { false },
        FlagParameter.PresenceFlagParameter(id, { true }, aliases.lowercase(), description)
    )
}
fun <S> SenderScope<S>.flag(
    name: String,
    aliases: Set<String> = setOf(),
    description: String = "",
): StructureElement<S, Flag<S, Boolean>> = flag(id(name), aliases, description)

fun <S, T : Any> SenderScope<S>.flag(
    id: TypedIdentifier<T>,
    default: ContextualValue<S, T>,
    presentValue: ContextualValue<S, T>,
    aliases: Set<String> = setOf(),
    description: String = "",
): StructureElement<S, Flag<S, T>> = {
    FlagImpl(
        default,
        FlagParameter.PresenceFlagParameter(id, presentValue, aliases.lowercase(), description)
    )
}
inline fun <S, reified T : Any> SenderScope<S>.flag(
    name: String,
    noinline default: ContextualValue<S, T>,
    noinline presentValue: ContextualValue<S, T>,
    aliases: Set<String> = setOf(),
    description: String = "",
): StructureElement<S, Flag<S, T>> = flag(id(name), default, presentValue, aliases, description)

fun <S, T : Any> SenderScope<S>.valueFlag(
    id: TypedIdentifier<T>,
    default: ContextualValue<S, T>,
    parameter: StructureElement<S, Parameter.FixedSize<S, T>>,
    aliases: Set<String> = setOf(),
    description: String = "",
): StructureElement<S, Flag<S, T>> = {
    FlagImpl(
        default,
        FlagParameter.ValueFlagParameter(id, parameter(this), aliases.lowercase(), description)
    )
}
inline fun <S, reified T : Any> SenderScope<S>.valueFlag(
    name: String,
    noinline default: ContextualValue<S, T>,
    noinline parameter: StructureElement<S, Parameter.FixedSize<S, T>>,
    aliases: Set<String> = setOf(),
    description: String = "",
): StructureElement<S, Flag<S, T>> = valueFlag(id(name), default, parameter, aliases, description)

fun <S, T : Any> SenderScope<S>.valueFlag(
    id: TypedIdentifier<T>,
    default: T,
    parameter: StructureElement<S, Parameter.FixedSize<S, T>>,
    aliases: Set<String> = setOf(),
    description: String = "",
): StructureElement<S, Flag<S, T>> =
    valueFlag(id, { default }, parameter, aliases.lowercase(), description)
inline fun <S, reified T : Any> SenderScope<S>.valueFlag(
    name: String,
    default: T,
    noinline parameter: StructureElement<S, Parameter.FixedSize<S, T>>,
    aliases: Set<String> = setOf(),
    description: String = "",
): StructureElement<S, Flag<S, T>> = valueFlag(id(name), default, parameter, aliases, description)
