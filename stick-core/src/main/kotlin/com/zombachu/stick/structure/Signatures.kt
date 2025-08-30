package com.zombachu.stick.structure

import com.zombachu.stick.Environment
import com.zombachu.stick.ExecutionResult
import com.zombachu.stick.Invocation
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

operator fun <E_ : Environment, S> StructureScope<E_, S>.invoke(
    execute: Invocation<E_, S>.() -> ExecutionResult,
): StructureElement<E_, S, Structure<E_, S>> = {
    build(Signature0(execute, Tuple0()))
}

operator fun <E_ : Environment, S, A : Any> StructureScope<E_, S>.invoke(
    element: StructureElement<E_, S, SignatureConstraint.Terminating<E_, S, A>>,
    execute: Invocation<E_, S>.(A) -> ExecutionResult = { ExecutionResult.success() },
): StructureElement<E_, S, Structure<E_, S>> = {
    build(Signature1(execute, Tuple1(element.invoke(this))))
}

operator fun <E_ : Environment, S, A : Any, B : Any> StructureScope<E_, S>.invoke(
    elementA: StructureElement<E_, S, SignatureConstraint.NonTerminating<E_, S, A>>,
    elementB: StructureElement<E_, S, SignatureConstraint.Terminating<E_, S, B>>,
    execute: Invocation<E_, S>.(A, B) -> ExecutionResult = { a, b -> ExecutionResult.success() },
): StructureElement<E_, S, Structure<E_, S>> = {
    build(Signature2(execute, Tuple2(elementA.invoke(this), elementB.invoke(this))))
}

operator fun <E_ : Environment, S, A : Any, B : Any, C : Any> StructureScope<E_, S>.invoke(
    elementA: StructureElement<E_, S, SignatureConstraint.NonTerminating<E_, S, A>>,
    elementB: StructureElement<E_, S, SignatureConstraint.NonTerminating<E_, S, B>>,
    elementC: StructureElement<E_, S, SignatureConstraint.Terminating<E_, S, C>>,
    execute: Invocation<E_, S>.(A, B, C) -> ExecutionResult = { a, b, c -> ExecutionResult.success() },
): StructureElement<E_, S, Structure<E_, S>> = {
    build(Signature3(execute,
        Tuple3(elementA.invoke(this), elementB.invoke(this), elementC.invoke(this))
    ))
}

operator fun <E_ : Environment, S, A : Any, B : Any, C : Any, D : Any> StructureScope<E_, S>.invoke(
    elementA: StructureElement<E_, S, SignatureConstraint.NonTerminating<E_, S, A>>,
    elementB: StructureElement<E_, S, SignatureConstraint.NonTerminating<E_, S, B>>,
    elementC: StructureElement<E_, S, SignatureConstraint.NonTerminating<E_, S, C>>,
    elementD: StructureElement<E_, S, SignatureConstraint.Terminating<E_, S, D>>,
    execute: Invocation<E_, S>.(A, B, C, D) -> ExecutionResult = { a, b, c, d -> ExecutionResult.success() },
): StructureElement<E_, S, Structure<E_, S>> = {
    build(Signature4(execute,
        Tuple4(elementA.invoke(this), elementB.invoke(this), elementC.invoke(this), elementD.invoke(this))
    ))
}

operator fun <E_ : Environment, S, A : Any, B : Any, C : Any, D : Any, E : Any> StructureScope<E_, S>.invoke(
    elementA: StructureElement<E_, S, SignatureConstraint.NonTerminating<E_, S, A>>,
    elementB: StructureElement<E_, S, SignatureConstraint.NonTerminating<E_, S, B>>,
    elementC: StructureElement<E_, S, SignatureConstraint.NonTerminating<E_, S, C>>,
    elementD: StructureElement<E_, S, SignatureConstraint.NonTerminating<E_, S, D>>,
    elementE: StructureElement<E_, S, SignatureConstraint.Terminating<E_, S, E>>,
    execute: Invocation<E_, S>.(A, B, C, D, E) -> ExecutionResult = { a, b, c, d, e -> ExecutionResult.success() },
): StructureElement<E_, S, Structure<E_, S>> = {
    build(Signature5(execute,
        Tuple5(elementA.invoke(this), elementB.invoke(this), elementC.invoke(this), elementD.invoke(this),
            elementE.invoke(this))
    ))
}

operator fun <E_ : Environment, S, A : Any, B : Any, C : Any, D : Any, E : Any, F : Any>
        StructureScope<E_, S>.invoke(
    elementA: StructureElement<E_, S, SignatureConstraint.NonTerminating<E_, S, A>>,
    elementB: StructureElement<E_, S, SignatureConstraint.NonTerminating<E_, S, B>>,
    elementC: StructureElement<E_, S, SignatureConstraint.NonTerminating<E_, S, C>>,
    elementD: StructureElement<E_, S, SignatureConstraint.NonTerminating<E_, S, D>>,
    elementE: StructureElement<E_, S, SignatureConstraint.NonTerminating<E_, S, E>>,
    elementF: StructureElement<E_, S, SignatureConstraint.Terminating<E_, S, F>>,
    execute: Invocation<E_, S>.(A, B, C, D, E, F) -> ExecutionResult = { a, b, c, d, e, f -> ExecutionResult.success() },
): StructureElement<E_, S, Structure<E_, S>> = {
    build(Signature6(execute,
        Tuple6(elementA.invoke(this), elementB.invoke(this), elementC.invoke(this), elementD.invoke(this),
            elementE.invoke(this), elementF.invoke(this))
    ))
}

operator fun <E_ : Environment, S, A : Any, B : Any, C : Any, D : Any, E : Any, F : Any, G : Any>
        StructureScope<E_, S>.invoke(
    elementA: StructureElement<E_, S, SignatureConstraint.NonTerminating<E_, S, A>>,
    elementB: StructureElement<E_, S, SignatureConstraint.NonTerminating<E_, S, B>>,
    elementC: StructureElement<E_, S, SignatureConstraint.NonTerminating<E_, S, C>>,
    elementD: StructureElement<E_, S, SignatureConstraint.NonTerminating<E_, S, D>>,
    elementE: StructureElement<E_, S, SignatureConstraint.NonTerminating<E_, S, E>>,
    elementF: StructureElement<E_, S, SignatureConstraint.NonTerminating<E_, S, F>>,
    elementG: StructureElement<E_, S, SignatureConstraint.Terminating<E_, S, G>>,
    execute: Invocation<E_, S>.(A, B, C, D, E, F, G) -> ExecutionResult = { a, b, c, d, e, f, g -> ExecutionResult.success() },
): StructureElement<E_, S, Structure<E_, S>> = {
    build(Signature7(execute,
        Tuple7(elementA.invoke(this), elementB.invoke(this), elementC.invoke(this), elementD.invoke(this),
            elementE.invoke(this), elementF.invoke(this), elementG.invoke(this))
    ))
}

operator fun <E_ : Environment, S, A : Any, B : Any, C : Any, D : Any, E : Any, F : Any, G : Any, H : Any>
        StructureScope<E_, S>.invoke(
    elementA: StructureElement<E_, S, SignatureConstraint.NonTerminating<E_, S, A>>,
    elementB: StructureElement<E_, S, SignatureConstraint.NonTerminating<E_, S, B>>,
    elementC: StructureElement<E_, S, SignatureConstraint.NonTerminating<E_, S, C>>,
    elementD: StructureElement<E_, S, SignatureConstraint.NonTerminating<E_, S, D>>,
    elementE: StructureElement<E_, S, SignatureConstraint.NonTerminating<E_, S, E>>,
    elementF: StructureElement<E_, S, SignatureConstraint.NonTerminating<E_, S, F>>,
    elementG: StructureElement<E_, S, SignatureConstraint.NonTerminating<E_, S, G>>,
    elementH: StructureElement<E_, S, SignatureConstraint.Terminating<E_, S, H>>,
    execute: Invocation<E_, S>.(A, B, C, D, E, F, G, H) -> ExecutionResult = { a, b, c, d, e, f, g, h -> ExecutionResult.success() },
): StructureElement<E_, S, Structure<E_, S>> = {
    build(Signature8(execute,
        Tuple8(elementA.invoke(this), elementB.invoke(this), elementC.invoke(this), elementD.invoke(this),
            elementE.invoke(this), elementF.invoke(this), elementG.invoke(this), elementH.invoke(this))
    ))
}

operator fun <E_ : Environment, S, A : Any, B : Any, C : Any, D : Any, E : Any, F : Any, G : Any, H : Any, I : Any>
        StructureScope<E_, S>.invoke(
    elementA: StructureElement<E_, S, SignatureConstraint.NonTerminating<E_, S, A>>,
    elementB: StructureElement<E_, S, SignatureConstraint.NonTerminating<E_, S, B>>,
    elementC: StructureElement<E_, S, SignatureConstraint.NonTerminating<E_, S, C>>,
    elementD: StructureElement<E_, S, SignatureConstraint.NonTerminating<E_, S, D>>,
    elementE: StructureElement<E_, S, SignatureConstraint.NonTerminating<E_, S, E>>,
    elementF: StructureElement<E_, S, SignatureConstraint.NonTerminating<E_, S, F>>,
    elementG: StructureElement<E_, S, SignatureConstraint.NonTerminating<E_, S, G>>,
    elementH: StructureElement<E_, S, SignatureConstraint.NonTerminating<E_, S, H>>,
    elementI: StructureElement<E_, S, SignatureConstraint.Terminating<E_, S, I>>,
    execute: Invocation<E_, S>.(A, B, C, D, E, F, G, H, I) -> ExecutionResult = { a, b, c, d, e, f, g, h, i -> ExecutionResult.success() },
): StructureElement<E_, S, Structure<E_, S>> = {
    build(Signature9(execute,
        Tuple9(elementA.invoke(this), elementB.invoke(this), elementC.invoke(this), elementD.invoke(this),
            elementE.invoke(this), elementF.invoke(this), elementG.invoke(this), elementH.invoke(this),
            elementI.invoke(this))
    ))
}

operator fun <E_ : Environment, S, A : Any, B : Any, C : Any, D : Any, E : Any, F : Any, G : Any, H : Any, I : Any,
        J : Any> StructureScope<E_, S>.invoke(
    elementA: StructureElement<E_, S, SignatureConstraint.NonTerminating<E_, S, A>>,
    elementB: StructureElement<E_, S, SignatureConstraint.NonTerminating<E_, S, B>>,
    elementC: StructureElement<E_, S, SignatureConstraint.NonTerminating<E_, S, C>>,
    elementD: StructureElement<E_, S, SignatureConstraint.NonTerminating<E_, S, D>>,
    elementE: StructureElement<E_, S, SignatureConstraint.NonTerminating<E_, S, E>>,
    elementF: StructureElement<E_, S, SignatureConstraint.NonTerminating<E_, S, F>>,
    elementG: StructureElement<E_, S, SignatureConstraint.NonTerminating<E_, S, G>>,
    elementH: StructureElement<E_, S, SignatureConstraint.NonTerminating<E_, S, H>>,
    elementI: StructureElement<E_, S, SignatureConstraint.NonTerminating<E_, S, I>>,
    elementJ: StructureElement<E_, S, SignatureConstraint.Terminating<E_, S, J>>,
    execute: Invocation<E_, S>.(A, B, C, D, E, F, G, H, I, J) -> ExecutionResult = { a, b, c, d, e, f, g, h, i, j -> ExecutionResult.success() },
): StructureElement<E_, S, Structure<E_, S>> = {
    build(Signature10(execute,
        Tuple10(elementA.invoke(this), elementB.invoke(this), elementC.invoke(this), elementD.invoke(this),
            elementE.invoke(this), elementF.invoke(this), elementG.invoke(this), elementH.invoke(this),
            elementI.invoke(this), elementJ.invoke(this))
    ))
}

operator fun <E_ : Environment, S, A : Any, B : Any, C : Any, D : Any, E : Any, F : Any, G : Any, H : Any, I : Any,
        J : Any, K : Any> StructureScope<E_, S>.invoke(
    elementA: StructureElement<E_, S, SignatureConstraint.NonTerminating<E_, S, A>>,
    elementB: StructureElement<E_, S, SignatureConstraint.NonTerminating<E_, S, B>>,
    elementC: StructureElement<E_, S, SignatureConstraint.NonTerminating<E_, S, C>>,
    elementD: StructureElement<E_, S, SignatureConstraint.NonTerminating<E_, S, D>>,
    elementE: StructureElement<E_, S, SignatureConstraint.NonTerminating<E_, S, E>>,
    elementF: StructureElement<E_, S, SignatureConstraint.NonTerminating<E_, S, F>>,
    elementG: StructureElement<E_, S, SignatureConstraint.NonTerminating<E_, S, G>>,
    elementH: StructureElement<E_, S, SignatureConstraint.NonTerminating<E_, S, H>>,
    elementI: StructureElement<E_, S, SignatureConstraint.NonTerminating<E_, S, I>>,
    elementJ: StructureElement<E_, S, SignatureConstraint.NonTerminating<E_, S, J>>,
    elementK: StructureElement<E_, S, SignatureConstraint.Terminating<E_, S, K>>,
    execute: Invocation<E_, S>.(A, B, C, D, E, F, G, H, I, J, K) -> ExecutionResult = { a, b, c, d, e, f, g, h, i, j, k -> ExecutionResult.success() },
): StructureElement<E_, S, Structure<E_, S>> = {
    build(Signature11(execute,
        Tuple11(elementA.invoke(this), elementB.invoke(this), elementC.invoke(this), elementD.invoke(this),
            elementE.invoke(this), elementF.invoke(this), elementG.invoke(this), elementH.invoke(this),
            elementI.invoke(this), elementJ.invoke(this), elementK.invoke(this))
    ))
}

operator fun <E_ : Environment, S, A : Any, B : Any, C : Any, D : Any, E : Any, F : Any, G : Any, H : Any, I : Any,
        J : Any, K : Any, L : Any> StructureScope<E_, S>.invoke(
    elementA: StructureElement<E_, S, SignatureConstraint.NonTerminating<E_, S, A>>,
    elementB: StructureElement<E_, S, SignatureConstraint.NonTerminating<E_, S, B>>,
    elementC: StructureElement<E_, S, SignatureConstraint.NonTerminating<E_, S, C>>,
    elementD: StructureElement<E_, S, SignatureConstraint.NonTerminating<E_, S, D>>,
    elementE: StructureElement<E_, S, SignatureConstraint.NonTerminating<E_, S, E>>,
    elementF: StructureElement<E_, S, SignatureConstraint.NonTerminating<E_, S, F>>,
    elementG: StructureElement<E_, S, SignatureConstraint.NonTerminating<E_, S, G>>,
    elementH: StructureElement<E_, S, SignatureConstraint.NonTerminating<E_, S, H>>,
    elementI: StructureElement<E_, S, SignatureConstraint.NonTerminating<E_, S, I>>,
    elementJ: StructureElement<E_, S, SignatureConstraint.NonTerminating<E_, S, J>>,
    elementK: StructureElement<E_, S, SignatureConstraint.NonTerminating<E_, S, K>>,
    elementL: StructureElement<E_, S, SignatureConstraint.Terminating<E_, S, L>>,
    execute: Invocation<E_, S>.(A, B, C, D, E, F, G, H, I, J, K, L) -> ExecutionResult = { a, b, c, d, e, f, g, h, i, j, k, l -> ExecutionResult.success() },
): StructureElement<E_, S, Structure<E_, S>> = {
    build(Signature12(execute,
        Tuple12(elementA.invoke(this), elementB.invoke(this), elementC.invoke(this), elementD.invoke(this),
            elementE.invoke(this), elementF.invoke(this), elementG.invoke(this), elementH.invoke(this),
            elementI.invoke(this), elementJ.invoke(this), elementK.invoke(this), elementL.invoke(this))
    ))
}
