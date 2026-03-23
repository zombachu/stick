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
import com.zombachu.stick.impl.BuilderScope
import com.zombachu.stick.impl.StructureElement
import kotlin.experimental.ExperimentalTypeInference

@OverloadResolutionByLambdaReturnType
@JvmName("pipelineFixedSizeParameter")
inline fun <E_ : Environment, S, A, reified B> StructureElement<E_, S, Parameter.FixedSize<E_, S, A>>.pipeline(
    noinline operation: PipelineOperation<E_, S, A, B>,
): StructureElement<E_, S, Parameter.FixedSize<E_, S, B>> = {
    val resolvedParameter = this@pipeline()
    PipelinedFixedSizeParameter(resolvedParameter, listOf(operation), id(resolvedParameter.id.name))
}

@OverloadResolutionByLambdaReturnType
@JvmName("pipelineFixedSizeParameter")
inline fun <E_ : Environment, S, A, B, reified C> StructureElement<E_, S, Parameter.FixedSize<E_, S, A>>.pipeline(
    noinline operationA: PipelineOperation<E_, S, A, B>,
    noinline operationB: PipelineOperation<E_, S, B, C>,
): StructureElement<E_, S, Parameter.FixedSize<E_, S, C>> = {
    val resolvedParameter = this@pipeline()
    PipelinedFixedSizeParameter(resolvedParameter, listOf(operationA, operationB), id(resolvedParameter.id.name))
}

@OverloadResolutionByLambdaReturnType
@JvmName("pipelineFixedSizeParameter")
inline fun <E_ : Environment, S, A, B, C, reified D> StructureElement<E_, S, Parameter.FixedSize<E_, S, A>>.pipeline(
    noinline operationA: PipelineOperation<E_, S, A, B>,
    noinline operationB: PipelineOperation<E_, S, B, C>,
    noinline operationC: PipelineOperation<E_, S, C, D>,
): StructureElement<E_, S, Parameter.FixedSize<E_, S, D>> = {
    val resolvedParameter = this@pipeline()
    PipelinedFixedSizeParameter(resolvedParameter, listOf(operationA, operationB, operationC), id(resolvedParameter.id.name))
}

@OverloadResolutionByLambdaReturnType
@JvmName("pipelineFixedSizeParameter")
inline fun <E_ : Environment, S, A, B, C, D, reified E> StructureElement<E_, S, Parameter.FixedSize<E_, S, A>>.pipeline(
    noinline operationA: PipelineOperation<E_, S, A, B>,
    noinline operationB: PipelineOperation<E_, S, B, C>,
    noinline operationC: PipelineOperation<E_, S, C, D>,
    noinline operationD: PipelineOperation<E_, S, C, E>,
): StructureElement<E_, S, Parameter.FixedSize<E_, S, E>> = {
    val resolvedParameter = this@pipeline()
    PipelinedFixedSizeParameter(resolvedParameter, listOf(operationA, operationB, operationC, operationD), id(resolvedParameter.id.name))
}

@OverloadResolutionByLambdaReturnType
@JvmName("pipelineUnknownSizeParameter")
inline fun <E_ : Environment, S, A, reified B> StructureElement<E_, S, Parameter.UnknownSize<E_, S, A>>.pipeline(
    noinline operation: PipelineOperation<E_, S, A, B>,
): StructureElement<E_, S, Parameter.UnknownSize<E_, S, B>> = {
    val resolvedParameter = this@pipeline()
    PipelinedUnknownSizeParameter(resolvedParameter, listOf(operation), id(resolvedParameter.id.name))
}

@OverloadResolutionByLambdaReturnType
@JvmName("pipelineUnknownSizeParameter")
inline fun <E_ : Environment, S, A, B, reified C> StructureElement<E_, S, Parameter.UnknownSize<E_, S, A>>.pipeline(
    noinline operationA: PipelineOperation<E_, S, A, B>,
    noinline operationB: PipelineOperation<E_, S, B, C>,
): StructureElement<E_, S, Parameter.UnknownSize<E_, S, C>> = {
    val resolvedParameter = this@pipeline()
    PipelinedUnknownSizeParameter(resolvedParameter, listOf(operationA, operationB), id(resolvedParameter.id.name))
}

@OverloadResolutionByLambdaReturnType
@JvmName("pipelineUnknownSizeParameter")
inline fun <E_ : Environment, S, A, B, C, reified D> StructureElement<E_, S, Parameter.UnknownSize<E_, S, A>>.pipeline(
    noinline operationA: PipelineOperation<E_, S, A, B>,
    noinline operationB: PipelineOperation<E_, S, B, C>,
    noinline operationC: PipelineOperation<E_, S, C, D>,
): StructureElement<E_, S, Parameter.UnknownSize<E_, S, D>> = {
    val resolvedParameter = this@pipeline()
    PipelinedUnknownSizeParameter(resolvedParameter, listOf(operationA, operationB, operationC), id(resolvedParameter.id.name))
}

@OverloadResolutionByLambdaReturnType
@JvmName("pipelineUnknownSizeParameter")
inline fun <E_ : Environment, S, A, B, C, D, reified E> StructureElement<E_, S, Parameter.UnknownSize<E_, S, A>>.pipeline(
    noinline operationA: PipelineOperation<E_, S, A, B>,
    noinline operationB: PipelineOperation<E_, S, B, C>,
    noinline operationC: PipelineOperation<E_, S, C, D>,
    noinline operationD: PipelineOperation<E_, S, C, E>,
): StructureElement<E_, S, Parameter.UnknownSize<E_, S, E>> = {
    val resolvedParameter = this@pipeline()
    PipelinedUnknownSizeParameter(resolvedParameter, listOf(operationA, operationB, operationC, operationD), id(resolvedParameter.id.name))
}

@OverloadResolutionByLambdaReturnType
@JvmName("pipelineValueFlag")
inline fun <E_ : Environment, S, A, reified B> StructureElement<E_, S, ValueFlag<E_, S, A>>.pipeline(
    noinline operation: PipelineOperation<E_, S, A, B>,
): StructureElement<E_, S, ValueFlag<E_, S, B>> = {
    val resolvedFlag = this@pipeline()
    PipelinedValueFlag(id(resolvedFlag.id.name), resolvedFlag, listOf(operation))
}

@OverloadResolutionByLambdaReturnType
@JvmName("pipelineValueFlag")
inline fun <E_ : Environment, S, A, B, reified C> StructureElement<E_, S, ValueFlag<E_, S, A>>.pipeline(
    noinline operationA: PipelineOperation<E_, S, A, B>,
    noinline operationB: PipelineOperation<E_, S, B, C>,
): StructureElement<E_, S, ValueFlag<E_, S, C>> = {
    val resolvedFlag = this@pipeline()
    PipelinedValueFlag(id(resolvedFlag.id.name), resolvedFlag, listOf(operationA, operationB))
}

@OverloadResolutionByLambdaReturnType
@JvmName("pipelineValueFlag")
inline fun <E_ : Environment, S, A, B, C, reified D> StructureElement<E_, S, ValueFlag<E_, S, A>>.pipeline(
    noinline operationA: PipelineOperation<E_, S, A, B>,
    noinline operationB: PipelineOperation<E_, S, B, C>,
    noinline operationC: PipelineOperation<E_, S, C, D>,
): StructureElement<E_, S, ValueFlag<E_, S, D>> = {
    val resolvedFlag = this@pipeline()
    PipelinedValueFlag(id(resolvedFlag.id.name), resolvedFlag, listOf(operationA, operationB, operationC))
}

@OverloadResolutionByLambdaReturnType
@JvmName("pipelineValueFlag")
inline fun <E_ : Environment, S, A, B, C, D, reified E> StructureElement<E_, S, ValueFlag<E_, S, A>>.pipeline(
    noinline operationA: PipelineOperation<E_, S, A, B>,
    noinline operationB: PipelineOperation<E_, S, B, C>,
    noinline operationC: PipelineOperation<E_, S, C, D>,
    noinline operationD: PipelineOperation<E_, S, C, E>,
): StructureElement<E_, S, ValueFlag<E_, S, E>> = {
    val resolvedFlag = this@pipeline()
    PipelinedValueFlag(id(resolvedFlag.id.name), resolvedFlag, listOf(operationA, operationB, operationC, operationD))
}

@OverloadResolutionByLambdaReturnType
@JvmName("pipelineHybridFlag")
inline fun <E_ : Environment, S, A, reified B> StructureElement<E_, S, HybridFlag<E_, S, A>>.pipeline(
    noinline operation: PipelineOperation<E_, S, A, B>,
): StructureElement<E_, S, HybridFlag<E_, S, B>> = {
    val resolvedFlag = this@pipeline()
    PipelinedHybridFlag(id(resolvedFlag.id.name), resolvedFlag, listOf(operation))
}

@OverloadResolutionByLambdaReturnType
@JvmName("pipelineHybridFlag")
inline fun <E_ : Environment, S, A, B, reified C> StructureElement<E_, S, HybridFlag<E_, S, A>>.pipeline(
    noinline operationA: PipelineOperation<E_, S, A, B>,
    noinline operationB: PipelineOperation<E_, S, B, C>,
): StructureElement<E_, S, HybridFlag<E_, S, C>> = {
    val resolvedFlag = this@pipeline()
    PipelinedHybridFlag(id(resolvedFlag.id.name), resolvedFlag, listOf(operationA, operationB))
}

@OverloadResolutionByLambdaReturnType
@JvmName("pipelineHybridFlag")
inline fun <E_ : Environment, S, A, B, C, reified D> StructureElement<E_, S, HybridFlag<E_, S, A>>.pipeline(
    noinline operationA: PipelineOperation<E_, S, A, B>,
    noinline operationB: PipelineOperation<E_, S, B, C>,
    noinline operationC: PipelineOperation<E_, S, C, D>,
): StructureElement<E_, S, HybridFlag<E_, S, D>> = {
    val resolvedFlag = this@pipeline()
    PipelinedHybridFlag(id(resolvedFlag.id.name), resolvedFlag, listOf(operationA, operationB, operationC))
}

@OverloadResolutionByLambdaReturnType
@JvmName("pipelineHybridFlag")
inline fun <E_ : Environment, S, A, B, C, D, reified E> StructureElement<E_, S, HybridFlag<E_, S, A>>.pipeline(
    noinline operationA: PipelineOperation<E_, S, A, B>,
    noinline operationB: PipelineOperation<E_, S, B, C>,
    noinline operationC: PipelineOperation<E_, S, C, D>,
    noinline operationD: PipelineOperation<E_, S, C, E>,
): StructureElement<E_, S, HybridFlag<E_, S, E>> = {
    val resolvedFlag = this@pipeline()
    PipelinedHybridFlag(id(resolvedFlag.id.name), resolvedFlag, listOf(operationA, operationB, operationC, operationD))
}

@OverloadResolutionByLambdaReturnType
@JvmName("pipelineOptional")
inline fun <E_ : Environment, S, A, reified B> StructureElement<E_, S, OptionalParameter<E_, S, A>>.pipeline(
    noinline operation: PipelineOperation<E_, S, A, B>,
): StructureElement<E_, S, OptionalParameter<E_, S, B>> = {
    val resolvedOptional = this@pipeline()
    PipelinedOptionalParameter(resolvedOptional, listOf(operation), id(resolvedOptional.id.name))
}

@OverloadResolutionByLambdaReturnType
@JvmName("pipelineOptional")
inline fun <E_ : Environment, S, A, B, reified C> StructureElement<E_, S, OptionalParameter<E_, S, A>>.pipeline(
    noinline operationA: PipelineOperation<E_, S, A, B>,
    noinline operationB: PipelineOperation<E_, S, B, C>,
): StructureElement<E_, S, OptionalParameter<E_, S, C>> = {
    val resolvedOptional = this@pipeline()
    PipelinedOptionalParameter(resolvedOptional, listOf(operationA, operationB), id(resolvedOptional.id.name))
}

@OverloadResolutionByLambdaReturnType
@JvmName("pipelineOptional")
inline fun <E_ : Environment, S, A, B, C, reified D> StructureElement<E_, S, OptionalParameter<E_, S, A>>.pipeline(
    noinline operationA: PipelineOperation<E_, S, A, B>,
    noinline operationB: PipelineOperation<E_, S, B, C>,
    noinline operationC: PipelineOperation<E_, S, C, D>,
): StructureElement<E_, S, OptionalParameter<E_, S, D>> = {
    val resolvedOptional = this@pipeline()
    PipelinedOptionalParameter(resolvedOptional, listOf(operationA, operationB, operationC), id(resolvedOptional.id.name))
}

@OverloadResolutionByLambdaReturnType
@JvmName("pipelineOptional")
inline fun <E_ : Environment, S, A, B, C, D, reified E> StructureElement<E_, S, OptionalParameter<E_, S, A>>.pipeline(
    noinline operationA: PipelineOperation<E_, S, A, B>,
    noinline operationB: PipelineOperation<E_, S, B, C>,
    noinline operationC: PipelineOperation<E_, S, C, D>,
    noinline operationD: PipelineOperation<E_, S, C, E>,
): StructureElement<E_, S, OptionalParameter<E_, S, E>> = {
    val resolvedOptional = this@pipeline()
    PipelinedOptionalParameter(resolvedOptional, listOf(operationA, operationB, operationC, operationD), id(resolvedOptional.id.name))
}

fun <E : Environment, S, A, B> BuilderScope<E, S>.map(block: Invocation<E, S>.(A) -> B): PipelineOperation<E, S, A, B> = {
    ParsingResult.success(block(it))
}
