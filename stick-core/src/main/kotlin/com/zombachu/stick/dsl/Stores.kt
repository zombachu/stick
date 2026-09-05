package com.zombachu.stick.dsl

import com.zombachu.stick.Environment
import com.zombachu.stick.HybridFlagResult
import com.zombachu.stick.Position
import com.zombachu.stick.TypedIdentifier
import com.zombachu.stick.element.Helper
import com.zombachu.stick.element.HybridFlag
import com.zombachu.stick.element.OptionalParameter
import com.zombachu.stick.element.Parameter
import com.zombachu.stick.element.StoredHelper
import com.zombachu.stick.element.StoredHybridFlag
import com.zombachu.stick.element.StoredOptionalParameter
import com.zombachu.stick.element.StoredParameter
import com.zombachu.stick.element.StoredValueFlag
import com.zombachu.stick.element.ValueFlag

fun <E : Environment, S, T> Helper<E, S, T>.store(id: TypedIdentifier<T>): Helper<E, S, T> = StoredHelper(this, id)

fun <E : Environment, S, T, P : Position> Parameter<E, S, T, P>.store(id: TypedIdentifier<T>): Parameter<E, S, T, P> =
    StoredParameter(this, id)

fun <E : Environment, S, T> ValueFlag<E, S, T>.store(id: TypedIdentifier<T>): ValueFlag<E, S, T> =
    StoredValueFlag(this, id)

fun <E : Environment, S, T> HybridFlag<E, S, T>.store(id: TypedIdentifier<HybridFlagResult<T>>): HybridFlag<E, S, T> =
    StoredHybridFlag(this, id)

fun <E : Environment, S, T, P : Position> OptionalParameter<E, S, T, P>.store(
    id: TypedIdentifier<T>
): OptionalParameter<E, S, T, P> = StoredOptionalParameter(this, id)
