@file:OptIn(ExperimentalTypeInference::class)

package com.zombachu.stick.structure

import com.zombachu.stick.ContextualValue
import com.zombachu.stick.Environment
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
fun <O : Any, O2 : Any, E : Environment, T : Any> SenderScope<E, O>.requireAs(
    transform: (O) -> O2,
    requirement: Requirement<E, O> = requirement { SenderValidationResult.success() },
    // Outer StructureElement is to provide syntax compatibility with other extension functions w/ trailing lambda
    parameter: StructureElement<E, O2, StructureElement<E, O2, Parameter<E, O2, T>>>,
): StructureElement<E, O, ValidatedParameter<E, O, T>> = {
    val scope: StructureScope<E, O2> = this.forSender()
    ValidatedParameterImpl(
        parameter(scope)(scope),
        requirement,
        transform,
    )
}

@OverloadResolutionByLambdaReturnType
fun <E : Environment, O : Any, O2 : Any, T : Any> SenderScope<E, O>.requireAs(
    transform: (O) -> O2,
    invalidDefault: ContextualValue<E, O, T>,
    requirement: Requirement<E, O> = requirement { SenderValidationResult.success() },
    // Outer StructureElement is to provide syntax compatibility with other extension functions w/ trailing lambda
    flag: StructureElement<E, O2, StructureElement<E, O2, Flag<E, O2, T>>>,
): StructureElement<E, O, Flag<E, O, T>> = {
    val scope: StructureScope<E, O2> = this.forSender()
    ValidatedFlag(
        flag(scope)(scope),
        requirement,
        invalidDefault,
        transform,
    )
}

@OverloadResolutionByLambdaReturnType
@JvmName("requireAsCommand")
fun <E : Environment, O : Any, O2 : Any> SenderScope<E, O>.requireAs(
    transform: (O) -> O2,
    requirement: Requirement<E, O> = requirement { SenderValidationResult.success() },
    // Outer StructureElement is to provide syntax compatibility with other extension functions w/ trailing lambda
    command: StructureElement<E, O2, StructureElement<E, O2, Structure<E, O2>>>,
): StructureElement<E, O, Structure<E, O>> = {
    val scope: StructureScope<E, O2> = this.forSender()
    ValidatedCommand(
        command(scope)(scope),
        requirement,
        transform,
    )
}

@OverloadResolutionByLambdaReturnType
@JvmName("requireIs")
inline fun <E : Environment, O : Any, reified O2 : O, T : Any> SenderScope<E, O>.requireIs(
    senderType: KClass<O2>,
    requirement: Requirement<E, O> = requirement { SenderValidationResult.success() },
    // Outer StructureElement is to provide syntax compatibility with other extension functions w/ trailing lambda
    noinline parameter: StructureElement<E, O2, StructureElement<E, O2, Parameter<E, O2, T>>>,
): StructureElement<E, O, ValidatedParameter<E, O, T>> =
    requireAs(
        { it as O2 },
        requirement + requirement(SenderValidationResult.failSenderType()) { it.sender is O2 },
        parameter
    )

@OverloadResolutionByLambdaReturnType
inline fun <E : Environment, O : Any, reified O2 : O, T : Any> SenderScope<E, O>.requireIs(
    senderType: KClass<O2>,
    noinline invalidDefault: ContextualValue<E, O, T>,
    requirement: Requirement<E, O> = requirement { SenderValidationResult.success() },
    // Outer StructureElement is to provide syntax compatibility with other extension functions w/ trailing lambda
    noinline flag: StructureElement<E, O2, StructureElement<E, O2, Flag<E, O2, T>>>,
): StructureElement<E, O, Flag<E, O, T>> =
    requireAs(
        { it as O2 },
        invalidDefault,
        requirement + requirement(SenderValidationResult.failSenderType()) { it.sender is O2 },
        flag
    )

@OverloadResolutionByLambdaReturnType
inline fun <E : Environment, O : Any, reified O2 : O, T : Any> SenderScope<E, O>.requireIs(
    senderType: KClass<O2>,
    invalidDefault: T,
    requirement: Requirement<E, O> = requirement { SenderValidationResult.success() },
    // Outer StructureElement is to provide syntax compatibility with other extension functions w/ trailing lambda
    noinline flag: StructureElement<E, O2, StructureElement<E, O2, Flag<E, O2, T>>>,
): StructureElement<E, O, Flag<E, O, T>> =
    requireAs(
        { it as O2 },
        { invalidDefault },
        requirement + requirement(SenderValidationResult.failSenderType()) { it.sender is O2 },
        flag
    )

@OverloadResolutionByLambdaReturnType
@JvmName("requireIsCommand")
inline fun <E : Environment, O : Any, reified O2 : O> SenderScope<E, O>.requireIs(
    senderType: KClass<O2>,
    requirement: Requirement<E, O> = requirement { SenderValidationResult.success() },
    // Outer StructureElement is to provide syntax compatibility with other extension functions w/ trailing lambda
    noinline command: StructureElement<E, O2, StructureElement<E, O2, Structure<E, O2>>>,
): StructureElement<E, O, Structure<E, O>> =
    requireAs(
        { it as O2 },
        requirement + requirement(SenderValidationResult.failSenderType()) { it.sender is O2 },
        command
    )

@OverloadResolutionByLambdaReturnType
@JvmName("require")
fun <E : Environment, O : Any, T : Any> SenderScope<E, O>.require(
    requirement: Requirement<E, O>,
    // Outer StructureElement is to provide syntax compatibility with other extension functions w/ trailing lambda
    parameter: StructureElement<E, O, StructureElement<E, O, Parameter<E, O, T>>>,
): StructureElement<E, O, ValidatedParameter<E, O, T>> =
    requireAs({ it }, requirement, parameter)

@OverloadResolutionByLambdaReturnType
fun <E : Environment, O : Any, T : Any> SenderScope<E, O>.require(
    invalidDefault: ContextualValue<E, O, T>,
    requirement: Requirement<E, O> = requirement { SenderValidationResult.success() },
    // Outer StructureElement is to provide syntax compatibility with other extension functions w/ trailing lambda
    flag: StructureElement<E, O, StructureElement<E, O, Flag<E, O, T>>>,
): StructureElement<E, O, Flag<E, O, T>> =
    requireAs({ it }, invalidDefault, requirement, flag)

@OverloadResolutionByLambdaReturnType
fun <E : Environment, O : Any, T : Any> SenderScope<E, O>.require(
    invalidDefault: T,
    requirement: Requirement<E, O> = requirement { SenderValidationResult.success() },
    // Outer StructureElement is to provide syntax compatibility with other extension functions w/ trailing lambda
    flag: StructureElement<E, O, StructureElement<E, O, Flag<E, O, T>>>,
): StructureElement<E, O, Flag<E, O, T>> =
    requireAs({ it }, { invalidDefault }, requirement, flag)

@OverloadResolutionByLambdaReturnType
@JvmName("requireCommand")
fun <E : Environment, O : Any> SenderScope<E, O>.require(
    requirement: Requirement<E, O> = requirement { SenderValidationResult.success() },
    // Outer StructureElement is to provide syntax compatibility with other extension functions w/ trailing lambda
    command: StructureElement<E, O, StructureElement<E, O, Structure<E, O>>>,
): StructureElement<E, O, Structure<E, O>> =
    requireAs({ it }, requirement, command)
