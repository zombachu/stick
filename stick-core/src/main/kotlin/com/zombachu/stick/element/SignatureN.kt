@file:Suppress("UNCHECKED_CAST")

package com.zombachu.stick.element

import com.zombachu.stick.Environment
import com.zombachu.stick.Invocation

internal class Signature0<E_ : Environment, S>(
    val execute: Invocation<E_, S>.() -> Unit,
    elements: List<SignatureConstraint<E_, S, Any?>>,
): Signature<E_, S>(elements) {
    internal constructor() : this({ }, emptyList())
    override fun executeParsed(context: Invocation<E_, S>, parsedValues: List<Any?>) {
        return context.execute()
    }
}

internal class Signature1<E_ : Environment, S, A>(
    val execute: Invocation<E_, S>.(A) -> Unit,
    elements: List<SignatureConstraint<E_, S, Any?>>,
): Signature<E_, S>(elements) {
    override fun executeParsed(context: Invocation<E_, S>, parsedValues: List<Any?>) {
        return context.execute(parsedValues[0] as A)
    }
}

internal class Signature2<E_ : Environment, S, A, B>(
    val execute: Invocation<E_, S>.(A, B) -> Unit,
    elements: List<SignatureConstraint<E_, S, Any?>>,
): Signature<E_, S>(elements) {
    override fun executeParsed(context: Invocation<E_, S>, parsedValues: List<Any?>) {
        return context.execute(parsedValues[0] as A, parsedValues[1] as B)
    }
}

internal class Signature3<E_ : Environment, S, A, B, C>(
    val execute: Invocation<E_, S>.(A, B, C) -> Unit,
    elements: List<SignatureConstraint<E_, S, Any?>>,
): Signature<E_, S>(elements) {
    override fun executeParsed(context: Invocation<E_, S>, parsedValues: List<Any?>) {
        return context.execute(parsedValues[0] as A, parsedValues[1] as B, parsedValues[2] as C)
    }
}

internal class Signature4<E_ : Environment, S, A, B, C, D>(
    val execute: Invocation<E_, S>.(A, B, C, D) -> Unit,
    elements: List<SignatureConstraint<E_, S, Any?>>,
): Signature<E_, S>(elements) {
    override fun executeParsed(context: Invocation<E_, S>, parsedValues: List<Any?>) {
        return context.execute(parsedValues[0] as A, parsedValues[1] as B, parsedValues[2] as C, parsedValues[3] as D)
    }
}

internal class Signature5<E_ : Environment, S, A, B, C, D, E>(
    val execute: Invocation<E_, S>.(A, B, C, D, E) -> Unit,
    elements: List<SignatureConstraint<E_, S, Any?>>,
): Signature<E_, S>(elements) {
    override fun executeParsed(context: Invocation<E_, S>, parsedValues: List<Any?>) {
        return context.execute(parsedValues[0] as A, parsedValues[1] as B, parsedValues[2] as C, parsedValues[3] as D,
            parsedValues[4] as E)
    }
}

internal class Signature6<E_ : Environment, S, A, B, C, D, E, F>(
    val execute: Invocation<E_, S>.(A, B, C, D, E, F) -> Unit,
    elements: List<SignatureConstraint<E_, S, Any?>>,
): Signature<E_, S>(elements) {
    override fun executeParsed(context: Invocation<E_, S>, parsedValues: List<Any?>) {
        return context.execute(parsedValues[0] as A, parsedValues[1] as B, parsedValues[2] as C, parsedValues[3] as D,
            parsedValues[4] as E, parsedValues[5] as F)
    }
}

internal class Signature7<E_ : Environment, S, A, B, C, D, E, F, G>(
    val execute: Invocation<E_, S>.(A, B, C, D, E, F, G) -> Unit,
    elements: List<SignatureConstraint<E_, S, Any?>>,
): Signature<E_, S>(elements) {
    override fun executeParsed(context: Invocation<E_, S>, parsedValues: List<Any?>) {
        return context.execute(parsedValues[0] as A, parsedValues[1] as B, parsedValues[2] as C, parsedValues[3] as D,
            parsedValues[4] as E, parsedValues[5] as F, parsedValues[6] as G)
    }
}

internal class Signature8<E_ : Environment, S, A, B, C, D, E, F, G, H>(
    val execute: Invocation<E_, S>.(A, B, C, D, E, F, G, H) -> Unit,
    elements: List<SignatureConstraint<E_, S, Any?>>,
): Signature<E_, S>(elements) {
    override fun executeParsed(context: Invocation<E_, S>, parsedValues: List<Any?>) {
        return context.execute(parsedValues[0] as A, parsedValues[1] as B, parsedValues[2] as C, parsedValues[3] as D,
            parsedValues[4] as E, parsedValues[5] as F, parsedValues[6] as G, parsedValues[7] as H)
    }
}

internal class Signature9<E_ : Environment, S, A, B, C, D, E, F, G, H, I>(
    val execute: Invocation<E_, S>.(A, B, C, D, E, F, G, H, I) -> Unit,
    elements: List<SignatureConstraint<E_, S, Any?>>,
): Signature<E_, S>(elements) {
    override fun executeParsed(context: Invocation<E_, S>, parsedValues: List<Any?>) {
        return context.execute(parsedValues[0] as A, parsedValues[1] as B, parsedValues[2] as C, parsedValues[3] as D,
            parsedValues[4] as E, parsedValues[5] as F, parsedValues[6] as G, parsedValues[7] as H,
            parsedValues[8] as I)
    }
}

internal class Signature10<E_ : Environment, S, A, B, C, D, E, F, G, H, I, J>(
    val execute: Invocation<E_, S>.(A, B, C, D, E, F, G, H, I, J) -> Unit,
    elements: List<SignatureConstraint<E_, S, Any?>>,
): Signature<E_, S>(elements) {
    override fun executeParsed(context: Invocation<E_, S>, parsedValues: List<Any?>) {
        return context.execute(parsedValues[0] as A, parsedValues[1] as B, parsedValues[2] as C, parsedValues[3] as D,
            parsedValues[4] as E, parsedValues[5] as F, parsedValues[6] as G, parsedValues[7] as H,
            parsedValues[8] as I, parsedValues[9] as J)
    }
}

internal class Signature11<E_ : Environment, S, A, B, C, D, E, F, G, H, I, J, K>(
    val execute: Invocation<E_, S>.(A, B, C, D, E, F, G, H, I, J, K) -> Unit,
    elements: List<SignatureConstraint<E_, S, Any?>>,
): Signature<E_, S>(elements) {
    override fun executeParsed(context: Invocation<E_, S>, parsedValues: List<Any?>) {
        return context.execute(parsedValues[0] as A, parsedValues[1] as B, parsedValues[2] as C, parsedValues[3] as D,
            parsedValues[4] as E, parsedValues[5] as F, parsedValues[6] as G, parsedValues[7] as H,
            parsedValues[8] as I, parsedValues[9] as J, parsedValues[10] as K)
    }
}

internal class Signature12<E_ : Environment, S, A, B, C, D, E, F, G, H, I, J, K, L>(
    val execute: Invocation<E_, S>.(A, B, C, D, E, F, G, H, I, J, K, L) -> Unit,
    elements: List<SignatureConstraint<E_, S, Any?>>,
): Signature<E_, S>(elements) {
    override fun executeParsed(context: Invocation<E_, S>, parsedValues: List<Any?>) {
        return context.execute(parsedValues[0] as A, parsedValues[1] as B, parsedValues[2] as C, parsedValues[3] as D,
            parsedValues[4] as E, parsedValues[5] as F, parsedValues[6] as G, parsedValues[7] as H,
            parsedValues[8] as I, parsedValues[9] as J, parsedValues[10] as K, parsedValues[11] as L)
    }
}
