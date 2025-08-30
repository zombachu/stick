package com.zombachu.stick.structure

import com.zombachu.stick.ContextualValue
import com.zombachu.stick.Environment
import com.zombachu.stick.TypedIdentifier
import com.zombachu.stick.element.Flag
import com.zombachu.stick.element.FlagImpl
import com.zombachu.stick.element.FlagParameter
import com.zombachu.stick.element.Parameter
import com.zombachu.stick.impl.BuilderScope
import com.zombachu.stick.impl.StructureElement
import com.zombachu.stick.lowercase

fun <E : Environment, S> BuilderScope<E, S>.flag(
    id: TypedIdentifier<Boolean>,
    aliases: Set<String> = setOf(),
    description: String = "",
): StructureElement<E, S, Flag<E, S, Boolean>> = {
    FlagImpl(
        { false },
        FlagParameter.PresenceFlagParameter(id, { true }, aliases.lowercase(), description)
    )
}
fun <E : Environment, S> BuilderScope<E, S>.flag(
    name: String,
    aliases: Set<String> = setOf(),
    description: String = "",
): StructureElement<E, S, Flag<E, S, Boolean>> = flag(id(name), aliases, description)

fun <E : Environment, S, T : Any> BuilderScope<E, S>.flag(
    id: TypedIdentifier<T>,
    default: ContextualValue<E, S, T>,
    presentValue: ContextualValue<E, S, T>,
    aliases: Set<String> = setOf(),
    description: String = "",
): StructureElement<E, S, Flag<E, S, T>> = {
    FlagImpl(
        default,
        FlagParameter.PresenceFlagParameter(id, presentValue, aliases.lowercase(), description)
    )
}
inline fun <E : Environment, S, reified T : Any> BuilderScope<E, S>.flag(
    name: String,
    noinline default: ContextualValue<E, S, T>,
    noinline presentValue: ContextualValue<E, S, T>,
    aliases: Set<String> = setOf(),
    description: String = "",
): StructureElement<E, S, Flag<E, S, T>> = flag(id(name), default, presentValue, aliases, description)

fun <E : Environment, S, T : Any> BuilderScope<E, S>.valueFlag(
    id: TypedIdentifier<T>,
    default: ContextualValue<E, S, T>,
    parameter: StructureElement<E, S, Parameter.FixedSize<E, S, T>>,
    aliases: Set<String> = setOf(),
    description: String = "",
): StructureElement<E, S, Flag<E, S, T>> = {
    FlagImpl(
        default,
        FlagParameter.ValueFlagParameter(id, parameter(this), aliases.lowercase(), description)
    )
}
inline fun <E : Environment, S, reified T : Any> BuilderScope<E, S>.valueFlag(
    name: String,
    noinline default: ContextualValue<E, S, T>,
    noinline parameter: StructureElement<E, S, Parameter.FixedSize<E, S, T>>,
    aliases: Set<String> = setOf(),
    description: String = "",
): StructureElement<E, S, Flag<E, S, T>> = valueFlag(id(name), default, parameter, aliases, description)

fun <E : Environment, S, T : Any> BuilderScope<E, S>.valueFlag(
    id: TypedIdentifier<T>,
    default: T,
    parameter: StructureElement<E, S, Parameter.FixedSize<E, S, T>>,
    aliases: Set<String> = setOf(),
    description: String = "",
): StructureElement<E, S, Flag<E, S, T>> =
    valueFlag(id, { default }, parameter, aliases.lowercase(), description)
inline fun <E : Environment, S, reified T : Any> BuilderScope<E, S>.valueFlag(
    name: String,
    default: T,
    noinline parameter: StructureElement<E, S, Parameter.FixedSize<E, S, T>>,
    aliases: Set<String> = setOf(),
    description: String = "",
): StructureElement<E, S, Flag<E, S, T>> = valueFlag(id(name), default, parameter, aliases, description)
