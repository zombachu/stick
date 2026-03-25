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
import com.zombachu.stick.impl.StructureElement
import kotlin.experimental.ExperimentalTypeInference

@JvmName("storeFixedSizeParameter")
fun <E : Environment, S, T> StructureElement<E, S, Parameter.FixedSize<E, S, T>>.store(
    id: TypedIdentifier<T>,
): StructureElement<E, S, Parameter.FixedSize<E, S, T>> = {
    val resolvedParameter = this@store()
    StoredFixedSizeParameter(resolvedParameter, id)
}

@JvmName("storeUnknownSizeParameter")
fun <E : Environment, S, T> StructureElement<E, S, Parameter.UnknownSize<E, S, T>>.store(
    id: TypedIdentifier<T>,
): StructureElement<E, S, Parameter.UnknownSize<E, S, T>> = {
    val resolvedParameter = this@store()
    StoredUnknownSizeParameter(resolvedParameter, id)
}

@JvmName("storeValueFlag")
fun <E : Environment, S, T> StructureElement<E, S, ValueFlag<E, S, T>>.store(
    id: TypedIdentifier<T>,
): StructureElement<E, S, ValueFlag<E, S, T>> = {
    val resolvedFlag = this@store()
    StoredValueFlag(resolvedFlag, id)
}

@JvmName("storeHybridFlag")
fun <E : Environment, S, T> StructureElement<E, S, HybridFlag<E, S, T>>.store(
    id: TypedIdentifier<HybridFlagResult<T>>,
): StructureElement<E, S, HybridFlag<E, S, T>> = {
    val resolvedFlag = this@store()
    StoredHybridFlag(resolvedFlag, id)
}

@JvmName("storeOptionalParameter")
fun <E : Environment, S, T> StructureElement<E, S, OptionalParameter<E, S, T>>.store(
    id: TypedIdentifier<T>,
): StructureElement<E, S, OptionalParameter<E, S, T>> = {
    val resolvedOptional = this@store()
    StoredOptionalParameter(resolvedOptional, id)
}
