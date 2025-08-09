package com.zombachu.stick

import com.zombachu.stick.impl.Flag
import com.zombachu.stick.impl.FlagParameter
import com.zombachu.stick.impl.Parameter
import com.zombachu.stick.impl.SenderScope
import com.zombachu.stick.impl.StructureElement
import com.zombachu.stick.impl.lowercase

fun <S> SenderScope<S>.flag(
    id: TypedIdentifier<Boolean>,
    aliases: Set<String> = setOf(),
    description: String = "",
): StructureElement<S, Flag<S, Boolean>> = {
    Flag(
        { false },
        FlagParameter.BooleanFlagParameter(id, aliases.lowercase(), description)
    )
}

fun <S, T : Any> SenderScope<S>.valueFlag(
    id: TypedIdentifier<T>,
    default: ContextualValue<S, T>,
    parameter: StructureElement<S, Parameter.FixedSize<S, T>>,
    aliases: Set<String> = setOf(),
    description: String = "",
): StructureElement<S, Flag<S, T>> = {
    Flag(
        default,
        FlagParameter.ValueFlagParameter(id, parameter(this), aliases.lowercase(), description)
    )
}

fun <S, T : Any> SenderScope<S>.valueFlag(
    id: TypedIdentifier<T>,
    default: T,
    parameter: StructureElement<S, Parameter.FixedSize<S, T>>,
    aliases: Set<String> = setOf(),
    description: String = "",
): StructureElement<S, Flag<S, T>> =
    valueFlag(id, { default }, parameter, aliases.lowercase(), description)
