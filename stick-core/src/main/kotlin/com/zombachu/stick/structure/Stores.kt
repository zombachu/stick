@file:OptIn(ExperimentalTypeInference::class)

package com.zombachu.stick.structure

import com.zombachu.stick.Environment
import com.zombachu.stick.HybridFlagResult
import com.zombachu.stick.TypedIdentifier
import com.zombachu.stick.element.HybridFlag
import com.zombachu.stick.element.OptionalParameter
import com.zombachu.stick.element.Parameter
import com.zombachu.stick.element.StoredFixedSizeParameter
import com.zombachu.stick.element.StoredHybridFlag
import com.zombachu.stick.element.StoredOptionalParameter
import com.zombachu.stick.element.StoredUnknownSizeParameter
import com.zombachu.stick.element.StoredValueFlag
import com.zombachu.stick.element.ValueFlag
import kotlin.experimental.ExperimentalTypeInference

@JvmName("storeFixedSizeParameter")
fun <E : Environment, S, T> Parameter.FixedSize<E, S, T>.store(id: TypedIdentifier<T>): Parameter.FixedSize<E, S, T> =
    StoredFixedSizeParameter(this, id)

@JvmName("storeUnknownSizeParameter")
fun <E : Environment, S, T> Parameter.UnknownSize<E, S, T>.store(
    id: TypedIdentifier<T>
): Parameter.UnknownSize<E, S, T> = StoredUnknownSizeParameter(this, id)

@JvmName("storeValueFlag")
fun <E : Environment, S, T> ValueFlag<E, S, T>.store(id: TypedIdentifier<T>): ValueFlag<E, S, T> =
    StoredValueFlag(this, id)

@JvmName("storeHybridFlag")
fun <E : Environment, S, T> HybridFlag<E, S, T>.store(id: TypedIdentifier<HybridFlagResult<T>>): HybridFlag<E, S, T> =
    StoredHybridFlag(this, id)

@JvmName("storeOptionalParameter")
fun <E : Environment, S, T> OptionalParameter<E, S, T>.store(id: TypedIdentifier<T>): OptionalParameter<E, S, T> =
    StoredOptionalParameter(this, id)
