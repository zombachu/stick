@file:Suppress("UNCHECKED_CAST", "MagicNumber")

package com.zombachu.stick.element

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

internal class Signature0<E_ : Environment, S>(
    private val execute: Invocation<E_, S>.() -> Unit,
    elements: List<Element<E_, S, Any?>>,
) : Signature<E_, S, Arguments0>(elements) {
    context(inv: Invocation<E_, S>)
    override fun executeParsed(parsedValues: List<Any?>): Arguments0 {
        return Arguments0().apply { inv.execute() }
    }
}

internal class Signature1<E_ : Environment, S, A>(
    private val execute: Invocation<E_, S>.(A) -> Unit,
    elements: List<Element<E_, S, Any?>>,
) : Signature<E_, S, Arguments1<A>>(elements) {
    context(inv: Invocation<E_, S>)
    override fun executeParsed(parsedValues: List<Any?>): Arguments1<A> {
        return Arguments1(parsedValues[0] as A).apply { inv.execute(a) }
    }
}

internal class Signature2<E_ : Environment, S, A, B>(
    val execute: Invocation<E_, S>.(A, B) -> Unit,
    elements: List<Element<E_, S, Any?>>,
) : Signature<E_, S, Arguments2<A, B>>(elements) {
    context(inv: Invocation<E_, S>)
    override fun executeParsed(parsedValues: List<Any?>): Arguments2<A, B> {
        return Arguments2(parsedValues[0] as A, parsedValues[1] as B).apply { inv.execute(a, b) }
    }
}

internal class Signature3<E_ : Environment, S, A, B, C>(
    private val execute: Invocation<E_, S>.(A, B, C) -> Unit,
    elements: List<Element<E_, S, Any?>>,
) : Signature<E_, S, Arguments3<A, B, C>>(elements) {
    context(inv: Invocation<E_, S>)
    override fun executeParsed(parsedValues: List<Any?>): Arguments3<A, B, C> {
        return Arguments3(parsedValues[0] as A, parsedValues[1] as B, parsedValues[2] as C).apply {
            inv.execute(a, b, c)
        }
    }
}

internal class Signature4<E_ : Environment, S, A, B, C, D>(
    private val execute: Invocation<E_, S>.(A, B, C, D) -> Unit,
    elements: List<Element<E_, S, Any?>>,
) : Signature<E_, S, Arguments4<A, B, C, D>>(elements) {
    context(inv: Invocation<E_, S>)
    override fun executeParsed(parsedValues: List<Any?>): Arguments4<A, B, C, D> {
        return Arguments4(parsedValues[0] as A, parsedValues[1] as B, parsedValues[2] as C, parsedValues[3] as D)
            .apply { inv.execute(a, b, c, d) }
    }
}

internal class Signature5<E_ : Environment, S, A, B, C, D, E>(
    private val execute: Invocation<E_, S>.(A, B, C, D, E) -> Unit,
    elements: List<Element<E_, S, Any?>>,
) : Signature<E_, S, Arguments5<A, B, C, D, E>>(elements) {
    context(inv: Invocation<E_, S>)
    override fun executeParsed(parsedValues: List<Any?>): Arguments5<A, B, C, D, E> {
        return Arguments5(
                parsedValues[0] as A,
                parsedValues[1] as B,
                parsedValues[2] as C,
                parsedValues[3] as D,
                parsedValues[4] as E,
            )
            .apply { inv.execute(a, b, c, d, e) }
    }
}

internal class Signature6<E_ : Environment, S, A, B, C, D, E, F>(
    private val execute: Invocation<E_, S>.(A, B, C, D, E, F) -> Unit,
    elements: List<Element<E_, S, Any?>>,
) : Signature<E_, S, Arguments6<A, B, C, D, E, F>>(elements) {
    context(inv: Invocation<E_, S>)
    override fun executeParsed(parsedValues: List<Any?>): Arguments6<A, B, C, D, E, F> {
        return Arguments6(
                parsedValues[0] as A,
                parsedValues[1] as B,
                parsedValues[2] as C,
                parsedValues[3] as D,
                parsedValues[4] as E,
                parsedValues[5] as F,
            )
            .apply { inv.execute(a, b, c, d, e, f) }
    }
}

internal class Signature7<E_ : Environment, S, A, B, C, D, E, F, G>(
    private val execute: Invocation<E_, S>.(A, B, C, D, E, F, G) -> Unit,
    elements: List<Element<E_, S, Any?>>,
) : Signature<E_, S, Arguments7<A, B, C, D, E, F, G>>(elements) {
    context(inv: Invocation<E_, S>)
    override fun executeParsed(parsedValues: List<Any?>): Arguments7<A, B, C, D, E, F, G> {
        return Arguments7(
                parsedValues[0] as A,
                parsedValues[1] as B,
                parsedValues[2] as C,
                parsedValues[3] as D,
                parsedValues[4] as E,
                parsedValues[5] as F,
                parsedValues[6] as G,
            )
            .apply { inv.execute(a, b, c, d, e, f, g) }
    }
}

internal class Signature8<E_ : Environment, S, A, B, C, D, E, F, G, H>(
    private val execute: Invocation<E_, S>.(A, B, C, D, E, F, G, H) -> Unit,
    elements: List<Element<E_, S, Any?>>,
) : Signature<E_, S, Arguments8<A, B, C, D, E, F, G, H>>(elements) {
    context(inv: Invocation<E_, S>)
    override fun executeParsed(parsedValues: List<Any?>): Arguments8<A, B, C, D, E, F, G, H> {
        return Arguments8(
                parsedValues[0] as A,
                parsedValues[1] as B,
                parsedValues[2] as C,
                parsedValues[3] as D,
                parsedValues[4] as E,
                parsedValues[5] as F,
                parsedValues[6] as G,
                parsedValues[7] as H,
            )
            .apply { inv.execute(a, b, c, d, e, f, g, h) }
    }
}

internal class Signature9<E_ : Environment, S, A, B, C, D, E, F, G, H, I>(
    private val execute: Invocation<E_, S>.(A, B, C, D, E, F, G, H, I) -> Unit,
    elements: List<Element<E_, S, Any?>>,
) : Signature<E_, S, Arguments9<A, B, C, D, E, F, G, H, I>>(elements) {
    context(inv: Invocation<E_, S>)
    override fun executeParsed(parsedValues: List<Any?>): Arguments9<A, B, C, D, E, F, G, H, I> {
        return Arguments9(
                parsedValues[0] as A,
                parsedValues[1] as B,
                parsedValues[2] as C,
                parsedValues[3] as D,
                parsedValues[4] as E,
                parsedValues[5] as F,
                parsedValues[6] as G,
                parsedValues[7] as H,
                parsedValues[8] as I,
            )
            .apply { inv.execute(a, b, c, d, e, f, g, h, i) }
    }
}

internal class Signature10<E_ : Environment, S, A, B, C, D, E, F, G, H, I, J>(
    private val execute: Invocation<E_, S>.(A, B, C, D, E, F, G, H, I, J) -> Unit,
    elements: List<Element<E_, S, Any?>>,
) : Signature<E_, S, Arguments10<A, B, C, D, E, F, G, H, I, J>>(elements) {
    context(inv: Invocation<E_, S>)
    override fun executeParsed(parsedValues: List<Any?>): Arguments10<A, B, C, D, E, F, G, H, I, J> {
        return Arguments10(
                parsedValues[0] as A,
                parsedValues[1] as B,
                parsedValues[2] as C,
                parsedValues[3] as D,
                parsedValues[4] as E,
                parsedValues[5] as F,
                parsedValues[6] as G,
                parsedValues[7] as H,
                parsedValues[8] as I,
                parsedValues[9] as J,
            )
            .apply { inv.execute(a, b, c, d, e, f, g, h, i, j) }
    }
}

internal class Signature11<E_ : Environment, S, A, B, C, D, E, F, G, H, I, J, K>(
    private val execute: Invocation<E_, S>.(A, B, C, D, E, F, G, H, I, J, K) -> Unit,
    elements: List<Element<E_, S, Any?>>,
) : Signature<E_, S, Arguments11<A, B, C, D, E, F, G, H, I, J, K>>(elements) {
    context(inv: Invocation<E_, S>)
    override fun executeParsed(parsedValues: List<Any?>): Arguments11<A, B, C, D, E, F, G, H, I, J, K> {
        return Arguments11(
                parsedValues[0] as A,
                parsedValues[1] as B,
                parsedValues[2] as C,
                parsedValues[3] as D,
                parsedValues[4] as E,
                parsedValues[5] as F,
                parsedValues[6] as G,
                parsedValues[7] as H,
                parsedValues[8] as I,
                parsedValues[9] as J,
                parsedValues[10] as K,
            )
            .apply { inv.execute(a, b, c, d, e, f, g, h, i, j, k) }
    }
}

internal class Signature12<E_ : Environment, S, A, B, C, D, E, F, G, H, I, J, K, L>(
    private val execute: Invocation<E_, S>.(A, B, C, D, E, F, G, H, I, J, K, L) -> Unit,
    elements: List<Element<E_, S, Any?>>,
) : Signature<E_, S, Arguments12<A, B, C, D, E, F, G, H, I, J, K, L>>(elements) {
    context(inv: Invocation<E_, S>)
    override fun executeParsed(parsedValues: List<Any?>): Arguments12<A, B, C, D, E, F, G, H, I, J, K, L> {
        return Arguments12(
                parsedValues[0] as A,
                parsedValues[1] as B,
                parsedValues[2] as C,
                parsedValues[3] as D,
                parsedValues[4] as E,
                parsedValues[5] as F,
                parsedValues[6] as G,
                parsedValues[7] as H,
                parsedValues[8] as I,
                parsedValues[9] as J,
                parsedValues[10] as K,
                parsedValues[11] as L,
            )
            .apply { inv.execute(a, b, c, d, e, f, g, h, i, j, k, l) }
    }
}
