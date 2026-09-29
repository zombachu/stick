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
import com.zombachu.stick.element.Structure
import com.zombachu.stick.element.ValueFlag
import com.zombachu.stick.failSenderType
import kotlin.experimental.ExperimentalTypeInference
import kotlin.reflect.KClass

@OverloadResolutionByLambdaReturnType
inline fun <E : Environment, S : Any, reified S2 : S, T, P : Position> StructureScope<E, S>.requireSender(
    senderType: KClass<S2>,
    noinline parameter: StructureScope<E, S2>.() -> Parameter<E, S2, T, P>,
): GatedParameter<E, S, T, P> =
    GatedParameterImpl(
        mapSender({ it as S2 }, parameter),
        requirement({ sender is S2 }) { failSenderType(senderType) },
    )

@OverloadResolutionByLambdaReturnType
inline fun <E : Environment, S : Any, reified S2 : S, T> StructureScope<E, S>.requireSender(
    senderType: KClass<S2>,
    invalidSenderDefault: InvalidSenderDefault<E, S, T>,
    noinline flag: StructureScope<E, S2>.() -> ValueFlag<E, S2, T>,
): ValueFlag<E, S, T> =
    GatedValueFlag(
        mapSender({ it as S2 }, flag),
        invalidDefault(
            invalidSenderDefault.value,
            requirement(invalidSenderDefault) + requirement({ sender is S2 }) { failSenderType(senderType) },
        ),
    )

@OverloadResolutionByLambdaReturnType
inline fun <E : Environment, S : Any, reified S2 : S, T> StructureScope<E, S>.requireSender(
    senderType: KClass<S2>,
    invalidSenderDefault: InvalidSenderDefault<E, S, HybridFlagResult<T>>,
    noinline flag: StructureScope<E, S2>.() -> HybridFlag<E, S2, T>,
): HybridFlag<E, S, T> =
    GatedHybridFlag(
        mapSender({ it as S2 }, flag),
        invalidDefault(
            invalidSenderDefault.value,
            requirement(invalidSenderDefault) + requirement({ sender is S2 }) { failSenderType(senderType) },
        ),
    )

@OverloadResolutionByLambdaReturnType
inline fun <E : Environment, S : Any, reified S2 : S, T, P : Position> StructureScope<E, S>.requireSender(
    senderType: KClass<S2>,
    invalidSenderDefault: InvalidSenderDefault<E, S, T>,
    noinline optional: StructureScope<E, S2>.() -> OptionalParameter<E, S2, T, P>,
): OptionalParameter<E, S, T, P> =
    GatedOptionalParameter(
        mapSender({ it as S2 }, optional),
        invalidDefault(
            invalidSenderDefault.value,
            requirement(invalidSenderDefault) + requirement({ sender is S2 }) { failSenderType(senderType) },
        ),
    )

@OverloadResolutionByLambdaReturnType
inline fun <E : Environment, S : Any, reified S2 : S, G : GroupResult?, P : Position> StructureScope<E, S>
    .requireSender(
    senderType: KClass<S2>,
    invalidSenderDefault: InvalidSenderDefault<E, S, G>,
    noinline optional: StructureScope<E, S2>.() -> OptionalGroup<E, S2, G, P>,
): OptionalGroup<E, S, G, P> =
    GatedOptionalGroup(
        mapSender({ it as S2 }, optional),
        invalidDefault(
            invalidSenderDefault.value,
            requirement(invalidSenderDefault) + requirement({ sender is S2 }) { failSenderType(senderType) },
        ),
    )

@OverloadResolutionByLambdaReturnType
inline fun <E : Environment, S : Any, reified S2 : S, T_ : Arguments> StructureScope<E, S>.requireSender(
    senderType: KClass<S2>,
    noinline command: StructureScope<E, S2>.() -> Structure<E, S2, T_>,
): Structure<E, S, T_> =
    GatedStructure(
        mapSender({ it as S2 }, command),
        requirement({ sender is S2 }) { failSenderType(senderType) },
    )

// TODO: unify with requireSender once T_ can be inferred
@OverloadResolutionByLambdaReturnType
inline fun <E : Environment, S : Any, reified S2 : S, T_ : Arguments> StructureScope<E, S>.branchRequireSender(
    senderType: KClass<S2>,
    noinline branch: StructureScope<E, S2>.() -> Branch<E, S2, T_>,
): Branch<E, S, T_> =
    GatedBranch(
        branchMapSender({ it as S2 }, branch),
        requirement({ sender is S2 }) { failSenderType(senderType) },
    )

@OverloadResolutionByLambdaReturnType
fun <E : Environment, S : Any, T, P : Position> StructureScope<E, S>.require(
    requirement: Requirement<E, S>,
    parameter: StructureScope<E, S>.() -> Parameter<E, S, T, P>,
): GatedParameter<E, S, T, P> = GatedParameterImpl(parameter(this.forSender()), requirement)

@OverloadResolutionByLambdaReturnType
fun <E : Environment, S : Any, T> StructureScope<E, S>.require(
    invalidSenderDefault: InvalidSenderDefault<E, S, T>,
    flag: StructureScope<E, S>.() -> ValueFlag<E, S, T>,
): ValueFlag<E, S, T> = GatedValueFlag(flag(this.forSender()), invalidSenderDefault)

@OverloadResolutionByLambdaReturnType
fun <E : Environment, S : Any, T> StructureScope<E, S>.require(
    invalidSenderDefault: InvalidSenderDefault<E, S, HybridFlagResult<T>>,
    flag: StructureScope<E, S>.() -> HybridFlag<E, S, T>,
): HybridFlag<E, S, T> = GatedHybridFlag(flag(this.forSender()), invalidSenderDefault)

@OverloadResolutionByLambdaReturnType
fun <E : Environment, S : Any, T, P : Position> StructureScope<E, S>.require(
    invalidSenderDefault: InvalidSenderDefault<E, S, T>,
    optional: StructureScope<E, S>.() -> OptionalParameter<E, S, T, P>,
): OptionalParameter<E, S, T, P> = GatedOptionalParameter(optional(this.forSender()), invalidSenderDefault)

@OverloadResolutionByLambdaReturnType
fun <E : Environment, S : Any, G : GroupResult?, P : Position> StructureScope<E, S>.require(
    invalidSenderDefault: InvalidSenderDefault<E, S, G>,
    optional: StructureScope<E, S>.() -> OptionalGroup<E, S, G, P>,
): OptionalGroup<E, S, G, P> = GatedOptionalGroup(optional(this.forSender()), invalidSenderDefault)
