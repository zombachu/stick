package com.zombachu.stick.impl

import com.zombachu.stick.Environment
import com.zombachu.stick.Invocation

interface Arguments

class Arguments0 : Arguments

data class Arguments1<A>(val a: A) : Arguments {
    context(inv: Invocation<E_, S>)
    fun <E_ : Environment, S> execute(toRun: Invocation<E_, S>.(A) -> Unit) {
        inv.toRun(a)
    }
}

data class Arguments2<A, B>(val a: A, val b: B) : Arguments {
    context(inv: Invocation<E_, S>)
    fun <E_ : Environment, S> execute(toRun: Invocation<E_, S>.(A, B) -> Unit) {
        inv.toRun(a, b)
    }
}

data class Arguments3<A, B, C>(val a: A, val b: B, val c: C) : Arguments {
    context(inv: Invocation<E_, S>)
    fun <E_ : Environment, S> execute(toRun: Invocation<E_, S>.(A, B, C) -> Unit) {
        inv.toRun(a, b, c)
    }
}

data class Arguments4<A, B, C, D>(val a: A, val b: B, val c: C, val d: D) : Arguments {
    context(inv: Invocation<E_, S>)
    fun <E_ : Environment, S> execute(toRun: Invocation<E_, S>.(A, B, C, D) -> Unit) {
        inv.toRun(a, b, c, d)
    }
}

data class Arguments5<A, B, C, D, E>(val a: A, val b: B, val c: C, val d: D, val e: E) : Arguments {
    context(inv: Invocation<E_, S>)
    fun <E_ : Environment, S> execute(toRun: Invocation<E_, S>.(A, B, C, D, E) -> Unit) {
        inv.toRun(a, b, c, d, e)
    }
}

data class Arguments6<A, B, C, D, E, F>(val a: A, val b: B, val c: C, val d: D, val e: E, val f: F) : Arguments {
    context(inv: Invocation<E_, S>)
    fun <E_ : Environment, S> execute(toRun: Invocation<E_, S>.(A, B, C, D, E, F) -> Unit) {
        inv.toRun(a, b, c, d, e, f)
    }
}

data class Arguments7<A, B, C, D, E, F, G>(val a: A, val b: B, val c: C, val d: D, val e: E, val f: F, val g: G) :
    Arguments {
    context(inv: Invocation<E_, S>)
    fun <E_ : Environment, S> execute(toRun: Invocation<E_, S>.(A, B, C, D, E, F, G) -> Unit) {
        inv.toRun(a, b, c, d, e, f, g)
    }
}

data class Arguments8<A, B, C, D, E, F, G, H>(
    val a: A,
    val b: B,
    val c: C,
    val d: D,
    val e: E,
    val f: F,
    val g: G,
    val h: H,
) : Arguments {
    context(inv: Invocation<E_, S>)
    fun <E_ : Environment, S> execute(toRun: Invocation<E_, S>.(A, B, C, D, E, F, G, H) -> Unit) {
        inv.toRun(a, b, c, d, e, f, g, h)
    }
}

data class Arguments9<A, B, C, D, E, F, G, H, I>(
    val a: A,
    val b: B,
    val c: C,
    val d: D,
    val e: E,
    val f: F,
    val g: G,
    val h: H,
    val i: I,
) : Arguments {
    context(inv: Invocation<E_, S>)
    fun <E_ : Environment, S> execute(toRun: Invocation<E_, S>.(A, B, C, D, E, F, G, H, I) -> Unit) {
        inv.toRun(a, b, c, d, e, f, g, h, i)
    }
}

data class Arguments10<A, B, C, D, E, F, G, H, I, J>(
    val a: A,
    val b: B,
    val c: C,
    val d: D,
    val e: E,
    val f: F,
    val g: G,
    val h: H,
    val i: I,
    val j: J,
) : Arguments {
    context(inv: Invocation<E_, S>)
    fun <E_ : Environment, S> execute(toRun: Invocation<E_, S>.(A, B, C, D, E, F, G, H, I, J) -> Unit) {
        inv.toRun(a, b, c, d, e, f, g, h, i, j)
    }
}

data class Arguments11<A, B, C, D, E, F, G, H, I, J, K>(
    val a: A,
    val b: B,
    val c: C,
    val d: D,
    val e: E,
    val f: F,
    val g: G,
    val h: H,
    val i: I,
    val j: J,
    val k: K,
) : Arguments {
    context(inv: Invocation<E_, S>)
    fun <E_ : Environment, S> execute(toRun: Invocation<E_, S>.(A, B, C, D, E, F, G, H, I, J, K) -> Unit) {
        inv.toRun(a, b, c, d, e, f, g, h, i, j, k)
    }
}

data class Arguments12<A, B, C, D, E, F, G, H, I, J, K, L>(
    val a: A,
    val b: B,
    val c: C,
    val d: D,
    val e: E,
    val f: F,
    val g: G,
    val h: H,
    val i: I,
    val j: J,
    val k: K,
    val l: L,
) : Arguments {
    context(inv: Invocation<E_, S>)
    fun <E_ : Environment, S> execute(toRun: Invocation<E_, S>.(A, B, C, D, E, F, G, H, I, J, K, L) -> Unit) {
        inv.toRun(a, b, c, d, e, f, g, h, i, j, k, l)
    }
}
