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
operator fun <E_ : Environment, O> StructureScope<E_, O>.invoke(
    execute: Invocation<E_, O>.() -> ExecutionResult,
): Structure<E_, O> =
    build(Signature0(execute, Tuple0()))

@OverloadResolutionByLambdaReturnType
operator fun <E_ : Environment, O, A : Any> StructureScope<E_, O>.invoke(
    execute: Invocation<E_, O>.(A) -> ExecutionResult,
    element: StructureElement<E_, O, SignatureConstraint.Terminating<E_, O, A>>,
): StructureElement<E_, O, Structure<E_, O>> = {
    build(Signature1(execute, Tuple1(element.invoke(this))))
}
@OverloadResolutionByLambdaReturnType
operator fun <E_ : Environment, O, A : Any> StructureScope<E_, O>.invoke(
    element: StructureElement<E_, O, SignatureConstraint.Terminating<E_, O, A>>,
): StructureElement<E_, O, Structure<E_, O>> =
    invoke({ a: A -> ExecutionResult.success() }, element)

@OverloadResolutionByLambdaReturnType
operator fun <E_ : Environment, O, A : Any, B : Any> StructureScope<E_, O>.invoke(
    execute: Invocation<E_, O>.(A, B) -> ExecutionResult,
    elementA: StructureElement<E_, O, SignatureConstraint.NonTerminating<E_, O, A>>,
    elementB: StructureElement<E_, O, SignatureConstraint.Terminating<E_, O, B>>,
): StructureElement<E_, O, Structure<E_, O>> = {
    build(Signature2(execute, Tuple2(elementA.invoke(this), elementB.invoke(this))))
}
@OverloadResolutionByLambdaReturnType
operator fun <E_ : Environment, O, A : Any, B : Any> StructureScope<E_, O>.invoke(
    elementA: StructureElement<E_, O, SignatureConstraint.NonTerminating<E_, O, A>>,
    elementB: StructureElement<E_, O, SignatureConstraint.Terminating<E_, O, B>>,
): StructureElement<E_, O, Structure<E_, O>> =
    invoke({ a, b -> ExecutionResult.success() }, elementA, elementB)

@OverloadResolutionByLambdaReturnType
operator fun <E_ : Environment, O, A : Any, B : Any, C : Any> StructureScope<E_, O>.invoke(
    execute: Invocation<E_, O>.(A, B, C) -> ExecutionResult,
    elementA: StructureElement<E_, O, SignatureConstraint.NonTerminating<E_, O, A>>,
    elementB: StructureElement<E_, O, SignatureConstraint.NonTerminating<E_, O, B>>,
    elementC: StructureElement<E_, O, SignatureConstraint.Terminating<E_, O, C>>,
): StructureElement<E_, O, Structure<E_, O>> = {
    build(Signature3(execute,
        Tuple3(elementA.invoke(this), elementB.invoke(this), elementC.invoke(this))
    ))
}
@OverloadResolutionByLambdaReturnType
operator fun <E_ : Environment, O, A : Any, B : Any, C : Any> StructureScope<E_, O>.invoke(
    elementA: StructureElement<E_, O, SignatureConstraint.NonTerminating<E_, O, A>>,
    elementB: StructureElement<E_, O, SignatureConstraint.NonTerminating<E_, O, B>>,
    elementC: StructureElement<E_, O, SignatureConstraint.Terminating<E_, O, C>>,
): StructureElement<E_, O, Structure<E_, O>> =
    invoke({ a, b, c -> ExecutionResult.success() }, elementA, elementB, elementC)

@OverloadResolutionByLambdaReturnType
operator fun <E_ : Environment, O, A : Any, B : Any, C : Any, D : Any> StructureScope<E_, O>.invoke(
    execute: Invocation<E_, O>.(A, B, C, D) -> ExecutionResult,
    elementA: StructureElement<E_, O, SignatureConstraint.NonTerminating<E_, O, A>>,
    elementB: StructureElement<E_, O, SignatureConstraint.NonTerminating<E_, O, B>>,
    elementC: StructureElement<E_, O, SignatureConstraint.NonTerminating<E_, O, C>>,
    elementD: StructureElement<E_, O, SignatureConstraint.Terminating<E_, O, D>>,
): StructureElement<E_, O, Structure<E_, O>> = {
    build(Signature4(execute,
        Tuple4(elementA.invoke(this), elementB.invoke(this), elementC.invoke(this), elementD.invoke(this))
    ))
}
@OverloadResolutionByLambdaReturnType
operator fun <E_ : Environment, O, A : Any, B : Any, C : Any, D : Any> StructureScope<E_, O>.invoke(
    elementA: StructureElement<E_, O, SignatureConstraint.NonTerminating<E_, O, A>>,
    elementB: StructureElement<E_, O, SignatureConstraint.NonTerminating<E_, O, B>>,
    elementC: StructureElement<E_, O, SignatureConstraint.NonTerminating<E_, O, C>>,
    elementD: StructureElement<E_, O, SignatureConstraint.Terminating<E_, O, D>>,
): StructureElement<E_, O, Structure<E_, O>> =
    invoke({ a, b, c, d -> ExecutionResult.success() }, elementA, elementB, elementC, elementD)

@OverloadResolutionByLambdaReturnType
operator fun <E_ : Environment, O, A : Any, B : Any, C : Any, D : Any, E : Any> StructureScope<E_, O>.invoke(
    execute: Invocation<E_, O>.(A, B, C, D, E) -> ExecutionResult,
    elementA: StructureElement<E_, O, SignatureConstraint.NonTerminating<E_, O, A>>,
    elementB: StructureElement<E_, O, SignatureConstraint.NonTerminating<E_, O, B>>,
    elementC: StructureElement<E_, O, SignatureConstraint.NonTerminating<E_, O, C>>,
    elementD: StructureElement<E_, O, SignatureConstraint.NonTerminating<E_, O, D>>,
    elementE: StructureElement<E_, O, SignatureConstraint.Terminating<E_, O, E>>,
): StructureElement<E_, O, Structure<E_, O>> = {
    build(Signature5(execute,
        Tuple5(elementA.invoke(this), elementB.invoke(this), elementC.invoke(this), elementD.invoke(this),
            elementE.invoke(this))
    ))
}
@OverloadResolutionByLambdaReturnType
operator fun <E_ : Environment, O, A : Any, B : Any, C : Any, D : Any, E : Any> StructureScope<E_, O>.invoke(
    elementA: StructureElement<E_, O, SignatureConstraint.NonTerminating<E_, O, A>>,
    elementB: StructureElement<E_, O, SignatureConstraint.NonTerminating<E_, O, B>>,
    elementC: StructureElement<E_, O, SignatureConstraint.NonTerminating<E_, O, C>>,
    elementD: StructureElement<E_, O, SignatureConstraint.NonTerminating<E_, O, D>>,
    elementE: StructureElement<E_, O, SignatureConstraint.Terminating<E_, O, E>>,
): StructureElement<E_, O, Structure<E_, O>> =
    invoke({ a, b, c, d, e -> ExecutionResult.success() }, elementA, elementB, elementC, elementD, elementE)

@OverloadResolutionByLambdaReturnType
operator fun <E_ : Environment, O, A : Any, B : Any, C : Any, D : Any, E : Any, F : Any> StructureScope<E_, O>.invoke(
    execute: Invocation<E_, O>.(A, B, C, D, E, F) -> ExecutionResult,
    elementA: StructureElement<E_, O, SignatureConstraint.NonTerminating<E_, O, A>>,
    elementB: StructureElement<E_, O, SignatureConstraint.NonTerminating<E_, O, B>>,
    elementC: StructureElement<E_, O, SignatureConstraint.NonTerminating<E_, O, C>>,
    elementD: StructureElement<E_, O, SignatureConstraint.NonTerminating<E_, O, D>>,
    elementE: StructureElement<E_, O, SignatureConstraint.NonTerminating<E_, O, E>>,
    elementF: StructureElement<E_, O, SignatureConstraint.Terminating<E_, O, F>>,
): StructureElement<E_, O, Structure<E_, O>> = {
    build(Signature6(execute,
        Tuple6(elementA.invoke(this), elementB.invoke(this), elementC.invoke(this), elementD.invoke(this),
            elementE.invoke(this), elementF.invoke(this))
    ))
}
@OverloadResolutionByLambdaReturnType
operator fun <E_ : Environment, O, A : Any, B : Any, C : Any, D : Any, E : Any, F : Any> StructureScope<E_, O>.invoke(
    elementA: StructureElement<E_, O, SignatureConstraint.NonTerminating<E_, O, A>>,
    elementB: StructureElement<E_, O, SignatureConstraint.NonTerminating<E_, O, B>>,
    elementC: StructureElement<E_, O, SignatureConstraint.NonTerminating<E_, O, C>>,
    elementD: StructureElement<E_, O, SignatureConstraint.NonTerminating<E_, O, D>>,
    elementE: StructureElement<E_, O, SignatureConstraint.NonTerminating<E_, O, E>>,
    elementF: StructureElement<E_, O, SignatureConstraint.Terminating<E_, O, F>>,
): StructureElement<E_, O, Structure<E_, O>> =
    invoke({ a, b, c, d, e, f -> ExecutionResult.success() }, elementA, elementB, elementC, elementD, elementE,
        elementF)

@OverloadResolutionByLambdaReturnType
operator fun <E_ : Environment, O, A : Any, B : Any, C : Any, D : Any, E : Any, F : Any, G : Any> StructureScope<E_, O>.invoke(
    execute: Invocation<E_, O>.(A, B, C, D, E, F, G) -> ExecutionResult,
    elementA: StructureElement<E_, O, SignatureConstraint.NonTerminating<E_, O, A>>,
    elementB: StructureElement<E_, O, SignatureConstraint.NonTerminating<E_, O, B>>,
    elementC: StructureElement<E_, O, SignatureConstraint.NonTerminating<E_, O, C>>,
    elementD: StructureElement<E_, O, SignatureConstraint.NonTerminating<E_, O, D>>,
    elementE: StructureElement<E_, O, SignatureConstraint.NonTerminating<E_, O, E>>,
    elementF: StructureElement<E_, O, SignatureConstraint.NonTerminating<E_, O, F>>,
    elementG: StructureElement<E_, O, SignatureConstraint.Terminating<E_, O, G>>,
): StructureElement<E_, O, Structure<E_, O>> = {
    build(Signature7(execute,
        Tuple7(elementA.invoke(this), elementB.invoke(this), elementC.invoke(this), elementD.invoke(this),
            elementE.invoke(this), elementF.invoke(this), elementG.invoke(this))
    ))
}
@OverloadResolutionByLambdaReturnType
operator fun <E_ : Environment, O, A : Any, B : Any, C : Any, D : Any, E : Any, F : Any, G : Any> StructureScope<E_, O>.invoke(
    elementA: StructureElement<E_, O, SignatureConstraint.NonTerminating<E_, O, A>>,
    elementB: StructureElement<E_, O, SignatureConstraint.NonTerminating<E_, O, B>>,
    elementC: StructureElement<E_, O, SignatureConstraint.NonTerminating<E_, O, C>>,
    elementD: StructureElement<E_, O, SignatureConstraint.NonTerminating<E_, O, D>>,
    elementE: StructureElement<E_, O, SignatureConstraint.NonTerminating<E_, O, E>>,
    elementF: StructureElement<E_, O, SignatureConstraint.NonTerminating<E_, O, F>>,
    elementG: StructureElement<E_, O, SignatureConstraint.Terminating<E_, O, G>>,
): StructureElement<E_, O, Structure<E_, O>> =
    invoke({ a, b, c, d, e, f, g -> ExecutionResult.success() }, elementA, elementB, elementC, elementD, elementE,
        elementF, elementG)

@OverloadResolutionByLambdaReturnType
operator fun <E_ : Environment, O, A : Any, B : Any, C : Any, D : Any, E : Any, F : Any, G : Any, H : Any> StructureScope<E_, O>.invoke(
    execute: Invocation<E_, O>.(A, B, C, D, E, F, G, H) -> ExecutionResult,
    elementA: StructureElement<E_, O, SignatureConstraint.NonTerminating<E_, O, A>>,
    elementB: StructureElement<E_, O, SignatureConstraint.NonTerminating<E_, O, B>>,
    elementC: StructureElement<E_, O, SignatureConstraint.NonTerminating<E_, O, C>>,
    elementD: StructureElement<E_, O, SignatureConstraint.NonTerminating<E_, O, D>>,
    elementE: StructureElement<E_, O, SignatureConstraint.NonTerminating<E_, O, E>>,
    elementF: StructureElement<E_, O, SignatureConstraint.NonTerminating<E_, O, F>>,
    elementG: StructureElement<E_, O, SignatureConstraint.NonTerminating<E_, O, G>>,
    elementH: StructureElement<E_, O, SignatureConstraint.Terminating<E_, O, H>>,
): StructureElement<E_, O, Structure<E_, O>> = {
    build(Signature8(execute,
        Tuple8(elementA.invoke(this), elementB.invoke(this), elementC.invoke(this), elementD.invoke(this),
            elementE.invoke(this), elementF.invoke(this), elementG.invoke(this), elementH.invoke(this))
    ))
}
@OverloadResolutionByLambdaReturnType
operator fun <E_ : Environment, O, A : Any, B : Any, C : Any, D : Any, E : Any, F : Any, G : Any, H : Any> StructureScope<E_, O>.invoke(
    elementA: StructureElement<E_, O, SignatureConstraint.NonTerminating<E_, O, A>>,
    elementB: StructureElement<E_, O, SignatureConstraint.NonTerminating<E_, O, B>>,
    elementC: StructureElement<E_, O, SignatureConstraint.NonTerminating<E_, O, C>>,
    elementD: StructureElement<E_, O, SignatureConstraint.NonTerminating<E_, O, D>>,
    elementE: StructureElement<E_, O, SignatureConstraint.NonTerminating<E_, O, E>>,
    elementF: StructureElement<E_, O, SignatureConstraint.NonTerminating<E_, O, F>>,
    elementG: StructureElement<E_, O, SignatureConstraint.NonTerminating<E_, O, G>>,
    elementH: StructureElement<E_, O, SignatureConstraint.Terminating<E_, O, H>>,
): StructureElement<E_, O, Structure<E_, O>> =
    invoke({ a, b, c, d, e, f, g, h -> ExecutionResult.success() }, elementA, elementB, elementC, elementD, elementE,
        elementF, elementG, elementH)

@OverloadResolutionByLambdaReturnType
operator fun <E_ : Environment, O, A : Any, B : Any, C : Any, D : Any, E : Any, F : Any, G : Any, H : Any, I : Any>
        StructureScope<E_, O>.invoke(
    execute: Invocation<E_, O>.(A, B, C, D, E, F, G, H, I) -> ExecutionResult,
    elementA: StructureElement<E_, O, SignatureConstraint.NonTerminating<E_, O, A>>,
    elementB: StructureElement<E_, O, SignatureConstraint.NonTerminating<E_, O, B>>,
    elementC: StructureElement<E_, O, SignatureConstraint.NonTerminating<E_, O, C>>,
    elementD: StructureElement<E_, O, SignatureConstraint.NonTerminating<E_, O, D>>,
    elementE: StructureElement<E_, O, SignatureConstraint.NonTerminating<E_, O, E>>,
    elementF: StructureElement<E_, O, SignatureConstraint.NonTerminating<E_, O, F>>,
    elementG: StructureElement<E_, O, SignatureConstraint.NonTerminating<E_, O, G>>,
    elementH: StructureElement<E_, O, SignatureConstraint.NonTerminating<E_, O, H>>,
    elementI: StructureElement<E_, O, SignatureConstraint.Terminating<E_, O, I>>,
): StructureElement<E_, O, Structure<E_, O>> = {
    build(Signature9(execute,
        Tuple9(elementA.invoke(this), elementB.invoke(this), elementC.invoke(this), elementD.invoke(this),
            elementE.invoke(this), elementF.invoke(this), elementG.invoke(this), elementH.invoke(this),
            elementI.invoke(this))
    ))
}
@OverloadResolutionByLambdaReturnType
operator fun <E_ : Environment, O, A : Any, B : Any, C : Any, D : Any, E : Any, F : Any, G : Any, H : Any, I : Any>
        StructureScope<E_, O>.invoke(
    elementA: StructureElement<E_, O, SignatureConstraint.NonTerminating<E_, O, A>>,
    elementB: StructureElement<E_, O, SignatureConstraint.NonTerminating<E_, O, B>>,
    elementC: StructureElement<E_, O, SignatureConstraint.NonTerminating<E_, O, C>>,
    elementD: StructureElement<E_, O, SignatureConstraint.NonTerminating<E_, O, D>>,
    elementE: StructureElement<E_, O, SignatureConstraint.NonTerminating<E_, O, E>>,
    elementF: StructureElement<E_, O, SignatureConstraint.NonTerminating<E_, O, F>>,
    elementG: StructureElement<E_, O, SignatureConstraint.NonTerminating<E_, O, G>>,
    elementH: StructureElement<E_, O, SignatureConstraint.NonTerminating<E_, O, H>>,
    elementI: StructureElement<E_, O, SignatureConstraint.Terminating<E_, O, I>>,
): StructureElement<E_, O, Structure<E_, O>> =
    invoke({ a, b, c, d, e, f, g, h, i -> ExecutionResult.success() }, elementA, elementB, elementC, elementD, elementE,
        elementF, elementG, elementH, elementI)

@OverloadResolutionByLambdaReturnType
operator fun <E_ : Environment, O, A : Any, B : Any, C : Any, D : Any, E : Any, F : Any, G : Any, H : Any, I : Any, J : Any>
        StructureScope<E_, O>.invoke(
    execute: Invocation<E_, O>.(A, B, C, D, E, F, G, H, I, J) -> ExecutionResult,
    elementA: StructureElement<E_, O, SignatureConstraint.NonTerminating<E_, O, A>>,
    elementB: StructureElement<E_, O, SignatureConstraint.NonTerminating<E_, O, B>>,
    elementC: StructureElement<E_, O, SignatureConstraint.NonTerminating<E_, O, C>>,
    elementD: StructureElement<E_, O, SignatureConstraint.NonTerminating<E_, O, D>>,
    elementE: StructureElement<E_, O, SignatureConstraint.NonTerminating<E_, O, E>>,
    elementF: StructureElement<E_, O, SignatureConstraint.NonTerminating<E_, O, F>>,
    elementG: StructureElement<E_, O, SignatureConstraint.NonTerminating<E_, O, G>>,
    elementH: StructureElement<E_, O, SignatureConstraint.NonTerminating<E_, O, H>>,
    elementI: StructureElement<E_, O, SignatureConstraint.NonTerminating<E_, O, I>>,
    elementJ: StructureElement<E_, O, SignatureConstraint.Terminating<E_, O, J>>,
): StructureElement<E_, O, Structure<E_, O>> = {
    build(Signature10(execute,
        Tuple10(elementA.invoke(this), elementB.invoke(this), elementC.invoke(this), elementD.invoke(this),
            elementE.invoke(this), elementF.invoke(this), elementG.invoke(this), elementH.invoke(this),
            elementI.invoke(this), elementJ.invoke(this))
    ))
}
@OverloadResolutionByLambdaReturnType
operator fun <E_ : Environment, O, A : Any, B : Any, C : Any, D : Any, E : Any, F : Any, G : Any, H : Any, I : Any, J : Any>
        StructureScope<E_, O>.invoke(
    elementA: StructureElement<E_, O, SignatureConstraint.NonTerminating<E_, O, A>>,
    elementB: StructureElement<E_, O, SignatureConstraint.NonTerminating<E_, O, B>>,
    elementC: StructureElement<E_, O, SignatureConstraint.NonTerminating<E_, O, C>>,
    elementD: StructureElement<E_, O, SignatureConstraint.NonTerminating<E_, O, D>>,
    elementE: StructureElement<E_, O, SignatureConstraint.NonTerminating<E_, O, E>>,
    elementF: StructureElement<E_, O, SignatureConstraint.NonTerminating<E_, O, F>>,
    elementG: StructureElement<E_, O, SignatureConstraint.NonTerminating<E_, O, G>>,
    elementH: StructureElement<E_, O, SignatureConstraint.NonTerminating<E_, O, H>>,
    elementI: StructureElement<E_, O, SignatureConstraint.NonTerminating<E_, O, I>>,
    elementJ: StructureElement<E_, O, SignatureConstraint.Terminating<E_, O, J>>,
): StructureElement<E_, O, Structure<E_, O>> =
    invoke({ a, b, c, d, e, f, g, h, i, j -> ExecutionResult.success() }, elementA, elementB, elementC, elementD,
        elementE, elementF, elementG, elementH, elementI, elementJ)

@OverloadResolutionByLambdaReturnType
operator fun <E_ : Environment, O, A : Any, B : Any, C : Any, D : Any, E : Any, F : Any, G : Any, H : Any, I : Any, J : Any, K : Any>
        StructureScope<E_, O>.invoke(
    execute: Invocation<E_, O>.(A, B, C, D, E, F, G, H, I, J, K) -> ExecutionResult,
    elementA: StructureElement<E_, O, SignatureConstraint.NonTerminating<E_, O, A>>,
    elementB: StructureElement<E_, O, SignatureConstraint.NonTerminating<E_, O, B>>,
    elementC: StructureElement<E_, O, SignatureConstraint.NonTerminating<E_, O, C>>,
    elementD: StructureElement<E_, O, SignatureConstraint.NonTerminating<E_, O, D>>,
    elementE: StructureElement<E_, O, SignatureConstraint.NonTerminating<E_, O, E>>,
    elementF: StructureElement<E_, O, SignatureConstraint.NonTerminating<E_, O, F>>,
    elementG: StructureElement<E_, O, SignatureConstraint.NonTerminating<E_, O, G>>,
    elementH: StructureElement<E_, O, SignatureConstraint.NonTerminating<E_, O, H>>,
    elementI: StructureElement<E_, O, SignatureConstraint.NonTerminating<E_, O, I>>,
    elementJ: StructureElement<E_, O, SignatureConstraint.NonTerminating<E_, O, J>>,
    elementK: StructureElement<E_, O, SignatureConstraint.Terminating<E_, O, K>>,
): StructureElement<E_, O, Structure<E_, O>> = {
    build(Signature11(execute,
        Tuple11(elementA.invoke(this), elementB.invoke(this), elementC.invoke(this), elementD.invoke(this),
            elementE.invoke(this), elementF.invoke(this), elementG.invoke(this), elementH.invoke(this),
            elementI.invoke(this), elementJ.invoke(this), elementK.invoke(this))
    ))
}
@OverloadResolutionByLambdaReturnType
operator fun <E_ : Environment, O, A : Any, B : Any, C : Any, D : Any, E : Any, F : Any, G : Any, H : Any, I : Any, J : Any, K : Any>
        StructureScope<E_, O>.invoke(
    elementA: StructureElement<E_, O, SignatureConstraint.NonTerminating<E_, O, A>>,
    elementB: StructureElement<E_, O, SignatureConstraint.NonTerminating<E_, O, B>>,
    elementC: StructureElement<E_, O, SignatureConstraint.NonTerminating<E_, O, C>>,
    elementD: StructureElement<E_, O, SignatureConstraint.NonTerminating<E_, O, D>>,
    elementE: StructureElement<E_, O, SignatureConstraint.NonTerminating<E_, O, E>>,
    elementF: StructureElement<E_, O, SignatureConstraint.NonTerminating<E_, O, F>>,
    elementG: StructureElement<E_, O, SignatureConstraint.NonTerminating<E_, O, G>>,
    elementH: StructureElement<E_, O, SignatureConstraint.NonTerminating<E_, O, H>>,
    elementI: StructureElement<E_, O, SignatureConstraint.NonTerminating<E_, O, I>>,
    elementJ: StructureElement<E_, O, SignatureConstraint.NonTerminating<E_, O, J>>,
    elementK: StructureElement<E_, O, SignatureConstraint.Terminating<E_, O, K>>,
): StructureElement<E_, O, Structure<E_, O>> =
    invoke({ a, b, c, d, e, f, g, h, i, j, k -> ExecutionResult.success() }, elementA, elementB, elementC, elementD,
        elementE, elementF, elementG, elementH, elementI, elementJ, elementK)

@OverloadResolutionByLambdaReturnType
operator fun <E_ : Environment, O, A : Any, B : Any, C : Any, D : Any, E : Any, F : Any, G : Any, H : Any, I : Any, J : Any, K : Any,
        L : Any> StructureScope<E_, O>.invoke(
    execute: Invocation<E_, O>.(A, B, C, D, E, F, G, H, I, J, K, L) -> ExecutionResult,
    elementA: StructureElement<E_, O, SignatureConstraint.NonTerminating<E_, O, A>>,
    elementB: StructureElement<E_, O, SignatureConstraint.NonTerminating<E_, O, B>>,
    elementC: StructureElement<E_, O, SignatureConstraint.NonTerminating<E_, O, C>>,
    elementD: StructureElement<E_, O, SignatureConstraint.NonTerminating<E_, O, D>>,
    elementE: StructureElement<E_, O, SignatureConstraint.NonTerminating<E_, O, E>>,
    elementF: StructureElement<E_, O, SignatureConstraint.NonTerminating<E_, O, F>>,
    elementG: StructureElement<E_, O, SignatureConstraint.NonTerminating<E_, O, G>>,
    elementH: StructureElement<E_, O, SignatureConstraint.NonTerminating<E_, O, H>>,
    elementI: StructureElement<E_, O, SignatureConstraint.NonTerminating<E_, O, I>>,
    elementJ: StructureElement<E_, O, SignatureConstraint.NonTerminating<E_, O, J>>,
    elementK: StructureElement<E_, O, SignatureConstraint.NonTerminating<E_, O, K>>,
    elementL: StructureElement<E_, O, SignatureConstraint.Terminating<E_, O, L>>,
): StructureElement<E_, O, Structure<E_, O>> = {
    build(Signature12(execute,
        Tuple12(elementA.invoke(this), elementB.invoke(this), elementC.invoke(this), elementD.invoke(this),
            elementE.invoke(this), elementF.invoke(this), elementG.invoke(this), elementH.invoke(this),
            elementI.invoke(this), elementJ.invoke(this), elementK.invoke(this), elementL.invoke(this))
    ))
}
@OverloadResolutionByLambdaReturnType
operator fun <E_ : Environment, O, A : Any, B : Any, C : Any, D : Any, E : Any, F : Any, G : Any, H : Any, I : Any, J : Any, K : Any,
        L : Any> StructureScope<E_, O>.invoke(
    elementA: StructureElement<E_, O, SignatureConstraint.NonTerminating<E_, O, A>>,
    elementB: StructureElement<E_, O, SignatureConstraint.NonTerminating<E_, O, B>>,
    elementC: StructureElement<E_, O, SignatureConstraint.NonTerminating<E_, O, C>>,
    elementD: StructureElement<E_, O, SignatureConstraint.NonTerminating<E_, O, D>>,
    elementE: StructureElement<E_, O, SignatureConstraint.NonTerminating<E_, O, E>>,
    elementF: StructureElement<E_, O, SignatureConstraint.NonTerminating<E_, O, F>>,
    elementG: StructureElement<E_, O, SignatureConstraint.NonTerminating<E_, O, G>>,
    elementH: StructureElement<E_, O, SignatureConstraint.NonTerminating<E_, O, H>>,
    elementI: StructureElement<E_, O, SignatureConstraint.NonTerminating<E_, O, I>>,
    elementJ: StructureElement<E_, O, SignatureConstraint.NonTerminating<E_, O, J>>,
    elementK: StructureElement<E_, O, SignatureConstraint.NonTerminating<E_, O, K>>,
    elementL: StructureElement<E_, O, SignatureConstraint.Terminating<E_, O, L>>,
): StructureElement<E_, O, Structure<E_, O>> =
    invoke({ a, b, c, d, e, f, g, h, i, j, k, l -> ExecutionResult.success() }, elementA, elementB, elementC, elementD,
        elementE, elementF, elementG, elementH, elementI, elementJ, elementK, elementL)
