package com.zombachu.stick.structure

import com.zombachu.stick.ContextualValue
import com.zombachu.stick.Environment
import com.zombachu.stick.TypedIdentifier
import com.zombachu.stick.element.Flag
import com.zombachu.stick.element.FlagImpl
import com.zombachu.stick.element.FlagParameter
import com.zombachu.stick.element.Parameter
import com.zombachu.stick.impl.SenderScope
import com.zombachu.stick.impl.StructureElement
import com.zombachu.stick.lowercase

fun <E : Environment, O> SenderScope<E, O>.flag(
    id: TypedIdentifier<Boolean>,
    aliases: Set<String> = setOf(),
    description: String = "",
): StructureElement<E, O, Flag<E, O, Boolean>> = {
    FlagImpl(
        { false },
        FlagParameter.PresenceFlagParameter(id, { true }, aliases.lowercase(), description)
    )
}
fun <E : Environment, O> SenderScope<E, O>.flag(
    name: String,
    aliases: Set<String> = setOf(),
    description: String = "",
): StructureElement<E, O, Flag<E, O, Boolean>> = flag(id(name), aliases, description)

fun <E : Environment, O, T : Any> SenderScope<E, O>.flag(
    id: TypedIdentifier<T>,
    default: ContextualValue<E, O, T>,
    presentValue: ContextualValue<E, O, T>,
    aliases: Set<String> = setOf(),
    description: String = "",
): StructureElement<E, O, Flag<E, O, T>> = {
    FlagImpl(
        default,
        FlagParameter.PresenceFlagParameter(id, presentValue, aliases.lowercase(), description)
    )
}
inline fun <E : Environment, O, reified T : Any> SenderScope<E, O>.flag(
    name: String,
    noinline default: ContextualValue<E, O, T>,
    noinline presentValue: ContextualValue<E, O, T>,
    aliases: Set<String> = setOf(),
    description: String = "",
): StructureElement<E, O, Flag<E, O, T>> = flag(id(name), default, presentValue, aliases, description)

fun <E : Environment, O, T : Any> SenderScope<E, O>.valueFlag(
    id: TypedIdentifier<T>,
    default: ContextualValue<E, O, T>,
    parameter: StructureElement<E, O, Parameter.FixedSize<E, O, T>>,
    aliases: Set<String> = setOf(),
    description: String = "",
): StructureElement<E, O, Flag<E, O, T>> = {
    FlagImpl(
        default,
        FlagParameter.ValueFlagParameter(id, parameter(this), aliases.lowercase(), description)
    )
}
inline fun <E : Environment, O, reified T : Any> SenderScope<E, O>.valueFlag(
    name: String,
    noinline default: ContextualValue<E, O, T>,
    noinline parameter: StructureElement<E, O, Parameter.FixedSize<E, O, T>>,
    aliases: Set<String> = setOf(),
    description: String = "",
): StructureElement<E, O, Flag<E, O, T>> = valueFlag(id(name), default, parameter, aliases, description)

fun <E : Environment, O, T : Any> SenderScope<E, O>.valueFlag(
    id: TypedIdentifier<T>,
    default: T,
    parameter: StructureElement<E, O, Parameter.FixedSize<E, O, T>>,
    aliases: Set<String> = setOf(),
    description: String = "",
): StructureElement<E, O, Flag<E, O, T>> =
    valueFlag(id, { default }, parameter, aliases.lowercase(), description)
inline fun <E : Environment, O, reified T : Any> SenderScope<E, O>.valueFlag(
    name: String,
    default: T,
    noinline parameter: StructureElement<E, O, Parameter.FixedSize<E, O, T>>,
    aliases: Set<String> = setOf(),
    description: String = "",
): StructureElement<E, O, Flag<E, O, T>> = valueFlag(id(name), default, parameter, aliases, description)
