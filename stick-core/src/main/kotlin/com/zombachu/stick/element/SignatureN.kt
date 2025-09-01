@file:Suppress("UNCHECKED_CAST")

package com.zombachu.stick.element

import com.zombachu.stick.Environment
import com.zombachu.stick.Invocation
import com.zombachu.stick.impl.Tuple
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

internal class Signature0<E_ : Environment, S>(
    val execute: Invocation<E_, S>.() -> Unit,
    elements: Tuple0<SignatureConstraint<E_, S, out Any>>,
): Signature<E_, S>(elements as Tuple<SignatureConstraint<E_, S, Any>>) {
    internal constructor() : this({ }, Tuple0())
    override fun executeParsed(context: Invocation<E_, S>, parsedValues: List<Any>) {
        return context.execute()
    }
}

internal class Signature1<E_ : Environment, S, A : Any>(
    val execute: Invocation<E_, S>.(A) -> Unit,
    elements: Tuple1<SignatureConstraint<E_, S, out Any>,
            SignatureConstraint.Terminating<E_, S, A>>,
): Signature<E_, S>(elements as Tuple<SignatureConstraint<E_, S, Any>>) {
    override fun executeParsed(context: Invocation<E_, S>, parsedValues: List<Any>) {
        return context.execute(parsedValues[0] as A)
    }
}

internal class Signature2<E_ : Environment, S, A : Any, B : Any>(
    val execute: Invocation<E_, S>.(A, B) -> Unit,
    elements: Tuple2<SignatureConstraint<E_, S, out Any>,
            SignatureConstraint.NonTerminating<E_, S, A>,
            SignatureConstraint.Terminating<E_, S, B>>,
): Signature<E_, S>(elements as Tuple<SignatureConstraint<E_, S, Any>>) {
    override fun executeParsed(context: Invocation<E_, S>, parsedValues: List<Any>) {
        return context.execute(parsedValues[0] as A, parsedValues[1] as B)
    }
}

internal class Signature3<E_ : Environment, S, A : Any, B : Any, C : Any>(
    val execute: Invocation<E_, S>.(A, B, C) -> Unit,
    elements: Tuple3<SignatureConstraint<E_, S, out Any>,
            SignatureConstraint.NonTerminating<E_, S, A>,
            SignatureConstraint.NonTerminating<E_, S, B>,
            SignatureConstraint.Terminating<E_, S, C>>,
): Signature<E_, S>(elements as Tuple<SignatureConstraint<E_, S, Any>>) {
    override fun executeParsed(context: Invocation<E_, S>, parsedValues: List<Any>) {
        return context.execute(parsedValues[0] as A, parsedValues[1] as B, parsedValues[2] as C)
    }
}

internal class Signature4<E_ : Environment, S, A : Any, B : Any, C : Any, D : Any>(
    val execute: Invocation<E_, S>.(A, B, C, D) -> Unit,
    elements: Tuple4<SignatureConstraint<E_, S, out Any>,
            SignatureConstraint.NonTerminating<E_, S, A>,
            SignatureConstraint.NonTerminating<E_, S, B>,
            SignatureConstraint.NonTerminating<E_, S, C>,
            SignatureConstraint.Terminating<E_, S, D>>,
): Signature<E_, S>(elements as Tuple<SignatureConstraint<E_, S, Any>>) {
    override fun executeParsed(context: Invocation<E_, S>, parsedValues: List<Any>) {
        return context.execute(parsedValues[0] as A, parsedValues[1] as B, parsedValues[2] as C, parsedValues[3] as D)
    }
}

internal class Signature5<E_ : Environment, S, A : Any, B : Any, C : Any, D : Any, E : Any>(
    val execute: Invocation<E_, S>.(A, B, C, D, E) -> Unit,
    elements: Tuple5<SignatureConstraint<E_, S, out Any>,
            SignatureConstraint.NonTerminating<E_, S, A>,
            SignatureConstraint.NonTerminating<E_, S, B>,
            SignatureConstraint.NonTerminating<E_, S, C>,
            SignatureConstraint.NonTerminating<E_, S, D>,
            SignatureConstraint.Terminating<E_, S, E>>,
): Signature<E_, S>(elements as Tuple<SignatureConstraint<E_, S, Any>>) {
    override fun executeParsed(context: Invocation<E_, S>, parsedValues: List<Any>) {
        return context.execute(parsedValues[0] as A, parsedValues[1] as B, parsedValues[2] as C, parsedValues[3] as D,
            parsedValues[4] as E)
    }
}

internal class Signature6<E_ : Environment, S, A : Any, B : Any, C : Any, D : Any, E : Any, F : Any>(
    val execute: Invocation<E_, S>.(A, B, C, D, E, F) -> Unit,
    elements: Tuple6<SignatureConstraint<E_, S, out Any>,
            SignatureConstraint.NonTerminating<E_, S, A>,
            SignatureConstraint.NonTerminating<E_, S, B>,
            SignatureConstraint.NonTerminating<E_, S, C>,
            SignatureConstraint.NonTerminating<E_, S, D>,
            SignatureConstraint.NonTerminating<E_, S, E>,
            SignatureConstraint.Terminating<E_, S, F>>,
): Signature<E_, S>(elements as Tuple<SignatureConstraint<E_, S, Any>>) {
    override fun executeParsed(context: Invocation<E_, S>, parsedValues: List<Any>) {
        return context.execute(parsedValues[0] as A, parsedValues[1] as B, parsedValues[2] as C, parsedValues[3] as D,
            parsedValues[4] as E, parsedValues[5] as F)
    }
}

internal class Signature7<E_ : Environment, S, A : Any, B : Any, C : Any, D : Any, E : Any, F : Any, G : Any>(
    val execute: Invocation<E_, S>.(A, B, C, D, E, F, G) -> Unit,
    elements: Tuple7<SignatureConstraint<E_, S, out Any>,
            SignatureConstraint.NonTerminating<E_, S, A>,
            SignatureConstraint.NonTerminating<E_, S, B>,
            SignatureConstraint.NonTerminating<E_, S, C>,
            SignatureConstraint.NonTerminating<E_, S, D>,
            SignatureConstraint.NonTerminating<E_, S, E>,
            SignatureConstraint.NonTerminating<E_, S, F>,
            SignatureConstraint.Terminating<E_, S, G>>,
): Signature<E_, S>(elements as Tuple<SignatureConstraint<E_, S, Any>>) {
    override fun executeParsed(context: Invocation<E_, S>, parsedValues: List<Any>) {
        return context.execute(parsedValues[0] as A, parsedValues[1] as B, parsedValues[2] as C, parsedValues[3] as D,
            parsedValues[4] as E, parsedValues[5] as F, parsedValues[6] as G)
    }
}

internal class Signature8<E_ : Environment, S, A : Any, B : Any, C : Any, D : Any, E : Any, F : Any, G : Any, H : Any>(
    val execute: Invocation<E_, S>.(A, B, C, D, E, F, G, H) -> Unit,
    elements: Tuple8<SignatureConstraint<E_, S, out Any>,
            SignatureConstraint.NonTerminating<E_, S, A>,
            SignatureConstraint.NonTerminating<E_, S, B>,
            SignatureConstraint.NonTerminating<E_, S, C>,
            SignatureConstraint.NonTerminating<E_, S, D>,
            SignatureConstraint.NonTerminating<E_, S, E>,
            SignatureConstraint.NonTerminating<E_, S, F>,
            SignatureConstraint.NonTerminating<E_, S, G>,
            SignatureConstraint.Terminating<E_, S, H>>,
): Signature<E_, S>(elements as Tuple<SignatureConstraint<E_, S, Any>>) {
    override fun executeParsed(context: Invocation<E_, S>, parsedValues: List<Any>) {
        return context.execute(parsedValues[0] as A, parsedValues[1] as B, parsedValues[2] as C, parsedValues[3] as D,
            parsedValues[4] as E, parsedValues[5] as F, parsedValues[6] as G, parsedValues[7] as H)
    }
}

internal class Signature9<E_ : Environment, S, A : Any, B : Any, C : Any, D : Any, E : Any, F : Any, G : Any, H : Any, I : Any>(
    val execute: Invocation<E_, S>.(A, B, C, D, E, F, G, H, I) -> Unit,
    elements: Tuple9<SignatureConstraint<E_, S, out Any>,
            SignatureConstraint.NonTerminating<E_, S, A>,
            SignatureConstraint.NonTerminating<E_, S, B>,
            SignatureConstraint.NonTerminating<E_, S, C>,
            SignatureConstraint.NonTerminating<E_, S, D>,
            SignatureConstraint.NonTerminating<E_, S, E>,
            SignatureConstraint.NonTerminating<E_, S, F>,
            SignatureConstraint.NonTerminating<E_, S, G>,
            SignatureConstraint.NonTerminating<E_, S, H>,
            SignatureConstraint.Terminating<E_, S, I>>,
): Signature<E_, S>(elements as Tuple<SignatureConstraint<E_, S, Any>>) {
    override fun executeParsed(context: Invocation<E_, S>, parsedValues: List<Any>) {
        return context.execute(parsedValues[0] as A, parsedValues[1] as B, parsedValues[2] as C, parsedValues[3] as D,
            parsedValues[4] as E, parsedValues[5] as F, parsedValues[6] as G, parsedValues[7] as H,
            parsedValues[8] as I)
    }
}

internal class Signature10<E_ : Environment, S, A : Any, B : Any, C : Any, D : Any, E : Any, F : Any, G : Any, H : Any, I : Any, J : Any>(
    val execute: Invocation<E_, S>.(A, B, C, D, E, F, G, H, I, J) -> Unit,
    elements: Tuple10<SignatureConstraint<E_, S, out Any>,
            SignatureConstraint.NonTerminating<E_, S, A>,
            SignatureConstraint.NonTerminating<E_, S, B>,
            SignatureConstraint.NonTerminating<E_, S, C>,
            SignatureConstraint.NonTerminating<E_, S, D>,
            SignatureConstraint.NonTerminating<E_, S, E>,
            SignatureConstraint.NonTerminating<E_, S, F>,
            SignatureConstraint.NonTerminating<E_, S, G>,
            SignatureConstraint.NonTerminating<E_, S, H>,
            SignatureConstraint.NonTerminating<E_, S, I>,
            SignatureConstraint.Terminating<E_, S, J>>,
): Signature<E_, S>(elements as Tuple<SignatureConstraint<E_, S, Any>>) {
    override fun executeParsed(context: Invocation<E_, S>, parsedValues: List<Any>) {
        return context.execute(parsedValues[0] as A, parsedValues[1] as B, parsedValues[2] as C, parsedValues[3] as D,
            parsedValues[4] as E, parsedValues[5] as F, parsedValues[6] as G, parsedValues[7] as H,
            parsedValues[8] as I, parsedValues[9] as J)
    }
}

internal class Signature11<E_ : Environment, S, A : Any, B : Any, C : Any, D : Any, E : Any, F : Any, G : Any, H : Any, I : Any, J : Any,
        K : Any>(
    val execute: Invocation<E_, S>.(A, B, C, D, E, F, G, H, I, J, K) -> Unit,
    elements: Tuple11<SignatureConstraint<E_, S, out Any>,
            SignatureConstraint.NonTerminating<E_, S, A>,
            SignatureConstraint.NonTerminating<E_, S, B>,
            SignatureConstraint.NonTerminating<E_, S, C>,
            SignatureConstraint.NonTerminating<E_, S, D>,
            SignatureConstraint.NonTerminating<E_, S, E>,
            SignatureConstraint.NonTerminating<E_, S, F>,
            SignatureConstraint.NonTerminating<E_, S, G>,
            SignatureConstraint.NonTerminating<E_, S, H>,
            SignatureConstraint.NonTerminating<E_, S, I>,
            SignatureConstraint.NonTerminating<E_, S, J>,
            SignatureConstraint.Terminating<E_, S, K>>,
): Signature<E_, S>(elements as Tuple<SignatureConstraint<E_, S, Any>>) {
    override fun executeParsed(context: Invocation<E_, S>, parsedValues: List<Any>) {
        return context.execute(parsedValues[0] as A, parsedValues[1] as B, parsedValues[2] as C, parsedValues[3] as D,
            parsedValues[4] as E, parsedValues[5] as F, parsedValues[6] as G, parsedValues[7] as H,
            parsedValues[8] as I, parsedValues[9] as J, parsedValues[10] as K)
    }
}

internal class Signature12<E_ : Environment, S, A : Any, B : Any, C : Any, D : Any, E : Any, F : Any, G : Any, H : Any, I : Any, J : Any,
        K : Any, L : Any>(
    val execute: Invocation<E_, S>.(A, B, C, D, E, F, G, H, I, J, K, L) -> Unit,
    elements: Tuple12<SignatureConstraint<E_, S, out Any>,
            SignatureConstraint.NonTerminating<E_, S, A>,
            SignatureConstraint.NonTerminating<E_, S, B>,
            SignatureConstraint.NonTerminating<E_, S, C>,
            SignatureConstraint.NonTerminating<E_, S, D>,
            SignatureConstraint.NonTerminating<E_, S, E>,
            SignatureConstraint.NonTerminating<E_, S, F>,
            SignatureConstraint.NonTerminating<E_, S, G>,
            SignatureConstraint.NonTerminating<E_, S, H>,
            SignatureConstraint.NonTerminating<E_, S, I>,
            SignatureConstraint.NonTerminating<E_, S, J>,
            SignatureConstraint.NonTerminating<E_, S, K>,
            SignatureConstraint.Terminating<E_, S, L>>,
): Signature<E_, S>(elements as Tuple<SignatureConstraint<E_, S, Any>>) {
    override fun executeParsed(context: Invocation<E_, S>, parsedValues: List<Any>) {
        return context.execute(parsedValues[0] as A, parsedValues[1] as B, parsedValues[2] as C, parsedValues[3] as D,
            parsedValues[4] as E, parsedValues[5] as F, parsedValues[6] as G, parsedValues[7] as H,
            parsedValues[8] as I, parsedValues[9] as J, parsedValues[10] as K, parsedValues[11] as L)
    }
}
