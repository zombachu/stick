@file:Suppress("UNCHECKED_CAST")

package com.zombachu.stick.element

import com.zombachu.stick.ExecutionResult
import com.zombachu.stick.SenderContext
import com.zombachu.stick.impl.ExecutionContextImpl
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

internal class Signature0<O, S : SenderContext>(
    val execute: ExecutionContextImpl<O, S>.() -> ExecutionResult,
    elements: Tuple0<SignatureConstraint<O, S, Any>>,
): Signature<O, S>(elements) {
    override fun executeParsed(context: ExecutionContextImpl<O, S>, parsedValues: List<Any>): ExecutionResult {
        return context.execute()
    }
}

internal class Signature1<O, S : SenderContext, A : Any>(
    val execute: ExecutionContextImpl<O, S>.(A) -> ExecutionResult,
    elements: Tuple1<SignatureConstraint<O, S, Any>,
            SignatureConstraint.Terminating<O, S, A>>,
): Signature<O, S>(elements) {
    override fun executeParsed(context: ExecutionContextImpl<O, S>, parsedValues: List<Any>): ExecutionResult {
        return context.execute(parsedValues[0] as A)
    }
}

internal class Signature2<O, S : SenderContext, A : Any, B : Any>(
    val execute: ExecutionContextImpl<O, S>.(A, B) -> ExecutionResult,
    elements: Tuple2<SignatureConstraint<O, S, Any>,
            SignatureConstraint.NonTerminating<O, S, A>,
            SignatureConstraint.Terminating<O, S, B>>,
): Signature<O, S>(elements) {
    override fun executeParsed(context: ExecutionContextImpl<O, S>, parsedValues: List<Any>): ExecutionResult {
        return context.execute(parsedValues[0] as A, parsedValues[1] as B)
    }
}

internal class Signature3<O, S : SenderContext, A : Any, B : Any, C : Any>(
    val execute: ExecutionContextImpl<O, S>.(A, B, C) -> ExecutionResult,
    elements: Tuple3<SignatureConstraint<O, S, Any>,
            SignatureConstraint.NonTerminating<O, S, A>,
            SignatureConstraint.NonTerminating<O, S, B>,
            SignatureConstraint.Terminating<O, S, C>>,
): Signature<O, S>(elements) {
    override fun executeParsed(context: ExecutionContextImpl<O, S>, parsedValues: List<Any>): ExecutionResult {
        return context.execute(parsedValues[0] as A, parsedValues[1] as B, parsedValues[2] as C)
    }
}

internal class Signature4<O, S : SenderContext, A : Any, B : Any, C : Any, D : Any>(
    val execute: ExecutionContextImpl<O, S>.(A, B, C, D) -> ExecutionResult,
    elements: Tuple4<SignatureConstraint<O, S, Any>,
            SignatureConstraint.NonTerminating<O, S, A>,
            SignatureConstraint.NonTerminating<O, S, B>,
            SignatureConstraint.NonTerminating<O, S, C>,
            SignatureConstraint.Terminating<O, S, D>>,
): Signature<O, S>(elements) {
    override fun executeParsed(context: ExecutionContextImpl<O, S>, parsedValues: List<Any>): ExecutionResult {
        return context.execute(parsedValues[0] as A, parsedValues[1] as B, parsedValues[2] as C, parsedValues[3] as D)
    }
}

internal class Signature5<O, S : SenderContext, A : Any, B : Any, C : Any, D : Any, E : Any>(
    val execute: ExecutionContextImpl<O, S>.(A, B, C, D, E) -> ExecutionResult,
    elements: Tuple5<SignatureConstraint<O, S, Any>,
            SignatureConstraint.NonTerminating<O, S, A>,
            SignatureConstraint.NonTerminating<O, S, B>,
            SignatureConstraint.NonTerminating<O, S, C>,
            SignatureConstraint.NonTerminating<O, S, D>,
            SignatureConstraint.Terminating<O, S, E>>,
): Signature<O, S>(elements) {
    override fun executeParsed(context: ExecutionContextImpl<O, S>, parsedValues: List<Any>): ExecutionResult {
        return context.execute(parsedValues[0] as A, parsedValues[1] as B, parsedValues[2] as C, parsedValues[3] as D,
            parsedValues[4] as E)
    }
}

internal class Signature6<O, S : SenderContext, A : Any, B : Any, C : Any, D : Any, E : Any, F : Any>(
    val execute: ExecutionContextImpl<O, S>.(A, B, C, D, E, F) -> ExecutionResult,
    elements: Tuple6<SignatureConstraint<O, S, Any>,
            SignatureConstraint.NonTerminating<O, S, A>,
            SignatureConstraint.NonTerminating<O, S, B>,
            SignatureConstraint.NonTerminating<O, S, C>,
            SignatureConstraint.NonTerminating<O, S, D>,
            SignatureConstraint.NonTerminating<O, S, E>,
            SignatureConstraint.Terminating<O, S, F>>,
): Signature<O, S>(elements) {
    override fun executeParsed(context: ExecutionContextImpl<O, S>, parsedValues: List<Any>): ExecutionResult {
        return context.execute(parsedValues[0] as A, parsedValues[1] as B, parsedValues[2] as C, parsedValues[3] as D,
            parsedValues[4] as E, parsedValues[5] as F)
    }
}

internal class Signature7<O, S : SenderContext, A : Any, B : Any, C : Any, D : Any, E : Any, F : Any, G : Any>(
    val execute: ExecutionContextImpl<O, S>.(A, B, C, D, E, F, G) -> ExecutionResult,
    elements: Tuple7<SignatureConstraint<O, S, Any>,
            SignatureConstraint.NonTerminating<O, S, A>,
            SignatureConstraint.NonTerminating<O, S, B>,
            SignatureConstraint.NonTerminating<O, S, C>,
            SignatureConstraint.NonTerminating<O, S, D>,
            SignatureConstraint.NonTerminating<O, S, E>,
            SignatureConstraint.NonTerminating<O, S, F>,
            SignatureConstraint.Terminating<O, S, G>>,
): Signature<O, S>(elements) {
    override fun executeParsed(context: ExecutionContextImpl<O, S>, parsedValues: List<Any>): ExecutionResult {
        return context.execute(parsedValues[0] as A, parsedValues[1] as B, parsedValues[2] as C, parsedValues[3] as D,
            parsedValues[4] as E, parsedValues[5] as F, parsedValues[6] as G)
    }
}

internal class Signature8<O, S : SenderContext, A : Any, B : Any, C : Any, D : Any, E : Any, F : Any, G : Any, H : Any>(
    val execute: ExecutionContextImpl<O, S>.(A, B, C, D, E, F, G, H) -> ExecutionResult,
    elements: Tuple8<SignatureConstraint<O, S, Any>,
            SignatureConstraint.NonTerminating<O, S, A>,
            SignatureConstraint.NonTerminating<O, S, B>,
            SignatureConstraint.NonTerminating<O, S, C>,
            SignatureConstraint.NonTerminating<O, S, D>,
            SignatureConstraint.NonTerminating<O, S, E>,
            SignatureConstraint.NonTerminating<O, S, F>,
            SignatureConstraint.NonTerminating<O, S, G>,
            SignatureConstraint.Terminating<O, S, H>>,
): Signature<O, S>(elements) {
    override fun executeParsed(context: ExecutionContextImpl<O, S>, parsedValues: List<Any>): ExecutionResult {
        return context.execute(parsedValues[0] as A, parsedValues[1] as B, parsedValues[2] as C, parsedValues[3] as D,
            parsedValues[4] as E, parsedValues[5] as F, parsedValues[6] as G, parsedValues[7] as H)
    }
}

internal class Signature9<O, S : SenderContext, A : Any, B : Any, C : Any, D : Any, E : Any, F : Any, G : Any, H : Any, I : Any>(
    val execute: ExecutionContextImpl<O, S>.(A, B, C, D, E, F, G, H, I) -> ExecutionResult,
    elements: Tuple9<SignatureConstraint<O, S, Any>,
            SignatureConstraint.NonTerminating<O, S, A>,
            SignatureConstraint.NonTerminating<O, S, B>,
            SignatureConstraint.NonTerminating<O, S, C>,
            SignatureConstraint.NonTerminating<O, S, D>,
            SignatureConstraint.NonTerminating<O, S, E>,
            SignatureConstraint.NonTerminating<O, S, F>,
            SignatureConstraint.NonTerminating<O, S, G>,
            SignatureConstraint.NonTerminating<O, S, H>,
            SignatureConstraint.Terminating<O, S, I>>,
): Signature<O, S>(elements) {
    override fun executeParsed(context: ExecutionContextImpl<O, S>, parsedValues: List<Any>): ExecutionResult {
        return context.execute(parsedValues[0] as A, parsedValues[1] as B, parsedValues[2] as C, parsedValues[3] as D,
            parsedValues[4] as E, parsedValues[5] as F, parsedValues[6] as G, parsedValues[7] as H,
            parsedValues[8] as I)
    }
}

internal class Signature10<O, S : SenderContext, A : Any, B : Any, C : Any, D : Any, E : Any, F : Any, G : Any, H : Any, I : Any, J : Any>(
    val execute: ExecutionContextImpl<O, S>.(A, B, C, D, E, F, G, H, I, J) -> ExecutionResult,
    elements: Tuple10<SignatureConstraint<O, S, Any>,
            SignatureConstraint.NonTerminating<O, S, A>,
            SignatureConstraint.NonTerminating<O, S, B>,
            SignatureConstraint.NonTerminating<O, S, C>,
            SignatureConstraint.NonTerminating<O, S, D>,
            SignatureConstraint.NonTerminating<O, S, E>,
            SignatureConstraint.NonTerminating<O, S, F>,
            SignatureConstraint.NonTerminating<O, S, G>,
            SignatureConstraint.NonTerminating<O, S, H>,
            SignatureConstraint.NonTerminating<O, S, I>,
            SignatureConstraint.Terminating<O, S, J>>,
): Signature<O, S>(elements) {
    override fun executeParsed(context: ExecutionContextImpl<O, S>, parsedValues: List<Any>): ExecutionResult {
        return context.execute(parsedValues[0] as A, parsedValues[1] as B, parsedValues[2] as C, parsedValues[3] as D,
            parsedValues[4] as E, parsedValues[5] as F, parsedValues[6] as G, parsedValues[7] as H,
            parsedValues[8] as I, parsedValues[9] as J)
    }
}

internal class Signature11<O, S : SenderContext, A : Any, B : Any, C : Any, D : Any, E : Any, F : Any, G : Any, H : Any, I : Any, J : Any,
        K : Any>(
    val execute: ExecutionContextImpl<O, S>.(A, B, C, D, E, F, G, H, I, J, K) -> ExecutionResult,
    elements: Tuple11<SignatureConstraint<O, S, Any>,
            SignatureConstraint.NonTerminating<O, S, A>,
            SignatureConstraint.NonTerminating<O, S, B>,
            SignatureConstraint.NonTerminating<O, S, C>,
            SignatureConstraint.NonTerminating<O, S, D>,
            SignatureConstraint.NonTerminating<O, S, E>,
            SignatureConstraint.NonTerminating<O, S, F>,
            SignatureConstraint.NonTerminating<O, S, G>,
            SignatureConstraint.NonTerminating<O, S, H>,
            SignatureConstraint.NonTerminating<O, S, I>,
            SignatureConstraint.NonTerminating<O, S, J>,
            SignatureConstraint.Terminating<O, S, K>>,
): Signature<O, S>(elements) {
    override fun executeParsed(context: ExecutionContextImpl<O, S>, parsedValues: List<Any>): ExecutionResult {
        return context.execute(parsedValues[0] as A, parsedValues[1] as B, parsedValues[2] as C, parsedValues[3] as D,
            parsedValues[4] as E, parsedValues[5] as F, parsedValues[6] as G, parsedValues[7] as H,
            parsedValues[8] as I, parsedValues[9] as J, parsedValues[10] as K)
    }
}

internal class Signature12<O, S : SenderContext, A : Any, B : Any, C : Any, D : Any, E : Any, F : Any, G : Any, H : Any, I : Any, J : Any,
        K : Any, L : Any>(
    val execute: ExecutionContextImpl<O, S>.(A, B, C, D, E, F, G, H, I, J, K, L) -> ExecutionResult,
    elements: Tuple12<SignatureConstraint<O, S, Any>,
            SignatureConstraint.NonTerminating<O, S, A>,
            SignatureConstraint.NonTerminating<O, S, B>,
            SignatureConstraint.NonTerminating<O, S, C>,
            SignatureConstraint.NonTerminating<O, S, D>,
            SignatureConstraint.NonTerminating<O, S, E>,
            SignatureConstraint.NonTerminating<O, S, F>,
            SignatureConstraint.NonTerminating<O, S, G>,
            SignatureConstraint.NonTerminating<O, S, H>,
            SignatureConstraint.NonTerminating<O, S, I>,
            SignatureConstraint.NonTerminating<O, S, J>,
            SignatureConstraint.NonTerminating<O, S, K>,
            SignatureConstraint.Terminating<O, S, L>>,
): Signature<O, S>(elements) {
    override fun executeParsed(context: ExecutionContextImpl<O, S>, parsedValues: List<Any>): ExecutionResult {
        return context.execute(parsedValues[0] as A, parsedValues[1] as B, parsedValues[2] as C, parsedValues[3] as D,
            parsedValues[4] as E, parsedValues[5] as F, parsedValues[6] as G, parsedValues[7] as H,
            parsedValues[8] as I, parsedValues[9] as J, parsedValues[10] as K, parsedValues[11] as L)
    }
}
