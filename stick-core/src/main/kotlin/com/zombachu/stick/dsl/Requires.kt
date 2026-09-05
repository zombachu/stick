@file:OptIn(ExperimentalTypeInference::class)

package com.zombachu.stick.dsl

import com.zombachu.stick.Arguments
import com.zombachu.stick.Environment
import com.zombachu.stick.HybridFlagResult
import com.zombachu.stick.Position
import com.zombachu.stick.Requirement
import com.zombachu.stick.SenderValidationResult
import com.zombachu.stick.StructureScope
import com.zombachu.stick.element.HybridFlag
import com.zombachu.stick.element.InvalidSenderDefault
import com.zombachu.stick.element.Parameter
import com.zombachu.stick.element.Structure
import com.zombachu.stick.element.TransformedHybridFlag
import com.zombachu.stick.element.TransformedParameter
import com.zombachu.stick.element.TransformedStructure
import com.zombachu.stick.element.TransformedValueFlag
import com.zombachu.stick.element.ValidatedParameter
import com.zombachu.stick.element.ValueFlag
import kotlin.experimental.ExperimentalTypeInference
import kotlin.reflect.KClass

@OverloadResolutionByLambdaReturnType
fun <S : Any, S2 : Any, E : Environment, T, P : Position> StructureScope<E, S>.requireAs(
    transform: (S) -> S2,
    requirement: Requirement<E, S> = requirement { SenderValidationResult.success() },
    // Outer StructureElement is to provide syntax compatibility with other extension functions w/ trailing lambda
    parameter: StructureScope<E, S2>.() -> Parameter<E, S2, T, P>,
): ValidatedParameter<E, S, T, P> = TransformedParameter(parameter(this.forSender()), transform, requirement)

@OverloadResolutionByLambdaReturnType
fun <E : Environment, S : Any, S2 : Any, T> StructureScope<E, S>.requireAs(
    transform: (S) -> S2,
    invalidSenderDefault: InvalidSenderDefault<E, S, T>,
    // Outer StructureElement is to provide syntax compatibility with other extension functions w/ trailing lambda
    flag: StructureScope<E, S2>.() -> ValueFlag<E, S2, T>,
): ValueFlag<E, S, T> = TransformedValueFlag(flag(this.forSender()), transform, invalidSenderDefault)

@OverloadResolutionByLambdaReturnType
fun <E : Environment, S : Any, S2 : Any, T> StructureScope<E, S>.requireAs(
    transform: (S) -> S2,
    invalidSenderDefault: InvalidSenderDefault<E, S, HybridFlagResult<T>>,
    // Outer StructureElement is to provide syntax compatibility with other extension functions w/ trailing lambda
    flag: StructureScope<E, S2>.() -> HybridFlag<E, S2, T>,
): HybridFlag<E, S, T> = TransformedHybridFlag(flag(this.forSender()), transform, invalidSenderDefault)

@OverloadResolutionByLambdaReturnType
fun <E : Environment, S : Any, S2 : Any, T_ : Arguments> StructureScope<E, S>.requireAs(
    transform: (S) -> S2,
    requirement: Requirement<E, S> = requirement { SenderValidationResult.success() },
    // Outer StructureElement is to provide syntax compatibility with other extension functions w/ trailing lambda
    command: StructureScope<E, S2>.() -> Structure<E, S2, T_>,
): Structure<E, S, T_> = TransformedStructure(command(this.forSender()), transform, requirement)

@OverloadResolutionByLambdaReturnType
inline fun <E : Environment, S : Any, reified S2 : S, T, P : Position> StructureScope<E, S>.requireIs(
    @Suppress("UnusedParameter") senderType: KClass<S2>,
    requirement: Requirement<E, S> = requirement { SenderValidationResult.success() },
    // Outer StructureElement is to provide syntax compatibility with other extension functions w/ trailing lambda
    noinline parameter: StructureScope<E, S2>.() -> Parameter<E, S2, T, P>,
): ValidatedParameter<E, S, T, P> =
    requireAs(
        { it as S2 },
        requirement + requirement({ SenderValidationResult.failSenderType() }) { it.sender is S2 },
        parameter,
    )

@OverloadResolutionByLambdaReturnType
inline fun <E : Environment, S : Any, reified S2 : S, T> StructureScope<E, S>.requireIs(
    @Suppress("UnusedParameter") senderType: KClass<S2>,
    invalidSenderDefault: InvalidSenderDefault<E, S, T>,
    // Outer StructureElement is to provide syntax compatibility with other extension functions w/ trailing lambda
    noinline flag: StructureScope<E, S2>.() -> ValueFlag<E, S2, T>,
): ValueFlag<E, S, T> =
    requireAs(
        { it as S2 },
        invalidDefault(
            invalidSenderDefault.value,
            requirement(invalidSenderDefault) +
                requirement({ SenderValidationResult.failSenderType() }) { it.sender is S2 },
        ),
        flag,
    )

@OverloadResolutionByLambdaReturnType
inline fun <E : Environment, S : Any, reified S2 : S, T> StructureScope<E, S>.requireIs(
    @Suppress("UnusedParameter") senderType: KClass<S2>,
    invalidSenderDefault: InvalidSenderDefault<E, S, HybridFlagResult<T>>,
    // Outer StructureElement is to provide syntax compatibility with other extension functions w/ trailing lambda
    noinline flag: StructureScope<E, S2>.() -> HybridFlag<E, S2, T>,
): HybridFlag<E, S, T> =
    requireAs(
        { it as S2 },
        invalidDefault(
            invalidSenderDefault.value,
            requirement(invalidSenderDefault) +
                requirement({ SenderValidationResult.failSenderType() }) { it.sender is S2 },
        ),
        flag,
    )

@OverloadResolutionByLambdaReturnType
inline fun <E : Environment, S : Any, reified S2 : S, T_ : Arguments> StructureScope<E, S>.requireIs(
    @Suppress("UnusedParameter") senderType: KClass<S2>,
    requirement: Requirement<E, S> = requirement { SenderValidationResult.success() },
    // Outer StructureElement is to provide syntax compatibility with other extension functions w/ trailing lambda
    noinline command: StructureScope<E, S2>.() -> Structure<E, S2, T_>,
): Structure<E, S, T_> =
    requireAs(
        { it as S2 },
        requirement + requirement({ SenderValidationResult.failSenderType() }) { it.sender is S2 },
        command,
    )

@OverloadResolutionByLambdaReturnType
fun <E : Environment, S : Any, T, P : Position> StructureScope<E, S>.require(
    requirement: Requirement<E, S>,
    // Outer StructureElement is to provide syntax compatibility with other extension functions w/ trailing lambda
    parameter: StructureScope<E, S>.() -> Parameter<E, S, T, P>,
): ValidatedParameter<E, S, T, P> = requireAs({ it }, requirement, parameter)

@OverloadResolutionByLambdaReturnType
fun <E : Environment, S : Any, T> StructureScope<E, S>.require(
    invalidSenderDefault: InvalidSenderDefault<E, S, T>,
    // Outer StructureElement is to provide syntax compatibility with other extension functions w/ trailing lambda
    flag: StructureScope<E, S>.() -> ValueFlag<E, S, T>,
): ValueFlag<E, S, T> = requireAs({ it }, invalidSenderDefault, flag)

@OverloadResolutionByLambdaReturnType
fun <E : Environment, S : Any, T> StructureScope<E, S>.require(
    invalidSenderDefault: InvalidSenderDefault<E, S, HybridFlagResult<T>>,
    // Outer StructureElement is to provide syntax compatibility with other extension functions w/ trailing lambda
    flag: StructureScope<E, S>.() -> HybridFlag<E, S, T>,
): HybridFlag<E, S, T> = requireAs({ it }, invalidSenderDefault, flag)

@OverloadResolutionByLambdaReturnType
fun <E : Environment, S : Any, T : Arguments> StructureScope<E, S>.require(
    requirement: Requirement<E, S> = requirement { SenderValidationResult.success() },
    // Outer StructureElement is to provide syntax compatibility with other extension functions w/ trailing lambda
    command: StructureScope<E, S>.() -> Structure<E, S, T>,
): Structure<E, S, T> = requireAs({ it }, requirement, command)
