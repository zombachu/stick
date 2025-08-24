@file:OptIn(ExperimentalTypeInference::class)

package com.zombachu.stick.structure

import com.zombachu.stick.ExecutionContext
import com.zombachu.stick.ExecutionResult
import com.zombachu.stick.SenderContext
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
operator fun <O, S : SenderContext<O>> StructureScope<O, S>.invoke(
    execute: ExecutionContext<O, S>.() -> ExecutionResult,
): Structure<O, S> =
    build(Signature0(execute, Tuple0()))

@OverloadResolutionByLambdaReturnType
operator fun <O, S : SenderContext<O>, A : Any> StructureScope<O, S>.invoke(
    execute: ExecutionContext<O, S>.(A) -> ExecutionResult,
    element: StructureElement<O, S, SignatureConstraint.Terminating<O, S, A>>,
): StructureElement<O, S, Structure<O, S>> = {
    build(Signature1(execute, Tuple1(element.invoke(this))))
}
@OverloadResolutionByLambdaReturnType
operator fun <O, S : SenderContext<O>, A : Any> StructureScope<O, S>.invoke(
    element: StructureElement<O, S, SignatureConstraint.Terminating<O, S, A>>,
): StructureElement<O, S, Structure<O, S>> =
    invoke({ a: A -> ExecutionResult.success() }, element)

@OverloadResolutionByLambdaReturnType
operator fun <O, S : SenderContext<O>, A : Any, B : Any> StructureScope<O, S>.invoke(
    execute: ExecutionContext<O, S>.(A, B) -> ExecutionResult,
    elementA: StructureElement<O, S, SignatureConstraint.NonTerminating<O, S, A>>,
    elementB: StructureElement<O, S, SignatureConstraint.Terminating<O, S, B>>,
): StructureElement<O, S, Structure<O, S>> = {
    build(Signature2(execute, Tuple2(elementA.invoke(this), elementB.invoke(this))))
}
@OverloadResolutionByLambdaReturnType
operator fun <O, S : SenderContext<O>, A : Any, B : Any> StructureScope<O, S>.invoke(
    elementA: StructureElement<O, S, SignatureConstraint.NonTerminating<O, S, A>>,
    elementB: StructureElement<O, S, SignatureConstraint.Terminating<O, S, B>>,
): StructureElement<O, S, Structure<O, S>> =
    invoke({ a, b -> ExecutionResult.success() }, elementA, elementB)

@OverloadResolutionByLambdaReturnType
operator fun <O, S : SenderContext<O>, A : Any, B : Any, C : Any> StructureScope<O, S>.invoke(
    execute: ExecutionContext<O, S>.(A, B, C) -> ExecutionResult,
    elementA: StructureElement<O, S, SignatureConstraint.NonTerminating<O, S, A>>,
    elementB: StructureElement<O, S, SignatureConstraint.NonTerminating<O, S, B>>,
    elementC: StructureElement<O, S, SignatureConstraint.Terminating<O, S, C>>,
): StructureElement<O, S, Structure<O, S>> = {
    build(Signature3(execute,
        Tuple3(elementA.invoke(this), elementB.invoke(this), elementC.invoke(this))
    ))
}
@OverloadResolutionByLambdaReturnType
operator fun <O, S : SenderContext<O>, A : Any, B : Any, C : Any> StructureScope<O, S>.invoke(
    elementA: StructureElement<O, S, SignatureConstraint.NonTerminating<O, S, A>>,
    elementB: StructureElement<O, S, SignatureConstraint.NonTerminating<O, S, B>>,
    elementC: StructureElement<O, S, SignatureConstraint.Terminating<O, S, C>>,
): StructureElement<O, S, Structure<O, S>> =
    invoke({ a, b, c -> ExecutionResult.success() }, elementA, elementB, elementC)

@OverloadResolutionByLambdaReturnType
operator fun <O, S : SenderContext<O>, A : Any, B : Any, C : Any, D : Any> StructureScope<O, S>.invoke(
    execute: ExecutionContext<O, S>.(A, B, C, D) -> ExecutionResult,
    elementA: StructureElement<O, S, SignatureConstraint.NonTerminating<O, S, A>>,
    elementB: StructureElement<O, S, SignatureConstraint.NonTerminating<O, S, B>>,
    elementC: StructureElement<O, S, SignatureConstraint.NonTerminating<O, S, C>>,
    elementD: StructureElement<O, S, SignatureConstraint.Terminating<O, S, D>>,
): StructureElement<O, S, Structure<O, S>> = {
    build(Signature4(execute,
        Tuple4(elementA.invoke(this), elementB.invoke(this), elementC.invoke(this), elementD.invoke(this))
    ))
}
@OverloadResolutionByLambdaReturnType
operator fun <O, S : SenderContext<O>, A : Any, B : Any, C : Any, D : Any> StructureScope<O, S>.invoke(
    elementA: StructureElement<O, S, SignatureConstraint.NonTerminating<O, S, A>>,
    elementB: StructureElement<O, S, SignatureConstraint.NonTerminating<O, S, B>>,
    elementC: StructureElement<O, S, SignatureConstraint.NonTerminating<O, S, C>>,
    elementD: StructureElement<O, S, SignatureConstraint.Terminating<O, S, D>>,
): StructureElement<O, S, Structure<O, S>> =
    invoke({ a, b, c, d -> ExecutionResult.success() }, elementA, elementB, elementC, elementD)

@OverloadResolutionByLambdaReturnType
operator fun <O, S : SenderContext<O>, A : Any, B : Any, C : Any, D : Any, E : Any> StructureScope<O, S>.invoke(
    execute: ExecutionContext<O, S>.(A, B, C, D, E) -> ExecutionResult,
    elementA: StructureElement<O, S, SignatureConstraint.NonTerminating<O, S, A>>,
    elementB: StructureElement<O, S, SignatureConstraint.NonTerminating<O, S, B>>,
    elementC: StructureElement<O, S, SignatureConstraint.NonTerminating<O, S, C>>,
    elementD: StructureElement<O, S, SignatureConstraint.NonTerminating<O, S, D>>,
    elementE: StructureElement<O, S, SignatureConstraint.Terminating<O, S, E>>,
): StructureElement<O, S, Structure<O, S>> = {
    build(Signature5(execute,
        Tuple5(elementA.invoke(this), elementB.invoke(this), elementC.invoke(this), elementD.invoke(this),
            elementE.invoke(this))
    ))
}
@OverloadResolutionByLambdaReturnType
operator fun <O, S : SenderContext<O>, A : Any, B : Any, C : Any, D : Any, E : Any> StructureScope<O, S>.invoke(
    elementA: StructureElement<O, S, SignatureConstraint.NonTerminating<O, S, A>>,
    elementB: StructureElement<O, S, SignatureConstraint.NonTerminating<O, S, B>>,
    elementC: StructureElement<O, S, SignatureConstraint.NonTerminating<O, S, C>>,
    elementD: StructureElement<O, S, SignatureConstraint.NonTerminating<O, S, D>>,
    elementE: StructureElement<O, S, SignatureConstraint.Terminating<O, S, E>>,
): StructureElement<O, S, Structure<O, S>> =
    invoke({ a, b, c, d, e -> ExecutionResult.success() }, elementA, elementB, elementC, elementD, elementE)

@OverloadResolutionByLambdaReturnType
operator fun <O, S : SenderContext<O>, A : Any, B : Any, C : Any, D : Any, E : Any, F : Any> StructureScope<O, S>.invoke(
    execute: ExecutionContext<O, S>.(A, B, C, D, E, F) -> ExecutionResult,
    elementA: StructureElement<O, S, SignatureConstraint.NonTerminating<O, S, A>>,
    elementB: StructureElement<O, S, SignatureConstraint.NonTerminating<O, S, B>>,
    elementC: StructureElement<O, S, SignatureConstraint.NonTerminating<O, S, C>>,
    elementD: StructureElement<O, S, SignatureConstraint.NonTerminating<O, S, D>>,
    elementE: StructureElement<O, S, SignatureConstraint.NonTerminating<O, S, E>>,
    elementF: StructureElement<O, S, SignatureConstraint.Terminating<O, S, F>>,
): StructureElement<O, S, Structure<O, S>> = {
    build(Signature6(execute,
        Tuple6(elementA.invoke(this), elementB.invoke(this), elementC.invoke(this), elementD.invoke(this),
            elementE.invoke(this), elementF.invoke(this))
    ))
}
@OverloadResolutionByLambdaReturnType
operator fun <O, S : SenderContext<O>, A : Any, B : Any, C : Any, D : Any, E : Any, F : Any> StructureScope<O, S>.invoke(
    elementA: StructureElement<O, S, SignatureConstraint.NonTerminating<O, S, A>>,
    elementB: StructureElement<O, S, SignatureConstraint.NonTerminating<O, S, B>>,
    elementC: StructureElement<O, S, SignatureConstraint.NonTerminating<O, S, C>>,
    elementD: StructureElement<O, S, SignatureConstraint.NonTerminating<O, S, D>>,
    elementE: StructureElement<O, S, SignatureConstraint.NonTerminating<O, S, E>>,
    elementF: StructureElement<O, S, SignatureConstraint.Terminating<O, S, F>>,
): StructureElement<O, S, Structure<O, S>> =
    invoke({ a, b, c, d, e, f -> ExecutionResult.success() }, elementA, elementB, elementC, elementD, elementE,
        elementF)

@OverloadResolutionByLambdaReturnType
operator fun <O, S : SenderContext<O>, A : Any, B : Any, C : Any, D : Any, E : Any, F : Any, G : Any> StructureScope<O, S>.invoke(
    execute: ExecutionContext<O, S>.(A, B, C, D, E, F, G) -> ExecutionResult,
    elementA: StructureElement<O, S, SignatureConstraint.NonTerminating<O, S, A>>,
    elementB: StructureElement<O, S, SignatureConstraint.NonTerminating<O, S, B>>,
    elementC: StructureElement<O, S, SignatureConstraint.NonTerminating<O, S, C>>,
    elementD: StructureElement<O, S, SignatureConstraint.NonTerminating<O, S, D>>,
    elementE: StructureElement<O, S, SignatureConstraint.NonTerminating<O, S, E>>,
    elementF: StructureElement<O, S, SignatureConstraint.NonTerminating<O, S, F>>,
    elementG: StructureElement<O, S, SignatureConstraint.Terminating<O, S, G>>,
): StructureElement<O, S, Structure<O, S>> = {
    build(Signature7(execute,
        Tuple7(elementA.invoke(this), elementB.invoke(this), elementC.invoke(this), elementD.invoke(this),
            elementE.invoke(this), elementF.invoke(this), elementG.invoke(this))
    ))
}
@OverloadResolutionByLambdaReturnType
operator fun <O, S : SenderContext<O>, A : Any, B : Any, C : Any, D : Any, E : Any, F : Any, G : Any> StructureScope<O, S>.invoke(
    elementA: StructureElement<O, S, SignatureConstraint.NonTerminating<O, S, A>>,
    elementB: StructureElement<O, S, SignatureConstraint.NonTerminating<O, S, B>>,
    elementC: StructureElement<O, S, SignatureConstraint.NonTerminating<O, S, C>>,
    elementD: StructureElement<O, S, SignatureConstraint.NonTerminating<O, S, D>>,
    elementE: StructureElement<O, S, SignatureConstraint.NonTerminating<O, S, E>>,
    elementF: StructureElement<O, S, SignatureConstraint.NonTerminating<O, S, F>>,
    elementG: StructureElement<O, S, SignatureConstraint.Terminating<O, S, G>>,
): StructureElement<O, S, Structure<O, S>> =
    invoke({ a, b, c, d, e, f, g -> ExecutionResult.success() }, elementA, elementB, elementC, elementD, elementE,
        elementF, elementG)

@OverloadResolutionByLambdaReturnType
operator fun <O, S : SenderContext<O>, A : Any, B : Any, C : Any, D : Any, E : Any, F : Any, G : Any, H : Any> StructureScope<O, S>.invoke(
    execute: ExecutionContext<O, S>.(A, B, C, D, E, F, G, H) -> ExecutionResult,
    elementA: StructureElement<O, S, SignatureConstraint.NonTerminating<O, S, A>>,
    elementB: StructureElement<O, S, SignatureConstraint.NonTerminating<O, S, B>>,
    elementC: StructureElement<O, S, SignatureConstraint.NonTerminating<O, S, C>>,
    elementD: StructureElement<O, S, SignatureConstraint.NonTerminating<O, S, D>>,
    elementE: StructureElement<O, S, SignatureConstraint.NonTerminating<O, S, E>>,
    elementF: StructureElement<O, S, SignatureConstraint.NonTerminating<O, S, F>>,
    elementG: StructureElement<O, S, SignatureConstraint.NonTerminating<O, S, G>>,
    elementH: StructureElement<O, S, SignatureConstraint.Terminating<O, S, H>>,
): StructureElement<O, S, Structure<O, S>> = {
    build(Signature8(execute,
        Tuple8(elementA.invoke(this), elementB.invoke(this), elementC.invoke(this), elementD.invoke(this),
            elementE.invoke(this), elementF.invoke(this), elementG.invoke(this), elementH.invoke(this))
    ))
}
@OverloadResolutionByLambdaReturnType
operator fun <O, S : SenderContext<O>, A : Any, B : Any, C : Any, D : Any, E : Any, F : Any, G : Any, H : Any> StructureScope<O, S>.invoke(
    elementA: StructureElement<O, S, SignatureConstraint.NonTerminating<O, S, A>>,
    elementB: StructureElement<O, S, SignatureConstraint.NonTerminating<O, S, B>>,
    elementC: StructureElement<O, S, SignatureConstraint.NonTerminating<O, S, C>>,
    elementD: StructureElement<O, S, SignatureConstraint.NonTerminating<O, S, D>>,
    elementE: StructureElement<O, S, SignatureConstraint.NonTerminating<O, S, E>>,
    elementF: StructureElement<O, S, SignatureConstraint.NonTerminating<O, S, F>>,
    elementG: StructureElement<O, S, SignatureConstraint.NonTerminating<O, S, G>>,
    elementH: StructureElement<O, S, SignatureConstraint.Terminating<O, S, H>>,
): StructureElement<O, S, Structure<O, S>> =
    invoke({ a, b, c, d, e, f, g, h -> ExecutionResult.success() }, elementA, elementB, elementC, elementD, elementE,
        elementF, elementG, elementH)

@OverloadResolutionByLambdaReturnType
operator fun <O, S : SenderContext<O>, A : Any, B : Any, C : Any, D : Any, E : Any, F : Any, G : Any, H : Any, I : Any>
        StructureScope<O, S>.invoke(
    execute: ExecutionContext<O, S>.(A, B, C, D, E, F, G, H, I) -> ExecutionResult,
    elementA: StructureElement<O, S, SignatureConstraint.NonTerminating<O, S, A>>,
    elementB: StructureElement<O, S, SignatureConstraint.NonTerminating<O, S, B>>,
    elementC: StructureElement<O, S, SignatureConstraint.NonTerminating<O, S, C>>,
    elementD: StructureElement<O, S, SignatureConstraint.NonTerminating<O, S, D>>,
    elementE: StructureElement<O, S, SignatureConstraint.NonTerminating<O, S, E>>,
    elementF: StructureElement<O, S, SignatureConstraint.NonTerminating<O, S, F>>,
    elementG: StructureElement<O, S, SignatureConstraint.NonTerminating<O, S, G>>,
    elementH: StructureElement<O, S, SignatureConstraint.NonTerminating<O, S, H>>,
    elementI: StructureElement<O, S, SignatureConstraint.Terminating<O, S, I>>,
): StructureElement<O, S, Structure<O, S>> = {
    build(Signature9(execute,
        Tuple9(elementA.invoke(this), elementB.invoke(this), elementC.invoke(this), elementD.invoke(this),
            elementE.invoke(this), elementF.invoke(this), elementG.invoke(this), elementH.invoke(this),
            elementI.invoke(this))
    ))
}
@OverloadResolutionByLambdaReturnType
operator fun <O, S : SenderContext<O>, A : Any, B : Any, C : Any, D : Any, E : Any, F : Any, G : Any, H : Any, I : Any>
        StructureScope<O, S>.invoke(
    elementA: StructureElement<O, S, SignatureConstraint.NonTerminating<O, S, A>>,
    elementB: StructureElement<O, S, SignatureConstraint.NonTerminating<O, S, B>>,
    elementC: StructureElement<O, S, SignatureConstraint.NonTerminating<O, S, C>>,
    elementD: StructureElement<O, S, SignatureConstraint.NonTerminating<O, S, D>>,
    elementE: StructureElement<O, S, SignatureConstraint.NonTerminating<O, S, E>>,
    elementF: StructureElement<O, S, SignatureConstraint.NonTerminating<O, S, F>>,
    elementG: StructureElement<O, S, SignatureConstraint.NonTerminating<O, S, G>>,
    elementH: StructureElement<O, S, SignatureConstraint.NonTerminating<O, S, H>>,
    elementI: StructureElement<O, S, SignatureConstraint.Terminating<O, S, I>>,
): StructureElement<O, S, Structure<O, S>> =
    invoke({ a, b, c, d, e, f, g, h, i -> ExecutionResult.success() }, elementA, elementB, elementC, elementD, elementE,
        elementF, elementG, elementH, elementI)

@OverloadResolutionByLambdaReturnType
operator fun <O, S : SenderContext<O>, A : Any, B : Any, C : Any, D : Any, E : Any, F : Any, G : Any, H : Any, I : Any, J : Any>
        StructureScope<O, S>.invoke(
    execute: ExecutionContext<O, S>.(A, B, C, D, E, F, G, H, I, J) -> ExecutionResult,
    elementA: StructureElement<O, S, SignatureConstraint.NonTerminating<O, S, A>>,
    elementB: StructureElement<O, S, SignatureConstraint.NonTerminating<O, S, B>>,
    elementC: StructureElement<O, S, SignatureConstraint.NonTerminating<O, S, C>>,
    elementD: StructureElement<O, S, SignatureConstraint.NonTerminating<O, S, D>>,
    elementE: StructureElement<O, S, SignatureConstraint.NonTerminating<O, S, E>>,
    elementF: StructureElement<O, S, SignatureConstraint.NonTerminating<O, S, F>>,
    elementG: StructureElement<O, S, SignatureConstraint.NonTerminating<O, S, G>>,
    elementH: StructureElement<O, S, SignatureConstraint.NonTerminating<O, S, H>>,
    elementI: StructureElement<O, S, SignatureConstraint.NonTerminating<O, S, I>>,
    elementJ: StructureElement<O, S, SignatureConstraint.Terminating<O, S, J>>,
): StructureElement<O, S, Structure<O, S>> = {
    build(Signature10(execute,
        Tuple10(elementA.invoke(this), elementB.invoke(this), elementC.invoke(this), elementD.invoke(this),
            elementE.invoke(this), elementF.invoke(this), elementG.invoke(this), elementH.invoke(this),
            elementI.invoke(this), elementJ.invoke(this))
    ))
}
@OverloadResolutionByLambdaReturnType
operator fun <O, S : SenderContext<O>, A : Any, B : Any, C : Any, D : Any, E : Any, F : Any, G : Any, H : Any, I : Any, J : Any>
        StructureScope<O, S>.invoke(
    elementA: StructureElement<O, S, SignatureConstraint.NonTerminating<O, S, A>>,
    elementB: StructureElement<O, S, SignatureConstraint.NonTerminating<O, S, B>>,
    elementC: StructureElement<O, S, SignatureConstraint.NonTerminating<O, S, C>>,
    elementD: StructureElement<O, S, SignatureConstraint.NonTerminating<O, S, D>>,
    elementE: StructureElement<O, S, SignatureConstraint.NonTerminating<O, S, E>>,
    elementF: StructureElement<O, S, SignatureConstraint.NonTerminating<O, S, F>>,
    elementG: StructureElement<O, S, SignatureConstraint.NonTerminating<O, S, G>>,
    elementH: StructureElement<O, S, SignatureConstraint.NonTerminating<O, S, H>>,
    elementI: StructureElement<O, S, SignatureConstraint.NonTerminating<O, S, I>>,
    elementJ: StructureElement<O, S, SignatureConstraint.Terminating<O, S, J>>,
): StructureElement<O, S, Structure<O, S>> =
    invoke({ a, b, c, d, e, f, g, h, i, j -> ExecutionResult.success() }, elementA, elementB, elementC, elementD,
        elementE, elementF, elementG, elementH, elementI, elementJ)

@OverloadResolutionByLambdaReturnType
operator fun <O, S : SenderContext<O>, A : Any, B : Any, C : Any, D : Any, E : Any, F : Any, G : Any, H : Any, I : Any, J : Any, K : Any>
        StructureScope<O, S>.invoke(
    execute: ExecutionContext<O, S>.(A, B, C, D, E, F, G, H, I, J, K) -> ExecutionResult,
    elementA: StructureElement<O, S, SignatureConstraint.NonTerminating<O, S, A>>,
    elementB: StructureElement<O, S, SignatureConstraint.NonTerminating<O, S, B>>,
    elementC: StructureElement<O, S, SignatureConstraint.NonTerminating<O, S, C>>,
    elementD: StructureElement<O, S, SignatureConstraint.NonTerminating<O, S, D>>,
    elementE: StructureElement<O, S, SignatureConstraint.NonTerminating<O, S, E>>,
    elementF: StructureElement<O, S, SignatureConstraint.NonTerminating<O, S, F>>,
    elementG: StructureElement<O, S, SignatureConstraint.NonTerminating<O, S, G>>,
    elementH: StructureElement<O, S, SignatureConstraint.NonTerminating<O, S, H>>,
    elementI: StructureElement<O, S, SignatureConstraint.NonTerminating<O, S, I>>,
    elementJ: StructureElement<O, S, SignatureConstraint.NonTerminating<O, S, J>>,
    elementK: StructureElement<O, S, SignatureConstraint.Terminating<O, S, K>>,
): StructureElement<O, S, Structure<O, S>> = {
    build(Signature11(execute,
        Tuple11(elementA.invoke(this), elementB.invoke(this), elementC.invoke(this), elementD.invoke(this),
            elementE.invoke(this), elementF.invoke(this), elementG.invoke(this), elementH.invoke(this),
            elementI.invoke(this), elementJ.invoke(this), elementK.invoke(this))
    ))
}
@OverloadResolutionByLambdaReturnType
operator fun <O, S : SenderContext<O>, A : Any, B : Any, C : Any, D : Any, E : Any, F : Any, G : Any, H : Any, I : Any, J : Any, K : Any>
        StructureScope<O, S>.invoke(
    elementA: StructureElement<O, S, SignatureConstraint.NonTerminating<O, S, A>>,
    elementB: StructureElement<O, S, SignatureConstraint.NonTerminating<O, S, B>>,
    elementC: StructureElement<O, S, SignatureConstraint.NonTerminating<O, S, C>>,
    elementD: StructureElement<O, S, SignatureConstraint.NonTerminating<O, S, D>>,
    elementE: StructureElement<O, S, SignatureConstraint.NonTerminating<O, S, E>>,
    elementF: StructureElement<O, S, SignatureConstraint.NonTerminating<O, S, F>>,
    elementG: StructureElement<O, S, SignatureConstraint.NonTerminating<O, S, G>>,
    elementH: StructureElement<O, S, SignatureConstraint.NonTerminating<O, S, H>>,
    elementI: StructureElement<O, S, SignatureConstraint.NonTerminating<O, S, I>>,
    elementJ: StructureElement<O, S, SignatureConstraint.NonTerminating<O, S, J>>,
    elementK: StructureElement<O, S, SignatureConstraint.Terminating<O, S, K>>,
): StructureElement<O, S, Structure<O, S>> =
    invoke({ a, b, c, d, e, f, g, h, i, j, k -> ExecutionResult.success() }, elementA, elementB, elementC, elementD,
        elementE, elementF, elementG, elementH, elementI, elementJ, elementK)

@OverloadResolutionByLambdaReturnType
operator fun <O, S : SenderContext<O>, A : Any, B : Any, C : Any, D : Any, E : Any, F : Any, G : Any, H : Any, I : Any, J : Any, K : Any,
        L : Any> StructureScope<O, S>.invoke(
    execute: ExecutionContext<O, S>.(A, B, C, D, E, F, G, H, I, J, K, L) -> ExecutionResult,
    elementA: StructureElement<O, S, SignatureConstraint.NonTerminating<O, S, A>>,
    elementB: StructureElement<O, S, SignatureConstraint.NonTerminating<O, S, B>>,
    elementC: StructureElement<O, S, SignatureConstraint.NonTerminating<O, S, C>>,
    elementD: StructureElement<O, S, SignatureConstraint.NonTerminating<O, S, D>>,
    elementE: StructureElement<O, S, SignatureConstraint.NonTerminating<O, S, E>>,
    elementF: StructureElement<O, S, SignatureConstraint.NonTerminating<O, S, F>>,
    elementG: StructureElement<O, S, SignatureConstraint.NonTerminating<O, S, G>>,
    elementH: StructureElement<O, S, SignatureConstraint.NonTerminating<O, S, H>>,
    elementI: StructureElement<O, S, SignatureConstraint.NonTerminating<O, S, I>>,
    elementJ: StructureElement<O, S, SignatureConstraint.NonTerminating<O, S, J>>,
    elementK: StructureElement<O, S, SignatureConstraint.NonTerminating<O, S, K>>,
    elementL: StructureElement<O, S, SignatureConstraint.Terminating<O, S, L>>,
): StructureElement<O, S, Structure<O, S>> = {
    build(Signature12(execute,
        Tuple12(elementA.invoke(this), elementB.invoke(this), elementC.invoke(this), elementD.invoke(this),
            elementE.invoke(this), elementF.invoke(this), elementG.invoke(this), elementH.invoke(this),
            elementI.invoke(this), elementJ.invoke(this), elementK.invoke(this), elementL.invoke(this))
    ))
}
@OverloadResolutionByLambdaReturnType
operator fun <O, S : SenderContext<O>, A : Any, B : Any, C : Any, D : Any, E : Any, F : Any, G : Any, H : Any, I : Any, J : Any, K : Any,
        L : Any> StructureScope<O, S>.invoke(
    elementA: StructureElement<O, S, SignatureConstraint.NonTerminating<O, S, A>>,
    elementB: StructureElement<O, S, SignatureConstraint.NonTerminating<O, S, B>>,
    elementC: StructureElement<O, S, SignatureConstraint.NonTerminating<O, S, C>>,
    elementD: StructureElement<O, S, SignatureConstraint.NonTerminating<O, S, D>>,
    elementE: StructureElement<O, S, SignatureConstraint.NonTerminating<O, S, E>>,
    elementF: StructureElement<O, S, SignatureConstraint.NonTerminating<O, S, F>>,
    elementG: StructureElement<O, S, SignatureConstraint.NonTerminating<O, S, G>>,
    elementH: StructureElement<O, S, SignatureConstraint.NonTerminating<O, S, H>>,
    elementI: StructureElement<O, S, SignatureConstraint.NonTerminating<O, S, I>>,
    elementJ: StructureElement<O, S, SignatureConstraint.NonTerminating<O, S, J>>,
    elementK: StructureElement<O, S, SignatureConstraint.NonTerminating<O, S, K>>,
    elementL: StructureElement<O, S, SignatureConstraint.Terminating<O, S, L>>,
): StructureElement<O, S, Structure<O, S>> =
    invoke({ a, b, c, d, e, f, g, h, i, j, k, l -> ExecutionResult.success() }, elementA, elementB, elementC, elementD,
        elementE, elementF, elementG, elementH, elementI, elementJ, elementK, elementL)
