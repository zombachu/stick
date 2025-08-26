@file:OptIn(ExperimentalTypeInference::class)

package com.zombachu.stick.structure

import com.zombachu.stick.ExecutionResult
import com.zombachu.stick.Invocation
import com.zombachu.stick.Environment
import com.zombachu.stick.element.Signature0
import com.zombachu.stick.element.Signature1
import com.zombachu.stick.element.Signature10
import com.zombachu.stick.element.Signature11
import com.zombachu.stick.element.Signature12
import com.zombachu.stick.element.Signature2
import com.zombachu.stick.element.Signature3
import com.zombachu.stick.element.Signature4
import com.zombachu.stick.element.Signature5
import com.zombachu.stick.element.Signature6
import com.zombachu.stick.element.Signature7
import com.zombachu.stick.element.Signature8
import com.zombachu.stick.element.Signature9
import com.zombachu.stick.element.SignatureConstraint
import com.zombachu.stick.element.Structure
import com.zombachu.stick.impl.StructureElement
import com.zombachu.stick.impl.StructureScope
import com.zombachu.stick.impl.Tuple0
import com.zombachu.stick.impl.Tuple1
import com.zombachu.stick.impl.Tuple10
import com.zombachu.stick.impl.Tuple11
import com.zombachu.stick.impl.Tuple12
import com.zombachu.stick.impl.Tuple2
import com.zombachu.stick.impl.Tuple3
import com.zombachu.stick.impl.Tuple4
import com.zombachu.stick.impl.Tuple5
import com.zombachu.stick.impl.Tuple6
import com.zombachu.stick.impl.Tuple7
import com.zombachu.stick.impl.Tuple8
import com.zombachu.stick.impl.Tuple9
import kotlin.experimental.ExperimentalTypeInference

@OverloadResolutionByLambdaReturnType
operator fun <S : Environment, O> StructureScope<S, O>.invoke(
    execute: Invocation<S, O>.() -> ExecutionResult,
): Structure<S, O> =
    build(Signature0(execute, Tuple0()))

@OverloadResolutionByLambdaReturnType
operator fun <S : Environment, O, A : Any> StructureScope<S, O>.invoke(
    execute: Invocation<S, O>.(A) -> ExecutionResult,
    element: StructureElement<S, O, SignatureConstraint.Terminating<S, O, A>>,
): StructureElement<S, O, Structure<S, O>> = {
    build(Signature1(execute, Tuple1(element.invoke(this))))
}
@OverloadResolutionByLambdaReturnType
operator fun <S : Environment, O, A : Any> StructureScope<S, O>.invoke(
    element: StructureElement<S, O, SignatureConstraint.Terminating<S, O, A>>,
): StructureElement<S, O, Structure<S, O>> =
    invoke({ a: A -> ExecutionResult.success() }, element)

@OverloadResolutionByLambdaReturnType
operator fun <S : Environment, O, A : Any, B : Any> StructureScope<S, O>.invoke(
    execute: Invocation<S, O>.(A, B) -> ExecutionResult,
    elementA: StructureElement<S, O, SignatureConstraint.NonTerminating<S, O, A>>,
    elementB: StructureElement<S, O, SignatureConstraint.Terminating<S, O, B>>,
): StructureElement<S, O, Structure<S, O>> = {
    build(Signature2(execute, Tuple2(elementA.invoke(this), elementB.invoke(this))))
}
@OverloadResolutionByLambdaReturnType
operator fun <S : Environment, O, A : Any, B : Any> StructureScope<S, O>.invoke(
    elementA: StructureElement<S, O, SignatureConstraint.NonTerminating<S, O, A>>,
    elementB: StructureElement<S, O, SignatureConstraint.Terminating<S, O, B>>,
): StructureElement<S, O, Structure<S, O>> =
    invoke({ a, b -> ExecutionResult.success() }, elementA, elementB)

@OverloadResolutionByLambdaReturnType
operator fun <S : Environment, O, A : Any, B : Any, C : Any> StructureScope<S, O>.invoke(
    execute: Invocation<S, O>.(A, B, C) -> ExecutionResult,
    elementA: StructureElement<S, O, SignatureConstraint.NonTerminating<S, O, A>>,
    elementB: StructureElement<S, O, SignatureConstraint.NonTerminating<S, O, B>>,
    elementC: StructureElement<S, O, SignatureConstraint.Terminating<S, O, C>>,
): StructureElement<S, O, Structure<S, O>> = {
    build(Signature3(execute,
        Tuple3(elementA.invoke(this), elementB.invoke(this), elementC.invoke(this))
    ))
}
@OverloadResolutionByLambdaReturnType
operator fun <S : Environment, O, A : Any, B : Any, C : Any> StructureScope<S, O>.invoke(
    elementA: StructureElement<S, O, SignatureConstraint.NonTerminating<S, O, A>>,
    elementB: StructureElement<S, O, SignatureConstraint.NonTerminating<S, O, B>>,
    elementC: StructureElement<S, O, SignatureConstraint.Terminating<S, O, C>>,
): StructureElement<S, O, Structure<S, O>> =
    invoke({ a, b, c -> ExecutionResult.success() }, elementA, elementB, elementC)

@OverloadResolutionByLambdaReturnType
operator fun <S : Environment, O, A : Any, B : Any, C : Any, D : Any> StructureScope<S, O>.invoke(
    execute: Invocation<S, O>.(A, B, C, D) -> ExecutionResult,
    elementA: StructureElement<S, O, SignatureConstraint.NonTerminating<S, O, A>>,
    elementB: StructureElement<S, O, SignatureConstraint.NonTerminating<S, O, B>>,
    elementC: StructureElement<S, O, SignatureConstraint.NonTerminating<S, O, C>>,
    elementD: StructureElement<S, O, SignatureConstraint.Terminating<S, O, D>>,
): StructureElement<S, O, Structure<S, O>> = {
    build(Signature4(execute,
        Tuple4(elementA.invoke(this), elementB.invoke(this), elementC.invoke(this), elementD.invoke(this))
    ))
}
@OverloadResolutionByLambdaReturnType
operator fun <S : Environment, O, A : Any, B : Any, C : Any, D : Any> StructureScope<S, O>.invoke(
    elementA: StructureElement<S, O, SignatureConstraint.NonTerminating<S, O, A>>,
    elementB: StructureElement<S, O, SignatureConstraint.NonTerminating<S, O, B>>,
    elementC: StructureElement<S, O, SignatureConstraint.NonTerminating<S, O, C>>,
    elementD: StructureElement<S, O, SignatureConstraint.Terminating<S, O, D>>,
): StructureElement<S, O, Structure<S, O>> =
    invoke({ a, b, c, d -> ExecutionResult.success() }, elementA, elementB, elementC, elementD)

@OverloadResolutionByLambdaReturnType
operator fun <S : Environment, O, A : Any, B : Any, C : Any, D : Any, E : Any> StructureScope<S, O>.invoke(
    execute: Invocation<S, O>.(A, B, C, D, E) -> ExecutionResult,
    elementA: StructureElement<S, O, SignatureConstraint.NonTerminating<S, O, A>>,
    elementB: StructureElement<S, O, SignatureConstraint.NonTerminating<S, O, B>>,
    elementC: StructureElement<S, O, SignatureConstraint.NonTerminating<S, O, C>>,
    elementD: StructureElement<S, O, SignatureConstraint.NonTerminating<S, O, D>>,
    elementE: StructureElement<S, O, SignatureConstraint.Terminating<S, O, E>>,
): StructureElement<S, O, Structure<S, O>> = {
    build(Signature5(execute,
        Tuple5(elementA.invoke(this), elementB.invoke(this), elementC.invoke(this), elementD.invoke(this),
            elementE.invoke(this))
    ))
}
@OverloadResolutionByLambdaReturnType
operator fun <S : Environment, O, A : Any, B : Any, C : Any, D : Any, E : Any> StructureScope<S, O>.invoke(
    elementA: StructureElement<S, O, SignatureConstraint.NonTerminating<S, O, A>>,
    elementB: StructureElement<S, O, SignatureConstraint.NonTerminating<S, O, B>>,
    elementC: StructureElement<S, O, SignatureConstraint.NonTerminating<S, O, C>>,
    elementD: StructureElement<S, O, SignatureConstraint.NonTerminating<S, O, D>>,
    elementE: StructureElement<S, O, SignatureConstraint.Terminating<S, O, E>>,
): StructureElement<S, O, Structure<S, O>> =
    invoke({ a, b, c, d, e -> ExecutionResult.success() }, elementA, elementB, elementC, elementD, elementE)

@OverloadResolutionByLambdaReturnType
operator fun <S : Environment, O, A : Any, B : Any, C : Any, D : Any, E : Any, F : Any> StructureScope<S, O>.invoke(
    execute: Invocation<S, O>.(A, B, C, D, E, F) -> ExecutionResult,
    elementA: StructureElement<S, O, SignatureConstraint.NonTerminating<S, O, A>>,
    elementB: StructureElement<S, O, SignatureConstraint.NonTerminating<S, O, B>>,
    elementC: StructureElement<S, O, SignatureConstraint.NonTerminating<S, O, C>>,
    elementD: StructureElement<S, O, SignatureConstraint.NonTerminating<S, O, D>>,
    elementE: StructureElement<S, O, SignatureConstraint.NonTerminating<S, O, E>>,
    elementF: StructureElement<S, O, SignatureConstraint.Terminating<S, O, F>>,
): StructureElement<S, O, Structure<S, O>> = {
    build(Signature6(execute,
        Tuple6(elementA.invoke(this), elementB.invoke(this), elementC.invoke(this), elementD.invoke(this),
            elementE.invoke(this), elementF.invoke(this))
    ))
}
@OverloadResolutionByLambdaReturnType
operator fun <S : Environment, O, A : Any, B : Any, C : Any, D : Any, E : Any, F : Any> StructureScope<S, O>.invoke(
    elementA: StructureElement<S, O, SignatureConstraint.NonTerminating<S, O, A>>,
    elementB: StructureElement<S, O, SignatureConstraint.NonTerminating<S, O, B>>,
    elementC: StructureElement<S, O, SignatureConstraint.NonTerminating<S, O, C>>,
    elementD: StructureElement<S, O, SignatureConstraint.NonTerminating<S, O, D>>,
    elementE: StructureElement<S, O, SignatureConstraint.NonTerminating<S, O, E>>,
    elementF: StructureElement<S, O, SignatureConstraint.Terminating<S, O, F>>,
): StructureElement<S, O, Structure<S, O>> =
    invoke({ a, b, c, d, e, f -> ExecutionResult.success() }, elementA, elementB, elementC, elementD, elementE,
        elementF)

@OverloadResolutionByLambdaReturnType
operator fun <S : Environment, O, A : Any, B : Any, C : Any, D : Any, E : Any, F : Any, G : Any> StructureScope<S, O>.invoke(
    execute: Invocation<S, O>.(A, B, C, D, E, F, G) -> ExecutionResult,
    elementA: StructureElement<S, O, SignatureConstraint.NonTerminating<S, O, A>>,
    elementB: StructureElement<S, O, SignatureConstraint.NonTerminating<S, O, B>>,
    elementC: StructureElement<S, O, SignatureConstraint.NonTerminating<S, O, C>>,
    elementD: StructureElement<S, O, SignatureConstraint.NonTerminating<S, O, D>>,
    elementE: StructureElement<S, O, SignatureConstraint.NonTerminating<S, O, E>>,
    elementF: StructureElement<S, O, SignatureConstraint.NonTerminating<S, O, F>>,
    elementG: StructureElement<S, O, SignatureConstraint.Terminating<S, O, G>>,
): StructureElement<S, O, Structure<S, O>> = {
    build(Signature7(execute,
        Tuple7(elementA.invoke(this), elementB.invoke(this), elementC.invoke(this), elementD.invoke(this),
            elementE.invoke(this), elementF.invoke(this), elementG.invoke(this))
    ))
}
@OverloadResolutionByLambdaReturnType
operator fun <S : Environment, O, A : Any, B : Any, C : Any, D : Any, E : Any, F : Any, G : Any> StructureScope<S, O>.invoke(
    elementA: StructureElement<S, O, SignatureConstraint.NonTerminating<S, O, A>>,
    elementB: StructureElement<S, O, SignatureConstraint.NonTerminating<S, O, B>>,
    elementC: StructureElement<S, O, SignatureConstraint.NonTerminating<S, O, C>>,
    elementD: StructureElement<S, O, SignatureConstraint.NonTerminating<S, O, D>>,
    elementE: StructureElement<S, O, SignatureConstraint.NonTerminating<S, O, E>>,
    elementF: StructureElement<S, O, SignatureConstraint.NonTerminating<S, O, F>>,
    elementG: StructureElement<S, O, SignatureConstraint.Terminating<S, O, G>>,
): StructureElement<S, O, Structure<S, O>> =
    invoke({ a, b, c, d, e, f, g -> ExecutionResult.success() }, elementA, elementB, elementC, elementD, elementE,
        elementF, elementG)

@OverloadResolutionByLambdaReturnType
operator fun <S : Environment, O, A : Any, B : Any, C : Any, D : Any, E : Any, F : Any, G : Any, H : Any> StructureScope<S, O>.invoke(
    execute: Invocation<S, O>.(A, B, C, D, E, F, G, H) -> ExecutionResult,
    elementA: StructureElement<S, O, SignatureConstraint.NonTerminating<S, O, A>>,
    elementB: StructureElement<S, O, SignatureConstraint.NonTerminating<S, O, B>>,
    elementC: StructureElement<S, O, SignatureConstraint.NonTerminating<S, O, C>>,
    elementD: StructureElement<S, O, SignatureConstraint.NonTerminating<S, O, D>>,
    elementE: StructureElement<S, O, SignatureConstraint.NonTerminating<S, O, E>>,
    elementF: StructureElement<S, O, SignatureConstraint.NonTerminating<S, O, F>>,
    elementG: StructureElement<S, O, SignatureConstraint.NonTerminating<S, O, G>>,
    elementH: StructureElement<S, O, SignatureConstraint.Terminating<S, O, H>>,
): StructureElement<S, O, Structure<S, O>> = {
    build(Signature8(execute,
        Tuple8(elementA.invoke(this), elementB.invoke(this), elementC.invoke(this), elementD.invoke(this),
            elementE.invoke(this), elementF.invoke(this), elementG.invoke(this), elementH.invoke(this))
    ))
}
@OverloadResolutionByLambdaReturnType
operator fun <S : Environment, O, A : Any, B : Any, C : Any, D : Any, E : Any, F : Any, G : Any, H : Any> StructureScope<S, O>.invoke(
    elementA: StructureElement<S, O, SignatureConstraint.NonTerminating<S, O, A>>,
    elementB: StructureElement<S, O, SignatureConstraint.NonTerminating<S, O, B>>,
    elementC: StructureElement<S, O, SignatureConstraint.NonTerminating<S, O, C>>,
    elementD: StructureElement<S, O, SignatureConstraint.NonTerminating<S, O, D>>,
    elementE: StructureElement<S, O, SignatureConstraint.NonTerminating<S, O, E>>,
    elementF: StructureElement<S, O, SignatureConstraint.NonTerminating<S, O, F>>,
    elementG: StructureElement<S, O, SignatureConstraint.NonTerminating<S, O, G>>,
    elementH: StructureElement<S, O, SignatureConstraint.Terminating<S, O, H>>,
): StructureElement<S, O, Structure<S, O>> =
    invoke({ a, b, c, d, e, f, g, h -> ExecutionResult.success() }, elementA, elementB, elementC, elementD, elementE,
        elementF, elementG, elementH)

@OverloadResolutionByLambdaReturnType
operator fun <S : Environment, O, A : Any, B : Any, C : Any, D : Any, E : Any, F : Any, G : Any, H : Any, I : Any>
        StructureScope<S, O>.invoke(
    execute: Invocation<S, O>.(A, B, C, D, E, F, G, H, I) -> ExecutionResult,
    elementA: StructureElement<S, O, SignatureConstraint.NonTerminating<S, O, A>>,
    elementB: StructureElement<S, O, SignatureConstraint.NonTerminating<S, O, B>>,
    elementC: StructureElement<S, O, SignatureConstraint.NonTerminating<S, O, C>>,
    elementD: StructureElement<S, O, SignatureConstraint.NonTerminating<S, O, D>>,
    elementE: StructureElement<S, O, SignatureConstraint.NonTerminating<S, O, E>>,
    elementF: StructureElement<S, O, SignatureConstraint.NonTerminating<S, O, F>>,
    elementG: StructureElement<S, O, SignatureConstraint.NonTerminating<S, O, G>>,
    elementH: StructureElement<S, O, SignatureConstraint.NonTerminating<S, O, H>>,
    elementI: StructureElement<S, O, SignatureConstraint.Terminating<S, O, I>>,
): StructureElement<S, O, Structure<S, O>> = {
    build(Signature9(execute,
        Tuple9(elementA.invoke(this), elementB.invoke(this), elementC.invoke(this), elementD.invoke(this),
            elementE.invoke(this), elementF.invoke(this), elementG.invoke(this), elementH.invoke(this),
            elementI.invoke(this))
    ))
}
@OverloadResolutionByLambdaReturnType
operator fun <S : Environment, O, A : Any, B : Any, C : Any, D : Any, E : Any, F : Any, G : Any, H : Any, I : Any>
        StructureScope<S, O>.invoke(
    elementA: StructureElement<S, O, SignatureConstraint.NonTerminating<S, O, A>>,
    elementB: StructureElement<S, O, SignatureConstraint.NonTerminating<S, O, B>>,
    elementC: StructureElement<S, O, SignatureConstraint.NonTerminating<S, O, C>>,
    elementD: StructureElement<S, O, SignatureConstraint.NonTerminating<S, O, D>>,
    elementE: StructureElement<S, O, SignatureConstraint.NonTerminating<S, O, E>>,
    elementF: StructureElement<S, O, SignatureConstraint.NonTerminating<S, O, F>>,
    elementG: StructureElement<S, O, SignatureConstraint.NonTerminating<S, O, G>>,
    elementH: StructureElement<S, O, SignatureConstraint.NonTerminating<S, O, H>>,
    elementI: StructureElement<S, O, SignatureConstraint.Terminating<S, O, I>>,
): StructureElement<S, O, Structure<S, O>> =
    invoke({ a, b, c, d, e, f, g, h, i -> ExecutionResult.success() }, elementA, elementB, elementC, elementD, elementE,
        elementF, elementG, elementH, elementI)

@OverloadResolutionByLambdaReturnType
operator fun <S : Environment, O, A : Any, B : Any, C : Any, D : Any, E : Any, F : Any, G : Any, H : Any, I : Any, J : Any>
        StructureScope<S, O>.invoke(
    execute: Invocation<S, O>.(A, B, C, D, E, F, G, H, I, J) -> ExecutionResult,
    elementA: StructureElement<S, O, SignatureConstraint.NonTerminating<S, O, A>>,
    elementB: StructureElement<S, O, SignatureConstraint.NonTerminating<S, O, B>>,
    elementC: StructureElement<S, O, SignatureConstraint.NonTerminating<S, O, C>>,
    elementD: StructureElement<S, O, SignatureConstraint.NonTerminating<S, O, D>>,
    elementE: StructureElement<S, O, SignatureConstraint.NonTerminating<S, O, E>>,
    elementF: StructureElement<S, O, SignatureConstraint.NonTerminating<S, O, F>>,
    elementG: StructureElement<S, O, SignatureConstraint.NonTerminating<S, O, G>>,
    elementH: StructureElement<S, O, SignatureConstraint.NonTerminating<S, O, H>>,
    elementI: StructureElement<S, O, SignatureConstraint.NonTerminating<S, O, I>>,
    elementJ: StructureElement<S, O, SignatureConstraint.Terminating<S, O, J>>,
): StructureElement<S, O, Structure<S, O>> = {
    build(Signature10(execute,
        Tuple10(elementA.invoke(this), elementB.invoke(this), elementC.invoke(this), elementD.invoke(this),
            elementE.invoke(this), elementF.invoke(this), elementG.invoke(this), elementH.invoke(this),
            elementI.invoke(this), elementJ.invoke(this))
    ))
}
@OverloadResolutionByLambdaReturnType
operator fun <S : Environment, O, A : Any, B : Any, C : Any, D : Any, E : Any, F : Any, G : Any, H : Any, I : Any, J : Any>
        StructureScope<S, O>.invoke(
    elementA: StructureElement<S, O, SignatureConstraint.NonTerminating<S, O, A>>,
    elementB: StructureElement<S, O, SignatureConstraint.NonTerminating<S, O, B>>,
    elementC: StructureElement<S, O, SignatureConstraint.NonTerminating<S, O, C>>,
    elementD: StructureElement<S, O, SignatureConstraint.NonTerminating<S, O, D>>,
    elementE: StructureElement<S, O, SignatureConstraint.NonTerminating<S, O, E>>,
    elementF: StructureElement<S, O, SignatureConstraint.NonTerminating<S, O, F>>,
    elementG: StructureElement<S, O, SignatureConstraint.NonTerminating<S, O, G>>,
    elementH: StructureElement<S, O, SignatureConstraint.NonTerminating<S, O, H>>,
    elementI: StructureElement<S, O, SignatureConstraint.NonTerminating<S, O, I>>,
    elementJ: StructureElement<S, O, SignatureConstraint.Terminating<S, O, J>>,
): StructureElement<S, O, Structure<S, O>> =
    invoke({ a, b, c, d, e, f, g, h, i, j -> ExecutionResult.success() }, elementA, elementB, elementC, elementD,
        elementE, elementF, elementG, elementH, elementI, elementJ)

@OverloadResolutionByLambdaReturnType
operator fun <S : Environment, O, A : Any, B : Any, C : Any, D : Any, E : Any, F : Any, G : Any, H : Any, I : Any, J : Any, K : Any>
        StructureScope<S, O>.invoke(
    execute: Invocation<S, O>.(A, B, C, D, E, F, G, H, I, J, K) -> ExecutionResult,
    elementA: StructureElement<S, O, SignatureConstraint.NonTerminating<S, O, A>>,
    elementB: StructureElement<S, O, SignatureConstraint.NonTerminating<S, O, B>>,
    elementC: StructureElement<S, O, SignatureConstraint.NonTerminating<S, O, C>>,
    elementD: StructureElement<S, O, SignatureConstraint.NonTerminating<S, O, D>>,
    elementE: StructureElement<S, O, SignatureConstraint.NonTerminating<S, O, E>>,
    elementF: StructureElement<S, O, SignatureConstraint.NonTerminating<S, O, F>>,
    elementG: StructureElement<S, O, SignatureConstraint.NonTerminating<S, O, G>>,
    elementH: StructureElement<S, O, SignatureConstraint.NonTerminating<S, O, H>>,
    elementI: StructureElement<S, O, SignatureConstraint.NonTerminating<S, O, I>>,
    elementJ: StructureElement<S, O, SignatureConstraint.NonTerminating<S, O, J>>,
    elementK: StructureElement<S, O, SignatureConstraint.Terminating<S, O, K>>,
): StructureElement<S, O, Structure<S, O>> = {
    build(Signature11(execute,
        Tuple11(elementA.invoke(this), elementB.invoke(this), elementC.invoke(this), elementD.invoke(this),
            elementE.invoke(this), elementF.invoke(this), elementG.invoke(this), elementH.invoke(this),
            elementI.invoke(this), elementJ.invoke(this), elementK.invoke(this))
    ))
}
@OverloadResolutionByLambdaReturnType
operator fun <S : Environment, O, A : Any, B : Any, C : Any, D : Any, E : Any, F : Any, G : Any, H : Any, I : Any, J : Any, K : Any>
        StructureScope<S, O>.invoke(
    elementA: StructureElement<S, O, SignatureConstraint.NonTerminating<S, O, A>>,
    elementB: StructureElement<S, O, SignatureConstraint.NonTerminating<S, O, B>>,
    elementC: StructureElement<S, O, SignatureConstraint.NonTerminating<S, O, C>>,
    elementD: StructureElement<S, O, SignatureConstraint.NonTerminating<S, O, D>>,
    elementE: StructureElement<S, O, SignatureConstraint.NonTerminating<S, O, E>>,
    elementF: StructureElement<S, O, SignatureConstraint.NonTerminating<S, O, F>>,
    elementG: StructureElement<S, O, SignatureConstraint.NonTerminating<S, O, G>>,
    elementH: StructureElement<S, O, SignatureConstraint.NonTerminating<S, O, H>>,
    elementI: StructureElement<S, O, SignatureConstraint.NonTerminating<S, O, I>>,
    elementJ: StructureElement<S, O, SignatureConstraint.NonTerminating<S, O, J>>,
    elementK: StructureElement<S, O, SignatureConstraint.Terminating<S, O, K>>,
): StructureElement<S, O, Structure<S, O>> =
    invoke({ a, b, c, d, e, f, g, h, i, j, k -> ExecutionResult.success() }, elementA, elementB, elementC, elementD,
        elementE, elementF, elementG, elementH, elementI, elementJ, elementK)

@OverloadResolutionByLambdaReturnType
operator fun <S : Environment, O, A : Any, B : Any, C : Any, D : Any, E : Any, F : Any, G : Any, H : Any, I : Any, J : Any, K : Any,
        L : Any> StructureScope<S, O>.invoke(
    execute: Invocation<S, O>.(A, B, C, D, E, F, G, H, I, J, K, L) -> ExecutionResult,
    elementA: StructureElement<S, O, SignatureConstraint.NonTerminating<S, O, A>>,
    elementB: StructureElement<S, O, SignatureConstraint.NonTerminating<S, O, B>>,
    elementC: StructureElement<S, O, SignatureConstraint.NonTerminating<S, O, C>>,
    elementD: StructureElement<S, O, SignatureConstraint.NonTerminating<S, O, D>>,
    elementE: StructureElement<S, O, SignatureConstraint.NonTerminating<S, O, E>>,
    elementF: StructureElement<S, O, SignatureConstraint.NonTerminating<S, O, F>>,
    elementG: StructureElement<S, O, SignatureConstraint.NonTerminating<S, O, G>>,
    elementH: StructureElement<S, O, SignatureConstraint.NonTerminating<S, O, H>>,
    elementI: StructureElement<S, O, SignatureConstraint.NonTerminating<S, O, I>>,
    elementJ: StructureElement<S, O, SignatureConstraint.NonTerminating<S, O, J>>,
    elementK: StructureElement<S, O, SignatureConstraint.NonTerminating<S, O, K>>,
    elementL: StructureElement<S, O, SignatureConstraint.Terminating<S, O, L>>,
): StructureElement<S, O, Structure<S, O>> = {
    build(Signature12(execute,
        Tuple12(elementA.invoke(this), elementB.invoke(this), elementC.invoke(this), elementD.invoke(this),
            elementE.invoke(this), elementF.invoke(this), elementG.invoke(this), elementH.invoke(this),
            elementI.invoke(this), elementJ.invoke(this), elementK.invoke(this), elementL.invoke(this))
    ))
}
@OverloadResolutionByLambdaReturnType
operator fun <S : Environment, O, A : Any, B : Any, C : Any, D : Any, E : Any, F : Any, G : Any, H : Any, I : Any, J : Any, K : Any,
        L : Any> StructureScope<S, O>.invoke(
    elementA: StructureElement<S, O, SignatureConstraint.NonTerminating<S, O, A>>,
    elementB: StructureElement<S, O, SignatureConstraint.NonTerminating<S, O, B>>,
    elementC: StructureElement<S, O, SignatureConstraint.NonTerminating<S, O, C>>,
    elementD: StructureElement<S, O, SignatureConstraint.NonTerminating<S, O, D>>,
    elementE: StructureElement<S, O, SignatureConstraint.NonTerminating<S, O, E>>,
    elementF: StructureElement<S, O, SignatureConstraint.NonTerminating<S, O, F>>,
    elementG: StructureElement<S, O, SignatureConstraint.NonTerminating<S, O, G>>,
    elementH: StructureElement<S, O, SignatureConstraint.NonTerminating<S, O, H>>,
    elementI: StructureElement<S, O, SignatureConstraint.NonTerminating<S, O, I>>,
    elementJ: StructureElement<S, O, SignatureConstraint.NonTerminating<S, O, J>>,
    elementK: StructureElement<S, O, SignatureConstraint.NonTerminating<S, O, K>>,
    elementL: StructureElement<S, O, SignatureConstraint.Terminating<S, O, L>>,
): StructureElement<S, O, Structure<S, O>> =
    invoke({ a, b, c, d, e, f, g, h, i, j, k, l -> ExecutionResult.success() }, elementA, elementB, elementC, elementD,
        elementE, elementF, elementG, elementH, elementI, elementJ, elementK, elementL)
