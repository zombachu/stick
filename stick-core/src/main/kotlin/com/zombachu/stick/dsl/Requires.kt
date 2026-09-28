@file:OptIn(ExperimentalTypeInference::class)

package com.zombachu.stick.dsl

import com.zombachu.stick.Arguments
import com.zombachu.stick.Environment
import com.zombachu.stick.GroupResult
import com.zombachu.stick.HybridFlagResult
import com.zombachu.stick.Position
import com.zombachu.stick.Requirement
import com.zombachu.stick.StructureScope
import com.zombachu.stick.element.Branch
import com.zombachu.stick.element.GatedBranch
import com.zombachu.stick.element.GatedHybridFlag
import com.zombachu.stick.element.GatedOptionalGroup
import com.zombachu.stick.element.GatedOptionalParameter
import com.zombachu.stick.element.GatedParameter
import com.zombachu.stick.element.GatedParameterImpl
import com.zombachu.stick.element.GatedStructure
import com.zombachu.stick.element.GatedValueFlag
import com.zombachu.stick.element.HybridFlag
import com.zombachu.stick.element.InvalidSenderDefault
import com.zombachu.stick.element.OptionalGroup
import com.zombachu.stick.element.OptionalParameter
import com.zombachu.stick.element.Parameter
import com.zombachu.stick.element.SenderMappedBranch
import com.zombachu.stick.element.SenderMappedHybridFlag
import com.zombachu.stick.element.SenderMappedOptionalGroup
import com.zombachu.stick.element.SenderMappedOptionalParameter
import com.zombachu.stick.element.SenderMappedParameter
import com.zombachu.stick.element.SenderMappedStructure
import com.zombachu.stick.element.SenderMappedValueFlag
import com.zombachu.stick.element.Structure
import com.zombachu.stick.element.ValueFlag
import com.zombachu.stick.failSenderType
import com.zombachu.stick.success
import kotlin.experimental.ExperimentalTypeInference
import kotlin.reflect.KClass

@OverloadResolutionByLambdaReturnType
fun <S : Any, S2 : Any, E : Environment, T, P : Position> StructureScope<E, S>.requireAs(
    transform: (S) -> S2,
    requirement: Requirement<E, S> = requirement { success() },
    // Outer StructureElement is to provide syntax compatibility with other extension functions w/ trailing lambda
    parameter: StructureScope<E, S2>.() -> Parameter<E, S2, T, P>,
): GatedParameter<E, S, T, P> =
    GatedParameterImpl(SenderMappedParameter(parameter(this.forSender()), transform), requirement)

@OverloadResolutionByLambdaReturnType
fun <E : Environment, S : Any, S2 : Any, T> StructureScope<E, S>.requireAs(
    transform: (S) -> S2,
    invalidSenderDefault: InvalidSenderDefault<E, S, T>,
    // Outer StructureElement is to provide syntax compatibility with other extension functions w/ trailing lambda
    flag: StructureScope<E, S2>.() -> ValueFlag<E, S2, T>,
): ValueFlag<E, S, T> = GatedValueFlag(SenderMappedValueFlag(flag(this.forSender()), transform), invalidSenderDefault)

@OverloadResolutionByLambdaReturnType
fun <E : Environment, S : Any, S2 : Any, T> StructureScope<E, S>.requireAs(
    transform: (S) -> S2,
    invalidSenderDefault: InvalidSenderDefault<E, S, HybridFlagResult<T>>,
    // Outer StructureElement is to provide syntax compatibility with other extension functions w/ trailing lambda
    flag: StructureScope<E, S2>.() -> HybridFlag<E, S2, T>,
): HybridFlag<E, S, T> =
    GatedHybridFlag(SenderMappedHybridFlag(flag(this.forSender()), transform), invalidSenderDefault)

@OverloadResolutionByLambdaReturnType
fun <E : Environment, S : Any, S2 : Any, T, P : Position> StructureScope<E, S>.requireAs(
    transform: (S) -> S2,
    invalidSenderDefault: InvalidSenderDefault<E, S, T>,
    // Outer StructureElement is to provide syntax compatibility with other extension functions w/ trailing lambda
    optional: StructureScope<E, S2>.() -> OptionalParameter<E, S2, T, P>,
): OptionalParameter<E, S, T, P> =
    GatedOptionalParameter(
        SenderMappedOptionalParameter(optional(this.forSender()), transform),
        invalidSenderDefault,
    )

@OverloadResolutionByLambdaReturnType
fun <E : Environment, S : Any, S2 : Any, G : GroupResult?, P : Position> StructureScope<E, S>.requireAs(
    transform: (S) -> S2,
    invalidSenderDefault: InvalidSenderDefault<E, S, G>,
    // Outer StructureElement is to provide syntax compatibility with other extension functions w/ trailing lambda
    optional: StructureScope<E, S2>.() -> OptionalGroup<E, S2, G, P>,
): OptionalGroup<E, S, G, P> =
    GatedOptionalGroup(SenderMappedOptionalGroup(optional(this.forSender()), transform), invalidSenderDefault)

@OverloadResolutionByLambdaReturnType
fun <E : Environment, S : Any, S2 : Any, T_ : Arguments> StructureScope<E, S>.requireAs(
    transform: (S) -> S2,
    requirement: Requirement<E, S> = requirement { success() },
    // Outer StructureElement is to provide syntax compatibility with other extension functions w/ trailing lambda
    command: StructureScope<E, S2>.() -> Structure<E, S2, T_>,
): Structure<E, S, T_> = GatedStructure(SenderMappedStructure(command(this.forSender()), transform), requirement)

// TODO: unify with requireAs once T_ can be inferred
@OverloadResolutionByLambdaReturnType
fun <E : Environment, S : Any, S2 : Any, T_ : Arguments> StructureScope<E, S>.branchRequireAs(
    transform: (S) -> S2,
    requirement: Requirement<E, S> = requirement { success() },
    // Outer StructureElement is to provide syntax compatibility with other extension functions w/ trailing lambda
    branch: StructureScope<E, S2>.() -> Branch<E, S2, T_>,
): Branch<E, S, T_> = GatedBranch(SenderMappedBranch(branch(this.forSender()), transform), requirement)

@OverloadResolutionByLambdaReturnType
inline fun <E : Environment, S : Any, reified S2 : S, T, P : Position> StructureScope<E, S>.requireIs(
    senderType: KClass<S2>,
    requirement: Requirement<E, S> = requirement { success() },
    // Outer StructureElement is to provide syntax compatibility with other extension functions w/ trailing lambda
    noinline parameter: StructureScope<E, S2>.() -> Parameter<E, S2, T, P>,
): GatedParameter<E, S, T, P> =
    requireAs(
        { it as S2 },
        requirement + requirement({ failSenderType(senderType) }) { sender is S2 },
        parameter,
    )

@OverloadResolutionByLambdaReturnType
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
            requirement(invalidSenderDefault) + requirement({ failSenderType(senderType) }) { sender is S2 },
        ),
        flag,
    )

@OverloadResolutionByLambdaReturnType
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
            requirement(invalidSenderDefault) + requirement({ failSenderType(senderType) }) { sender is S2 },
        ),
        flag,
    )

@OverloadResolutionByLambdaReturnType
inline fun <E : Environment, S : Any, reified S2 : S, T, P : Position> StructureScope<E, S>.requireIs(
    senderType: KClass<S2>,
    invalidSenderDefault: InvalidSenderDefault<E, S, T>,
    // Outer StructureElement is to provide syntax compatibility with other extension functions w/ trailing lambda
    noinline optional: StructureScope<E, S2>.() -> OptionalParameter<E, S2, T, P>,
): OptionalParameter<E, S, T, P> =
    requireAs(
        { it as S2 },
        invalidDefault(
            invalidSenderDefault.value,
            requirement(invalidSenderDefault) + requirement({ failSenderType(senderType) }) { sender is S2 },
        ),
        optional,
    )

@OverloadResolutionByLambdaReturnType
inline fun <E : Environment, S : Any, reified S2 : S, G : GroupResult?, P : Position> StructureScope<E, S>.requireIs(
    senderType: KClass<S2>,
    invalidSenderDefault: InvalidSenderDefault<E, S, G>,
    // Outer StructureElement is to provide syntax compatibility with other extension functions w/ trailing lambda
    noinline optional: StructureScope<E, S2>.() -> OptionalGroup<E, S2, G, P>,
): OptionalGroup<E, S, G, P> =
    requireAs(
        { it as S2 },
        invalidDefault(
            invalidSenderDefault.value,
            requirement(invalidSenderDefault) + requirement({ failSenderType(senderType) }) { sender is S2 },
        ),
        optional,
    )

@OverloadResolutionByLambdaReturnType
inline fun <E : Environment, S : Any, reified S2 : S, T_ : Arguments> StructureScope<E, S>.requireIs(
    senderType: KClass<S2>,
    requirement: Requirement<E, S> = requirement { success() },
    // Outer StructureElement is to provide syntax compatibility with other extension functions w/ trailing lambda
    noinline command: StructureScope<E, S2>.() -> Structure<E, S2, T_>,
): Structure<E, S, T_> =
    requireAs(
        { it as S2 },
        requirement + requirement({ failSenderType(senderType) }) { sender is S2 },
        command,
    )

// TODO: unify with requireIs once T_ can be inferred
@OverloadResolutionByLambdaReturnType
inline fun <E : Environment, S : Any, reified S2 : S, T_ : Arguments> StructureScope<E, S>.branchRequireIs(
    senderType: KClass<S2>,
    requirement: Requirement<E, S> = requirement { success() },
    // Outer StructureElement is to provide syntax compatibility with other extension functions w/ trailing lambda
    noinline branch: StructureScope<E, S2>.() -> Branch<E, S2, T_>,
): Branch<E, S, T_> =
    branchRequireAs(
        { it as S2 },
        requirement + requirement({ failSenderType(senderType) }) { sender is S2 },
        branch,
    )

@OverloadResolutionByLambdaReturnType
fun <E : Environment, S : Any, T, P : Position> StructureScope<E, S>.require(
    requirement: Requirement<E, S>,
    // Outer StructureElement is to provide syntax compatibility with other extension functions w/ trailing lambda
    parameter: StructureScope<E, S>.() -> Parameter<E, S, T, P>,
): GatedParameter<E, S, T, P> = GatedParameterImpl(parameter(this.forSender()), requirement)

@OverloadResolutionByLambdaReturnType
fun <E : Environment, S : Any, T> StructureScope<E, S>.require(
    invalidSenderDefault: InvalidSenderDefault<E, S, T>,
    // Outer StructureElement is to provide syntax compatibility with other extension functions w/ trailing lambda
    flag: StructureScope<E, S>.() -> ValueFlag<E, S, T>,
): ValueFlag<E, S, T> = GatedValueFlag(flag(this.forSender()), invalidSenderDefault)

@OverloadResolutionByLambdaReturnType
fun <E : Environment, S : Any, T> StructureScope<E, S>.require(
    invalidSenderDefault: InvalidSenderDefault<E, S, HybridFlagResult<T>>,
    // Outer StructureElement is to provide syntax compatibility with other extension functions w/ trailing lambda
    flag: StructureScope<E, S>.() -> HybridFlag<E, S, T>,
): HybridFlag<E, S, T> = GatedHybridFlag(flag(this.forSender()), invalidSenderDefault)

@OverloadResolutionByLambdaReturnType
fun <E : Environment, S : Any, T, P : Position> StructureScope<E, S>.require(
    invalidSenderDefault: InvalidSenderDefault<E, S, T>,
    // Outer StructureElement is to provide syntax compatibility with other extension functions w/ trailing lambda
    optional: StructureScope<E, S>.() -> OptionalParameter<E, S, T, P>,
): OptionalParameter<E, S, T, P> = GatedOptionalParameter(optional(this.forSender()), invalidSenderDefault)

@OverloadResolutionByLambdaReturnType
fun <E : Environment, S : Any, G : GroupResult?, P : Position> StructureScope<E, S>.require(
    invalidSenderDefault: InvalidSenderDefault<E, S, G>,
    // Outer StructureElement is to provide syntax compatibility with other extension functions w/ trailing lambda
    optional: StructureScope<E, S>.() -> OptionalGroup<E, S, G, P>,
): OptionalGroup<E, S, G, P> = GatedOptionalGroup(optional(this.forSender()), invalidSenderDefault)

@OverloadResolutionByLambdaReturnType
fun <E : Environment, S : Any, T : Arguments> StructureScope<E, S>.require(
    requirement: Requirement<E, S> = requirement { success() },
    // Outer StructureElement is to provide syntax compatibility with other extension functions w/ trailing lambda
    command: StructureScope<E, S>.() -> Structure<E, S, T>,
): Structure<E, S, T> = GatedStructure(command(this.forSender()), requirement)

// TODO: unify with require once T_ can be inferred
@OverloadResolutionByLambdaReturnType
fun <E : Environment, S : Any, T : Arguments> StructureScope<E, S>.branchRequire(
    requirement: Requirement<E, S> = requirement { success() },
    // Outer StructureElement is to provide syntax compatibility with other extension functions w/ trailing lambda
    branch: StructureScope<E, S>.() -> Branch<E, S, T>,
): Branch<E, S, T> = GatedBranch(branch(this.forSender()), requirement)
