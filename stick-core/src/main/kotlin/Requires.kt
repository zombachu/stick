@file:OptIn(ExperimentalTypeInference::class)

package com.zombachu.stick

import com.zombachu.stick.impl.Flag
import com.zombachu.stick.impl.Groupable
import com.zombachu.stick.impl.Parameter
import com.zombachu.stick.impl.Requirement
import com.zombachu.stick.impl.SenderScope
import com.zombachu.stick.impl.Structure
import com.zombachu.stick.impl.StructureContext
import com.zombachu.stick.impl.StructureElement
import com.zombachu.stick.impl.ValidatedCommand
import com.zombachu.stick.impl.ValidatedFlag
import com.zombachu.stick.impl.ValidatedParameter
import kotlin.experimental.ExperimentalTypeInference
import kotlin.reflect.KClass

@OverloadResolutionByLambdaReturnType
@JvmName("requireAs")
fun <S, S2, T : Any> SenderScope<S>.requireAs(
    transform: (S) -> S2,
    requirement: Requirement<S> = Requirement { true },
    // Outer StructureElement is to provide syntax compatibility with other extension functions w/ trailing lambda
    parameter: StructureElement<S2, StructureElement<S2, Parameter<S2, T>>>,
): StructureElement<S, Groupable<S, T>> = {
    val scope: StructureContext<S2> = this.forSender()
    ValidatedParameter(
        parameter(scope)(scope),
        { requirement(it) },
        transform,
    )
}

@OverloadResolutionByLambdaReturnType
fun <S, S2, T : Any> SenderScope<S>.requireAs(
    transform: (S) -> S2,
    invalidDefault: ContextualValue<S, T>,
    requirement: Requirement<S> = Requirement { true },
    // Outer StructureElement is to provide syntax compatibility with other extension functions w/ trailing lambda
    flag: StructureElement<S2, StructureElement<S2, Flag<S2, T>>>,
): StructureElement<S, Flag<S, T>> = {
    val scope: StructureContext<S2> = this.forSender()
    ValidatedFlag(
        flag(scope)(scope),
        { requirement(it) },
        invalidDefault,
        transform,
    )
}

@OverloadResolutionByLambdaReturnType
@JvmName("requireAsCommand")
fun <S, S2> SenderScope<S>.requireAs(
    transform: (S) -> S2,
    requirement: Requirement<S> = Requirement { true },
    // Outer StructureElement is to provide syntax compatibility with other extension functions w/ trailing lambda
    command: StructureElement<S2, StructureElement<S2, Structure<S2>>>,
): StructureElement<S, Structure<S>> = {
    val scope: StructureContext<S2> = this.forSender()
    ValidatedCommand(
        command(scope)(scope),
        { requirement(it) },
        transform,
    )
}

@OverloadResolutionByLambdaReturnType
@JvmName("requireIs")
inline fun <S : Any, reified S2 : S, T : Any> SenderScope<S>.requireIs(
    senderType: KClass<S2>,
    requirement: Requirement<S> = Requirement { true },
    // Outer StructureElement is to provide syntax compatibility with other extension functions w/ trailing lambda
    noinline parameter: StructureElement<S2, StructureElement<S2, Parameter<S2, T>>>,
): StructureElement<S, Groupable<S, T>> =
    requireAs(
        { it as S2 },
        requirement + { it is S2 },
        parameter
    )

@OverloadResolutionByLambdaReturnType
inline fun <S : Any, reified S2 : S, T : Any> SenderScope<S>.requireIs(
    senderType: KClass<S2>,
    noinline invalidDefault: ContextualValue<S, T>,
    requirement: Requirement<S> = Requirement { true },
    // Outer StructureElement is to provide syntax compatibility with other extension functions w/ trailing lambda
    noinline flag: StructureElement<S2, StructureElement<S2, Flag<S2, T>>>,
): StructureElement<S, Flag<S, T>> =
    requireAs(
        { it as S2 },
        invalidDefault,
        requirement + { it is S2 },
        flag
    )

@OverloadResolutionByLambdaReturnType
inline fun <S : Any, reified S2 : S, T : Any> SenderScope<S>.requireIs(
    senderType: KClass<S2>,
    invalidDefault: T,
    requirement: Requirement<S> = Requirement { true },
    // Outer StructureElement is to provide syntax compatibility with other extension functions w/ trailing lambda
    noinline flag: StructureElement<S2, StructureElement<S2, Flag<S2, T>>>,
): StructureElement<S, Flag<S, T>> =
    requireAs(
        { it as S2 },
        { invalidDefault },
        requirement + { it is S2 },
        flag
    )

@OverloadResolutionByLambdaReturnType
@JvmName("requireIsCommand")
inline fun <S : Any, reified S2 : S> SenderScope<S>.requireIs(
    senderType: KClass<S2>,
    requirement: Requirement<S> = Requirement { true },
    // Outer StructureElement is to provide syntax compatibility with other extension functions w/ trailing lambda
    noinline command: StructureElement<S2, StructureElement<S2, Structure<S2>>>,
): StructureElement<S, Structure<S>> =
    requireAs(
        { it as S2 },
        requirement + { it is S2 },
        command
    )

@OverloadResolutionByLambdaReturnType
@JvmName("require")
fun <S, T : Any> SenderScope<S>.require(
    requirement: Requirement<S>,
    // Outer StructureElement is to provide syntax compatibility with other extension functions w/ trailing lambda
    parameter: StructureElement<S, StructureElement<S, Parameter<S, T>>>,
): StructureElement<S, Groupable<S, T>> =
    requireAs({ it }, requirement, parameter)

@OverloadResolutionByLambdaReturnType
fun <S, T : Any> SenderScope<S>.require(
    invalidDefault: ContextualValue<S, T>,
    requirement: Requirement<S> = Requirement { true },
    // Outer StructureElement is to provide syntax compatibility with other extension functions w/ trailing lambda
    flag: StructureElement<S, StructureElement<S, Flag<S, T>>>,
): StructureElement<S, Flag<S, T>> =
    requireAs({ it }, invalidDefault, requirement, flag)

@OverloadResolutionByLambdaReturnType
fun <S, T : Any> SenderScope<S>.require(
    invalidDefault: T,
    requirement: Requirement<S> = Requirement { true },
    // Outer StructureElement is to provide syntax compatibility with other extension functions w/ trailing lambda
    flag: StructureElement<S, StructureElement<S, Flag<S, T>>>,
): StructureElement<S, Flag<S, T>> =
    requireAs({ it }, { invalidDefault }, requirement, flag)

@OverloadResolutionByLambdaReturnType
@JvmName("requireCommand")
fun <S> SenderScope<S>.require(
    requirement: Requirement<S> = Requirement { true },
    // Outer StructureElement is to provide syntax compatibility with other extension functions w/ trailing lambda
    command: StructureElement<S, StructureElement<S, Structure<S>>>,
): StructureElement<S, Structure<S>> =
    requireAs({ it }, requirement, command)
