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
fun <O : Any, O2 : Any, S : SenderContext, T : Any> SenderScope<O, S>.requireAs(
    transform: (O) -> O2,
    requirement: Requirement<O, S> = requirement { SenderValidationResult.success() },
    // Outer StructureElement is to provide syntax compatibility with other extension functions w/ trailing lambda
    parameter: StructureElement<O2, S, StructureElement<O2, S, Parameter<O2, S, T>>>,
): StructureElement<O, S, ValidatedParameter<O, S, T>> = {
    val scope: StructureScope<O2, S> = this.forSender()
    ValidatedParameterImpl(
        parameter(scope)(scope),
        requirement,
        transform,
    )
}

@OverloadResolutionByLambdaReturnType
fun <O : Any, S : SenderContext, O2 : Any, T : Any> SenderScope<O, S>.requireAs(
    transform: (O) -> O2,
    invalidDefault: ContextualValue<O, S, T>,
    requirement: Requirement<O, S> = requirement { SenderValidationResult.success() },
    // Outer StructureElement is to provide syntax compatibility with other extension functions w/ trailing lambda
    flag: StructureElement<O2, S, StructureElement<O2, S, Flag<O2, S, T>>>,
): StructureElement<O, S, Flag<O, S, T>> = {
    val scope: StructureScope<O2, S> = this.forSender()
    ValidatedFlag(
        flag(scope)(scope),
        requirement,
        invalidDefault,
        transform,
    )
}

@OverloadResolutionByLambdaReturnType
@JvmName("requireAsCommand")
fun <O : Any, S : SenderContext, O2 : Any> SenderScope<O, S>.requireAs(
    transform: (O) -> O2,
    requirement: Requirement<O, S> = requirement { SenderValidationResult.success() },
    // Outer StructureElement is to provide syntax compatibility with other extension functions w/ trailing lambda
    command: StructureElement<O2, S, StructureElement<O2, S, Structure<O2, S>>>,
): StructureElement<O, S, Structure<O, S>> = {
    val scope: StructureScope<O2, S> = this.forSender()
    ValidatedCommand(
        command(scope)(scope),
        requirement,
        transform,
    )
}

@OverloadResolutionByLambdaReturnType
@JvmName("requireIs")
inline fun <O : Any, S : SenderContext, reified O2 : O, T : Any> SenderScope<O, S>.requireIs(
    senderType: KClass<O2>,
    requirement: Requirement<O, S> = requirement { SenderValidationResult.success() },
    // Outer StructureElement is to provide syntax compatibility with other extension functions w/ trailing lambda
    noinline parameter: StructureElement<O2, S, StructureElement<O2, S, Parameter<O2, S, T>>>,
): StructureElement<O, S, ValidatedParameter<O, S, T>> =
    requireAs(
        { it as O2 },
        requirement + requirement(SenderValidationResult.failSenderType()) { it.sender is O2 },
        parameter
    )

@OverloadResolutionByLambdaReturnType
inline fun <O : Any, S : SenderContext, reified O2 : O, T : Any> SenderScope<O, S>.requireIs(
    senderType: KClass<O2>,
    noinline invalidDefault: ContextualValue<O, S, T>,
    requirement: Requirement<O, S> = requirement { SenderValidationResult.success() },
    // Outer StructureElement is to provide syntax compatibility with other extension functions w/ trailing lambda
    noinline flag: StructureElement<O2, S, StructureElement<O2, S, Flag<O2, S, T>>>,
): StructureElement<O, S, Flag<O, S, T>> =
    requireAs(
        { it as O2 },
        invalidDefault,
        requirement + requirement(SenderValidationResult.failSenderType()) { it.sender is O2 },
        flag
    )

@OverloadResolutionByLambdaReturnType
inline fun <O : Any, S : SenderContext, reified O2 : O, T : Any> SenderScope<O, S>.requireIs(
    senderType: KClass<O2>,
    invalidDefault: T,
    requirement: Requirement<O, S> = requirement { SenderValidationResult.success() },
    // Outer StructureElement is to provide syntax compatibility with other extension functions w/ trailing lambda
    noinline flag: StructureElement<O2, S, StructureElement<O2, S, Flag<O2, S, T>>>,
): StructureElement<O, S, Flag<O, S, T>> =
    requireAs(
        { it as O2 },
        { invalidDefault },
        requirement + requirement(SenderValidationResult.failSenderType()) { it.sender is O2 },
        flag
    )

@OverloadResolutionByLambdaReturnType
@JvmName("requireIsCommand")
inline fun <O : Any, S : SenderContext, reified O2 : O> SenderScope<O, S>.requireIs(
    senderType: KClass<O2>,
    requirement: Requirement<O, S> = requirement { SenderValidationResult.success() },
    // Outer StructureElement is to provide syntax compatibility with other extension functions w/ trailing lambda
    noinline command: StructureElement<O2, S, StructureElement<O2, S, Structure<O2, S>>>,
): StructureElement<O, S, Structure<O, S>> =
    requireAs(
        { it as O2 },
        requirement + requirement(SenderValidationResult.failSenderType()) { it.sender is O2 },
        command
    )

@OverloadResolutionByLambdaReturnType
@JvmName("require")
fun <O : Any, S : SenderContext, T : Any> SenderScope<O, S>.require(
    requirement: Requirement<O, S>,
    // Outer StructureElement is to provide syntax compatibility with other extension functions w/ trailing lambda
    parameter: StructureElement<O, S, StructureElement<O, S, Parameter<O, S, T>>>,
): StructureElement<O, S, ValidatedParameter<O, S, T>> =
    requireAs({ it }, requirement, parameter)

@OverloadResolutionByLambdaReturnType
fun <O : Any, S : SenderContext, T : Any> SenderScope<O, S>.require(
    invalidDefault: ContextualValue<O, S, T>,
    requirement: Requirement<O, S> = requirement { SenderValidationResult.success() },
    // Outer StructureElement is to provide syntax compatibility with other extension functions w/ trailing lambda
    flag: StructureElement<O, S, StructureElement<O, S, Flag<O, S, T>>>,
): StructureElement<O, S, Flag<O, S, T>> =
    requireAs({ it }, invalidDefault, requirement, flag)

@OverloadResolutionByLambdaReturnType
fun <O : Any, S : SenderContext, T : Any> SenderScope<O, S>.require(
    invalidDefault: T,
    requirement: Requirement<O, S> = requirement { SenderValidationResult.success() },
    // Outer StructureElement is to provide syntax compatibility with other extension functions w/ trailing lambda
    flag: StructureElement<O, S, StructureElement<O, S, Flag<O, S, T>>>,
): StructureElement<O, S, Flag<O, S, T>> =
    requireAs({ it }, { invalidDefault }, requirement, flag)

@OverloadResolutionByLambdaReturnType
@JvmName("requireCommand")
fun <O : Any, S : SenderContext> SenderScope<O, S>.require(
    requirement: Requirement<O, S> = requirement { SenderValidationResult.success() },
    // Outer StructureElement is to provide syntax compatibility with other extension functions w/ trailing lambda
    command: StructureElement<O, S, StructureElement<O, S, Structure<O, S>>>,
): StructureElement<O, S, Structure<O, S>> =
    requireAs({ it }, requirement, command)
