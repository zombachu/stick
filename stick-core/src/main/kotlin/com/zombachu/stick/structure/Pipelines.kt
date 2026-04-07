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

@OverloadResolutionByLambdaReturnType
@JvmName("pipelineFixedSizeParameter")
fun <E_ : Environment, S, A, B> Parameter.FixedSize<E_, S, A>.pipeline(
    operation: PipelineOperation<E_, S, A, B>,
): Parameter.FixedSize<E_, S, B> = PipelinedFixedSizeParameter(this, listOf(operation))

@OverloadResolutionByLambdaReturnType
@JvmName("pipelineFixedSizeParameter")
fun <E_ : Environment, S, A, B, C> Parameter.FixedSize<E_, S, A>.pipeline(
    operationA: PipelineOperation<E_, S, A, B>,
    operationB: PipelineOperation<E_, S, B, C>,
): Parameter.FixedSize<E_, S, C> = PipelinedFixedSizeParameter(this, listOf(operationA, operationB))

@OverloadResolutionByLambdaReturnType
@JvmName("pipelineFixedSizeParameter")
fun <E_ : Environment, S, A, B, C, D> Parameter.FixedSize<E_, S, A>.pipeline(
    operationA: PipelineOperation<E_, S, A, B>,
    operationB: PipelineOperation<E_, S, B, C>,
    operationC: PipelineOperation<E_, S, C, D>,
): Parameter.FixedSize<E_, S, D> = PipelinedFixedSizeParameter(this, listOf(operationA, operationB, operationC))

@OverloadResolutionByLambdaReturnType
@JvmName("pipelineFixedSizeParameter")
fun <E_ : Environment, S, A, B, C, D, E> Parameter.FixedSize<E_, S, A>.pipeline(
    operationA: PipelineOperation<E_, S, A, B>,
    operationB: PipelineOperation<E_, S, B, C>,
    operationC: PipelineOperation<E_, S, C, D>,
    operationD: PipelineOperation<E_, S, C, E>,
): Parameter.FixedSize<E_, S, E> = PipelinedFixedSizeParameter(this, listOf(operationA, operationB, operationC, operationD))

@OverloadResolutionByLambdaReturnType
@JvmName("pipelineUnknownSizeParameter")
fun <E_ : Environment, S, A, B> Parameter.UnknownSize<E_, S, A>.pipeline(
    operation: PipelineOperation<E_, S, A, B>,
): Parameter.UnknownSize<E_, S, B> = PipelinedUnknownSizeParameter(this, listOf(operation))

@OverloadResolutionByLambdaReturnType
@JvmName("pipelineUnknownSizeParameter")
fun <E_ : Environment, S, A, B, C> Parameter.UnknownSize<E_, S, A>.pipeline(
    operationA: PipelineOperation<E_, S, A, B>,
    operationB: PipelineOperation<E_, S, B, C>,
): Parameter.UnknownSize<E_, S, C> = PipelinedUnknownSizeParameter(this, listOf(operationA, operationB))

@OverloadResolutionByLambdaReturnType
@JvmName("pipelineUnknownSizeParameter")
fun <E_ : Environment, S, A, B, C, D> Parameter.UnknownSize<E_, S, A>.pipeline(
    operationA: PipelineOperation<E_, S, A, B>,
    operationB: PipelineOperation<E_, S, B, C>,
    operationC: PipelineOperation<E_, S, C, D>,
): Parameter.UnknownSize<E_, S, D> = PipelinedUnknownSizeParameter(this, listOf(operationA, operationB, operationC))

@OverloadResolutionByLambdaReturnType
@JvmName("pipelineUnknownSizeParameter")
fun <E_ : Environment, S, A, B, C, D, E> Parameter.UnknownSize<E_, S, A>.pipeline(
    operationA: PipelineOperation<E_, S, A, B>,
    operationB: PipelineOperation<E_, S, B, C>,
    operationC: PipelineOperation<E_, S, C, D>,
    operationD: PipelineOperation<E_, S, C, E>,
): Parameter.UnknownSize<E_, S, E> = PipelinedUnknownSizeParameter(this, listOf(operationA, operationB, operationC, operationD))

@OverloadResolutionByLambdaReturnType
@JvmName("pipelineValueFlag")
fun <E_ : Environment, S, A, B> ValueFlag<E_, S, A>.pipeline(
    operation: PipelineOperation<E_, S, A, B>,
): ValueFlag<E_, S, B> = PipelinedValueFlag(this, listOf(operation))

@OverloadResolutionByLambdaReturnType
@JvmName("pipelineValueFlag")
fun <E_ : Environment, S, A, B, C> ValueFlag<E_, S, A>.pipeline(
    operationA: PipelineOperation<E_, S, A, B>,
    operationB: PipelineOperation<E_, S, B, C>,
): ValueFlag<E_, S, C> = PipelinedValueFlag(this, listOf(operationA, operationB))

@OverloadResolutionByLambdaReturnType
@JvmName("pipelineValueFlag")
fun <E_ : Environment, S, A, B, C, D> ValueFlag<E_, S, A>.pipeline(
    operationA: PipelineOperation<E_, S, A, B>,
    operationB: PipelineOperation<E_, S, B, C>,
    operationC: PipelineOperation<E_, S, C, D>,
): ValueFlag<E_, S, D> = PipelinedValueFlag(this, listOf(operationA, operationB, operationC))

@OverloadResolutionByLambdaReturnType
@JvmName("pipelineValueFlag")
fun <E_ : Environment, S, A, B, C, D, E> ValueFlag<E_, S, A>.pipeline(
    operationA: PipelineOperation<E_, S, A, B>,
    operationB: PipelineOperation<E_, S, B, C>,
    operationC: PipelineOperation<E_, S, C, D>,
    operationD: PipelineOperation<E_, S, C, E>,
): ValueFlag<E_, S, E> = PipelinedValueFlag(this, listOf(operationA, operationB, operationC, operationD))

@OverloadResolutionByLambdaReturnType
@JvmName("pipelineHybridFlag")
fun <E_ : Environment, S, A, B> HybridFlag<E_, S, A>.pipeline(
    operation: PipelineOperation<E_, S, A, B>,
): HybridFlag<E_, S, B> = PipelinedHybridFlag(this, listOf(operation))

@OverloadResolutionByLambdaReturnType
@JvmName("pipelineHybridFlag")
fun <E_ : Environment, S, A, B, C> HybridFlag<E_, S, A>.pipeline(
    operationA: PipelineOperation<E_, S, A, B>,
    operationB: PipelineOperation<E_, S, B, C>,
): HybridFlag<E_, S, C> = PipelinedHybridFlag(this, listOf(operationA, operationB))

@OverloadResolutionByLambdaReturnType
@JvmName("pipelineHybridFlag")
fun <E_ : Environment, S, A, B, C, D> HybridFlag<E_, S, A>.pipeline(
    operationA: PipelineOperation<E_, S, A, B>,
    operationB: PipelineOperation<E_, S, B, C>,
    operationC: PipelineOperation<E_, S, C, D>,
): HybridFlag<E_, S, D> = PipelinedHybridFlag(this, listOf(operationA, operationB, operationC))

@OverloadResolutionByLambdaReturnType
@JvmName("pipelineHybridFlag")
fun <E_ : Environment, S, A, B, C, D, E> HybridFlag<E_, S, A>.pipeline(
    operationA: PipelineOperation<E_, S, A, B>,
    operationB: PipelineOperation<E_, S, B, C>,
    operationC: PipelineOperation<E_, S, C, D>,
    operationD: PipelineOperation<E_, S, C, E>,
): HybridFlag<E_, S, E> = PipelinedHybridFlag(this, listOf(operationA, operationB, operationC, operationD))

@OverloadResolutionByLambdaReturnType
@JvmName("pipelineOptional")
fun <E_ : Environment, S, A, B> OptionalParameter<E_, S, A>.pipeline(
    operation: PipelineOperation<E_, S, A, B>,
): OptionalParameter<E_, S, B> = PipelinedOptionalParameter(this, listOf(operation))

@OverloadResolutionByLambdaReturnType
@JvmName("pipelineOptional")
fun <E_ : Environment, S, A, B, C> OptionalParameter<E_, S, A>.pipeline(
    operationA: PipelineOperation<E_, S, A, B>,
    operationB: PipelineOperation<E_, S, B, C>,
): OptionalParameter<E_, S, C> = PipelinedOptionalParameter(this, listOf(operationA, operationB))

@OverloadResolutionByLambdaReturnType
@JvmName("pipelineOptional")
fun <E_ : Environment, S, A, B, C, D> OptionalParameter<E_, S, A>.pipeline(
    operationA: PipelineOperation<E_, S, A, B>,
    operationB: PipelineOperation<E_, S, B, C>,
    operationC: PipelineOperation<E_, S, C, D>,
): OptionalParameter<E_, S, D> = PipelinedOptionalParameter(this, listOf(operationA, operationB, operationC))

@OverloadResolutionByLambdaReturnType
@JvmName("pipelineOptional")
fun <E_ : Environment, S, A, B, C, D, E> OptionalParameter<E_, S, A>.pipeline(
    operationA: PipelineOperation<E_, S, A, B>,
    operationB: PipelineOperation<E_, S, B, C>,
    operationC: PipelineOperation<E_, S, C, D>,
    operationD: PipelineOperation<E_, S, C, E>,
): OptionalParameter<E_, S, E> = PipelinedOptionalParameter(this, listOf(operationA, operationB, operationC, operationD))

fun <E : Environment, S, A, B> StructureScope<E, S>.map(block: Invocation<E, S>.(A) -> B): PipelineOperation<E, S, A, B> = {
    ParsingResult.success(block(it))
}
