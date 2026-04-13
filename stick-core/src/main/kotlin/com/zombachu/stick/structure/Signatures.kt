package com.zombachu.stick.structure

import com.zombachu.stick.Environment
import com.zombachu.stick.Invocation
import com.zombachu.stick.element.Element
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
import com.zombachu.stick.element.Structure
import com.zombachu.stick.impl.Arguments0
import com.zombachu.stick.impl.Arguments1
import com.zombachu.stick.impl.Arguments10
import com.zombachu.stick.impl.Arguments11
import com.zombachu.stick.impl.Arguments12
import com.zombachu.stick.impl.Arguments2
import com.zombachu.stick.impl.Arguments3
import com.zombachu.stick.impl.Arguments4
import com.zombachu.stick.impl.Arguments5
import com.zombachu.stick.impl.Arguments6
import com.zombachu.stick.impl.Arguments7
import com.zombachu.stick.impl.Arguments8
import com.zombachu.stick.impl.Arguments9
import com.zombachu.stick.impl.StructureScope

operator fun <E_ : Environment, S> StructureScope<E_, S>.invoke(
    execute: Invocation<E_, S>.() -> Unit = {}
): Structure<E_, S, Arguments0> = this@invoke.build(Signature0(execute, emptyList()))

operator fun <E_ : Environment, S, A> StructureScope<E_, S>.invoke(
    element: Element<E_, S, A>,
    execute: Invocation<E_, S>.(A) -> Unit = {},
): Structure<E_, S, Arguments1<A>> = this@invoke.build(Signature1(execute, listOf(element)))

operator fun <E_ : Environment, S, A, B> StructureScope<E_, S>.invoke(
    elementA: Element.NonTerminating<E_, S, A>,
    elementB: Element<E_, S, B>,
    execute: Invocation<E_, S>.(A, B) -> Unit = { a, b -> },
): Structure<E_, S, Arguments2<A, B>> = this@invoke.build(Signature2(execute, listOf(elementA, elementB)))

operator fun <E_ : Environment, S, A, B, C> StructureScope<E_, S>.invoke(
    elementA: Element.NonTerminating<E_, S, A>,
    elementB: Element.NonTerminating<E_, S, B>,
    elementC: Element<E_, S, C>,
    execute: Invocation<E_, S>.(A, B, C) -> Unit = { a, b, c -> },
): Structure<E_, S, Arguments3<A, B, C>> = this@invoke.build(Signature3(execute, listOf(elementA, elementB, elementC)))

operator fun <E_ : Environment, S, A, B, C, D> StructureScope<E_, S>.invoke(
    elementA: Element.NonTerminating<E_, S, A>,
    elementB: Element.NonTerminating<E_, S, B>,
    elementC: Element.NonTerminating<E_, S, C>,
    elementD: Element<E_, S, D>,
    execute: Invocation<E_, S>.(A, B, C, D) -> Unit = { a, b, c, d -> },
): Structure<E_, S, Arguments4<A, B, C, D>> =
    this@invoke.build(Signature4(execute, listOf(elementA, elementB, elementC, elementD)))

operator fun <E_ : Environment, S, A, B, C, D, E> StructureScope<E_, S>.invoke(
    elementA: Element.NonTerminating<E_, S, A>,
    elementB: Element.NonTerminating<E_, S, B>,
    elementC: Element.NonTerminating<E_, S, C>,
    elementD: Element.NonTerminating<E_, S, D>,
    elementE: Element<E_, S, E>,
    execute: Invocation<E_, S>.(A, B, C, D, E) -> Unit = { a, b, c, d, e -> },
): Structure<E_, S, Arguments5<A, B, C, D, E>> =
    this@invoke.build(Signature5(execute, listOf(elementA, elementB, elementC, elementD, elementE)))

operator fun <E_ : Environment, S, A, B, C, D, E, F> StructureScope<E_, S>.invoke(
    elementA: Element.NonTerminating<E_, S, A>,
    elementB: Element.NonTerminating<E_, S, B>,
    elementC: Element.NonTerminating<E_, S, C>,
    elementD: Element.NonTerminating<E_, S, D>,
    elementE: Element.NonTerminating<E_, S, E>,
    elementF: Element<E_, S, F>,
    execute: Invocation<E_, S>.(A, B, C, D, E, F) -> Unit = { a, b, c, d, e, f -> },
): Structure<E_, S, Arguments6<A, B, C, D, E, F>> =
    this@invoke.build(Signature6(execute, listOf(elementA, elementB, elementC, elementD, elementE, elementF)))

operator fun <E_ : Environment, S, A, B, C, D, E, F, G> StructureScope<E_, S>.invoke(
    elementA: Element.NonTerminating<E_, S, A>,
    elementB: Element.NonTerminating<E_, S, B>,
    elementC: Element.NonTerminating<E_, S, C>,
    elementD: Element.NonTerminating<E_, S, D>,
    elementE: Element.NonTerminating<E_, S, E>,
    elementF: Element.NonTerminating<E_, S, F>,
    elementG: Element<E_, S, G>,
    execute: Invocation<E_, S>.(A, B, C, D, E, F, G) -> Unit = { a, b, c, d, e, f, g -> },
): Structure<E_, S, Arguments7<A, B, C, D, E, F, G>> =
    this@invoke.build(Signature7(execute, listOf(elementA, elementB, elementC, elementD, elementE, elementF, elementG)))

operator fun <E_ : Environment, S, A, B, C, D, E, F, G, H> StructureScope<E_, S>.invoke(
    elementA: Element.NonTerminating<E_, S, A>,
    elementB: Element.NonTerminating<E_, S, B>,
    elementC: Element.NonTerminating<E_, S, C>,
    elementD: Element.NonTerminating<E_, S, D>,
    elementE: Element.NonTerminating<E_, S, E>,
    elementF: Element.NonTerminating<E_, S, F>,
    elementG: Element.NonTerminating<E_, S, G>,
    elementH: Element<E_, S, H>,
    execute: Invocation<E_, S>.(A, B, C, D, E, F, G, H) -> Unit = { a, b, c, d, e, f, g, h -> },
): Structure<E_, S, Arguments8<A, B, C, D, E, F, G, H>> =
    this@invoke.build(
        Signature8(execute, listOf(elementA, elementB, elementC, elementD, elementE, elementF, elementG, elementH))
    )

operator fun <E_ : Environment, S, A, B, C, D, E, F, G, H, I> StructureScope<E_, S>.invoke(
    elementA: Element.NonTerminating<E_, S, A>,
    elementB: Element.NonTerminating<E_, S, B>,
    elementC: Element.NonTerminating<E_, S, C>,
    elementD: Element.NonTerminating<E_, S, D>,
    elementE: Element.NonTerminating<E_, S, E>,
    elementF: Element.NonTerminating<E_, S, F>,
    elementG: Element.NonTerminating<E_, S, G>,
    elementH: Element.NonTerminating<E_, S, H>,
    elementI: Element<E_, S, I>,
    execute: Invocation<E_, S>.(A, B, C, D, E, F, G, H, I) -> Unit = { a, b, c, d, e, f, g, h, i -> },
): Structure<E_, S, Arguments9<A, B, C, D, E, F, G, H, I>> =
    this@invoke.build(
        Signature9(
            execute,
            listOf(elementA, elementB, elementC, elementD, elementE, elementF, elementG, elementH, elementI),
        )
    )

operator fun <E_ : Environment, S, A, B, C, D, E, F, G, H, I, J> StructureScope<E_, S>.invoke(
    elementA: Element.NonTerminating<E_, S, A>,
    elementB: Element.NonTerminating<E_, S, B>,
    elementC: Element.NonTerminating<E_, S, C>,
    elementD: Element.NonTerminating<E_, S, D>,
    elementE: Element.NonTerminating<E_, S, E>,
    elementF: Element.NonTerminating<E_, S, F>,
    elementG: Element.NonTerminating<E_, S, G>,
    elementH: Element.NonTerminating<E_, S, H>,
    elementI: Element.NonTerminating<E_, S, I>,
    elementJ: Element<E_, S, J>,
    execute: Invocation<E_, S>.(A, B, C, D, E, F, G, H, I, J) -> Unit = { a, b, c, d, e, f, g, h, i, j -> },
): Structure<E_, S, Arguments10<A, B, C, D, E, F, G, H, I, J>> =
    this@invoke.build(
        Signature10(
            execute,
            listOf(elementA, elementB, elementC, elementD, elementE, elementF, elementG, elementH, elementI, elementJ),
        )
    )

operator fun <E_ : Environment, S, A, B, C, D, E, F, G, H, I, J, K> StructureScope<E_, S>.invoke(
    elementA: Element.NonTerminating<E_, S, A>,
    elementB: Element.NonTerminating<E_, S, B>,
    elementC: Element.NonTerminating<E_, S, C>,
    elementD: Element.NonTerminating<E_, S, D>,
    elementE: Element.NonTerminating<E_, S, E>,
    elementF: Element.NonTerminating<E_, S, F>,
    elementG: Element.NonTerminating<E_, S, G>,
    elementH: Element.NonTerminating<E_, S, H>,
    elementI: Element.NonTerminating<E_, S, I>,
    elementJ: Element.NonTerminating<E_, S, J>,
    elementK: Element<E_, S, K>,
    execute: Invocation<E_, S>.(A, B, C, D, E, F, G, H, I, J, K) -> Unit = { a, b, c, d, e, f, g, h, i, j, k -> },
): Structure<E_, S, Arguments11<A, B, C, D, E, F, G, H, I, J, K>> =
    this@invoke.build(
        Signature11(
            execute,
            listOf(
                elementA,
                elementB,
                elementC,
                elementD,
                elementE,
                elementF,
                elementG,
                elementH,
                elementI,
                elementJ,
                elementK,
            ),
        )
    )

operator fun <E_ : Environment, S, A, B, C, D, E, F, G, H, I, J, K, L> StructureScope<E_, S>.invoke(
    elementA: Element.NonTerminating<E_, S, A>,
    elementB: Element.NonTerminating<E_, S, B>,
    elementC: Element.NonTerminating<E_, S, C>,
    elementD: Element.NonTerminating<E_, S, D>,
    elementE: Element.NonTerminating<E_, S, E>,
    elementF: Element.NonTerminating<E_, S, F>,
    elementG: Element.NonTerminating<E_, S, G>,
    elementH: Element.NonTerminating<E_, S, H>,
    elementI: Element.NonTerminating<E_, S, I>,
    elementJ: Element.NonTerminating<E_, S, J>,
    elementK: Element.NonTerminating<E_, S, K>,
    elementL: Element<E_, S, L>,
    execute: Invocation<E_, S>.(A, B, C, D, E, F, G, H, I, J, K, L) -> Unit = { a, b, c, d, e, f, g, h, i, j, k, l -> },
): Structure<E_, S, Arguments12<A, B, C, D, E, F, G, H, I, J, K, L>> =
    this@invoke.build(
        Signature12(
            execute,
            listOf(
                elementA,
                elementB,
                elementC,
                elementD,
                elementE,
                elementF,
                elementG,
                elementH,
                elementI,
                elementJ,
                elementK,
                elementL,
            ),
        )
    )
