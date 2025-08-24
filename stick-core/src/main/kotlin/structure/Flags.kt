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

fun <O, S : SenderContext<O>> SenderScope<O, S>.flag(
    id: TypedIdentifier<Boolean>,
    aliases: Set<String> = setOf(),
    description: String = "",
): StructureElement<O, S, Flag<O, S, Boolean>> = {
    FlagImpl(
        { false },
        FlagParameter.PresenceFlagParameter(id, { true }, aliases.lowercase(), description)
    )
}
fun <O, S : SenderContext<O>> SenderScope<O, S>.flag(
    name: String,
    aliases: Set<String> = setOf(),
    description: String = "",
): StructureElement<O, S, Flag<O, S, Boolean>> = flag(id(name), aliases, description)

fun <O, S : SenderContext<O>, T : Any> SenderScope<O, S>.flag(
    id: TypedIdentifier<T>,
    default: ContextualValue<O, S, T>,
    presentValue: ContextualValue<O, S, T>,
    aliases: Set<String> = setOf(),
    description: String = "",
): StructureElement<O, S, Flag<O, S, T>> = {
    FlagImpl(
        default,
        FlagParameter.PresenceFlagParameter(id, presentValue, aliases.lowercase(), description)
    )
}
inline fun <O, S : SenderContext<O>, reified T : Any> SenderScope<O, S>.flag(
    name: String,
    noinline default: ContextualValue<O, S, T>,
    noinline presentValue: ContextualValue<O, S, T>,
    aliases: Set<String> = setOf(),
    description: String = "",
): StructureElement<O, S, Flag<O, S, T>> = flag(id(name), default, presentValue, aliases, description)

fun <O, S : SenderContext<O>, T : Any> SenderScope<O, S>.valueFlag(
    id: TypedIdentifier<T>,
    default: ContextualValue<O, S, T>,
    parameter: StructureElement<O, S, Parameter.FixedSize<O, S, T>>,
    aliases: Set<String> = setOf(),
    description: String = "",
): StructureElement<O, S, Flag<O, S, T>> = {
    FlagImpl(
        default,
        FlagParameter.ValueFlagParameter(id, parameter(this), aliases.lowercase(), description)
    )
}
inline fun <O, S : SenderContext<O>, reified T : Any> SenderScope<O, S>.valueFlag(
    name: String,
    noinline default: ContextualValue<O, S, T>,
    noinline parameter: StructureElement<O, S, Parameter.FixedSize<O, S, T>>,
    aliases: Set<String> = setOf(),
    description: String = "",
): StructureElement<O, S, Flag<O, S, T>> = valueFlag(id(name), default, parameter, aliases, description)

fun <O, S : SenderContext<O>, T : Any> SenderScope<O, S>.valueFlag(
    id: TypedIdentifier<T>,
    default: T,
    parameter: StructureElement<O, S, Parameter.FixedSize<O, S, T>>,
    aliases: Set<String> = setOf(),
    description: String = "",
): StructureElement<O, S, Flag<O, S, T>> =
    valueFlag(id, { default }, parameter, aliases.lowercase(), description)
inline fun <O, S : SenderContext<O>, reified T : Any> SenderScope<O, S>.valueFlag(
    name: String,
    default: T,
    noinline parameter: StructureElement<O, S, Parameter.FixedSize<O, S, T>>,
    aliases: Set<String> = setOf(),
    description: String = "",
): StructureElement<O, S, Flag<O, S, T>> = valueFlag(id(name), default, parameter, aliases, description)
