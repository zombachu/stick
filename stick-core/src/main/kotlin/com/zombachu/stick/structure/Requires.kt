@file:OptIn(ExperimentalTypeInference::class)

package com.zombachu.stick.structure

import com.zombachu.stick.Environment
import com.zombachu.stick.HybridFlagResult
import com.zombachu.stick.SenderValidationResult
import com.zombachu.stick.element.HybridFlag
import com.zombachu.stick.element.Parameter
import com.zombachu.stick.element.Structure
import com.zombachu.stick.element.TransformedHybridFlag
import com.zombachu.stick.element.TransformedParameter
import com.zombachu.stick.element.TransformedStructure
import com.zombachu.stick.element.TransformedValueFlag
import com.zombachu.stick.element.ValidatedParameter
import com.zombachu.stick.element.ValueFlag
import com.zombachu.stick.impl.Arguments
import com.zombachu.stick.impl.InvalidSenderDefault
import com.zombachu.stick.impl.Requirement
import com.zombachu.stick.impl.StructureScope
import kotlin.experimental.ExperimentalTypeInference
import kotlin.reflect.KClass

@OverloadResolutionByLambdaReturnType
@JvmName("requireAsUnknownSize")
fun <S : Any, S2 : Any, E : Environment, T> StructureScope<E, S>.requireAs(
    transform: (S) -> S2,
    requirement: Requirement<E, S> = requirement { SenderValidationResult.success() },
    // Outer StructureElement is to provide syntax compatibility with other extension functions w/ trailing lambda
    parameter: StructureScope<E, S2>.() -> Parameter.UnknownSize<E, S2, T>,
): ValidatedParameter.UnknownSize<E, S, T> =
    TransformedParameter(
        parameter(this.forSender()),
        transform,
        requirement,
    )

@OverloadResolutionByLambdaReturnType
@JvmName("requireAsFixedSize")
fun <S : Any, S2 : Any, E : Environment, T> StructureScope<E, S>.requireAs(
    transform: (S) -> S2,
    requirement: Requirement<E, S> = requirement { SenderValidationResult.success() },
    // Outer StructureElement is to provide syntax compatibility with other extension functions w/ trailing lambda
    parameter: StructureScope<E, S2>.() -> Parameter.FixedSize<E, S2, T>,
): ValidatedParameter.FixedSize<E, S, T> =
    TransformedParameter(
        parameter(this.forSender()),
        transform,
        requirement,
    )

@OverloadResolutionByLambdaReturnType
@JvmName("requireAsValueFlag")
fun <E : Environment, S : Any, S2 : Any, T> StructureScope<E, S>.requireAs(
    transform: (S) -> S2,
    invalidSenderDefault: InvalidSenderDefault<E, S, T>,
    // Outer StructureElement is to provide syntax compatibility with other extension functions w/ trailing lambda
    flag: StructureScope<E, S2>.() -> ValueFlag<E, S2, T>,
): ValueFlag<E, S, T> =
    TransformedValueFlag(
        flag(this.forSender()),
        transform,
        invalidSenderDefault,
    )

@OverloadResolutionByLambdaReturnType
@JvmName("requireAsHybridFlag")
fun <E : Environment, S : Any, S2 : Any, T> StructureScope<E, S>.requireAs(
    transform: (S) -> S2,
    invalidSenderDefault: InvalidSenderDefault<E, S, HybridFlagResult<T>>,
    // Outer StructureElement is to provide syntax compatibility with other extension functions w/ trailing lambda
    flag: StructureScope<E, S2>.() -> HybridFlag<E, S2, T>,
): HybridFlag<E, S, T> =
    TransformedHybridFlag(
        flag(this.forSender()),
        transform,
        invalidSenderDefault,
    )

@OverloadResolutionByLambdaReturnType
@JvmName("requireAsCommand")
fun <E : Environment, S : Any, S2 : Any, T_ : Arguments> StructureScope<E, S>.requireAs(
    transform: (S) -> S2,
    requirement: Requirement<E, S> = requirement { SenderValidationResult.success() },
    // Outer StructureElement is to provide syntax compatibility with other extension functions w/ trailing lambda
    command: StructureScope<E, S2>.() -> Structure<E, S2, T_>,
): Structure<E, S, T_> =
    TransformedStructure(
        command(this.forSender()),
        transform,
        requirement,
    )

@OverloadResolutionByLambdaReturnType
@JvmName("requireIsUnknownSize")
inline fun <E : Environment, S : Any, reified S2 : S, T> StructureScope<E, S>.requireIs(
    senderType: KClass<S2>,
    requirement: Requirement<E, S> = requirement { SenderValidationResult.success() },
    // Outer StructureElement is to provide syntax compatibility with other extension functions w/ trailing lambda
    noinline parameter: StructureScope<E, S2>.() -> Parameter.UnknownSize<E, S2, T>,
): ValidatedParameter.UnknownSize<E, S, T> =
    requireAs(
        { it as S2 },
        requirement + requirement(SenderValidationResult::failSenderType) { it.sender is S2 },
        parameter
    )

@OverloadResolutionByLambdaReturnType
@JvmName("requireIsFixedSize")
inline fun <E : Environment, S : Any, reified S2 : S, T> StructureScope<E, S>.requireIs(
    senderType: KClass<S2>,
    requirement: Requirement<E, S> = requirement { SenderValidationResult.success() },
    // Outer StructureElement is to provide syntax compatibility with other extension functions w/ trailing lambda
    noinline parameter: StructureScope<E, S2>.() -> Parameter.FixedSize<E, S2, T>,
): ValidatedParameter.FixedSize<E, S, T> =
    requireAs(
        { it as S2 },
        requirement + requirement(SenderValidationResult::failSenderType) { it.sender is S2 },
        parameter
    )

@OverloadResolutionByLambdaReturnType
@JvmName("requireIsValueFlag")
inline fun <E : Environment, S : Any, reified S2 : S, T> StructureScope<E, S>.requireIs(
    senderType: KClass<S2>,
    invalidSenderDefault: InvalidSenderDefault<E, S, T>,
    // Outer StructureElement is to provide syntax compatibility with other extension functions w/ trailing lambda
    noinline flag: StructureScope<E, S2>.() -> ValueFlag<E, S2, T>,
): ValueFlag<E, S, T> =
    requireAs(
        { it as S2 },
        invalidDefault(
            invalidSenderDefault.value,
            requirement(invalidSenderDefault) + requirement(SenderValidationResult::failSenderType) { it.sender is S2 }
        ),
        flag
    )

@OverloadResolutionByLambdaReturnType
@JvmName("requireIsHybridFlag")
inline fun <E : Environment, S : Any, reified S2 : S, T> StructureScope<E, S>.requireIs(
    senderType: KClass<S2>,
    invalidSenderDefault: InvalidSenderDefault<E, S, HybridFlagResult<T>>,
    // Outer StructureElement is to provide syntax compatibility with other extension functions w/ trailing lambda
    noinline flag: StructureScope<E, S2>.() -> HybridFlag<E, S2, T>,
): HybridFlag<E, S, T> =
    requireAs(
        { it as S2 },
        invalidDefault(
            invalidSenderDefault.value,
            requirement(invalidSenderDefault) + requirement(SenderValidationResult::failSenderType) { it.sender is S2 }
        ),
        flag
    )

@OverloadResolutionByLambdaReturnType
@JvmName("requireIsCommand")
inline fun <E : Environment, S : Any, reified S2 : S, T_ : Arguments> StructureScope<E, S>.requireIs(
    senderType: KClass<S2>,
    requirement: Requirement<E, S> = requirement { SenderValidationResult.success() },
    // Outer StructureElement is to provide syntax compatibility with other extension functions w/ trailing lambda
    noinline command: StructureScope<E, S2>.() -> Structure<E, S2, T_>,
): Structure<E, S, T_> =
    requireAs(
        { it as S2 },
        requirement + requirement(SenderValidationResult::failSenderType) { it.sender is S2 },
        command
    )

@OverloadResolutionByLambdaReturnType
@JvmName("requireUnknownSize")
fun <E : Environment, S : Any, T> StructureScope<E, S>.require(
    requirement: Requirement<E, S>,
    // Outer StructureElement is to provide syntax compatibility with other extension functions w/ trailing lambda
    parameter: StructureScope<E, S>.() -> Parameter.UnknownSize<E, S, T>,
): ValidatedParameter.UnknownSize<E, S, T> =
    requireAs({ it }, requirement, parameter)

@OverloadResolutionByLambdaReturnType
@JvmName("requireFixedSize")
fun <E : Environment, S : Any, T> StructureScope<E, S>.require(
    requirement: Requirement<E, S>,
    // Outer StructureElement is to provide syntax compatibility with other extension functions w/ trailing lambda
    parameter: StructureScope<E, S>.() -> Parameter.FixedSize<E, S, T>,
): ValidatedParameter.FixedSize<E, S, T> =
    requireAs({ it }, requirement, parameter)

@OverloadResolutionByLambdaReturnType
@JvmName("requireValueFlag")
fun <E : Environment, S : Any, T> StructureScope<E, S>.require(
    invalidSenderDefault: InvalidSenderDefault<E, S, T>,
    // Outer StructureElement is to provide syntax compatibility with other extension functions w/ trailing lambda
    flag: StructureScope<E, S>.() -> ValueFlag<E, S, T>,
): ValueFlag<E, S, T> =
    requireAs({ it }, invalidSenderDefault, flag)

@OverloadResolutionByLambdaReturnType
@JvmName("requireHybridFlag")
fun <E : Environment, S : Any, T> StructureScope<E, S>.require(
    invalidSenderDefault: InvalidSenderDefault<E, S, HybridFlagResult<T>>,
    // Outer StructureElement is to provide syntax compatibility with other extension functions w/ trailing lambda
    flag: StructureScope<E, S>.() -> HybridFlag<E, S, T>,
): HybridFlag<E, S, T> =
    requireAs({ it }, invalidSenderDefault, flag)

@OverloadResolutionByLambdaReturnType
@JvmName("requireCommand")
fun <E : Environment, S : Any, T : Arguments> StructureScope<E, S>.require(
    requirement: Requirement<E, S> = requirement { SenderValidationResult.success() },
    // Outer StructureElement is to provide syntax compatibility with other extension functions w/ trailing lambda
    command: StructureScope<E, S>.() -> Structure<E, S, T>,
): Structure<E, S, T> =
    requireAs({ it }, requirement, command)
