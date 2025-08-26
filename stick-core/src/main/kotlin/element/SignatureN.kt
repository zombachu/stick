@file:Suppress("UNCHECKED_CAST")

package com.zombachu.stick.element

import com.zombachu.stick.ExecutionResult
import com.zombachu.stick.Invocation
import com.zombachu.stick.Environment
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

internal class Signature0<E_ : Environment, O>(
    val execute: Invocation<E_, O>.() -> ExecutionResult,
    elements: Tuple0<SignatureConstraint<E_, O, Any>>,
): Signature<E_, O>(elements) {
    override fun executeParsed(context: Invocation<E_, O>, parsedValues: List<Any>): ExecutionResult {
        return context.execute()
    }
}

internal class Signature1<E_ : Environment, O, A : Any>(
    val execute: Invocation<E_, O>.(A) -> ExecutionResult,
    elements: Tuple1<SignatureConstraint<E_, O, Any>,
            SignatureConstraint.Terminating<E_, O, A>>,
): Signature<E_, O>(elements) {
    override fun executeParsed(context: Invocation<E_, O>, parsedValues: List<Any>): ExecutionResult {
        return context.execute(parsedValues[0] as A)
    }
}

internal class Signature2<E_ : Environment, O, A : Any, B : Any>(
    val execute: Invocation<E_, O>.(A, B) -> ExecutionResult,
    elements: Tuple2<SignatureConstraint<E_, O, Any>,
            SignatureConstraint.NonTerminating<E_, O, A>,
            SignatureConstraint.Terminating<E_, O, B>>,
): Signature<E_, O>(elements) {
    override fun executeParsed(context: Invocation<E_, O>, parsedValues: List<Any>): ExecutionResult {
        return context.execute(parsedValues[0] as A, parsedValues[1] as B)
    }
}

internal class Signature3<E_ : Environment, O, A : Any, B : Any, C : Any>(
    val execute: Invocation<E_, O>.(A, B, C) -> ExecutionResult,
    elements: Tuple3<SignatureConstraint<E_, O, Any>,
            SignatureConstraint.NonTerminating<E_, O, A>,
            SignatureConstraint.NonTerminating<E_, O, B>,
            SignatureConstraint.Terminating<E_, O, C>>,
): Signature<E_, O>(elements) {
    override fun executeParsed(context: Invocation<E_, O>, parsedValues: List<Any>): ExecutionResult {
        return context.execute(parsedValues[0] as A, parsedValues[1] as B, parsedValues[2] as C)
    }
}

internal class Signature4<E_ : Environment, O, A : Any, B : Any, C : Any, D : Any>(
    val execute: Invocation<E_, O>.(A, B, C, D) -> ExecutionResult,
    elements: Tuple4<SignatureConstraint<E_, O, Any>,
            SignatureConstraint.NonTerminating<E_, O, A>,
            SignatureConstraint.NonTerminating<E_, O, B>,
            SignatureConstraint.NonTerminating<E_, O, C>,
            SignatureConstraint.Terminating<E_, O, D>>,
): Signature<E_, O>(elements) {
    override fun executeParsed(context: Invocation<E_, O>, parsedValues: List<Any>): ExecutionResult {
        return context.execute(parsedValues[0] as A, parsedValues[1] as B, parsedValues[2] as C, parsedValues[3] as D)
    }
}

internal class Signature5<E_ : Environment, O, A : Any, B : Any, C : Any, D : Any, E : Any>(
    val execute: Invocation<E_, O>.(A, B, C, D, E) -> ExecutionResult,
    elements: Tuple5<SignatureConstraint<E_, O, Any>,
            SignatureConstraint.NonTerminating<E_, O, A>,
            SignatureConstraint.NonTerminating<E_, O, B>,
            SignatureConstraint.NonTerminating<E_, O, C>,
            SignatureConstraint.NonTerminating<E_, O, D>,
            SignatureConstraint.Terminating<E_, O, E>>,
): Signature<E_, O>(elements) {
    override fun executeParsed(context: Invocation<E_, O>, parsedValues: List<Any>): ExecutionResult {
        return context.execute(parsedValues[0] as A, parsedValues[1] as B, parsedValues[2] as C, parsedValues[3] as D,
            parsedValues[4] as E)
    }
}

internal class Signature6<E_ : Environment, O, A : Any, B : Any, C : Any, D : Any, E : Any, F : Any>(
    val execute: Invocation<E_, O>.(A, B, C, D, E, F) -> ExecutionResult,
    elements: Tuple6<SignatureConstraint<E_, O, Any>,
            SignatureConstraint.NonTerminating<E_, O, A>,
            SignatureConstraint.NonTerminating<E_, O, B>,
            SignatureConstraint.NonTerminating<E_, O, C>,
            SignatureConstraint.NonTerminating<E_, O, D>,
            SignatureConstraint.NonTerminating<E_, O, E>,
            SignatureConstraint.Terminating<E_, O, F>>,
): Signature<E_, O>(elements) {
    override fun executeParsed(context: Invocation<E_, O>, parsedValues: List<Any>): ExecutionResult {
        return context.execute(parsedValues[0] as A, parsedValues[1] as B, parsedValues[2] as C, parsedValues[3] as D,
            parsedValues[4] as E, parsedValues[5] as F)
    }
}

internal class Signature7<E_ : Environment, O, A : Any, B : Any, C : Any, D : Any, E : Any, F : Any, G : Any>(
    val execute: Invocation<E_, O>.(A, B, C, D, E, F, G) -> ExecutionResult,
    elements: Tuple7<SignatureConstraint<E_, O, Any>,
            SignatureConstraint.NonTerminating<E_, O, A>,
            SignatureConstraint.NonTerminating<E_, O, B>,
            SignatureConstraint.NonTerminating<E_, O, C>,
            SignatureConstraint.NonTerminating<E_, O, D>,
            SignatureConstraint.NonTerminating<E_, O, E>,
            SignatureConstraint.NonTerminating<E_, O, F>,
            SignatureConstraint.Terminating<E_, O, G>>,
): Signature<E_, O>(elements) {
    override fun executeParsed(context: Invocation<E_, O>, parsedValues: List<Any>): ExecutionResult {
        return context.execute(parsedValues[0] as A, parsedValues[1] as B, parsedValues[2] as C, parsedValues[3] as D,
            parsedValues[4] as E, parsedValues[5] as F, parsedValues[6] as G)
    }
}

internal class Signature8<E_ : Environment, O, A : Any, B : Any, C : Any, D : Any, E : Any, F : Any, G : Any, H : Any>(
    val execute: Invocation<E_, O>.(A, B, C, D, E, F, G, H) -> ExecutionResult,
    elements: Tuple8<SignatureConstraint<E_, O, Any>,
            SignatureConstraint.NonTerminating<E_, O, A>,
            SignatureConstraint.NonTerminating<E_, O, B>,
            SignatureConstraint.NonTerminating<E_, O, C>,
            SignatureConstraint.NonTerminating<E_, O, D>,
            SignatureConstraint.NonTerminating<E_, O, E>,
            SignatureConstraint.NonTerminating<E_, O, F>,
            SignatureConstraint.NonTerminating<E_, O, G>,
            SignatureConstraint.Terminating<E_, O, H>>,
): Signature<E_, O>(elements) {
    override fun executeParsed(context: Invocation<E_, O>, parsedValues: List<Any>): ExecutionResult {
        return context.execute(parsedValues[0] as A, parsedValues[1] as B, parsedValues[2] as C, parsedValues[3] as D,
            parsedValues[4] as E, parsedValues[5] as F, parsedValues[6] as G, parsedValues[7] as H)
    }
}

internal class Signature9<E_ : Environment, O, A : Any, B : Any, C : Any, D : Any, E : Any, F : Any, G : Any, H : Any, I : Any>(
    val execute: Invocation<E_, O>.(A, B, C, D, E, F, G, H, I) -> ExecutionResult,
    elements: Tuple9<SignatureConstraint<E_, O, Any>,
            SignatureConstraint.NonTerminating<E_, O, A>,
            SignatureConstraint.NonTerminating<E_, O, B>,
            SignatureConstraint.NonTerminating<E_, O, C>,
            SignatureConstraint.NonTerminating<E_, O, D>,
            SignatureConstraint.NonTerminating<E_, O, E>,
            SignatureConstraint.NonTerminating<E_, O, F>,
            SignatureConstraint.NonTerminating<E_, O, G>,
            SignatureConstraint.NonTerminating<E_, O, H>,
            SignatureConstraint.Terminating<E_, O, I>>,
): Signature<E_, O>(elements) {
    override fun executeParsed(context: Invocation<E_, O>, parsedValues: List<Any>): ExecutionResult {
        return context.execute(parsedValues[0] as A, parsedValues[1] as B, parsedValues[2] as C, parsedValues[3] as D,
            parsedValues[4] as E, parsedValues[5] as F, parsedValues[6] as G, parsedValues[7] as H,
            parsedValues[8] as I)
    }
}

internal class Signature10<E_ : Environment, O, A : Any, B : Any, C : Any, D : Any, E : Any, F : Any, G : Any, H : Any, I : Any, J : Any>(
    val execute: Invocation<E_, O>.(A, B, C, D, E, F, G, H, I, J) -> ExecutionResult,
    elements: Tuple10<SignatureConstraint<E_, O, Any>,
            SignatureConstraint.NonTerminating<E_, O, A>,
            SignatureConstraint.NonTerminating<E_, O, B>,
            SignatureConstraint.NonTerminating<E_, O, C>,
            SignatureConstraint.NonTerminating<E_, O, D>,
            SignatureConstraint.NonTerminating<E_, O, E>,
            SignatureConstraint.NonTerminating<E_, O, F>,
            SignatureConstraint.NonTerminating<E_, O, G>,
            SignatureConstraint.NonTerminating<E_, O, H>,
            SignatureConstraint.NonTerminating<E_, O, I>,
            SignatureConstraint.Terminating<E_, O, J>>,
): Signature<E_, O>(elements) {
    override fun executeParsed(context: Invocation<E_, O>, parsedValues: List<Any>): ExecutionResult {
        return context.execute(parsedValues[0] as A, parsedValues[1] as B, parsedValues[2] as C, parsedValues[3] as D,
            parsedValues[4] as E, parsedValues[5] as F, parsedValues[6] as G, parsedValues[7] as H,
            parsedValues[8] as I, parsedValues[9] as J)
    }
}

internal class Signature11<E_ : Environment, O, A : Any, B : Any, C : Any, D : Any, E : Any, F : Any, G : Any, H : Any, I : Any, J : Any,
        K : Any>(
    val execute: Invocation<E_, O>.(A, B, C, D, E, F, G, H, I, J, K) -> ExecutionResult,
    elements: Tuple11<SignatureConstraint<E_, O, Any>,
            SignatureConstraint.NonTerminating<E_, O, A>,
            SignatureConstraint.NonTerminating<E_, O, B>,
            SignatureConstraint.NonTerminating<E_, O, C>,
            SignatureConstraint.NonTerminating<E_, O, D>,
            SignatureConstraint.NonTerminating<E_, O, E>,
            SignatureConstraint.NonTerminating<E_, O, F>,
            SignatureConstraint.NonTerminating<E_, O, G>,
            SignatureConstraint.NonTerminating<E_, O, H>,
            SignatureConstraint.NonTerminating<E_, O, I>,
            SignatureConstraint.NonTerminating<E_, O, J>,
            SignatureConstraint.Terminating<E_, O, K>>,
): Signature<E_, O>(elements) {
    override fun executeParsed(context: Invocation<E_, O>, parsedValues: List<Any>): ExecutionResult {
        return context.execute(parsedValues[0] as A, parsedValues[1] as B, parsedValues[2] as C, parsedValues[3] as D,
            parsedValues[4] as E, parsedValues[5] as F, parsedValues[6] as G, parsedValues[7] as H,
            parsedValues[8] as I, parsedValues[9] as J, parsedValues[10] as K)
    }
}

internal class Signature12<E_ : Environment, O, A : Any, B : Any, C : Any, D : Any, E : Any, F : Any, G : Any, H : Any, I : Any, J : Any,
        K : Any, L : Any>(
    val execute: Invocation<E_, O>.(A, B, C, D, E, F, G, H, I, J, K, L) -> ExecutionResult,
    elements: Tuple12<SignatureConstraint<E_, O, Any>,
            SignatureConstraint.NonTerminating<E_, O, A>,
            SignatureConstraint.NonTerminating<E_, O, B>,
            SignatureConstraint.NonTerminating<E_, O, C>,
            SignatureConstraint.NonTerminating<E_, O, D>,
            SignatureConstraint.NonTerminating<E_, O, E>,
            SignatureConstraint.NonTerminating<E_, O, F>,
            SignatureConstraint.NonTerminating<E_, O, G>,
            SignatureConstraint.NonTerminating<E_, O, H>,
            SignatureConstraint.NonTerminating<E_, O, I>,
            SignatureConstraint.NonTerminating<E_, O, J>,
            SignatureConstraint.NonTerminating<E_, O, K>,
            SignatureConstraint.Terminating<E_, O, L>>,
): Signature<E_, O>(elements) {
    override fun executeParsed(context: Invocation<E_, O>, parsedValues: List<Any>): ExecutionResult {
        return context.execute(parsedValues[0] as A, parsedValues[1] as B, parsedValues[2] as C, parsedValues[3] as D,
            parsedValues[4] as E, parsedValues[5] as F, parsedValues[6] as G, parsedValues[7] as H,
            parsedValues[8] as I, parsedValues[9] as J, parsedValues[10] as K, parsedValues[11] as L)
    }
}
