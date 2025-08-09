package com.zombachu.stick.impl

internal interface Tuple<T> {
    fun toList(): List<T>
}

internal class Tuple0<T>: Tuple<T> {
    override fun toList(): List<T> = listOf()
}
internal class Tuple1<T, A:T>(val a: A): Tuple<T> {
    override fun toList(): List<T> = listOf(a)
}
internal class Tuple2<T, A:T, B:T>(val a: A, val b: B): Tuple<T> {
    override fun toList(): List<T> = listOf(a, b)
}
internal class Tuple3<T, A:T, B:T, C:T>(val a: A, val b: B, val c: C): Tuple<T> {
    override fun toList(): List<T> = listOf(a, b, c)
}
internal class Tuple4<T, A:T, B:T, C:T, D:T>(val a: A, val b: B, val c: C, val d: D): Tuple<T> {
    override fun toList(): List<T> = listOf(a, b, c, d)
}
internal class Tuple5<T, A:T, B:T, C:T, D:T, E:T>(val a: A, val b: B, val c: C, val d: D, val e: E): Tuple<T> {
    override fun toList(): List<T> = listOf(a, b, c, d, e)
}
internal class Tuple6<T, A:T, B:T, C:T, D:T, E:T, F:T>(val a: A, val b: B, val c: C, val d: D, val e: E, val f: F): Tuple<T> {
    override fun toList(): List<T> = listOf(a, b, c, d, e, f)
}
internal class Tuple7<T, A:T, B:T, C:T, D:T, E:T, F:T, G:T>(val a: A, val b: B, val c: C, val d: D, val e: E, val f: F, val g: G): Tuple<T> {
    override fun toList(): List<T> = listOf(a, b, c, d, e, f, g)
}
internal class Tuple8<T, A:T, B:T, C:T, D:T, E:T, F:T, G:T, S:T>(val a: A, val b: B, val c: C, val d: D, val e: E, val f: F, val g: G, val h: S): Tuple<T> {
    override fun toList(): List<T> = listOf(a, b, c, d, e, f, g, h)
}
internal class Tuple9<T, A:T, B:T, C:T, D:T, E:T, F:T, G:T, S:T, I:T>(val a: A, val b: B, val c: C, val d: D, val e: E, val f: F, val g: G, val h: S, val i: I): Tuple<T> {
    override fun toList(): List<T> = listOf(a, b, c, d, e, f, g, h, i)
}
internal class Tuple10<T, A:T, B:T, C:T, D:T, E:T, F:T, G:T, S:T, I:T, J:T>(val a: A, val b: B, val c: C, val d: D, val e: E, val f: F, val g: G, val h: S, val i: I, val j: J): Tuple<T> {
    override fun toList(): List<T> = listOf(a, b, c, d, e, f, g, h, i, j)
}
internal class Tuple11<T, A:T, B:T, C:T, D:T, E:T, F:T, G:T, S:T, I:T, J:T, K:T>(val a: A, val b: B, val c: C, val d: D, val e: E, val f: F, val g: G, val h: S, val i: I, val j: J, val k: K): Tuple<T> {
    override fun toList(): List<T> = listOf(a, b, c, d, e, f, g, h, i, j, k)
}
internal class Tuple12<T, A:T, B:T, C:T, D:T, E:T, F:T, G:T, S:T, I:T, J:T, K:T, L:T>(val a: A, val b: B, val c: C, val d: D, val e: E, val f: F, val g: G, val h: S, val i: I, val j: J, val k: K, val l: L): Tuple<T> {
    override fun toList(): List<T> = listOf(a, b, c, d, e, f, g, h, i, j, k, l)
}
