@file:OptIn(ExperimentalTypeInference::class)

package com.zombachu.stick.structure

import com.zombachu.stick.ContextualValue
import com.zombachu.stick.SenderContext
import com.zombachu.stick.SenderValidationResult
import com.zombachu.stick.element.Flag
import com.zombachu.stick.element.Parameter
import com.zombachu.stick.element.Structure
import com.zombachu.stick.element.ValidatedCommand
import com.zombachu.stick.element.ValidatedFlag
import com.zombachu.stick.element.ValidatedParameter
import com.zombachu.stick.element.ValidatedParameterImpl
import com.zombachu.stick.impl.Requirement
import com.zombachu.stick.impl.SenderScope
import com.zombachu.stick.impl.StructureElement
import com.zombachu.stick.impl.StructureScope
import kotlin.experimental.ExperimentalTypeInference
import kotlin.reflect.KClass

@OverloadResolutionByLambdaReturnType
@JvmName("requireAs")
fun <O : Any, O2 : Any, S : SenderContext, T : Any> SenderScope<S, O>.requireAs(
    transform: (O) -> O2,
    requirement: Requirement<S, O> = requirement { SenderValidationResult.success() },
    // Outer StructureElement is to provide syntax compatibility with other extension functions w/ trailing lambda
    parameter: StructureElement<S, O2, StructureElement<S, O2, Parameter<S, O2, T>>>,
): StructureElement<S, O, ValidatedParameter<S, O, T>> = {
    val scope: StructureScope<S, O2> = this.forSender()
    ValidatedParameterImpl(
        parameter(scope)(scope),
        requirement,
        transform,
    )
}

@OverloadResolutionByLambdaReturnType
fun <S : SenderContext, O : Any, O2 : Any, T : Any> SenderScope<S, O>.requireAs(
    transform: (O) -> O2,
    invalidDefault: ContextualValue<S, O, T>,
    requirement: Requirement<S, O> = requirement { SenderValidationResult.success() },
    // Outer StructureElement is to provide syntax compatibility with other extension functions w/ trailing lambda
    flag: StructureElement<S, O2, StructureElement<S, O2, Flag<S, O2, T>>>,
): StructureElement<S, O, Flag<S, O, T>> = {
    val scope: StructureScope<S, O2> = this.forSender()
    ValidatedFlag(
        flag(scope)(scope),
        requirement,
        invalidDefault,
        transform,
    )
}

@OverloadResolutionByLambdaReturnType
@JvmName("requireAsCommand")
fun <S : SenderContext, O : Any, O2 : Any> SenderScope<S, O>.requireAs(
    transform: (O) -> O2,
    requirement: Requirement<S, O> = requirement { SenderValidationResult.success() },
    // Outer StructureElement is to provide syntax compatibility with other extension functions w/ trailing lambda
    command: StructureElement<S, O2, StructureElement<S, O2, Structure<S, O2>>>,
): StructureElement<S, O, Structure<S, O>> = {
    val scope: StructureScope<S, O2> = this.forSender()
    ValidatedCommand(
        command(scope)(scope),
        requirement,
        transform,
    )
}

@OverloadResolutionByLambdaReturnType
@JvmName("requireIs")
inline fun <S : SenderContext, O : Any, reified O2 : O, T : Any> SenderScope<S, O>.requireIs(
    senderType: KClass<O2>,
    requirement: Requirement<S, O> = requirement { SenderValidationResult.success() },
    // Outer StructureElement is to provide syntax compatibility with other extension functions w/ trailing lambda
    noinline parameter: StructureElement<S, O2, StructureElement<S, O2, Parameter<S, O2, T>>>,
): StructureElement<S, O, ValidatedParameter<S, O, T>> =
    requireAs(
        { it as O2 },
        requirement + requirement(SenderValidationResult.failSenderType()) { it.sender is O2 },
        parameter
    )

@OverloadResolutionByLambdaReturnType
inline fun <S : SenderContext, O : Any, reified O2 : O, T : Any> SenderScope<S, O>.requireIs(
    senderType: KClass<O2>,
    noinline invalidDefault: ContextualValue<S, O, T>,
    requirement: Requirement<S, O> = requirement { SenderValidationResult.success() },
    // Outer StructureElement is to provide syntax compatibility with other extension functions w/ trailing lambda
    noinline flag: StructureElement<S, O2, StructureElement<S, O2, Flag<S, O2, T>>>,
): StructureElement<S, O, Flag<S, O, T>> =
    requireAs(
        { it as O2 },
        invalidDefault,
        requirement + requirement(SenderValidationResult.failSenderType()) { it.sender is O2 },
        flag
    )

@OverloadResolutionByLambdaReturnType
inline fun <S : SenderContext, O : Any, reified O2 : O, T : Any> SenderScope<S, O>.requireIs(
    senderType: KClass<O2>,
    invalidDefault: T,
    requirement: Requirement<S, O> = requirement { SenderValidationResult.success() },
    // Outer StructureElement is to provide syntax compatibility with other extension functions w/ trailing lambda
    noinline flag: StructureElement<S, O2, StructureElement<S, O2, Flag<S, O2, T>>>,
): StructureElement<S, O, Flag<S, O, T>> =
    requireAs(
        { it as O2 },
        { invalidDefault },
        requirement + requirement(SenderValidationResult.failSenderType()) { it.sender is O2 },
        flag
    )

@OverloadResolutionByLambdaReturnType
@JvmName("requireIsCommand")
inline fun <S : SenderContext, O : Any, reified O2 : O> SenderScope<S, O>.requireIs(
    senderType: KClass<O2>,
    requirement: Requirement<S, O> = requirement { SenderValidationResult.success() },
    // Outer StructureElement is to provide syntax compatibility with other extension functions w/ trailing lambda
    noinline command: StructureElement<S, O2, StructureElement<S, O2, Structure<S, O2>>>,
): StructureElement<S, O, Structure<S, O>> =
    requireAs(
        { it as O2 },
        requirement + requirement(SenderValidationResult.failSenderType()) { it.sender is O2 },
        command
    )

@OverloadResolutionByLambdaReturnType
@JvmName("require")
fun <S : SenderContext, O : Any, T : Any> SenderScope<S, O>.require(
    requirement: Requirement<S, O>,
    // Outer StructureElement is to provide syntax compatibility with other extension functions w/ trailing lambda
    parameter: StructureElement<S, O, StructureElement<S, O, Parameter<S, O, T>>>,
): StructureElement<S, O, ValidatedParameter<S, O, T>> =
    requireAs({ it }, requirement, parameter)

@OverloadResolutionByLambdaReturnType
fun <S : SenderContext, O : Any, T : Any> SenderScope<S, O>.require(
    invalidDefault: ContextualValue<S, O, T>,
    requirement: Requirement<S, O> = requirement { SenderValidationResult.success() },
    // Outer StructureElement is to provide syntax compatibility with other extension functions w/ trailing lambda
    flag: StructureElement<S, O, StructureElement<S, O, Flag<S, O, T>>>,
): StructureElement<S, O, Flag<S, O, T>> =
    requireAs({ it }, invalidDefault, requirement, flag)

@OverloadResolutionByLambdaReturnType
fun <S : SenderContext, O : Any, T : Any> SenderScope<S, O>.require(
    invalidDefault: T,
    requirement: Requirement<S, O> = requirement { SenderValidationResult.success() },
    // Outer StructureElement is to provide syntax compatibility with other extension functions w/ trailing lambda
    flag: StructureElement<S, O, StructureElement<S, O, Flag<S, O, T>>>,
): StructureElement<S, O, Flag<S, O, T>> =
    requireAs({ it }, { invalidDefault }, requirement, flag)

@OverloadResolutionByLambdaReturnType
@JvmName("requireCommand")
fun <S : SenderContext, O : Any> SenderScope<S, O>.require(
    requirement: Requirement<S, O> = requirement { SenderValidationResult.success() },
    // Outer StructureElement is to provide syntax compatibility with other extension functions w/ trailing lambda
    command: StructureElement<S, O, StructureElement<S, O, Structure<S, O>>>,
): StructureElement<S, O, Structure<S, O>> =
    requireAs({ it }, requirement, command)
