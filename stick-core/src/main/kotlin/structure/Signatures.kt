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
operator fun <S : SenderContext> StructureScope<S>.invoke(
    execute: ExecutionContext<S>.() -> ExecutionResult,
): Structure<S> =
    build(Signature0(execute, Tuple0()))

@OverloadResolutionByLambdaReturnType
operator fun <S : SenderContext, A : Any> StructureScope<S>.invoke(
    execute: ExecutionContext<S>.(A) -> ExecutionResult,
    element: StructureElement<S, SignatureConstraint.Terminating<S, A>>,
): StructureElement<S, Structure<S>> = {
    build(Signature1(execute, Tuple1(element.invoke(this))))
}
@OverloadResolutionByLambdaReturnType
operator fun <S : SenderContext, A : Any> StructureScope<S>.invoke(
    element: StructureElement<S, SignatureConstraint.Terminating<S, A>>,
): StructureElement<S, Structure<S>> =
    invoke({ a: A -> ExecutionResult.success() }, element)

@OverloadResolutionByLambdaReturnType
operator fun <S : SenderContext, A : Any, B : Any> StructureScope<S>.invoke(
    execute: ExecutionContext<S>.(A, B) -> ExecutionResult,
    elementA: StructureElement<S, SignatureConstraint.NonTerminating<S, A>>,
    elementB: StructureElement<S, SignatureConstraint.Terminating<S, B>>,
): StructureElement<S, Structure<S>> = {
    build(Signature2(execute, Tuple2(elementA.invoke(this), elementB.invoke(this))))
}
@OverloadResolutionByLambdaReturnType
operator fun <S : SenderContext, A : Any, B : Any> StructureScope<S>.invoke(
    elementA: StructureElement<S, SignatureConstraint.NonTerminating<S, A>>,
    elementB: StructureElement<S, SignatureConstraint.Terminating<S, B>>,
): StructureElement<S, Structure<S>> =
    invoke({ a, b -> ExecutionResult.success() }, elementA, elementB)

@OverloadResolutionByLambdaReturnType
operator fun <S : SenderContext, A : Any, B : Any, C : Any> StructureScope<S>.invoke(
    execute: ExecutionContext<S>.(A, B, C) -> ExecutionResult,
    elementA: StructureElement<S, SignatureConstraint.NonTerminating<S, A>>,
    elementB: StructureElement<S, SignatureConstraint.NonTerminating<S, B>>,
    elementC: StructureElement<S, SignatureConstraint.Terminating<S, C>>,
): StructureElement<S, Structure<S>> = {
    build(Signature3(execute,
        Tuple3(elementA.invoke(this), elementB.invoke(this), elementC.invoke(this))
    ))
}
@OverloadResolutionByLambdaReturnType
operator fun <S : SenderContext, A : Any, B : Any, C : Any> StructureScope<S>.invoke(
    elementA: StructureElement<S, SignatureConstraint.NonTerminating<S, A>>,
    elementB: StructureElement<S, SignatureConstraint.NonTerminating<S, B>>,
    elementC: StructureElement<S, SignatureConstraint.Terminating<S, C>>,
): StructureElement<S, Structure<S>> =
    invoke({ a, b, c -> ExecutionResult.success() }, elementA, elementB, elementC)

@OverloadResolutionByLambdaReturnType
operator fun <S : SenderContext, A : Any, B : Any, C : Any, D : Any> StructureScope<S>.invoke(
    execute: ExecutionContext<S>.(A, B, C, D) -> ExecutionResult,
    elementA: StructureElement<S, SignatureConstraint.NonTerminating<S, A>>,
    elementB: StructureElement<S, SignatureConstraint.NonTerminating<S, B>>,
    elementC: StructureElement<S, SignatureConstraint.NonTerminating<S, C>>,
    elementD: StructureElement<S, SignatureConstraint.Terminating<S, D>>,
): StructureElement<S, Structure<S>> = {
    build(Signature4(execute,
        Tuple4(elementA.invoke(this), elementB.invoke(this), elementC.invoke(this), elementD.invoke(this))
    ))
}
@OverloadResolutionByLambdaReturnType
operator fun <S : SenderContext, A : Any, B : Any, C : Any, D : Any> StructureScope<S>.invoke(
    elementA: StructureElement<S, SignatureConstraint.NonTerminating<S, A>>,
    elementB: StructureElement<S, SignatureConstraint.NonTerminating<S, B>>,
    elementC: StructureElement<S, SignatureConstraint.NonTerminating<S, C>>,
    elementD: StructureElement<S, SignatureConstraint.Terminating<S, D>>,
): StructureElement<S, Structure<S>> =
    invoke({ a, b, c, d -> ExecutionResult.success() }, elementA, elementB, elementC, elementD)

@OverloadResolutionByLambdaReturnType
operator fun <S : SenderContext, A : Any, B : Any, C : Any, D : Any, E : Any> StructureScope<S>.invoke(
    execute: ExecutionContext<S>.(A, B, C, D, E) -> ExecutionResult,
    elementA: StructureElement<S, SignatureConstraint.NonTerminating<S, A>>,
    elementB: StructureElement<S, SignatureConstraint.NonTerminating<S, B>>,
    elementC: StructureElement<S, SignatureConstraint.NonTerminating<S, C>>,
    elementD: StructureElement<S, SignatureConstraint.NonTerminating<S, D>>,
    elementE: StructureElement<S, SignatureConstraint.Terminating<S, E>>,
): StructureElement<S, Structure<S>> = {
    build(Signature5(execute,
        Tuple5(elementA.invoke(this), elementB.invoke(this), elementC.invoke(this), elementD.invoke(this),
            elementE.invoke(this))
    ))
}
@OverloadResolutionByLambdaReturnType
operator fun <S : SenderContext, A : Any, B : Any, C : Any, D : Any, E : Any> StructureScope<S>.invoke(
    elementA: StructureElement<S, SignatureConstraint.NonTerminating<S, A>>,
    elementB: StructureElement<S, SignatureConstraint.NonTerminating<S, B>>,
    elementC: StructureElement<S, SignatureConstraint.NonTerminating<S, C>>,
    elementD: StructureElement<S, SignatureConstraint.NonTerminating<S, D>>,
    elementE: StructureElement<S, SignatureConstraint.Terminating<S, E>>,
): StructureElement<S, Structure<S>> =
    invoke({ a, b, c, d, e -> ExecutionResult.success() }, elementA, elementB, elementC, elementD, elementE)

@OverloadResolutionByLambdaReturnType
operator fun <S : SenderContext, A : Any, B : Any, C : Any, D : Any, E : Any, F : Any> StructureScope<S>.invoke(
    execute: ExecutionContext<S>.(A, B, C, D, E, F) -> ExecutionResult,
    elementA: StructureElement<S, SignatureConstraint.NonTerminating<S, A>>,
    elementB: StructureElement<S, SignatureConstraint.NonTerminating<S, B>>,
    elementC: StructureElement<S, SignatureConstraint.NonTerminating<S, C>>,
    elementD: StructureElement<S, SignatureConstraint.NonTerminating<S, D>>,
    elementE: StructureElement<S, SignatureConstraint.NonTerminating<S, E>>,
    elementF: StructureElement<S, SignatureConstraint.Terminating<S, F>>,
): StructureElement<S, Structure<S>> = {
    build(Signature6(execute,
        Tuple6(elementA.invoke(this), elementB.invoke(this), elementC.invoke(this), elementD.invoke(this),
            elementE.invoke(this), elementF.invoke(this))
    ))
}
@OverloadResolutionByLambdaReturnType
operator fun <S : SenderContext, A : Any, B : Any, C : Any, D : Any, E : Any, F : Any> StructureScope<S>.invoke(
    elementA: StructureElement<S, SignatureConstraint.NonTerminating<S, A>>,
    elementB: StructureElement<S, SignatureConstraint.NonTerminating<S, B>>,
    elementC: StructureElement<S, SignatureConstraint.NonTerminating<S, C>>,
    elementD: StructureElement<S, SignatureConstraint.NonTerminating<S, D>>,
    elementE: StructureElement<S, SignatureConstraint.NonTerminating<S, E>>,
    elementF: StructureElement<S, SignatureConstraint.Terminating<S, F>>,
): StructureElement<S, Structure<S>> =
    invoke({ a, b, c, d, e, f -> ExecutionResult.success() }, elementA, elementB, elementC, elementD, elementE,
        elementF)

@OverloadResolutionByLambdaReturnType
operator fun <S : SenderContext, A : Any, B : Any, C : Any, D : Any, E : Any, F : Any, G : Any> StructureScope<S>.invoke(
    execute: ExecutionContext<S>.(A, B, C, D, E, F, G) -> ExecutionResult,
    elementA: StructureElement<S, SignatureConstraint.NonTerminating<S, A>>,
    elementB: StructureElement<S, SignatureConstraint.NonTerminating<S, B>>,
    elementC: StructureElement<S, SignatureConstraint.NonTerminating<S, C>>,
    elementD: StructureElement<S, SignatureConstraint.NonTerminating<S, D>>,
    elementE: StructureElement<S, SignatureConstraint.NonTerminating<S, E>>,
    elementF: StructureElement<S, SignatureConstraint.NonTerminating<S, F>>,
    elementG: StructureElement<S, SignatureConstraint.Terminating<S, G>>,
): StructureElement<S, Structure<S>> = {
    build(Signature7(execute,
        Tuple7(elementA.invoke(this), elementB.invoke(this), elementC.invoke(this), elementD.invoke(this),
            elementE.invoke(this), elementF.invoke(this), elementG.invoke(this))
    ))
}
@OverloadResolutionByLambdaReturnType
operator fun <S : SenderContext, A : Any, B : Any, C : Any, D : Any, E : Any, F : Any, G : Any> StructureScope<S>.invoke(
    elementA: StructureElement<S, SignatureConstraint.NonTerminating<S, A>>,
    elementB: StructureElement<S, SignatureConstraint.NonTerminating<S, B>>,
    elementC: StructureElement<S, SignatureConstraint.NonTerminating<S, C>>,
    elementD: StructureElement<S, SignatureConstraint.NonTerminating<S, D>>,
    elementE: StructureElement<S, SignatureConstraint.NonTerminating<S, E>>,
    elementF: StructureElement<S, SignatureConstraint.NonTerminating<S, F>>,
    elementG: StructureElement<S, SignatureConstraint.Terminating<S, G>>,
): StructureElement<S, Structure<S>> =
    invoke({ a, b, c, d, e, f, g -> ExecutionResult.success() }, elementA, elementB, elementC, elementD, elementE,
        elementF, elementG)

@OverloadResolutionByLambdaReturnType
operator fun <S : SenderContext, A : Any, B : Any, C : Any, D : Any, E : Any, F : Any, G : Any, H : Any> StructureScope<S>.invoke(
    execute: ExecutionContext<S>.(A, B, C, D, E, F, G, H) -> ExecutionResult,
    elementA: StructureElement<S, SignatureConstraint.NonTerminating<S, A>>,
    elementB: StructureElement<S, SignatureConstraint.NonTerminating<S, B>>,
    elementC: StructureElement<S, SignatureConstraint.NonTerminating<S, C>>,
    elementD: StructureElement<S, SignatureConstraint.NonTerminating<S, D>>,
    elementE: StructureElement<S, SignatureConstraint.NonTerminating<S, E>>,
    elementF: StructureElement<S, SignatureConstraint.NonTerminating<S, F>>,
    elementG: StructureElement<S, SignatureConstraint.NonTerminating<S, G>>,
    elementH: StructureElement<S, SignatureConstraint.Terminating<S, H>>,
): StructureElement<S, Structure<S>> = {
    build(Signature8(execute,
        Tuple8(elementA.invoke(this), elementB.invoke(this), elementC.invoke(this), elementD.invoke(this),
            elementE.invoke(this), elementF.invoke(this), elementG.invoke(this), elementH.invoke(this))
    ))
}
@OverloadResolutionByLambdaReturnType
operator fun <S : SenderContext, A : Any, B : Any, C : Any, D : Any, E : Any, F : Any, G : Any, H : Any> StructureScope<S>.invoke(
    elementA: StructureElement<S, SignatureConstraint.NonTerminating<S, A>>,
    elementB: StructureElement<S, SignatureConstraint.NonTerminating<S, B>>,
    elementC: StructureElement<S, SignatureConstraint.NonTerminating<S, C>>,
    elementD: StructureElement<S, SignatureConstraint.NonTerminating<S, D>>,
    elementE: StructureElement<S, SignatureConstraint.NonTerminating<S, E>>,
    elementF: StructureElement<S, SignatureConstraint.NonTerminating<S, F>>,
    elementG: StructureElement<S, SignatureConstraint.NonTerminating<S, G>>,
    elementH: StructureElement<S, SignatureConstraint.Terminating<S, H>>,
): StructureElement<S, Structure<S>> =
    invoke({ a, b, c, d, e, f, g, h -> ExecutionResult.success() }, elementA, elementB, elementC, elementD, elementE,
        elementF, elementG, elementH)

@OverloadResolutionByLambdaReturnType
operator fun <S : SenderContext, A : Any, B : Any, C : Any, D : Any, E : Any, F : Any, G : Any, H : Any, I : Any>
        StructureScope<S>.invoke(
    execute: ExecutionContext<S>.(A, B, C, D, E, F, G, H, I) -> ExecutionResult,
    elementA: StructureElement<S, SignatureConstraint.NonTerminating<S, A>>,
    elementB: StructureElement<S, SignatureConstraint.NonTerminating<S, B>>,
    elementC: StructureElement<S, SignatureConstraint.NonTerminating<S, C>>,
    elementD: StructureElement<S, SignatureConstraint.NonTerminating<S, D>>,
    elementE: StructureElement<S, SignatureConstraint.NonTerminating<S, E>>,
    elementF: StructureElement<S, SignatureConstraint.NonTerminating<S, F>>,
    elementG: StructureElement<S, SignatureConstraint.NonTerminating<S, G>>,
    elementH: StructureElement<S, SignatureConstraint.NonTerminating<S, H>>,
    elementI: StructureElement<S, SignatureConstraint.Terminating<S, I>>,
): StructureElement<S, Structure<S>> = {
    build(Signature9(execute,
        Tuple9(elementA.invoke(this), elementB.invoke(this), elementC.invoke(this), elementD.invoke(this),
            elementE.invoke(this), elementF.invoke(this), elementG.invoke(this), elementH.invoke(this),
            elementI.invoke(this))
    ))
}
@OverloadResolutionByLambdaReturnType
operator fun <S : SenderContext, A : Any, B : Any, C : Any, D : Any, E : Any, F : Any, G : Any, H : Any, I : Any>
        StructureScope<S>.invoke(
    elementA: StructureElement<S, SignatureConstraint.NonTerminating<S, A>>,
    elementB: StructureElement<S, SignatureConstraint.NonTerminating<S, B>>,
    elementC: StructureElement<S, SignatureConstraint.NonTerminating<S, C>>,
    elementD: StructureElement<S, SignatureConstraint.NonTerminating<S, D>>,
    elementE: StructureElement<S, SignatureConstraint.NonTerminating<S, E>>,
    elementF: StructureElement<S, SignatureConstraint.NonTerminating<S, F>>,
    elementG: StructureElement<S, SignatureConstraint.NonTerminating<S, G>>,
    elementH: StructureElement<S, SignatureConstraint.NonTerminating<S, H>>,
    elementI: StructureElement<S, SignatureConstraint.Terminating<S, I>>,
): StructureElement<S, Structure<S>> =
    invoke({ a, b, c, d, e, f, g, h, i -> ExecutionResult.success() }, elementA, elementB, elementC, elementD, elementE,
        elementF, elementG, elementH, elementI)

@OverloadResolutionByLambdaReturnType
operator fun <S : SenderContext, A : Any, B : Any, C : Any, D : Any, E : Any, F : Any, G : Any, H : Any, I : Any, J : Any>
        StructureScope<S>.invoke(
    execute: ExecutionContext<S>.(A, B, C, D, E, F, G, H, I, J) -> ExecutionResult,
    elementA: StructureElement<S, SignatureConstraint.NonTerminating<S, A>>,
    elementB: StructureElement<S, SignatureConstraint.NonTerminating<S, B>>,
    elementC: StructureElement<S, SignatureConstraint.NonTerminating<S, C>>,
    elementD: StructureElement<S, SignatureConstraint.NonTerminating<S, D>>,
    elementE: StructureElement<S, SignatureConstraint.NonTerminating<S, E>>,
    elementF: StructureElement<S, SignatureConstraint.NonTerminating<S, F>>,
    elementG: StructureElement<S, SignatureConstraint.NonTerminating<S, G>>,
    elementH: StructureElement<S, SignatureConstraint.NonTerminating<S, H>>,
    elementI: StructureElement<S, SignatureConstraint.NonTerminating<S, I>>,
    elementJ: StructureElement<S, SignatureConstraint.Terminating<S, J>>,
): StructureElement<S, Structure<S>> = {
    build(Signature10(execute,
        Tuple10(elementA.invoke(this), elementB.invoke(this), elementC.invoke(this), elementD.invoke(this),
            elementE.invoke(this), elementF.invoke(this), elementG.invoke(this), elementH.invoke(this),
            elementI.invoke(this), elementJ.invoke(this))
    ))
}
@OverloadResolutionByLambdaReturnType
operator fun <S : SenderContext, A : Any, B : Any, C : Any, D : Any, E : Any, F : Any, G : Any, H : Any, I : Any, J : Any>
        StructureScope<S>.invoke(
    elementA: StructureElement<S, SignatureConstraint.NonTerminating<S, A>>,
    elementB: StructureElement<S, SignatureConstraint.NonTerminating<S, B>>,
    elementC: StructureElement<S, SignatureConstraint.NonTerminating<S, C>>,
    elementD: StructureElement<S, SignatureConstraint.NonTerminating<S, D>>,
    elementE: StructureElement<S, SignatureConstraint.NonTerminating<S, E>>,
    elementF: StructureElement<S, SignatureConstraint.NonTerminating<S, F>>,
    elementG: StructureElement<S, SignatureConstraint.NonTerminating<S, G>>,
    elementH: StructureElement<S, SignatureConstraint.NonTerminating<S, H>>,
    elementI: StructureElement<S, SignatureConstraint.NonTerminating<S, I>>,
    elementJ: StructureElement<S, SignatureConstraint.Terminating<S, J>>,
): StructureElement<S, Structure<S>> =
    invoke({ a, b, c, d, e, f, g, h, i, j -> ExecutionResult.success() }, elementA, elementB, elementC, elementD,
        elementE, elementF, elementG, elementH, elementI, elementJ)

@OverloadResolutionByLambdaReturnType
operator fun <S : SenderContext, A : Any, B : Any, C : Any, D : Any, E : Any, F : Any, G : Any, H : Any, I : Any, J : Any, K : Any>
        StructureScope<S>.invoke(
    execute: ExecutionContext<S>.(A, B, C, D, E, F, G, H, I, J, K) -> ExecutionResult,
    elementA: StructureElement<S, SignatureConstraint.NonTerminating<S, A>>,
    elementB: StructureElement<S, SignatureConstraint.NonTerminating<S, B>>,
    elementC: StructureElement<S, SignatureConstraint.NonTerminating<S, C>>,
    elementD: StructureElement<S, SignatureConstraint.NonTerminating<S, D>>,
    elementE: StructureElement<S, SignatureConstraint.NonTerminating<S, E>>,
    elementF: StructureElement<S, SignatureConstraint.NonTerminating<S, F>>,
    elementG: StructureElement<S, SignatureConstraint.NonTerminating<S, G>>,
    elementH: StructureElement<S, SignatureConstraint.NonTerminating<S, H>>,
    elementI: StructureElement<S, SignatureConstraint.NonTerminating<S, I>>,
    elementJ: StructureElement<S, SignatureConstraint.NonTerminating<S, J>>,
    elementK: StructureElement<S, SignatureConstraint.Terminating<S, K>>,
): StructureElement<S, Structure<S>> = {
    build(Signature11(execute,
        Tuple11(elementA.invoke(this), elementB.invoke(this), elementC.invoke(this), elementD.invoke(this),
            elementE.invoke(this), elementF.invoke(this), elementG.invoke(this), elementH.invoke(this),
            elementI.invoke(this), elementJ.invoke(this), elementK.invoke(this))
    ))
}
@OverloadResolutionByLambdaReturnType
operator fun <S : SenderContext, A : Any, B : Any, C : Any, D : Any, E : Any, F : Any, G : Any, H : Any, I : Any, J : Any, K : Any>
        StructureScope<S>.invoke(
    elementA: StructureElement<S, SignatureConstraint.NonTerminating<S, A>>,
    elementB: StructureElement<S, SignatureConstraint.NonTerminating<S, B>>,
    elementC: StructureElement<S, SignatureConstraint.NonTerminating<S, C>>,
    elementD: StructureElement<S, SignatureConstraint.NonTerminating<S, D>>,
    elementE: StructureElement<S, SignatureConstraint.NonTerminating<S, E>>,
    elementF: StructureElement<S, SignatureConstraint.NonTerminating<S, F>>,
    elementG: StructureElement<S, SignatureConstraint.NonTerminating<S, G>>,
    elementH: StructureElement<S, SignatureConstraint.NonTerminating<S, H>>,
    elementI: StructureElement<S, SignatureConstraint.NonTerminating<S, I>>,
    elementJ: StructureElement<S, SignatureConstraint.NonTerminating<S, J>>,
    elementK: StructureElement<S, SignatureConstraint.Terminating<S, K>>,
): StructureElement<S, Structure<S>> =
    invoke({ a, b, c, d, e, f, g, h, i, j, k -> ExecutionResult.success() }, elementA, elementB, elementC, elementD,
        elementE, elementF, elementG, elementH, elementI, elementJ, elementK)

@OverloadResolutionByLambdaReturnType
operator fun <S : SenderContext, A : Any, B : Any, C : Any, D : Any, E : Any, F : Any, G : Any, H : Any, I : Any, J : Any, K : Any,
        L : Any> StructureScope<S>.invoke(
    execute: ExecutionContext<S>.(A, B, C, D, E, F, G, H, I, J, K, L) -> ExecutionResult,
    elementA: StructureElement<S, SignatureConstraint.NonTerminating<S, A>>,
    elementB: StructureElement<S, SignatureConstraint.NonTerminating<S, B>>,
    elementC: StructureElement<S, SignatureConstraint.NonTerminating<S, C>>,
    elementD: StructureElement<S, SignatureConstraint.NonTerminating<S, D>>,
    elementE: StructureElement<S, SignatureConstraint.NonTerminating<S, E>>,
    elementF: StructureElement<S, SignatureConstraint.NonTerminating<S, F>>,
    elementG: StructureElement<S, SignatureConstraint.NonTerminating<S, G>>,
    elementH: StructureElement<S, SignatureConstraint.NonTerminating<S, H>>,
    elementI: StructureElement<S, SignatureConstraint.NonTerminating<S, I>>,
    elementJ: StructureElement<S, SignatureConstraint.NonTerminating<S, J>>,
    elementK: StructureElement<S, SignatureConstraint.NonTerminating<S, K>>,
    elementL: StructureElement<S, SignatureConstraint.Terminating<S, L>>,
): StructureElement<S, Structure<S>> = {
    build(Signature12(execute,
        Tuple12(elementA.invoke(this), elementB.invoke(this), elementC.invoke(this), elementD.invoke(this),
            elementE.invoke(this), elementF.invoke(this), elementG.invoke(this), elementH.invoke(this),
            elementI.invoke(this), elementJ.invoke(this), elementK.invoke(this), elementL.invoke(this))
    ))
}
@OverloadResolutionByLambdaReturnType
operator fun <S : SenderContext, A : Any, B : Any, C : Any, D : Any, E : Any, F : Any, G : Any, H : Any, I : Any, J : Any, K : Any,
        L : Any> StructureScope<S>.invoke(
    elementA: StructureElement<S, SignatureConstraint.NonTerminating<S, A>>,
    elementB: StructureElement<S, SignatureConstraint.NonTerminating<S, B>>,
    elementC: StructureElement<S, SignatureConstraint.NonTerminating<S, C>>,
    elementD: StructureElement<S, SignatureConstraint.NonTerminating<S, D>>,
    elementE: StructureElement<S, SignatureConstraint.NonTerminating<S, E>>,
    elementF: StructureElement<S, SignatureConstraint.NonTerminating<S, F>>,
    elementG: StructureElement<S, SignatureConstraint.NonTerminating<S, G>>,
    elementH: StructureElement<S, SignatureConstraint.NonTerminating<S, H>>,
    elementI: StructureElement<S, SignatureConstraint.NonTerminating<S, I>>,
    elementJ: StructureElement<S, SignatureConstraint.NonTerminating<S, J>>,
    elementK: StructureElement<S, SignatureConstraint.NonTerminating<S, K>>,
    elementL: StructureElement<S, SignatureConstraint.Terminating<S, L>>,
): StructureElement<S, Structure<S>> =
    invoke({ a, b, c, d, e, f, g, h, i, j, k, l -> ExecutionResult.success() }, elementA, elementB, elementC, elementD,
        elementE, elementF, elementG, elementH, elementI, elementJ, elementK, elementL)
