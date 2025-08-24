package com.zombachu.stick.structure

import com.zombachu.stick.ContextualValue
import com.zombachu.stick.SenderContext
import com.zombachu.stick.TypedIdentifier
import com.zombachu.stick.element.Flag
import com.zombachu.stick.element.FlagImpl
import com.zombachu.stick.element.FlagParameter
import com.zombachu.stick.element.Parameter
import com.zombachu.stick.impl.SenderScope
import com.zombachu.stick.impl.StructureElement
import com.zombachu.stick.lowercase

fun <S : SenderContext, O> SenderScope<S, O>.flag(
    id: TypedIdentifier<Boolean>,
    aliases: Set<String> = setOf(),
    description: String = "",
): StructureElement<S, O, Flag<S, O, Boolean>> = {
    FlagImpl(
        { false },
        FlagParameter.PresenceFlagParameter(id, { true }, aliases.lowercase(), description)
    )
}
fun <S : SenderContext, O> SenderScope<S, O>.flag(
    name: String,
    aliases: Set<String> = setOf(),
    description: String = "",
): StructureElement<S, O, Flag<S, O, Boolean>> = flag(id(name), aliases, description)

fun <S : SenderContext, O, T : Any> SenderScope<S, O>.flag(
    id: TypedIdentifier<T>,
    default: ContextualValue<S, O, T>,
    presentValue: ContextualValue<S, O, T>,
    aliases: Set<String> = setOf(),
    description: String = "",
): StructureElement<S, O, Flag<S, O, T>> = {
    FlagImpl(
        default,
        FlagParameter.PresenceFlagParameter(id, presentValue, aliases.lowercase(), description)
    )
}
inline fun <S : SenderContext, O, reified T : Any> SenderScope<S, O>.flag(
    name: String,
    noinline default: ContextualValue<S, O, T>,
    noinline presentValue: ContextualValue<S, O, T>,
    aliases: Set<String> = setOf(),
    description: String = "",
): StructureElement<S, O, Flag<S, O, T>> = flag(id(name), default, presentValue, aliases, description)

fun <S : SenderContext, O, T : Any> SenderScope<S, O>.valueFlag(
    id: TypedIdentifier<T>,
    default: ContextualValue<S, O, T>,
    parameter: StructureElement<S, O, Parameter.FixedSize<S, O, T>>,
    aliases: Set<String> = setOf(),
    description: String = "",
): StructureElement<S, O, Flag<S, O, T>> = {
    FlagImpl(
        default,
        FlagParameter.ValueFlagParameter(id, parameter(this), aliases.lowercase(), description)
    )
}
inline fun <S : SenderContext, O, reified T : Any> SenderScope<S, O>.valueFlag(
    name: String,
    noinline default: ContextualValue<S, O, T>,
    noinline parameter: StructureElement<S, O, Parameter.FixedSize<S, O, T>>,
    aliases: Set<String> = setOf(),
    description: String = "",
): StructureElement<S, O, Flag<S, O, T>> = valueFlag(id(name), default, parameter, aliases, description)

fun <S : SenderContext, O, T : Any> SenderScope<S, O>.valueFlag(
    id: TypedIdentifier<T>,
    default: T,
    parameter: StructureElement<S, O, Parameter.FixedSize<S, O, T>>,
    aliases: Set<String> = setOf(),
    description: String = "",
): StructureElement<S, O, Flag<S, O, T>> =
    valueFlag(id, { default }, parameter, aliases.lowercase(), description)
inline fun <S : SenderContext, O, reified T : Any> SenderScope<S, O>.valueFlag(
    name: String,
    default: T,
    noinline parameter: StructureElement<S, O, Parameter.FixedSize<S, O, T>>,
    aliases: Set<String> = setOf(),
    description: String = "",
): StructureElement<S, O, Flag<S, O, T>> = valueFlag(id(name), default, parameter, aliases, description)
