package com.zombachu.stick.dsl

import com.zombachu.stick.Environment
import com.zombachu.stick.Invocation
import com.zombachu.stick.ParsingResult
import com.zombachu.stick.Position
import com.zombachu.stick.StructureScope
import com.zombachu.stick.element.OptionalParameter
import com.zombachu.stick.element.Parameter
import com.zombachu.stick.element.PipelineOperation
import com.zombachu.stick.element.PipelinedOptionalParameter
import com.zombachu.stick.element.PipelinedParameter
import com.zombachu.stick.element.PipelinedValueFlag
import com.zombachu.stick.element.ValueFlag

fun <E_ : Environment, S, A, B, P : Position> Parameter<E_, S, A, P>.pipeline(
    operation: PipelineOperation<E_, S, A, B>
): Parameter<E_, S, B, P> = PipelinedParameter(this, [operation])

fun <E_ : Environment, S, A, B, C, P : Position> Parameter<E_, S, A, P>.pipeline(
    operationA: PipelineOperation<E_, S, A, B>,
    operationB: PipelineOperation<E_, S, B, C>,
): Parameter<E_, S, C, P> = PipelinedParameter(this, [operationA, operationB])

fun <E_ : Environment, S, A, B, C, D, P : Position> Parameter<E_, S, A, P>.pipeline(
    operationA: PipelineOperation<E_, S, A, B>,
    operationB: PipelineOperation<E_, S, B, C>,
    operationC: PipelineOperation<E_, S, C, D>,
): Parameter<E_, S, D, P> = PipelinedParameter(this, [operationA, operationB, operationC])

fun <E_ : Environment, S, A, B> ValueFlag<E_, S, A>.pipeline(
    operation: PipelineOperation<E_, S, A, B>
): ValueFlag<E_, S, B> = PipelinedValueFlag(this, [operation])

fun <E_ : Environment, S, A, B, C> ValueFlag<E_, S, A>.pipeline(
    operationA: PipelineOperation<E_, S, A, B>,
    operationB: PipelineOperation<E_, S, B, C>,
): ValueFlag<E_, S, C> = PipelinedValueFlag(this, [operationA, operationB])

fun <E_ : Environment, S, A, B, C, D> ValueFlag<E_, S, A>.pipeline(
    operationA: PipelineOperation<E_, S, A, B>,
    operationB: PipelineOperation<E_, S, B, C>,
    operationC: PipelineOperation<E_, S, C, D>,
): ValueFlag<E_, S, D> = PipelinedValueFlag(this, [operationA, operationB, operationC])

fun <E_ : Environment, S, A, B, P : Position> OptionalParameter<E_, S, A, P>.pipeline(
    operation: PipelineOperation<E_, S, A, B>
): OptionalParameter<E_, S, B, P> = PipelinedOptionalParameter(this, [operation])

fun <E_ : Environment, S, A, B, C, P : Position> OptionalParameter<E_, S, A, P>.pipeline(
    operationA: PipelineOperation<E_, S, A, B>,
    operationB: PipelineOperation<E_, S, B, C>,
): OptionalParameter<E_, S, C, P> = PipelinedOptionalParameter(this, [operationA, operationB])

fun <E_ : Environment, S, A, B, C, D, P : Position> OptionalParameter<E_, S, A, P>.pipeline(
    operationA: PipelineOperation<E_, S, A, B>,
    operationB: PipelineOperation<E_, S, B, C>,
    operationC: PipelineOperation<E_, S, C, D>,
): OptionalParameter<E_, S, D, P> = PipelinedOptionalParameter(this, [operationA, operationB, operationC])

fun <E : Environment, S, A, B> StructureScope<E, S>.map(
    block: Invocation<E, S>.(A) -> B
): PipelineOperation<E, S, A, B> = { ParsingResult.success(block(it)) }
