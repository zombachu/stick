package com.zombachu.stick.impl

import com.zombachu.stick.ExecutionContext
import com.zombachu.stick.ExecutionResult

internal class Signature0<S>(
    val execute: ExecutionContext<S>.() -> ExecutionResult,
    elements: Tuple0<SignatureConstraint<S, Any>>,
): Signature<S>(elements) {
    override fun executeParsed(context: ExecutionContext<S>, parsedValues: List<Any>): ExecutionResult {
        return context.execute()
    }
}

internal class Signature1<S, A : Any>(
    val execute: ExecutionContext<S>.(A) -> ExecutionResult,
    elements: Tuple1<SignatureConstraint<S, Any>,
            SignatureConstraint.Terminating<S, A>>,
): Signature<S>(elements) {
    override fun executeParsed(context: ExecutionContext<S>, parsedValues: List<Any>): ExecutionResult {
        return context.execute(parsedValues[0] as A)
    }
}

internal class Signature2<S, A : Any, B : Any>(
    val execute: ExecutionContext<S>.(A, B) -> ExecutionResult,
    elements: Tuple2<SignatureConstraint<S, Any>,
            SignatureConstraint.NonTerminating<S, A>,
            SignatureConstraint.Terminating<S, B>>,
): Signature<S>(elements) {
    override fun executeParsed(context: ExecutionContext<S>, parsedValues: List<Any>): ExecutionResult {
        return context.execute(parsedValues[0] as A, parsedValues[1] as B)
    }
}

internal class Signature3<S, A : Any, B : Any, C : Any>(
    val execute: ExecutionContext<S>.(A, B, C) -> ExecutionResult,
    elements: Tuple3<SignatureConstraint<S, Any>,
            SignatureConstraint.NonTerminating<S, A>,
            SignatureConstraint.NonTerminating<S, B>,
            SignatureConstraint.Terminating<S, C>>,
): Signature<S>(elements) {
    override fun executeParsed(context: ExecutionContext<S>, parsedValues: List<Any>): ExecutionResult {
        return context.execute(parsedValues[0] as A, parsedValues[1] as B, parsedValues[2] as C)
    }
}

internal class Signature4<S, A : Any, B : Any, C : Any, D : Any>(
    val execute: ExecutionContext<S>.(A, B, C, D) -> ExecutionResult,
    elements: Tuple4<SignatureConstraint<S, Any>,
            SignatureConstraint.NonTerminating<S, A>,
            SignatureConstraint.NonTerminating<S, B>,
            SignatureConstraint.NonTerminating<S, C>,
            SignatureConstraint.Terminating<S, D>>,
): Signature<S>(elements) {
    override fun executeParsed(context: ExecutionContext<S>, parsedValues: List<Any>): ExecutionResult {
        return context.execute(parsedValues[0] as A, parsedValues[1] as B, parsedValues[2] as C, parsedValues[3] as D)
    }
}

internal class Signature5<S, A : Any, B : Any, C : Any, D : Any, E : Any>(
    val execute: ExecutionContext<S>.(A, B, C, D, E) -> ExecutionResult,
    elements: Tuple5<SignatureConstraint<S, Any>,
            SignatureConstraint.NonTerminating<S, A>,
            SignatureConstraint.NonTerminating<S, B>,
            SignatureConstraint.NonTerminating<S, C>,
            SignatureConstraint.NonTerminating<S, D>,
            SignatureConstraint.Terminating<S, E>>,
): Signature<S>(elements) {
    override fun executeParsed(context: ExecutionContext<S>, parsedValues: List<Any>): ExecutionResult {
        return context.execute(parsedValues[0] as A, parsedValues[1] as B, parsedValues[2] as C, parsedValues[3] as D,
            parsedValues[4] as E)
    }
}

internal class Signature6<S, A : Any, B : Any, C : Any, D : Any, E : Any, F : Any>(
    val execute: ExecutionContext<S>.(A, B, C, D, E, F) -> ExecutionResult,
    elements: Tuple6<SignatureConstraint<S, Any>,
            SignatureConstraint.NonTerminating<S, A>,
            SignatureConstraint.NonTerminating<S, B>,
            SignatureConstraint.NonTerminating<S, C>,
            SignatureConstraint.NonTerminating<S, D>,
            SignatureConstraint.NonTerminating<S, E>,
            SignatureConstraint.Terminating<S, F>>,
): Signature<S>(elements) {
    override fun executeParsed(context: ExecutionContext<S>, parsedValues: List<Any>): ExecutionResult {
        return context.execute(parsedValues[0] as A, parsedValues[1] as B, parsedValues[2] as C, parsedValues[3] as D,
            parsedValues[4] as E, parsedValues[5] as F)
    }
}

internal class Signature7<S, A : Any, B : Any, C : Any, D : Any, E : Any, F : Any, G : Any>(
    val execute: ExecutionContext<S>.(A, B, C, D, E, F, G) -> ExecutionResult,
    elements: Tuple7<SignatureConstraint<S, Any>,
            SignatureConstraint.NonTerminating<S, A>,
            SignatureConstraint.NonTerminating<S, B>,
            SignatureConstraint.NonTerminating<S, C>,
            SignatureConstraint.NonTerminating<S, D>,
            SignatureConstraint.NonTerminating<S, E>,
            SignatureConstraint.NonTerminating<S, F>,
            SignatureConstraint.Terminating<S, G>>,
): Signature<S>(elements) {
    override fun executeParsed(context: ExecutionContext<S>, parsedValues: List<Any>): ExecutionResult {
        return context.execute(parsedValues[0] as A, parsedValues[1] as B, parsedValues[2] as C, parsedValues[3] as D,
            parsedValues[4] as E, parsedValues[5] as F, parsedValues[6] as G)
    }
}

internal class Signature8<S, A : Any, B : Any, C : Any, D : Any, E : Any, F : Any, G : Any, H : Any>(
    val execute: ExecutionContext<S>.(A, B, C, D, E, F, G, H) -> ExecutionResult,
    elements: Tuple8<SignatureConstraint<S, Any>,
            SignatureConstraint.NonTerminating<S, A>,
            SignatureConstraint.NonTerminating<S, B>,
            SignatureConstraint.NonTerminating<S, C>,
            SignatureConstraint.NonTerminating<S, D>,
            SignatureConstraint.NonTerminating<S, E>,
            SignatureConstraint.NonTerminating<S, F>,
            SignatureConstraint.NonTerminating<S, G>,
            SignatureConstraint.Terminating<S, H>>,
): Signature<S>(elements) {
    override fun executeParsed(context: ExecutionContext<S>, parsedValues: List<Any>): ExecutionResult {
        return context.execute(parsedValues[0] as A, parsedValues[1] as B, parsedValues[2] as C, parsedValues[3] as D,
            parsedValues[4] as E, parsedValues[5] as F, parsedValues[6] as G, parsedValues[7] as H)
    }
}

internal class Signature9<S, A : Any, B : Any, C : Any, D : Any, E : Any, F : Any, G : Any, H : Any, I : Any>(
    val execute: ExecutionContext<S>.(A, B, C, D, E, F, G, H, I) -> ExecutionResult,
    elements: Tuple9<SignatureConstraint<S, Any>,
            SignatureConstraint.NonTerminating<S, A>,
            SignatureConstraint.NonTerminating<S, B>,
            SignatureConstraint.NonTerminating<S, C>,
            SignatureConstraint.NonTerminating<S, D>,
            SignatureConstraint.NonTerminating<S, E>,
            SignatureConstraint.NonTerminating<S, F>,
            SignatureConstraint.NonTerminating<S, G>,
            SignatureConstraint.NonTerminating<S, H>,
            SignatureConstraint.Terminating<S, I>>,
): Signature<S>(elements) {
    override fun executeParsed(context: ExecutionContext<S>, parsedValues: List<Any>): ExecutionResult {
        return context.execute(parsedValues[0] as A, parsedValues[1] as B, parsedValues[2] as C, parsedValues[3] as D,
            parsedValues[4] as E, parsedValues[5] as F, parsedValues[6] as G, parsedValues[7] as H,
            parsedValues[8] as I)
    }
}

internal class Signature10<S, A : Any, B : Any, C : Any, D : Any, E : Any, F : Any, G : Any, H : Any, I : Any, J : Any>(
    val execute: ExecutionContext<S>.(A, B, C, D, E, F, G, H, I, J) -> ExecutionResult,
    elements: Tuple10<SignatureConstraint<S, Any>,
            SignatureConstraint.NonTerminating<S, A>,
            SignatureConstraint.NonTerminating<S, B>,
            SignatureConstraint.NonTerminating<S, C>,
            SignatureConstraint.NonTerminating<S, D>,
            SignatureConstraint.NonTerminating<S, E>,
            SignatureConstraint.NonTerminating<S, F>,
            SignatureConstraint.NonTerminating<S, G>,
            SignatureConstraint.NonTerminating<S, H>,
            SignatureConstraint.NonTerminating<S, I>,
            SignatureConstraint.Terminating<S, J>>,
): Signature<S>(elements) {
    override fun executeParsed(context: ExecutionContext<S>, parsedValues: List<Any>): ExecutionResult {
        return context.execute(parsedValues[0] as A, parsedValues[1] as B, parsedValues[2] as C, parsedValues[3] as D,
            parsedValues[4] as E, parsedValues[5] as F, parsedValues[6] as G, parsedValues[7] as H,
            parsedValues[8] as I, parsedValues[9] as J)
    }
}

internal class Signature11<S, A : Any, B : Any, C : Any, D : Any, E : Any, F : Any, G : Any, H : Any, I : Any, J : Any,
        K : Any>(
    val execute: ExecutionContext<S>.(A, B, C, D, E, F, G, H, I, J, K) -> ExecutionResult,
    elements: Tuple11<SignatureConstraint<S, Any>,
            SignatureConstraint.NonTerminating<S, A>,
            SignatureConstraint.NonTerminating<S, B>,
            SignatureConstraint.NonTerminating<S, C>,
            SignatureConstraint.NonTerminating<S, D>,
            SignatureConstraint.NonTerminating<S, E>,
            SignatureConstraint.NonTerminating<S, F>,
            SignatureConstraint.NonTerminating<S, G>,
            SignatureConstraint.NonTerminating<S, H>,
            SignatureConstraint.NonTerminating<S, I>,
            SignatureConstraint.NonTerminating<S, J>,
            SignatureConstraint.Terminating<S, K>>,
): Signature<S>(elements) {
    override fun executeParsed(context: ExecutionContext<S>, parsedValues: List<Any>): ExecutionResult {
        return context.execute(parsedValues[0] as A, parsedValues[1] as B, parsedValues[2] as C, parsedValues[3] as D,
            parsedValues[4] as E, parsedValues[5] as F, parsedValues[6] as G, parsedValues[7] as H,
            parsedValues[8] as I, parsedValues[9] as J, parsedValues[10] as K)
    }
}

internal class Signature12<S, A : Any, B : Any, C : Any, D : Any, E : Any, F : Any, G : Any, H : Any, I : Any, J : Any,
        K : Any, L : Any>(
    val execute: ExecutionContext<S>.(A, B, C, D, E, F, G, H, I, J, K, L) -> ExecutionResult,
    elements: Tuple12<SignatureConstraint<S, Any>,
            SignatureConstraint.NonTerminating<S, A>,
            SignatureConstraint.NonTerminating<S, B>,
            SignatureConstraint.NonTerminating<S, C>,
            SignatureConstraint.NonTerminating<S, D>,
            SignatureConstraint.NonTerminating<S, E>,
            SignatureConstraint.NonTerminating<S, F>,
            SignatureConstraint.NonTerminating<S, G>,
            SignatureConstraint.NonTerminating<S, H>,
            SignatureConstraint.NonTerminating<S, I>,
            SignatureConstraint.NonTerminating<S, J>,
            SignatureConstraint.NonTerminating<S, K>,
            SignatureConstraint.Terminating<S, L>>,
): Signature<S>(elements) {
    override fun executeParsed(context: ExecutionContext<S>, parsedValues: List<Any>): ExecutionResult {
        return context.execute(parsedValues[0] as A, parsedValues[1] as B, parsedValues[2] as C, parsedValues[3] as D,
            parsedValues[4] as E, parsedValues[5] as F, parsedValues[6] as G, parsedValues[7] as H,
            parsedValues[8] as I, parsedValues[9] as J, parsedValues[10] as K, parsedValues[11] as L)
    }
}
