package com.zombachu.stick.dsl

import com.zombachu.stick.Arguments0
import com.zombachu.stick.Arguments1
import com.zombachu.stick.Arguments10
import com.zombachu.stick.Arguments11
import com.zombachu.stick.Arguments12
import com.zombachu.stick.Arguments2
import com.zombachu.stick.Arguments3
import com.zombachu.stick.Arguments4
import com.zombachu.stick.Arguments5
import com.zombachu.stick.Arguments6
import com.zombachu.stick.Arguments7
import com.zombachu.stick.Arguments8
import com.zombachu.stick.Arguments9
import com.zombachu.stick.Environment
import com.zombachu.stick.Invocation
import com.zombachu.stick.Position
import com.zombachu.stick.StructureScope
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

operator fun <E_ : Environment, S> StructureScope<E_, S>.invoke(
    execute: Invocation<E_, S>.() -> Unit = {}
): Structure<E_, S, Arguments0> = this@invoke.build(Signature0(execute, []))

operator fun <E_ : Environment, S, A> StructureScope<E_, S>.invoke(
    elementA: Element.Positioned<E_, S, A, Position.Last>,
    execute: Invocation<E_, S>.(A) -> Unit = { a -> },
): Structure<E_, S, Arguments1<A>> = this@invoke.build(Signature1(execute, [elementA]))

operator fun <E_ : Environment, S, A, B> StructureScope<E_, S>.invoke(
    elementA: Element.Positioned<E_, S, A, Position.Leading>,
    elementB: Element.Positioned<E_, S, B, Position.Last>,
    execute: Invocation<E_, S>.(A, B) -> Unit = { a, b -> },
): Structure<E_, S, Arguments2<A, B>> = this@invoke.build(Signature2(execute, [elementA, elementB]))

operator fun <E_ : Environment, S, A, B, C> StructureScope<E_, S>.invoke(
    elementA: Element.Positioned<E_, S, A, Position.Leading>,
    elementB: Element.Positioned<E_, S, B, Position.Leading>,
    elementC: Element.Positioned<E_, S, C, Position.Last>,
    execute: Invocation<E_, S>.(A, B, C) -> Unit = { a, b, c -> },
): Structure<E_, S, Arguments3<A, B, C>> = this@invoke.build(Signature3(execute, [elementA, elementB, elementC]))

operator fun <E_ : Environment, S, A, B, C, D> StructureScope<E_, S>.invoke(
    elementA: Element.Positioned<E_, S, A, Position.Leading>,
    elementB: Element.Positioned<E_, S, B, Position.Leading>,
    elementC: Element.Positioned<E_, S, C, Position.Leading>,
    elementD: Element.Positioned<E_, S, D, Position.Last>,
    execute: Invocation<E_, S>.(A, B, C, D) -> Unit = { a, b, c, d -> },
): Structure<E_, S, Arguments4<A, B, C, D>> =
    this@invoke.build(Signature4(execute, [elementA, elementB, elementC, elementD]))

operator fun <E_ : Environment, S, A, B, C, D, E> StructureScope<E_, S>.invoke(
    elementA: Element.Positioned<E_, S, A, Position.Leading>,
    elementB: Element.Positioned<E_, S, B, Position.Leading>,
    elementC: Element.Positioned<E_, S, C, Position.Leading>,
    elementD: Element.Positioned<E_, S, D, Position.Leading>,
    elementE: Element.Positioned<E_, S, E, Position.Last>,
    execute: Invocation<E_, S>.(A, B, C, D, E) -> Unit = { a, b, c, d, e -> },
): Structure<E_, S, Arguments5<A, B, C, D, E>> =
    this@invoke.build(Signature5(execute, [elementA, elementB, elementC, elementD, elementE]))

operator fun <E_ : Environment, S, A, B, C, D, E, F> StructureScope<E_, S>.invoke(
    elementA: Element.Positioned<E_, S, A, Position.Leading>,
    elementB: Element.Positioned<E_, S, B, Position.Leading>,
    elementC: Element.Positioned<E_, S, C, Position.Leading>,
    elementD: Element.Positioned<E_, S, D, Position.Leading>,
    elementE: Element.Positioned<E_, S, E, Position.Leading>,
    elementF: Element.Positioned<E_, S, F, Position.Last>,
    execute: Invocation<E_, S>.(A, B, C, D, E, F) -> Unit = { a, b, c, d, e, f -> },
): Structure<E_, S, Arguments6<A, B, C, D, E, F>> =
    this@invoke.build(Signature6(execute, [elementA, elementB, elementC, elementD, elementE, elementF]))

operator fun <E_ : Environment, S, A, B, C, D, E, F, G> StructureScope<E_, S>.invoke(
    elementA: Element.Positioned<E_, S, A, Position.Leading>,
    elementB: Element.Positioned<E_, S, B, Position.Leading>,
    elementC: Element.Positioned<E_, S, C, Position.Leading>,
    elementD: Element.Positioned<E_, S, D, Position.Leading>,
    elementE: Element.Positioned<E_, S, E, Position.Leading>,
    elementF: Element.Positioned<E_, S, F, Position.Leading>,
    elementG: Element.Positioned<E_, S, G, Position.Last>,
    execute: Invocation<E_, S>.(A, B, C, D, E, F, G) -> Unit = { a, b, c, d, e, f, g -> },
): Structure<E_, S, Arguments7<A, B, C, D, E, F, G>> =
    this@invoke.build(Signature7(execute, [elementA, elementB, elementC, elementD, elementE, elementF, elementG]))

operator fun <E_ : Environment, S, A, B, C, D, E, F, G, H> StructureScope<E_, S>.invoke(
    elementA: Element.Positioned<E_, S, A, Position.Leading>,
    elementB: Element.Positioned<E_, S, B, Position.Leading>,
    elementC: Element.Positioned<E_, S, C, Position.Leading>,
    elementD: Element.Positioned<E_, S, D, Position.Leading>,
    elementE: Element.Positioned<E_, S, E, Position.Leading>,
    elementF: Element.Positioned<E_, S, F, Position.Leading>,
    elementG: Element.Positioned<E_, S, G, Position.Leading>,
    elementH: Element.Positioned<E_, S, H, Position.Last>,
    execute: Invocation<E_, S>.(A, B, C, D, E, F, G, H) -> Unit = { a, b, c, d, e, f, g, h -> },
): Structure<E_, S, Arguments8<A, B, C, D, E, F, G, H>> =
    this@invoke.build(
        Signature8(execute, [elementA, elementB, elementC, elementD, elementE, elementF, elementG, elementH])
    )

operator fun <E_ : Environment, S, A, B, C, D, E, F, G, H, I> StructureScope<E_, S>.invoke(
    elementA: Element.Positioned<E_, S, A, Position.Leading>,
    elementB: Element.Positioned<E_, S, B, Position.Leading>,
    elementC: Element.Positioned<E_, S, C, Position.Leading>,
    elementD: Element.Positioned<E_, S, D, Position.Leading>,
    elementE: Element.Positioned<E_, S, E, Position.Leading>,
    elementF: Element.Positioned<E_, S, F, Position.Leading>,
    elementG: Element.Positioned<E_, S, G, Position.Leading>,
    elementH: Element.Positioned<E_, S, H, Position.Leading>,
    elementI: Element.Positioned<E_, S, I, Position.Last>,
    execute: Invocation<E_, S>.(A, B, C, D, E, F, G, H, I) -> Unit = { a, b, c, d, e, f, g, h, i -> },
): Structure<E_, S, Arguments9<A, B, C, D, E, F, G, H, I>> =
    this@invoke.build(
        Signature9(execute, [elementA, elementB, elementC, elementD, elementE, elementF, elementG, elementH, elementI])
    )

operator fun <E_ : Environment, S, A, B, C, D, E, F, G, H, I, J> StructureScope<E_, S>.invoke(
    elementA: Element.Positioned<E_, S, A, Position.Leading>,
    elementB: Element.Positioned<E_, S, B, Position.Leading>,
    elementC: Element.Positioned<E_, S, C, Position.Leading>,
    elementD: Element.Positioned<E_, S, D, Position.Leading>,
    elementE: Element.Positioned<E_, S, E, Position.Leading>,
    elementF: Element.Positioned<E_, S, F, Position.Leading>,
    elementG: Element.Positioned<E_, S, G, Position.Leading>,
    elementH: Element.Positioned<E_, S, H, Position.Leading>,
    elementI: Element.Positioned<E_, S, I, Position.Leading>,
    elementJ: Element.Positioned<E_, S, J, Position.Last>,
    execute: Invocation<E_, S>.(A, B, C, D, E, F, G, H, I, J) -> Unit = { a, b, c, d, e, f, g, h, i, j -> },
): Structure<E_, S, Arguments10<A, B, C, D, E, F, G, H, I, J>> =
    this@invoke.build(
        Signature10(
            execute,
            [elementA, elementB, elementC, elementD, elementE, elementF, elementG, elementH, elementI, elementJ],
        )
    )

operator fun <E_ : Environment, S, A, B, C, D, E, F, G, H, I, J, K> StructureScope<E_, S>.invoke(
    elementA: Element.Positioned<E_, S, A, Position.Leading>,
    elementB: Element.Positioned<E_, S, B, Position.Leading>,
    elementC: Element.Positioned<E_, S, C, Position.Leading>,
    elementD: Element.Positioned<E_, S, D, Position.Leading>,
    elementE: Element.Positioned<E_, S, E, Position.Leading>,
    elementF: Element.Positioned<E_, S, F, Position.Leading>,
    elementG: Element.Positioned<E_, S, G, Position.Leading>,
    elementH: Element.Positioned<E_, S, H, Position.Leading>,
    elementI: Element.Positioned<E_, S, I, Position.Leading>,
    elementJ: Element.Positioned<E_, S, J, Position.Leading>,
    elementK: Element.Positioned<E_, S, K, Position.Last>,
    execute: Invocation<E_, S>.(A, B, C, D, E, F, G, H, I, J, K) -> Unit = { a, b, c, d, e, f, g, h, i, j, k -> },
): Structure<E_, S, Arguments11<A, B, C, D, E, F, G, H, I, J, K>> =
    this@invoke.build(
        Signature11(
            execute,
            [
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
            ],
        )
    )

operator fun <E_ : Environment, S, A, B, C, D, E, F, G, H, I, J, K, L> StructureScope<E_, S>.invoke(
    elementA: Element.Positioned<E_, S, A, Position.Leading>,
    elementB: Element.Positioned<E_, S, B, Position.Leading>,
    elementC: Element.Positioned<E_, S, C, Position.Leading>,
    elementD: Element.Positioned<E_, S, D, Position.Leading>,
    elementE: Element.Positioned<E_, S, E, Position.Leading>,
    elementF: Element.Positioned<E_, S, F, Position.Leading>,
    elementG: Element.Positioned<E_, S, G, Position.Leading>,
    elementH: Element.Positioned<E_, S, H, Position.Leading>,
    elementI: Element.Positioned<E_, S, I, Position.Leading>,
    elementJ: Element.Positioned<E_, S, J, Position.Leading>,
    elementK: Element.Positioned<E_, S, K, Position.Leading>,
    elementL: Element.Positioned<E_, S, L, Position.Last>,
    execute: Invocation<E_, S>.(A, B, C, D, E, F, G, H, I, J, K, L) -> Unit = { a, b, c, d, e, f, g, h, i, j, k, l -> },
): Structure<E_, S, Arguments12<A, B, C, D, E, F, G, H, I, J, K, L>> =
    this@invoke.build(
        Signature12(
            execute,
            [
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
            ],
        )
    )
