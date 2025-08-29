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
fun <S : Any, S2 : Any, E : Environment, T : Any> SenderScope<E, S>.requireAs(
    transform: (S) -> S2,
    requirement: Requirement<E, S> = requirement { SenderValidationResult.success() },
    // Outer StructureElement is to provide syntax compatibility with other extension functions w/ trailing lambda
    parameter: StructureElement<E, S2, StructureElement<E, S2, Parameter<E, S2, T>>>,
): StructureElement<E, S, ValidatedParameter<E, S, T>> = {
    val scope: StructureScope<E, S2> = this.forSender()
    ValidatedParameterImpl(
        parameter(scope)(scope),
        requirement,
        transform,
    )
}

@OverloadResolutionByLambdaReturnType
fun <E : Environment, S : Any, S2 : Any, T : Any> SenderScope<E, S>.requireAs(
    transform: (S) -> S2,
    invalidDefault: ContextualValue<E, S, T>,
    requirement: Requirement<E, S> = requirement { SenderValidationResult.success() },
    // Outer StructureElement is to provide syntax compatibility with other extension functions w/ trailing lambda
    flag: StructureElement<E, S2, StructureElement<E, S2, Flag<E, S2, T>>>,
): StructureElement<E, S, Flag<E, S, T>> = {
    val scope: StructureScope<E, S2> = this.forSender()
    ValidatedFlag(
        flag(scope)(scope),
        requirement,
        invalidDefault,
        transform,
    )
}

@OverloadResolutionByLambdaReturnType
@JvmName("requireAsCommand")
fun <E : Environment, S : Any, S2 : Any> SenderScope<E, S>.requireAs(
    transform: (S) -> S2,
    requirement: Requirement<E, S> = requirement { SenderValidationResult.success() },
    // Outer StructureElement is to provide syntax compatibility with other extension functions w/ trailing lambda
    command: StructureElement<E, S2, StructureElement<E, S2, Structure<E, S2>>>,
): StructureElement<E, S, Structure<E, S>> = {
    val scope: StructureScope<E, S2> = this.forSender()
    ValidatedCommand(
        command(scope)(scope),
        requirement,
        transform,
    )
}

@OverloadResolutionByLambdaReturnType
@JvmName("requireIs")
inline fun <E : Environment, S : Any, reified S2 : S, T : Any> SenderScope<E, S>.requireIs(
    senderType: KClass<S2>,
    requirement: Requirement<E, S> = requirement { SenderValidationResult.success() },
    // Outer StructureElement is to provide syntax compatibility with other extension functions w/ trailing lambda
    noinline parameter: StructureElement<E, S2, StructureElement<E, S2, Parameter<E, S2, T>>>,
): StructureElement<E, S, ValidatedParameter<E, S, T>> =
    requireAs(
        { it as S2 },
        requirement + requirement(SenderValidationResult.failSenderType()) { it.sender is S2 },
        parameter
    )

@OverloadResolutionByLambdaReturnType
inline fun <E : Environment, S : Any, reified S2 : S, T : Any> SenderScope<E, S>.requireIs(
    senderType: KClass<S2>,
    noinline invalidDefault: ContextualValue<E, S, T>,
    requirement: Requirement<E, S> = requirement { SenderValidationResult.success() },
    // Outer StructureElement is to provide syntax compatibility with other extension functions w/ trailing lambda
    noinline flag: StructureElement<E, S2, StructureElement<E, S2, Flag<E, S2, T>>>,
): StructureElement<E, S, Flag<E, S, T>> =
    requireAs(
        { it as S2 },
        invalidDefault,
        requirement + requirement(SenderValidationResult.failSenderType()) { it.sender is S2 },
        flag
    )

@OverloadResolutionByLambdaReturnType
inline fun <E : Environment, S : Any, reified S2 : S, T : Any> SenderScope<E, S>.requireIs(
    senderType: KClass<S2>,
    invalidDefault: T,
    requirement: Requirement<E, S> = requirement { SenderValidationResult.success() },
    // Outer StructureElement is to provide syntax compatibility with other extension functions w/ trailing lambda
    noinline flag: StructureElement<E, S2, StructureElement<E, S2, Flag<E, S2, T>>>,
): StructureElement<E, S, Flag<E, S, T>> =
    requireAs(
        { it as S2 },
        { invalidDefault },
        requirement + requirement(SenderValidationResult.failSenderType()) { it.sender is S2 },
        flag
    )

@OverloadResolutionByLambdaReturnType
@JvmName("requireIsCommand")
inline fun <E : Environment, S : Any, reified S2 : S> SenderScope<E, S>.requireIs(
    senderType: KClass<S2>,
    requirement: Requirement<E, S> = requirement { SenderValidationResult.success() },
    // Outer StructureElement is to provide syntax compatibility with other extension functions w/ trailing lambda
    noinline command: StructureElement<E, S2, StructureElement<E, S2, Structure<E, S2>>>,
): StructureElement<E, S, Structure<E, S>> =
    requireAs(
        { it as S2 },
        requirement + requirement(SenderValidationResult.failSenderType()) { it.sender is S2 },
        command
    )

@OverloadResolutionByLambdaReturnType
@JvmName("require")
fun <E : Environment, S : Any, T : Any> SenderScope<E, S>.require(
    requirement: Requirement<E, S>,
    // Outer StructureElement is to provide syntax compatibility with other extension functions w/ trailing lambda
    parameter: StructureElement<E, S, StructureElement<E, S, Parameter<E, S, T>>>,
): StructureElement<E, S, ValidatedParameter<E, S, T>> =
    requireAs({ it }, requirement, parameter)

@OverloadResolutionByLambdaReturnType
fun <E : Environment, S : Any, T : Any> SenderScope<E, S>.require(
    invalidDefault: ContextualValue<E, S, T>,
    requirement: Requirement<E, S> = requirement { SenderValidationResult.success() },
    // Outer StructureElement is to provide syntax compatibility with other extension functions w/ trailing lambda
    flag: StructureElement<E, S, StructureElement<E, S, Flag<E, S, T>>>,
): StructureElement<E, S, Flag<E, S, T>> =
    requireAs({ it }, invalidDefault, requirement, flag)

@OverloadResolutionByLambdaReturnType
fun <E : Environment, S : Any, T : Any> SenderScope<E, S>.require(
    invalidDefault: T,
    requirement: Requirement<E, S> = requirement { SenderValidationResult.success() },
    // Outer StructureElement is to provide syntax compatibility with other extension functions w/ trailing lambda
    flag: StructureElement<E, S, StructureElement<E, S, Flag<E, S, T>>>,
): StructureElement<E, S, Flag<E, S, T>> =
    requireAs({ it }, { invalidDefault }, requirement, flag)

@OverloadResolutionByLambdaReturnType
@JvmName("requireCommand")
fun <E : Environment, S : Any> SenderScope<E, S>.require(
    requirement: Requirement<E, S> = requirement { SenderValidationResult.success() },
    // Outer StructureElement is to provide syntax compatibility with other extension functions w/ trailing lambda
    command: StructureElement<E, S, StructureElement<E, S, Structure<E, S>>>,
): StructureElement<E, S, Structure<E, S>> =
    requireAs({ it }, requirement, command)
