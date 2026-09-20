package com.zombachu.stick.dsl

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
import com.zombachu.stick.BranchScope
import com.zombachu.stick.Environment
import com.zombachu.stick.Invocation
import com.zombachu.stick.Position
import com.zombachu.stick.StructureScope
import com.zombachu.stick.element.Branch
import com.zombachu.stick.element.BranchImpl
import com.zombachu.stick.element.LeadingElementType
import com.zombachu.stick.element.Parameter
import com.zombachu.stick.element.Signature1
import com.zombachu.stick.element.Signature10
import com.zombachu.stick.element.Signature11
import com.zombachu.stick.element.Signature12
import com.zombachu.stick.element.Signature2
import com.zombachu.stick.element.Signature3
import com.zombachu.stick.element.Signature4
import com.zombachu.stick.element.Signature5
import com.zombachu.stick.element.Signature6
import com.zombachu.stick.element.Signature7
import com.zombachu.stick.element.Signature8
import com.zombachu.stick.element.Signature9
import com.zombachu.stick.element.SignatureElement

fun <E_ : Environment, S, A> StructureScope<E_, S>.branch(
    leading: Parameter<E_, S, A, Position.Leading>
): BranchScope<E_, S, A> = BranchScope(leading)

operator fun <E_ : Environment, S, A> BranchScope<E_, S, A>.invoke(
    execute: Invocation<E_, S>.(A) -> Unit = { a -> }
): Branch<E_, S, Arguments1<A>> = BranchImpl(Signature1(execute, LeadingElementType.Argument, [leading]))

operator fun <E_ : Environment, S, A, B> BranchScope<E_, S, A>.invoke(
    elementB: SignatureElement<E_, S, B, Position.Last>,
    execute: Invocation<E_, S>.(A, B) -> Unit = { a, b -> },
): Branch<E_, S, Arguments2<A, B>> = BranchImpl(Signature2(execute, LeadingElementType.Argument, [leading, elementB]))

operator fun <E_ : Environment, S, A, B, C> BranchScope<E_, S, A>.invoke(
    elementB: SignatureElement<E_, S, B, Position.Leading>,
    elementC: SignatureElement<E_, S, C, Position.Last>,
    execute: Invocation<E_, S>.(A, B, C) -> Unit = { a, b, c -> },
): Branch<E_, S, Arguments3<A, B, C>> =
    BranchImpl(Signature3(execute, LeadingElementType.Argument, [leading, elementB, elementC]))

operator fun <E_ : Environment, S, A, B, C, D> BranchScope<E_, S, A>.invoke(
    elementB: SignatureElement<E_, S, B, Position.Leading>,
    elementC: SignatureElement<E_, S, C, Position.Leading>,
    elementD: SignatureElement<E_, S, D, Position.Last>,
    execute: Invocation<E_, S>.(A, B, C, D) -> Unit = { a, b, c, d -> },
): Branch<E_, S, Arguments4<A, B, C, D>> =
    BranchImpl(Signature4(execute, LeadingElementType.Argument, [leading, elementB, elementC, elementD]))

operator fun <E_ : Environment, S, A, B, C, D, E> BranchScope<E_, S, A>.invoke(
    elementB: SignatureElement<E_, S, B, Position.Leading>,
    elementC: SignatureElement<E_, S, C, Position.Leading>,
    elementD: SignatureElement<E_, S, D, Position.Leading>,
    elementE: SignatureElement<E_, S, E, Position.Last>,
    execute: Invocation<E_, S>.(A, B, C, D, E) -> Unit = { a, b, c, d, e -> },
): Branch<E_, S, Arguments5<A, B, C, D, E>> =
    BranchImpl(Signature5(execute, LeadingElementType.Argument, [leading, elementB, elementC, elementD, elementE]))

operator fun <E_ : Environment, S, A, B, C, D, E, F> BranchScope<E_, S, A>.invoke(
    elementB: SignatureElement<E_, S, B, Position.Leading>,
    elementC: SignatureElement<E_, S, C, Position.Leading>,
    elementD: SignatureElement<E_, S, D, Position.Leading>,
    elementE: SignatureElement<E_, S, E, Position.Leading>,
    elementF: SignatureElement<E_, S, F, Position.Last>,
    execute: Invocation<E_, S>.(A, B, C, D, E, F) -> Unit = { a, b, c, d, e, f -> },
): Branch<E_, S, Arguments6<A, B, C, D, E, F>> =
    BranchImpl(
        Signature6(execute, LeadingElementType.Argument, [leading, elementB, elementC, elementD, elementE, elementF])
    )

operator fun <E_ : Environment, S, A, B, C, D, E, F, G> BranchScope<E_, S, A>.invoke(
    elementB: SignatureElement<E_, S, B, Position.Leading>,
    elementC: SignatureElement<E_, S, C, Position.Leading>,
    elementD: SignatureElement<E_, S, D, Position.Leading>,
    elementE: SignatureElement<E_, S, E, Position.Leading>,
    elementF: SignatureElement<E_, S, F, Position.Leading>,
    elementG: SignatureElement<E_, S, G, Position.Last>,
    execute: Invocation<E_, S>.(A, B, C, D, E, F, G) -> Unit = { a, b, c, d, e, f, g -> },
): Branch<E_, S, Arguments7<A, B, C, D, E, F, G>> =
    BranchImpl(
        Signature7(
            execute,
            LeadingElementType.Argument,
            [leading, elementB, elementC, elementD, elementE, elementF, elementG],
        )
    )

operator fun <E_ : Environment, S, A, B, C, D, E, F, G, H> BranchScope<E_, S, A>.invoke(
    elementB: SignatureElement<E_, S, B, Position.Leading>,
    elementC: SignatureElement<E_, S, C, Position.Leading>,
    elementD: SignatureElement<E_, S, D, Position.Leading>,
    elementE: SignatureElement<E_, S, E, Position.Leading>,
    elementF: SignatureElement<E_, S, F, Position.Leading>,
    elementG: SignatureElement<E_, S, G, Position.Leading>,
    elementH: SignatureElement<E_, S, H, Position.Last>,
    execute: Invocation<E_, S>.(A, B, C, D, E, F, G, H) -> Unit = { a, b, c, d, e, f, g, h -> },
): Branch<E_, S, Arguments8<A, B, C, D, E, F, G, H>> =
    BranchImpl(
        Signature8(
            execute,
            LeadingElementType.Argument,
            [leading, elementB, elementC, elementD, elementE, elementF, elementG, elementH],
        )
    )

operator fun <E_ : Environment, S, A, B, C, D, E, F, G, H, I> BranchScope<E_, S, A>.invoke(
    elementB: SignatureElement<E_, S, B, Position.Leading>,
    elementC: SignatureElement<E_, S, C, Position.Leading>,
    elementD: SignatureElement<E_, S, D, Position.Leading>,
    elementE: SignatureElement<E_, S, E, Position.Leading>,
    elementF: SignatureElement<E_, S, F, Position.Leading>,
    elementG: SignatureElement<E_, S, G, Position.Leading>,
    elementH: SignatureElement<E_, S, H, Position.Leading>,
    elementI: SignatureElement<E_, S, I, Position.Last>,
    execute: Invocation<E_, S>.(A, B, C, D, E, F, G, H, I) -> Unit = { a, b, c, d, e, f, g, h, i -> },
): Branch<E_, S, Arguments9<A, B, C, D, E, F, G, H, I>> =
    BranchImpl(
        Signature9(
            execute,
            LeadingElementType.Argument,
            [leading, elementB, elementC, elementD, elementE, elementF, elementG, elementH, elementI],
        )
    )

operator fun <E_ : Environment, S, A, B, C, D, E, F, G, H, I, J> BranchScope<E_, S, A>.invoke(
    elementB: SignatureElement<E_, S, B, Position.Leading>,
    elementC: SignatureElement<E_, S, C, Position.Leading>,
    elementD: SignatureElement<E_, S, D, Position.Leading>,
    elementE: SignatureElement<E_, S, E, Position.Leading>,
    elementF: SignatureElement<E_, S, F, Position.Leading>,
    elementG: SignatureElement<E_, S, G, Position.Leading>,
    elementH: SignatureElement<E_, S, H, Position.Leading>,
    elementI: SignatureElement<E_, S, I, Position.Leading>,
    elementJ: SignatureElement<E_, S, J, Position.Last>,
    execute: Invocation<E_, S>.(A, B, C, D, E, F, G, H, I, J) -> Unit = { a, b, c, d, e, f, g, h, i, j -> },
): Branch<E_, S, Arguments10<A, B, C, D, E, F, G, H, I, J>> =
    BranchImpl(
        Signature10(
            execute,
            LeadingElementType.Argument,
            [leading, elementB, elementC, elementD, elementE, elementF, elementG, elementH, elementI, elementJ],
        )
    )

operator fun <E_ : Environment, S, A, B, C, D, E, F, G, H, I, J, K> BranchScope<E_, S, A>.invoke(
    elementB: SignatureElement<E_, S, B, Position.Leading>,
    elementC: SignatureElement<E_, S, C, Position.Leading>,
    elementD: SignatureElement<E_, S, D, Position.Leading>,
    elementE: SignatureElement<E_, S, E, Position.Leading>,
    elementF: SignatureElement<E_, S, F, Position.Leading>,
    elementG: SignatureElement<E_, S, G, Position.Leading>,
    elementH: SignatureElement<E_, S, H, Position.Leading>,
    elementI: SignatureElement<E_, S, I, Position.Leading>,
    elementJ: SignatureElement<E_, S, J, Position.Leading>,
    elementK: SignatureElement<E_, S, K, Position.Last>,
    execute: Invocation<E_, S>.(A, B, C, D, E, F, G, H, I, J, K) -> Unit = { a, b, c, d, e, f, g, h, i, j, k -> },
): Branch<E_, S, Arguments11<A, B, C, D, E, F, G, H, I, J, K>> =
    BranchImpl(
        Signature11(
            execute,
            LeadingElementType.Argument,
            [
                leading,
                elementB,
                elementC,
                elementD,
                elementE,
                elementF,
                elementG,
                elementH,
                elementI,
                elementJ,
                elementK,
            ],
        )
    )

operator fun <E_ : Environment, S, A, B, C, D, E, F, G, H, I, J, K, L> BranchScope<E_, S, A>.invoke(
    elementB: SignatureElement<E_, S, B, Position.Leading>,
    elementC: SignatureElement<E_, S, C, Position.Leading>,
    elementD: SignatureElement<E_, S, D, Position.Leading>,
    elementE: SignatureElement<E_, S, E, Position.Leading>,
    elementF: SignatureElement<E_, S, F, Position.Leading>,
    elementG: SignatureElement<E_, S, G, Position.Leading>,
    elementH: SignatureElement<E_, S, H, Position.Leading>,
    elementI: SignatureElement<E_, S, I, Position.Leading>,
    elementJ: SignatureElement<E_, S, J, Position.Leading>,
    elementK: SignatureElement<E_, S, K, Position.Leading>,
    elementL: SignatureElement<E_, S, L, Position.Last>,
    execute: Invocation<E_, S>.(A, B, C, D, E, F, G, H, I, J, K, L) -> Unit = { a, b, c, d, e, f, g, h, i, j, k, l -> },
): Branch<E_, S, Arguments12<A, B, C, D, E, F, G, H, I, J, K, L>> =
    BranchImpl(
        Signature12(
            execute,
            LeadingElementType.Argument,
            [
                leading,
                elementB,
                elementC,
                elementD,
                elementE,
                elementF,
                elementG,
                elementH,
                elementI,
                elementJ,
                elementK,
                elementL,
            ],
        )
    )
