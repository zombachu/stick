@file:OptIn(ExperimentalTypeInference::class)

package com.zombachu.stick.structure

import com.zombachu.stick.Environment
import com.zombachu.stick.Invocation
import com.zombachu.stick.ParsingResult
import com.zombachu.stick.element.HybridFlag
import com.zombachu.stick.element.OptionalParameter
import com.zombachu.stick.element.Parameter
import com.zombachu.stick.element.PipelineOperation
import com.zombachu.stick.element.PipelinedFixedSizeParameter
import com.zombachu.stick.element.PipelinedHybridFlag
import com.zombachu.stick.element.PipelinedOptionalParameter
import com.zombachu.stick.element.PipelinedUnknownSizeParameter
import com.zombachu.stick.element.PipelinedValueFlag
import com.zombachu.stick.element.ValueFlag
import com.zombachu.stick.impl.StructureScope
import kotlin.experimental.ExperimentalTypeInference

@JvmName("pipelineFixedSizeParameter")
fun <E_ : Environment, S, A, B> Parameter.FixedSize<E_, S, A>.pipeline(
    operation: PipelineOperation<E_, S, A, B>
): Parameter.FixedSize<E_, S, B> = PipelinedFixedSizeParameter(this, listOf(operation))

@JvmName("pipelineFixedSizeParameter")
fun <E_ : Environment, S, A, B, C> Parameter.FixedSize<E_, S, A>.pipeline(
    operationA: PipelineOperation<E_, S, A, B>,
    operationB: PipelineOperation<E_, S, B, C>,
): Parameter.FixedSize<E_, S, C> = PipelinedFixedSizeParameter(this, listOf(operationA, operationB))

@JvmName("pipelineFixedSizeParameter")
fun <E_ : Environment, S, A, B, C, D> Parameter.FixedSize<E_, S, A>.pipeline(
    operationA: PipelineOperation<E_, S, A, B>,
    operationB: PipelineOperation<E_, S, B, C>,
    operationC: PipelineOperation<E_, S, C, D>,
): Parameter.FixedSize<E_, S, D> = PipelinedFixedSizeParameter(this, listOf(operationA, operationB, operationC))

@JvmName("pipelineUnknownSizeParameter")
fun <E_ : Environment, S, A, B> Parameter.UnknownSize<E_, S, A>.pipeline(
    operation: PipelineOperation<E_, S, A, B>
): Parameter.UnknownSize<E_, S, B> = PipelinedUnknownSizeParameter(this, listOf(operation))

@JvmName("pipelineUnknownSizeParameter")
fun <E_ : Environment, S, A, B, C> Parameter.UnknownSize<E_, S, A>.pipeline(
    operationA: PipelineOperation<E_, S, A, B>,
    operationB: PipelineOperation<E_, S, B, C>,
): Parameter.UnknownSize<E_, S, C> = PipelinedUnknownSizeParameter(this, listOf(operationA, operationB))

@JvmName("pipelineUnknownSizeParameter")
fun <E_ : Environment, S, A, B, C, D> Parameter.UnknownSize<E_, S, A>.pipeline(
    operationA: PipelineOperation<E_, S, A, B>,
    operationB: PipelineOperation<E_, S, B, C>,
    operationC: PipelineOperation<E_, S, C, D>,
): Parameter.UnknownSize<E_, S, D> = PipelinedUnknownSizeParameter(this, listOf(operationA, operationB, operationC))

@JvmName("pipelineValueFlag")
fun <E_ : Environment, S, A, B> ValueFlag<E_, S, A>.pipeline(
    operation: PipelineOperation<E_, S, A, B>
): ValueFlag<E_, S, B> = PipelinedValueFlag(this, listOf(operation))

@JvmName("pipelineValueFlag")
fun <E_ : Environment, S, A, B, C> ValueFlag<E_, S, A>.pipeline(
    operationA: PipelineOperation<E_, S, A, B>,
    operationB: PipelineOperation<E_, S, B, C>,
): ValueFlag<E_, S, C> = PipelinedValueFlag(this, listOf(operationA, operationB))

@JvmName("pipelineValueFlag")
fun <E_ : Environment, S, A, B, C, D> ValueFlag<E_, S, A>.pipeline(
    operationA: PipelineOperation<E_, S, A, B>,
    operationB: PipelineOperation<E_, S, B, C>,
    operationC: PipelineOperation<E_, S, C, D>,
): ValueFlag<E_, S, D> = PipelinedValueFlag(this, listOf(operationA, operationB, operationC))

@JvmName("pipelineHybridFlag")
fun <E_ : Environment, S, A, B> HybridFlag<E_, S, A>.pipeline(
    operation: PipelineOperation<E_, S, A, B>
): HybridFlag<E_, S, B> = PipelinedHybridFlag(this, listOf(operation))

@JvmName("pipelineHybridFlag")
fun <E_ : Environment, S, A, B, C> HybridFlag<E_, S, A>.pipeline(
    operationA: PipelineOperation<E_, S, A, B>,
    operationB: PipelineOperation<E_, S, B, C>,
): HybridFlag<E_, S, C> = PipelinedHybridFlag(this, listOf(operationA, operationB))

@JvmName("pipelineHybridFlag")
fun <E_ : Environment, S, A, B, C, D> HybridFlag<E_, S, A>.pipeline(
    operationA: PipelineOperation<E_, S, A, B>,
    operationB: PipelineOperation<E_, S, B, C>,
    operationC: PipelineOperation<E_, S, C, D>,
): HybridFlag<E_, S, D> = PipelinedHybridFlag(this, listOf(operationA, operationB, operationC))

@JvmName("pipelineOptional")
fun <E_ : Environment, S, A, B> OptionalParameter<E_, S, A>.pipeline(
    operation: PipelineOperation<E_, S, A, B>
): OptionalParameter<E_, S, B> = PipelinedOptionalParameter(this, listOf(operation))

@JvmName("pipelineOptional")
fun <E_ : Environment, S, A, B, C> OptionalParameter<E_, S, A>.pipeline(
    operationA: PipelineOperation<E_, S, A, B>,
    operationB: PipelineOperation<E_, S, B, C>,
): OptionalParameter<E_, S, C> = PipelinedOptionalParameter(this, listOf(operationA, operationB))

@JvmName("pipelineOptional")
fun <E_ : Environment, S, A, B, C, D> OptionalParameter<E_, S, A>.pipeline(
    operationA: PipelineOperation<E_, S, A, B>,
    operationB: PipelineOperation<E_, S, B, C>,
    operationC: PipelineOperation<E_, S, C, D>,
): OptionalParameter<E_, S, D> = PipelinedOptionalParameter(this, listOf(operationA, operationB, operationC))

fun <E : Environment, S, A, B> StructureScope<E, S>.map(
    block: Invocation<E, S>.(A) -> B
): PipelineOperation<E, S, A, B> = { ParsingResult.success(block(it)) }
