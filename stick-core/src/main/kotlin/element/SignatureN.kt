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

internal class Signature0<S : Environment, O>(
    val execute: Invocation<S, O>.() -> ExecutionResult,
    elements: Tuple0<SignatureConstraint<S, O, Any>>,
): Signature<S, O>(elements) {
    override fun executeParsed(context: Invocation<S, O>, parsedValues: List<Any>): ExecutionResult {
        return context.execute()
    }
}

internal class Signature1<S : Environment, O, A : Any>(
    val execute: Invocation<S, O>.(A) -> ExecutionResult,
    elements: Tuple1<SignatureConstraint<S, O, Any>,
            SignatureConstraint.Terminating<S, O, A>>,
): Signature<S, O>(elements) {
    override fun executeParsed(context: Invocation<S, O>, parsedValues: List<Any>): ExecutionResult {
        return context.execute(parsedValues[0] as A)
    }
}

internal class Signature2<S : Environment, O, A : Any, B : Any>(
    val execute: Invocation<S, O>.(A, B) -> ExecutionResult,
    elements: Tuple2<SignatureConstraint<S, O, Any>,
            SignatureConstraint.NonTerminating<S, O, A>,
            SignatureConstraint.Terminating<S, O, B>>,
): Signature<S, O>(elements) {
    override fun executeParsed(context: Invocation<S, O>, parsedValues: List<Any>): ExecutionResult {
        return context.execute(parsedValues[0] as A, parsedValues[1] as B)
    }
}

internal class Signature3<S : Environment, O, A : Any, B : Any, C : Any>(
    val execute: Invocation<S, O>.(A, B, C) -> ExecutionResult,
    elements: Tuple3<SignatureConstraint<S, O, Any>,
            SignatureConstraint.NonTerminating<S, O, A>,
            SignatureConstraint.NonTerminating<S, O, B>,
            SignatureConstraint.Terminating<S, O, C>>,
): Signature<S, O>(elements) {
    override fun executeParsed(context: Invocation<S, O>, parsedValues: List<Any>): ExecutionResult {
        return context.execute(parsedValues[0] as A, parsedValues[1] as B, parsedValues[2] as C)
    }
}

internal class Signature4<S : Environment, O, A : Any, B : Any, C : Any, D : Any>(
    val execute: Invocation<S, O>.(A, B, C, D) -> ExecutionResult,
    elements: Tuple4<SignatureConstraint<S, O, Any>,
            SignatureConstraint.NonTerminating<S, O, A>,
            SignatureConstraint.NonTerminating<S, O, B>,
            SignatureConstraint.NonTerminating<S, O, C>,
            SignatureConstraint.Terminating<S, O, D>>,
): Signature<S, O>(elements) {
    override fun executeParsed(context: Invocation<S, O>, parsedValues: List<Any>): ExecutionResult {
        return context.execute(parsedValues[0] as A, parsedValues[1] as B, parsedValues[2] as C, parsedValues[3] as D)
    }
}

internal class Signature5<S : Environment, O, A : Any, B : Any, C : Any, D : Any, E : Any>(
    val execute: Invocation<S, O>.(A, B, C, D, E) -> ExecutionResult,
    elements: Tuple5<SignatureConstraint<S, O, Any>,
            SignatureConstraint.NonTerminating<S, O, A>,
            SignatureConstraint.NonTerminating<S, O, B>,
            SignatureConstraint.NonTerminating<S, O, C>,
            SignatureConstraint.NonTerminating<S, O, D>,
            SignatureConstraint.Terminating<S, O, E>>,
): Signature<S, O>(elements) {
    override fun executeParsed(context: Invocation<S, O>, parsedValues: List<Any>): ExecutionResult {
        return context.execute(parsedValues[0] as A, parsedValues[1] as B, parsedValues[2] as C, parsedValues[3] as D,
            parsedValues[4] as E)
    }
}

internal class Signature6<S : Environment, O, A : Any, B : Any, C : Any, D : Any, E : Any, F : Any>(
    val execute: Invocation<S, O>.(A, B, C, D, E, F) -> ExecutionResult,
    elements: Tuple6<SignatureConstraint<S, O, Any>,
            SignatureConstraint.NonTerminating<S, O, A>,
            SignatureConstraint.NonTerminating<S, O, B>,
            SignatureConstraint.NonTerminating<S, O, C>,
            SignatureConstraint.NonTerminating<S, O, D>,
            SignatureConstraint.NonTerminating<S, O, E>,
            SignatureConstraint.Terminating<S, O, F>>,
): Signature<S, O>(elements) {
    override fun executeParsed(context: Invocation<S, O>, parsedValues: List<Any>): ExecutionResult {
        return context.execute(parsedValues[0] as A, parsedValues[1] as B, parsedValues[2] as C, parsedValues[3] as D,
            parsedValues[4] as E, parsedValues[5] as F)
    }
}

internal class Signature7<S : Environment, O, A : Any, B : Any, C : Any, D : Any, E : Any, F : Any, G : Any>(
    val execute: Invocation<S, O>.(A, B, C, D, E, F, G) -> ExecutionResult,
    elements: Tuple7<SignatureConstraint<S, O, Any>,
            SignatureConstraint.NonTerminating<S, O, A>,
            SignatureConstraint.NonTerminating<S, O, B>,
            SignatureConstraint.NonTerminating<S, O, C>,
            SignatureConstraint.NonTerminating<S, O, D>,
            SignatureConstraint.NonTerminating<S, O, E>,
            SignatureConstraint.NonTerminating<S, O, F>,
            SignatureConstraint.Terminating<S, O, G>>,
): Signature<S, O>(elements) {
    override fun executeParsed(context: Invocation<S, O>, parsedValues: List<Any>): ExecutionResult {
        return context.execute(parsedValues[0] as A, parsedValues[1] as B, parsedValues[2] as C, parsedValues[3] as D,
            parsedValues[4] as E, parsedValues[5] as F, parsedValues[6] as G)
    }
}

internal class Signature8<S : Environment, O, A : Any, B : Any, C : Any, D : Any, E : Any, F : Any, G : Any, H : Any>(
    val execute: Invocation<S, O>.(A, B, C, D, E, F, G, H) -> ExecutionResult,
    elements: Tuple8<SignatureConstraint<S, O, Any>,
            SignatureConstraint.NonTerminating<S, O, A>,
            SignatureConstraint.NonTerminating<S, O, B>,
            SignatureConstraint.NonTerminating<S, O, C>,
            SignatureConstraint.NonTerminating<S, O, D>,
            SignatureConstraint.NonTerminating<S, O, E>,
            SignatureConstraint.NonTerminating<S, O, F>,
            SignatureConstraint.NonTerminating<S, O, G>,
            SignatureConstraint.Terminating<S, O, H>>,
): Signature<S, O>(elements) {
    override fun executeParsed(context: Invocation<S, O>, parsedValues: List<Any>): ExecutionResult {
        return context.execute(parsedValues[0] as A, parsedValues[1] as B, parsedValues[2] as C, parsedValues[3] as D,
            parsedValues[4] as E, parsedValues[5] as F, parsedValues[6] as G, parsedValues[7] as H)
    }
}

internal class Signature9<S : Environment, O, A : Any, B : Any, C : Any, D : Any, E : Any, F : Any, G : Any, H : Any, I : Any>(
    val execute: Invocation<S, O>.(A, B, C, D, E, F, G, H, I) -> ExecutionResult,
    elements: Tuple9<SignatureConstraint<S, O, Any>,
            SignatureConstraint.NonTerminating<S, O, A>,
            SignatureConstraint.NonTerminating<S, O, B>,
            SignatureConstraint.NonTerminating<S, O, C>,
            SignatureConstraint.NonTerminating<S, O, D>,
            SignatureConstraint.NonTerminating<S, O, E>,
            SignatureConstraint.NonTerminating<S, O, F>,
            SignatureConstraint.NonTerminating<S, O, G>,
            SignatureConstraint.NonTerminating<S, O, H>,
            SignatureConstraint.Terminating<S, O, I>>,
): Signature<S, O>(elements) {
    override fun executeParsed(context: Invocation<S, O>, parsedValues: List<Any>): ExecutionResult {
        return context.execute(parsedValues[0] as A, parsedValues[1] as B, parsedValues[2] as C, parsedValues[3] as D,
            parsedValues[4] as E, parsedValues[5] as F, parsedValues[6] as G, parsedValues[7] as H,
            parsedValues[8] as I)
    }
}

internal class Signature10<S : Environment, O, A : Any, B : Any, C : Any, D : Any, E : Any, F : Any, G : Any, H : Any, I : Any, J : Any>(
    val execute: Invocation<S, O>.(A, B, C, D, E, F, G, H, I, J) -> ExecutionResult,
    elements: Tuple10<SignatureConstraint<S, O, Any>,
            SignatureConstraint.NonTerminating<S, O, A>,
            SignatureConstraint.NonTerminating<S, O, B>,
            SignatureConstraint.NonTerminating<S, O, C>,
            SignatureConstraint.NonTerminating<S, O, D>,
            SignatureConstraint.NonTerminating<S, O, E>,
            SignatureConstraint.NonTerminating<S, O, F>,
            SignatureConstraint.NonTerminating<S, O, G>,
            SignatureConstraint.NonTerminating<S, O, H>,
            SignatureConstraint.NonTerminating<S, O, I>,
            SignatureConstraint.Terminating<S, O, J>>,
): Signature<S, O>(elements) {
    override fun executeParsed(context: Invocation<S, O>, parsedValues: List<Any>): ExecutionResult {
        return context.execute(parsedValues[0] as A, parsedValues[1] as B, parsedValues[2] as C, parsedValues[3] as D,
            parsedValues[4] as E, parsedValues[5] as F, parsedValues[6] as G, parsedValues[7] as H,
            parsedValues[8] as I, parsedValues[9] as J)
    }
}

internal class Signature11<S : Environment, O, A : Any, B : Any, C : Any, D : Any, E : Any, F : Any, G : Any, H : Any, I : Any, J : Any,
        K : Any>(
    val execute: Invocation<S, O>.(A, B, C, D, E, F, G, H, I, J, K) -> ExecutionResult,
    elements: Tuple11<SignatureConstraint<S, O, Any>,
            SignatureConstraint.NonTerminating<S, O, A>,
            SignatureConstraint.NonTerminating<S, O, B>,
            SignatureConstraint.NonTerminating<S, O, C>,
            SignatureConstraint.NonTerminating<S, O, D>,
            SignatureConstraint.NonTerminating<S, O, E>,
            SignatureConstraint.NonTerminating<S, O, F>,
            SignatureConstraint.NonTerminating<S, O, G>,
            SignatureConstraint.NonTerminating<S, O, H>,
            SignatureConstraint.NonTerminating<S, O, I>,
            SignatureConstraint.NonTerminating<S, O, J>,
            SignatureConstraint.Terminating<S, O, K>>,
): Signature<S, O>(elements) {
    override fun executeParsed(context: Invocation<S, O>, parsedValues: List<Any>): ExecutionResult {
        return context.execute(parsedValues[0] as A, parsedValues[1] as B, parsedValues[2] as C, parsedValues[3] as D,
            parsedValues[4] as E, parsedValues[5] as F, parsedValues[6] as G, parsedValues[7] as H,
            parsedValues[8] as I, parsedValues[9] as J, parsedValues[10] as K)
    }
}

internal class Signature12<S : Environment, O, A : Any, B : Any, C : Any, D : Any, E : Any, F : Any, G : Any, H : Any, I : Any, J : Any,
        K : Any, L : Any>(
    val execute: Invocation<S, O>.(A, B, C, D, E, F, G, H, I, J, K, L) -> ExecutionResult,
    elements: Tuple12<SignatureConstraint<S, O, Any>,
            SignatureConstraint.NonTerminating<S, O, A>,
            SignatureConstraint.NonTerminating<S, O, B>,
            SignatureConstraint.NonTerminating<S, O, C>,
            SignatureConstraint.NonTerminating<S, O, D>,
            SignatureConstraint.NonTerminating<S, O, E>,
            SignatureConstraint.NonTerminating<S, O, F>,
            SignatureConstraint.NonTerminating<S, O, G>,
            SignatureConstraint.NonTerminating<S, O, H>,
            SignatureConstraint.NonTerminating<S, O, I>,
            SignatureConstraint.NonTerminating<S, O, J>,
            SignatureConstraint.NonTerminating<S, O, K>,
            SignatureConstraint.Terminating<S, O, L>>,
): Signature<S, O>(elements) {
    override fun executeParsed(context: Invocation<S, O>, parsedValues: List<Any>): ExecutionResult {
        return context.execute(parsedValues[0] as A, parsedValues[1] as B, parsedValues[2] as C, parsedValues[3] as D,
            parsedValues[4] as E, parsedValues[5] as F, parsedValues[6] as G, parsedValues[7] as H,
            parsedValues[8] as I, parsedValues[9] as J, parsedValues[10] as K, parsedValues[11] as L)
    }
}
