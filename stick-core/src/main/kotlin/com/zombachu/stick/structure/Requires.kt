@file:OptIn(ExperimentalTypeInference::class)

package com.zombachu.stick.structure

import com.zombachu.stick.ContextualValue
import com.zombachu.stick.Environment
import com.zombachu.stick.ParsingResult
import com.zombachu.stick.SenderValidationResult
import com.zombachu.stick.element.Flag
import com.zombachu.stick.element.Parameter
import com.zombachu.stick.element.Structure
import com.zombachu.stick.element.TransformedFlag
import com.zombachu.stick.element.TransformedParameter
import com.zombachu.stick.element.TransformedStructure
import com.zombachu.stick.element.ValidatedParameter
import com.zombachu.stick.impl.BuilderScope
import com.zombachu.stick.impl.Requirement
import com.zombachu.stick.impl.StructureElement
import com.zombachu.stick.impl.StructureScope
import kotlin.experimental.ExperimentalTypeInference
import kotlin.reflect.KClass

@OverloadResolutionByLambdaReturnType
@JvmName("requireAs")
fun <S : Any, S2 : Any, E : Environment, T : Any> BuilderScope<E, S>.requireAs(
    transform: (S) -> S2,
    requirement: Requirement<E, S> = requirement { SenderValidationResult.success() },
    // Outer StructureElement is to provide syntax compatibility with other extension functions w/ trailing lambda
    parameter: StructureElement<E, S2, StructureElement<E, S2, Parameter<E, S2, T>>>,
): StructureElement<E, S, ValidatedParameter<E, S, T>> = {
    val scope: StructureScope<E, S2> = this.forSender()
    TransformedParameter(
        parameter(scope)(scope),
        transform,
        requirement,
    )
}

@OverloadResolutionByLambdaReturnType
fun <E : Environment, S : Any, S2 : Any, T : Any> BuilderScope<E, S>.requireAs(
    transform: (S) -> S2,
    invalidDefault: ContextualValue<E, S, T>,
    requirement: Requirement<E, S> = requirement { SenderValidationResult.success() },
    // Outer StructureElement is to provide syntax compatibility with other extension functions w/ trailing lambda
    flag: StructureElement<E, S2, StructureElement<E, S2, Flag<E, S2, T>>>,
): StructureElement<E, S, Flag<E, S, T>> = {
    val scope: StructureScope<E, S2> = this.forSender()
    TransformedFlag(
        flag(scope)(scope),
        transform,
        requirement,
        invalidDefault,
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
@JvmName("requireIs")
inline fun <E : Environment, S : Any, reified S2 : S, T : Any> BuilderScope<E, S>.requireIs(
    senderType: KClass<S2>,
    requirement: Requirement<E, S> = requirement { SenderValidationResult.success() },
    // Outer StructureElement is to provide syntax compatibility with other extension functions w/ trailing lambda
    noinline parameter: StructureElement<E, S2, StructureElement<E, S2, Parameter<E, S2, T>>>,
): StructureElement<E, S, ValidatedParameter<E, S, T>> =
    requireAs(
        { it as S2 },
        requirement + requirement(SenderValidationResult::failSenderType) { it.sender is S2 },
        parameter
    )

@OverloadResolutionByLambdaReturnType
inline fun <E : Environment, S : Any, reified S2 : S, T : Any> BuilderScope<E, S>.requireIs(
    senderType: KClass<S2>,
    noinline invalidDefault: ContextualValue<E, S, T>,
    requirement: Requirement<E, S> = requirement { SenderValidationResult.success() },
    // Outer StructureElement is to provide syntax compatibility with other extension functions w/ trailing lambda
    noinline flag: StructureElement<E, S2, StructureElement<E, S2, Flag<E, S2, T>>>,
): StructureElement<E, S, Flag<E, S, T>> =
    requireAs(
        { it as S2 },
        invalidDefault,
        requirement + requirement(SenderValidationResult::failSenderType) { it.sender is S2 },
        flag
    )

@OverloadResolutionByLambdaReturnType
inline fun <E : Environment, S : Any, reified S2 : S, T : Any> BuilderScope<E, S>.requireIs(
    senderType: KClass<S2>,
    invalidDefault: T,
    requirement: Requirement<E, S> = requirement { SenderValidationResult.success() },
    // Outer StructureElement is to provide syntax compatibility with other extension functions w/ trailing lambda
    noinline flag: StructureElement<E, S2, StructureElement<E, S2, Flag<E, S2, T>>>,
): StructureElement<E, S, Flag<E, S, T>> =
    requireAs(
        { it as S2 },
        { ParsingResult.success(invalidDefault) },
        requirement + requirement(SenderValidationResult::failSenderType) { it.sender is S2 },
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
@JvmName("require")
fun <E : Environment, S : Any, T : Any> BuilderScope<E, S>.require(
    requirement: Requirement<E, S>,
    // Outer StructureElement is to provide syntax compatibility with other extension functions w/ trailing lambda
    parameter: StructureElement<E, S, StructureElement<E, S, Parameter<E, S, T>>>,
): StructureElement<E, S, ValidatedParameter<E, S, T>> =
    requireAs({ it }, requirement, parameter)

@OverloadResolutionByLambdaReturnType
fun <E : Environment, S : Any, T : Any> BuilderScope<E, S>.require(
    invalidDefault: ContextualValue<E, S, T>,
    requirement: Requirement<E, S> = requirement { SenderValidationResult.success() },
    // Outer StructureElement is to provide syntax compatibility with other extension functions w/ trailing lambda
    flag: StructureElement<E, S, StructureElement<E, S, Flag<E, S, T>>>,
): StructureElement<E, S, Flag<E, S, T>> =
    requireAs({ it }, invalidDefault, requirement, flag)

@OverloadResolutionByLambdaReturnType
fun <E : Environment, S : Any, T : Any> BuilderScope<E, S>.require(
    invalidDefault: T,
    requirement: Requirement<E, S> = requirement { SenderValidationResult.success() },
    // Outer StructureElement is to provide syntax compatibility with other extension functions w/ trailing lambda
    flag: StructureElement<E, S, StructureElement<E, S, Flag<E, S, T>>>,
): StructureElement<E, S, Flag<E, S, T>> =
    requireAs({ it }, { ParsingResult.success(invalidDefault) }, requirement, flag)

@OverloadResolutionByLambdaReturnType
@JvmName("requireCommand")
fun <E : Environment, S : Any> BuilderScope<E, S>.require(
    requirement: Requirement<E, S> = requirement { SenderValidationResult.success() },
    // Outer StructureElement is to provide syntax compatibility with other extension functions w/ trailing lambda
    command: StructureElement<E, S, StructureElement<E, S, Structure<E, S>>>,
): StructureElement<E, S, Structure<E, S>> =
    requireAs({ it }, requirement, command)
