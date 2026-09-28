package com.zombachu.stick.dsl

import com.zombachu.stick.CommandResult
import com.zombachu.stick.Environment
import com.zombachu.stick.Execution
import com.zombachu.stick.Position
import com.zombachu.stick.element.MappedOptionalParameter
import com.zombachu.stick.element.MappedParameter
import com.zombachu.stick.element.MappedValueFlag
import com.zombachu.stick.element.OptionalParameter
import com.zombachu.stick.element.Parameter
import com.zombachu.stick.element.ValueFlag

fun <E : Environment, S, A, B, P : Position> Parameter<E, S, A, P>.map(
    transform: Execution<E, S>.(A) -> CommandResult<B>
): Parameter<E, S, B, P> = MappedParameter(this, transform)

fun <E : Environment, S, A, B> ValueFlag<E, S, A>.map(
    transform: Execution<E, S>.(A) -> CommandResult<B>
): ValueFlag<E, S, B> = MappedValueFlag(this, transform)

fun <E : Environment, S, A, B, P : Position> OptionalParameter<E, S, A, P>.map(
    transform: Execution<E, S>.(A) -> CommandResult<B>
): OptionalParameter<E, S, B, P> = MappedOptionalParameter(this, transform)
