package com.zombachu.stick.structure

import com.zombachu.stick.Environment
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
    execute: Invocation<E_, S>.() -> Unit,
): StructureElement<E_, S, Structure<E_, S>> = {
    this@invoke.build(Signature0(execute, Tuple0()))
}

operator fun <E_ : Environment, S, A> StructureScope<E_, S>.invoke(
    element: StructureElement<E_, S, SignatureConstraint.Terminating<E_, S, A>>,
    execute: Invocation<E_, S>.(A) -> Unit = {  },
): StructureElement<E_, S, Structure<E_, S>> = {
    this@invoke.build(Signature1(execute, Tuple1(element.invoke(this))))
}

operator fun <E_ : Environment, S, A, B> StructureScope<E_, S>.invoke(
    elementA: StructureElement<E_, S, SignatureConstraint.NonTerminating<E_, S, A>>,
    elementB: StructureElement<E_, S, SignatureConstraint.Terminating<E_, S, B>>,
    execute: Invocation<E_, S>.(A, B) -> Unit = { a, b -> },
): StructureElement<E_, S, Structure<E_, S>> = {
    this@invoke.build(Signature2(execute, Tuple2(elementA.invoke(this), elementB.invoke(this))))
}

operator fun <E_ : Environment, S, A, B, C> StructureScope<E_, S>.invoke(
    elementA: StructureElement<E_, S, SignatureConstraint.NonTerminating<E_, S, A>>,
    elementB: StructureElement<E_, S, SignatureConstraint.NonTerminating<E_, S, B>>,
    elementC: StructureElement<E_, S, SignatureConstraint.Terminating<E_, S, C>>,
    execute: Invocation<E_, S>.(A, B, C) -> Unit = { a, b, c -> },
): StructureElement<E_, S, Structure<E_, S>> = {
    this@invoke.build(Signature3(execute,
        Tuple3(elementA.invoke(this), elementB.invoke(this), elementC.invoke(this))
    ))
}

operator fun <E_ : Environment, S, A, B, C, D> StructureScope<E_, S>.invoke(
    elementA: StructureElement<E_, S, SignatureConstraint.NonTerminating<E_, S, A>>,
    elementB: StructureElement<E_, S, SignatureConstraint.NonTerminating<E_, S, B>>,
    elementC: StructureElement<E_, S, SignatureConstraint.NonTerminating<E_, S, C>>,
    elementD: StructureElement<E_, S, SignatureConstraint.Terminating<E_, S, D>>,
    execute: Invocation<E_, S>.(A, B, C, D) -> Unit = { a, b, c, d -> },
): StructureElement<E_, S, Structure<E_, S>> = {
    this@invoke.build(Signature4(execute,
        Tuple4(elementA.invoke(this), elementB.invoke(this), elementC.invoke(this), elementD.invoke(this))
    ))
}

operator fun <E_ : Environment, S, A, B, C, D, E> StructureScope<E_, S>.invoke(
    elementA: StructureElement<E_, S, SignatureConstraint.NonTerminating<E_, S, A>>,
    elementB: StructureElement<E_, S, SignatureConstraint.NonTerminating<E_, S, B>>,
    elementC: StructureElement<E_, S, SignatureConstraint.NonTerminating<E_, S, C>>,
    elementD: StructureElement<E_, S, SignatureConstraint.NonTerminating<E_, S, D>>,
    elementE: StructureElement<E_, S, SignatureConstraint.Terminating<E_, S, E>>,
    execute: Invocation<E_, S>.(A, B, C, D, E) -> Unit = { a, b, c, d, e -> },
): StructureElement<E_, S, Structure<E_, S>> = {
    this@invoke.build(Signature5(execute,
        Tuple5(elementA.invoke(this), elementB.invoke(this), elementC.invoke(this), elementD.invoke(this),
            elementE.invoke(this))
    ))
}

operator fun <E_ : Environment, S, A, B, C, D, E, F> StructureScope<E_, S>.invoke(
    elementA: StructureElement<E_, S, SignatureConstraint.NonTerminating<E_, S, A>>,
    elementB: StructureElement<E_, S, SignatureConstraint.NonTerminating<E_, S, B>>,
    elementC: StructureElement<E_, S, SignatureConstraint.NonTerminating<E_, S, C>>,
    elementD: StructureElement<E_, S, SignatureConstraint.NonTerminating<E_, S, D>>,
    elementE: StructureElement<E_, S, SignatureConstraint.NonTerminating<E_, S, E>>,
    elementF: StructureElement<E_, S, SignatureConstraint.Terminating<E_, S, F>>,
    execute: Invocation<E_, S>.(A, B, C, D, E, F) -> Unit = { a, b, c, d, e, f -> },
): StructureElement<E_, S, Structure<E_, S>> = {
    this@invoke.build(Signature6(execute,
        Tuple6(elementA.invoke(this), elementB.invoke(this), elementC.invoke(this), elementD.invoke(this),
            elementE.invoke(this), elementF.invoke(this))
    ))
}

operator fun <E_ : Environment, S, A, B, C, D, E, F, G> StructureScope<E_, S>.invoke(
    elementA: StructureElement<E_, S, SignatureConstraint.NonTerminating<E_, S, A>>,
    elementB: StructureElement<E_, S, SignatureConstraint.NonTerminating<E_, S, B>>,
    elementC: StructureElement<E_, S, SignatureConstraint.NonTerminating<E_, S, C>>,
    elementD: StructureElement<E_, S, SignatureConstraint.NonTerminating<E_, S, D>>,
    elementE: StructureElement<E_, S, SignatureConstraint.NonTerminating<E_, S, E>>,
    elementF: StructureElement<E_, S, SignatureConstraint.NonTerminating<E_, S, F>>,
    elementG: StructureElement<E_, S, SignatureConstraint.Terminating<E_, S, G>>,
    execute: Invocation<E_, S>.(A, B, C, D, E, F, G) -> Unit = { a, b, c, d, e, f, g -> },
): StructureElement<E_, S, Structure<E_, S>> = {
    this@invoke.build(Signature7(execute,
        Tuple7(elementA.invoke(this), elementB.invoke(this), elementC.invoke(this), elementD.invoke(this),
            elementE.invoke(this), elementF.invoke(this), elementG.invoke(this))
    ))
}

operator fun <E_ : Environment, S, A, B, C, D, E, F, G, H> StructureScope<E_, S>.invoke(
    elementA: StructureElement<E_, S, SignatureConstraint.NonTerminating<E_, S, A>>,
    elementB: StructureElement<E_, S, SignatureConstraint.NonTerminating<E_, S, B>>,
    elementC: StructureElement<E_, S, SignatureConstraint.NonTerminating<E_, S, C>>,
    elementD: StructureElement<E_, S, SignatureConstraint.NonTerminating<E_, S, D>>,
    elementE: StructureElement<E_, S, SignatureConstraint.NonTerminating<E_, S, E>>,
    elementF: StructureElement<E_, S, SignatureConstraint.NonTerminating<E_, S, F>>,
    elementG: StructureElement<E_, S, SignatureConstraint.NonTerminating<E_, S, G>>,
    elementH: StructureElement<E_, S, SignatureConstraint.Terminating<E_, S, H>>,
    execute: Invocation<E_, S>.(A, B, C, D, E, F, G, H) -> Unit = { a, b, c, d, e, f, g, h -> },
): StructureElement<E_, S, Structure<E_, S>> = {
    this@invoke.build(Signature8(execute,
        Tuple8(elementA.invoke(this), elementB.invoke(this), elementC.invoke(this), elementD.invoke(this),
            elementE.invoke(this), elementF.invoke(this), elementG.invoke(this), elementH.invoke(this))
    ))
}

operator fun <E_ : Environment, S, A, B, C, D, E, F, G, H, I> StructureScope<E_, S>.invoke(
    elementA: StructureElement<E_, S, SignatureConstraint.NonTerminating<E_, S, A>>,
    elementB: StructureElement<E_, S, SignatureConstraint.NonTerminating<E_, S, B>>,
    elementC: StructureElement<E_, S, SignatureConstraint.NonTerminating<E_, S, C>>,
    elementD: StructureElement<E_, S, SignatureConstraint.NonTerminating<E_, S, D>>,
    elementE: StructureElement<E_, S, SignatureConstraint.NonTerminating<E_, S, E>>,
    elementF: StructureElement<E_, S, SignatureConstraint.NonTerminating<E_, S, F>>,
    elementG: StructureElement<E_, S, SignatureConstraint.NonTerminating<E_, S, G>>,
    elementH: StructureElement<E_, S, SignatureConstraint.NonTerminating<E_, S, H>>,
    elementI: StructureElement<E_, S, SignatureConstraint.Terminating<E_, S, I>>,
    execute: Invocation<E_, S>.(A, B, C, D, E, F, G, H, I) -> Unit = { a, b, c, d, e, f, g, h, i -> },
): StructureElement<E_, S, Structure<E_, S>> = {
    this@invoke.build(Signature9(execute,
        Tuple9(elementA.invoke(this), elementB.invoke(this), elementC.invoke(this), elementD.invoke(this),
            elementE.invoke(this), elementF.invoke(this), elementG.invoke(this), elementH.invoke(this),
            elementI.invoke(this))
    ))
}

operator fun <E_ : Environment, S, A, B, C, D, E, F, G, H, I, J> StructureScope<E_, S>.invoke(
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
    execute: Invocation<E_, S>.(A, B, C, D, E, F, G, H, I, J) -> Unit = { a, b, c, d, e, f, g, h, i, j -> },
): StructureElement<E_, S, Structure<E_, S>> = {
    this@invoke.build(Signature10(execute,
        Tuple10(elementA.invoke(this), elementB.invoke(this), elementC.invoke(this), elementD.invoke(this),
            elementE.invoke(this), elementF.invoke(this), elementG.invoke(this), elementH.invoke(this),
            elementI.invoke(this), elementJ.invoke(this))
    ))
}

operator fun <E_ : Environment, S, A, B, C, D, E, F, G, H, I, J, K> StructureScope<E_, S>.invoke(
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
    execute: Invocation<E_, S>.(A, B, C, D, E, F, G, H, I, J, K) -> Unit = { a, b, c, d, e, f, g, h, i, j, k -> },
): StructureElement<E_, S, Structure<E_, S>> = {
    this@invoke.build(Signature11(execute,
        Tuple11(elementA.invoke(this), elementB.invoke(this), elementC.invoke(this), elementD.invoke(this),
            elementE.invoke(this), elementF.invoke(this), elementG.invoke(this), elementH.invoke(this),
            elementI.invoke(this), elementJ.invoke(this), elementK.invoke(this))
    ))
}

operator fun <E_ : Environment, S, A, B, C, D, E, F, G, H, I, J, K, L> StructureScope<E_, S>.invoke(
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
    execute: Invocation<E_, S>.(A, B, C, D, E, F, G, H, I, J, K, L) -> Unit = { a, b, c, d, e, f, g, h, i, j, k, l -> },
): StructureElement<E_, S, Structure<E_, S>> = {
    this@invoke.build(Signature12(execute,
        Tuple12(elementA.invoke(this), elementB.invoke(this), elementC.invoke(this), elementD.invoke(this),
            elementE.invoke(this), elementF.invoke(this), elementG.invoke(this), elementH.invoke(this),
            elementI.invoke(this), elementJ.invoke(this), elementK.invoke(this), elementL.invoke(this))
    ))
}
