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
import com.zombachu.stick.impl.BuilderScope
import com.zombachu.stick.impl.InvalidSenderDefault
import com.zombachu.stick.impl.Requirement
import com.zombachu.stick.impl.StructureElement
import com.zombachu.stick.impl.StructureScope
import kotlin.experimental.ExperimentalTypeInference
import kotlin.reflect.KClass

@OverloadResolutionByLambdaReturnType
@JvmName("requireAsUnknownSize")
fun <S : Any, S2 : Any, E : Environment, T> BuilderScope<E, S>.requireAs(
    transform: (S) -> S2,
    requirement: Requirement<E, S> = requirement { SenderValidationResult.success() },
    // Outer StructureElement is to provide syntax compatibility with other extension functions w/ trailing lambda
    parameter: StructureElement<E, S2, StructureElement<E, S2, Parameter.UnknownSize<E, S2, T>>>,
): StructureElement<E, S, ValidatedParameter.UnknownSize<E, S, T>> = {
    val scope: StructureScope<E, S2> = this.forSender()
    TransformedParameter(
        parameter(scope)(scope),
        transform,
        requirement,
    )
}

@OverloadResolutionByLambdaReturnType
@JvmName("requireAsFixedSize")
fun <S : Any, S2 : Any, E : Environment, T> BuilderScope<E, S>.requireAs(
    transform: (S) -> S2,
    requirement: Requirement<E, S> = requirement { SenderValidationResult.success() },
    // Outer StructureElement is to provide syntax compatibility with other extension functions w/ trailing lambda
    parameter: StructureElement<E, S2, StructureElement<E, S2, Parameter.FixedSize<E, S2, T>>>,
): StructureElement<E, S, ValidatedParameter.FixedSize<E, S, T>> = {
    val scope: StructureScope<E, S2> = this.forSender()
    TransformedParameter(
        parameter(scope)(scope),
        transform,
        requirement,
    )
}

@OverloadResolutionByLambdaReturnType
@JvmName("requireAsValueFlag")
fun <E : Environment, S : Any, S2 : Any, T> BuilderScope<E, S>.requireAs(
    transform: (S) -> S2,
    invalidSenderDefault: InvalidSenderDefault<E, S, T>,
    // Outer StructureElement is to provide syntax compatibility with other extension functions w/ trailing lambda
    flag: StructureElement<E, S2, StructureElement<E, S2, ValueFlag<E, S2, T>>>,
): StructureElement<E, S, ValueFlag<E, S, T>> = {
    val scope: StructureScope<E, S2> = this.forSender()
    TransformedValueFlag(
        flag(scope)(scope),
        transform,
        invalidSenderDefault,
    )
}

@OverloadResolutionByLambdaReturnType
@JvmName("requireAsHybridFlag")
fun <E : Environment, S : Any, S2 : Any, T> BuilderScope<E, S>.requireAs(
    transform: (S) -> S2,
    invalidSenderDefault: InvalidSenderDefault<E, S, HybridFlagResult<T>>,
    // Outer StructureElement is to provide syntax compatibility with other extension functions w/ trailing lambda
    flag: StructureElement<E, S2, StructureElement<E, S2, HybridFlag<E, S2, T>>>,
): StructureElement<E, S, HybridFlag<E, S, T>> = {
    val scope: StructureScope<E, S2> = this.forSender()
    TransformedHybridFlag(
        flag(scope)(scope),
        transform,
        invalidSenderDefault,
    )
}

@OverloadResolutionByLambdaReturnType
@JvmName("requireAsCommand")
fun <E : Environment, S : Any, S2 : Any> BuilderScope<E, S>.requireAs(
    transform: (S) -> S2,
    requirement: Requirement<E, S> = requirement { SenderValidationResult.success() },
    // Outer StructureElement is to provide syntax compatibility with other extension functions w/ trailing lambda
    command: StructureElement<E, S2, StructureElement<E, S2, Structure<E, S2>>>,
): StructureElement<E, S, Structure<E, S>> = {
    val scope: StructureScope<E, S2> = this.forSender()
    TransformedStructure(
        command(scope)(scope),
        transform,
        requirement,
    )
}

@OverloadResolutionByLambdaReturnType
@JvmName("requireIsUnknownSize")
inline fun <E : Environment, S : Any, reified S2 : S, T> BuilderScope<E, S>.requireIs(
    senderType: KClass<S2>,
    requirement: Requirement<E, S> = requirement { SenderValidationResult.success() },
    // Outer StructureElement is to provide syntax compatibility with other extension functions w/ trailing lambda
    noinline parameter: StructureElement<E, S2, StructureElement<E, S2, Parameter.UnknownSize<E, S2, T>>>,
): StructureElement<E, S, ValidatedParameter.UnknownSize<E, S, T>> =
    requireAs(
        { it as S2 },
        requirement + requirement(SenderValidationResult::failSenderType) { it.sender is S2 },
        parameter
    )

@OverloadResolutionByLambdaReturnType
@JvmName("requireIsFixedSize")
inline fun <E : Environment, S : Any, reified S2 : S, T> BuilderScope<E, S>.requireIs(
    senderType: KClass<S2>,
    requirement: Requirement<E, S> = requirement { SenderValidationResult.success() },
    // Outer StructureElement is to provide syntax compatibility with other extension functions w/ trailing lambda
    noinline parameter: StructureElement<E, S2, StructureElement<E, S2, Parameter.FixedSize<E, S2, T>>>,
): StructureElement<E, S, ValidatedParameter.FixedSize<E, S, T>> =
    requireAs(
        { it as S2 },
        requirement + requirement(SenderValidationResult::failSenderType) { it.sender is S2 },
        parameter
    )

@OverloadResolutionByLambdaReturnType
@JvmName("requireIsValueFlag")
inline fun <E : Environment, S : Any, reified S2 : S, T> BuilderScope<E, S>.requireIs(
    senderType: KClass<S2>,
    invalidSenderDefault: InvalidSenderDefault<E, S, T>,
    // Outer StructureElement is to provide syntax compatibility with other extension functions w/ trailing lambda
    noinline flag: StructureElement<E, S2, StructureElement<E, S2, ValueFlag<E, S2, T>>>,
): StructureElement<E, S, ValueFlag<E, S, T>> =
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
inline fun <E : Environment, S : Any, reified S2 : S, T> BuilderScope<E, S>.requireIs(
    senderType: KClass<S2>,
    invalidSenderDefault: InvalidSenderDefault<E, S, HybridFlagResult<T>>,
    // Outer StructureElement is to provide syntax compatibility with other extension functions w/ trailing lambda
    noinline flag: StructureElement<E, S2, StructureElement<E, S2, HybridFlag<E, S2, T>>>,
): StructureElement<E, S, HybridFlag<E, S, T>> =
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
inline fun <E : Environment, S : Any, reified S2 : S> BuilderScope<E, S>.requireIs(
    senderType: KClass<S2>,
    requirement: Requirement<E, S> = requirement { SenderValidationResult.success() },
    // Outer StructureElement is to provide syntax compatibility with other extension functions w/ trailing lambda
    noinline command: StructureElement<E, S2, StructureElement<E, S2, Structure<E, S2>>>,
): StructureElement<E, S, Structure<E, S>> =
    requireAs(
        { it as S2 },
        requirement + requirement(SenderValidationResult::failSenderType) { it.sender is S2 },
        command
    )

@OverloadResolutionByLambdaReturnType
@JvmName("requireUnknownSize")
fun <E : Environment, S : Any, T> BuilderScope<E, S>.require(
    requirement: Requirement<E, S>,
    // Outer StructureElement is to provide syntax compatibility with other extension functions w/ trailing lambda
    parameter: StructureElement<E, S, StructureElement<E, S, Parameter.UnknownSize<E, S, T>>>,
): StructureElement<E, S, ValidatedParameter.UnknownSize<E, S, T>> =
    requireAs({ it }, requirement, parameter)

@OverloadResolutionByLambdaReturnType
@JvmName("requireFixedSize")
fun <E : Environment, S : Any, T> BuilderScope<E, S>.require(
    requirement: Requirement<E, S>,
    // Outer StructureElement is to provide syntax compatibility with other extension functions w/ trailing lambda
    parameter: StructureElement<E, S, StructureElement<E, S, Parameter.FixedSize<E, S, T>>>,
): StructureElement<E, S, ValidatedParameter.FixedSize<E, S, T>> =
    requireAs({ it }, requirement, parameter)

@OverloadResolutionByLambdaReturnType
@JvmName("requireValueFlag")
fun <E : Environment, S : Any, T> BuilderScope<E, S>.require(
    invalidSenderDefault: InvalidSenderDefault<E, S, T>,
    // Outer StructureElement is to provide syntax compatibility with other extension functions w/ trailing lambda
    flag: StructureElement<E, S, StructureElement<E, S, ValueFlag<E, S, T>>>,
): StructureElement<E, S, ValueFlag<E, S, T>> =
    requireAs({ it }, invalidSenderDefault, flag)

@OverloadResolutionByLambdaReturnType
@JvmName("requireHybridFlag")
fun <E : Environment, S : Any, T> BuilderScope<E, S>.require(
    invalidSenderDefault: InvalidSenderDefault<E, S, HybridFlagResult<T>>,
    // Outer StructureElement is to provide syntax compatibility with other extension functions w/ trailing lambda
    flag: StructureElement<E, S, StructureElement<E, S, HybridFlag<E, S, T>>>,
): StructureElement<E, S, HybridFlag<E, S, T>> =
    requireAs({ it }, invalidSenderDefault, flag)

@OverloadResolutionByLambdaReturnType
@JvmName("requireCommand")
fun <E : Environment, S : Any> BuilderScope<E, S>.require(
    requirement: Requirement<E, S> = requirement { SenderValidationResult.success() },
    // Outer StructureElement is to provide syntax compatibility with other extension functions w/ trailing lambda
    command: StructureElement<E, S, StructureElement<E, S, Structure<E, S>>>,
): StructureElement<E, S, Structure<E, S>> =
    requireAs({ it }, requirement, command)
