@file:OptIn(ExperimentalTypeInference::class)

package com.zombachu.stick.dsl

import com.zombachu.stick.Arguments
import com.zombachu.stick.Environment
import com.zombachu.stick.GroupResult
import com.zombachu.stick.Position
import com.zombachu.stick.StructureScope
import com.zombachu.stick.element.Branch
import com.zombachu.stick.element.HybridFlag
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
import kotlin.experimental.ExperimentalTypeInference

@OverloadResolutionByLambdaReturnType
fun <S : Any, S2 : Any, E : Environment, T, P : Position> StructureScope<E, S>.mapSender(
    transform: (S) -> S2,
    // Outer StructureElement is to provide syntax compatibility with other extension functions w/ trailing lambda
    parameter: StructureScope<E, S2>.() -> Parameter<E, S2, T, P>,
): Parameter<E, S, T, P> = SenderMappedParameter(parameter(this.forSender()), transform)

@OverloadResolutionByLambdaReturnType
fun <E : Environment, S : Any, S2 : Any, T> StructureScope<E, S>.mapSender(
    transform: (S) -> S2,
    // Outer StructureElement is to provide syntax compatibility with other extension functions w/ trailing lambda
    flag: StructureScope<E, S2>.() -> ValueFlag<E, S2, T>,
): ValueFlag<E, S, T> = SenderMappedValueFlag(flag(this.forSender()), transform)

@OverloadResolutionByLambdaReturnType
fun <E : Environment, S : Any, S2 : Any, T> StructureScope<E, S>.mapSender(
    transform: (S) -> S2,
    // Outer StructureElement is to provide syntax compatibility with other extension functions w/ trailing lambda
    flag: StructureScope<E, S2>.() -> HybridFlag<E, S2, T>,
): HybridFlag<E, S, T> = SenderMappedHybridFlag(flag(this.forSender()), transform)

@OverloadResolutionByLambdaReturnType
fun <E : Environment, S : Any, S2 : Any, T, P : Position> StructureScope<E, S>.mapSender(
    transform: (S) -> S2,
    // Outer StructureElement is to provide syntax compatibility with other extension functions w/ trailing lambda
    optional: StructureScope<E, S2>.() -> OptionalParameter<E, S2, T, P>,
): OptionalParameter<E, S, T, P> = SenderMappedOptionalParameter(optional(this.forSender()), transform)

@OverloadResolutionByLambdaReturnType
fun <E : Environment, S : Any, S2 : Any, G : GroupResult?, P : Position> StructureScope<E, S>.mapSender(
    transform: (S) -> S2,
    // Outer StructureElement is to provide syntax compatibility with other extension functions w/ trailing lambda
    optional: StructureScope<E, S2>.() -> OptionalGroup<E, S2, G, P>,
): OptionalGroup<E, S, G, P> = SenderMappedOptionalGroup(optional(this.forSender()), transform)

@OverloadResolutionByLambdaReturnType
fun <E : Environment, S : Any, S2 : Any, T_ : Arguments> StructureScope<E, S>.mapSender(
    transform: (S) -> S2,
    // Outer StructureElement is to provide syntax compatibility with other extension functions w/ trailing lambda
    command: StructureScope<E, S2>.() -> Structure<E, S2, T_>,
): Structure<E, S, T_> = SenderMappedStructure(command(this.forSender()), transform)

// TODO: unify with mapSender once T_ can be inferred
@OverloadResolutionByLambdaReturnType
fun <E : Environment, S : Any, S2 : Any, T_ : Arguments> StructureScope<E, S>.branchMapSender(
    transform: (S) -> S2,
    // Outer StructureElement is to provide syntax compatibility with other extension functions w/ trailing lambda
    branch: StructureScope<E, S2>.() -> Branch<E, S2, T_>,
): Branch<E, S, T_> = SenderMappedBranch(branch(this.forSender()), transform)
