package com.zombachu.stick.impl

interface Tuple<T> {
    fun toList(): List<T>
}

class Tuple0<T>: Tuple<T> {
    override fun toList(): List<T> = listOf()
}
class Tuple1<T, A:T>(val a: A): Tuple<T> {
    override fun toList(): List<T> = listOf(a)
}
class Tuple2<T, A:T, B:T>(val a: A, val b: B): Tuple<T> {
    override fun toList(): List<T> = listOf(a, b)
}
class Tuple3<T, A:T, B:T, C:T>(val a: A, val b: B, val c: C): Tuple<T> {
    override fun toList(): List<T> = listOf(a, b, c)
}
class Tuple4<T, A:T, B:T, C:T, D:T>(val a: A, val b: B, val c: C, val d: D): Tuple<T> {
    override fun toList(): List<T> = listOf(a, b, c, d)
}
class Tuple5<T, A:T, B:T, C:T, D:T, E:T>(val a: A, val b: B, val c: C, val d: D, val e: E): Tuple<T> {
    override fun toList(): List<T> = listOf(a, b, c, d, e)
}
class Tuple6<T, A:T, B:T, C:T, D:T, E:T, F:T>(val a: A, val b: B, val c: C, val d: D, val e: E, val f: F): Tuple<T> {
    override fun toList(): List<T> = listOf(a, b, c, d, e, f)
}
class Tuple7<T, A:T, B:T, C:T, D:T, E:T, F:T, G:T>(val a: A, val b: B, val c: C, val d: D, val e: E, val f: F, val g: G): Tuple<T> {
    override fun toList(): List<T> = listOf(a, b, c, d, e, f, g)
}
class Tuple8<T, A:T, B:T, C:T, D:T, E:T, F:T, G:T, H:T>(val a: A, val b: B, val c: C, val d: D, val e: E, val f: F, val g: G, val h: H): Tuple<T> {
    override fun toList(): List<T> = listOf(a, b, c, d, e, f, g, h)
}
class Tuple9<T, A:T, B:T, C:T, D:T, E:T, F:T, G:T, H:T, I:T>(val a: A, val b: B, val c: C, val d: D, val e: E, val f: F, val g: G, val h: H, val i: I): Tuple<T> {
    override fun toList(): List<T> = listOf(a, b, c, d, e, f, g, h, i)
}
class Tuple10<T, A:T, B:T, C:T, D:T, E:T, F:T, G:T, H:T, I:T, J:T>(val a: A, val b: B, val c: C, val d: D, val e: E, val f: F, val g: G, val h: H, val i: I, val j: J): Tuple<T> {
    override fun toList(): List<T> = listOf(a, b, c, d, e, f, g, h, i, j)
}
class Tuple11<T, A:T, B:T, C:T, D:T, E:T, F:T, G:T, H:T, I:T, J:T, K:T>(val a: A, val b: B, val c: C, val d: D, val e: E, val f: F, val g: G, val h: H, val i: I, val j: J, val k: K): Tuple<T> {
    override fun toList(): List<T> = listOf(a, b, c, d, e, f, g, h, i, j, k)
}
class Tuple12<T, A:T, B:T, C:T, D:T, E:T, F:T, G:T, H:T, I:T, J:T, K:T, L:T>(val a: A, val b: B, val c: C, val d: D, val e: E, val f: F, val g: G, val h: H, val i: I, val j: J, val k: K, val l: L): Tuple<T> {
    override fun toList(): List<T> = listOf(a, b, c, d, e, f, g, h, i, j, k, l)
}

typealias Array<T> = Tuple<T>
typealias Array0<T> = Tuple0<T>
typealias Array1<T> = Tuple1<T, T>
typealias Array2<T> = Tuple2<T, T, T>
typealias Array3<T> = Tuple3<T, T, T, T>
typealias Array4<T> = Tuple4<T, T, T, T, T>
typealias Array5<T> = Tuple5<T, T, T, T, T, T>
typealias Array6<T> = Tuple6<T, T, T, T, T, T, T>
typealias Array7<T> = Tuple7<T, T, T, T, T, T, T, T>
typealias Array8<T> = Tuple8<T, T, T, T, T, T, T, T, T>
typealias Array9<T> = Tuple9<T, T, T, T, T, T, T, T, T, T>
typealias Array10<T> = Tuple10<T, T, T, T, T, T, T, T, T, T, T>
typealias Array11<T> = Tuple11<T, T, T, T, T, T, T, T, T, T, T, T>
typealias Array12<T> = Tuple12<T, T, T, T, T, T, T, T, T, T, T, T, T>
