@file:Suppress("UNCHECKED_CAST", "MagicNumber")

package com.zombachu.stick.element

import com.zombachu.stick.Arguments
import com.zombachu.stick.Arguments2
import com.zombachu.stick.Arguments3
import com.zombachu.stick.Arguments4
import com.zombachu.stick.Arguments5
import com.zombachu.stick.Arguments6
import com.zombachu.stick.Arguments7
import com.zombachu.stick.Arguments8
import com.zombachu.stick.CommandResult
import com.zombachu.stick.Environment
import com.zombachu.stick.Invocation
import com.zombachu.stick.Position
import com.zombachu.stick.Size

internal open class OptionalsImpl<E : Environment, S, T : Arguments>(
    internal val elements: List<Element<E, S, Any?>>,
    internal val combine: (List<Any?>) -> T,
) : Element.Positioned<E, S, T, Position.Last> {

    override val type: ElementType = ElementType.Default
    override val size: Size = elements.fold(Size(0) as Size) { total, element -> total + element.size }

    context(inv: Invocation<E, S>)
    override fun parse(args: List<String>): CommandResult<T> = unusedValue()
}

internal class Optionals2Impl<E_ : Environment, S, A, B>(
    elementA: Element<E_, S, A>,
    elementB: Element<E_, S, B>,
) :
    OptionalsImpl<E_, S, Arguments2<A, B>>(
        [elementA, elementB],
        { values -> Arguments2(values[0] as A, values[1] as B) },
    )

internal class Optionals3Impl<E_ : Environment, S, A, B, C>(
    elementA: Element<E_, S, A>,
    elementB: Element<E_, S, B>,
    elementC: Element<E_, S, C>,
) :
    OptionalsImpl<E_, S, Arguments3<A, B, C>>(
        [elementA, elementB, elementC],
        { values -> Arguments3(values[0] as A, values[1] as B, values[2] as C) },
    )

internal class Optionals4Impl<E_ : Environment, S, A, B, C, D>(
    elementA: Element<E_, S, A>,
    elementB: Element<E_, S, B>,
    elementC: Element<E_, S, C>,
    elementD: Element<E_, S, D>,
) :
    OptionalsImpl<E_, S, Arguments4<A, B, C, D>>(
        [elementA, elementB, elementC, elementD],
        { values -> Arguments4(values[0] as A, values[1] as B, values[2] as C, values[3] as D) },
    )

internal class Optionals5Impl<E_ : Environment, S, A, B, C, D, E>(
    elementA: Element<E_, S, A>,
    elementB: Element<E_, S, B>,
    elementC: Element<E_, S, C>,
    elementD: Element<E_, S, D>,
    elementE: Element<E_, S, E>,
) :
    OptionalsImpl<E_, S, Arguments5<A, B, C, D, E>>(
        [elementA, elementB, elementC, elementD, elementE],
        { values -> Arguments5(values[0] as A, values[1] as B, values[2] as C, values[3] as D, values[4] as E) },
    )

internal class Optionals6Impl<E_ : Environment, S, A, B, C, D, E, F>(
    elementA: Element<E_, S, A>,
    elementB: Element<E_, S, B>,
    elementC: Element<E_, S, C>,
    elementD: Element<E_, S, D>,
    elementE: Element<E_, S, E>,
    elementF: Element<E_, S, F>,
) :
    OptionalsImpl<E_, S, Arguments6<A, B, C, D, E, F>>(
        [elementA, elementB, elementC, elementD, elementE, elementF],
        { values ->
            Arguments6(values[0] as A, values[1] as B, values[2] as C, values[3] as D, values[4] as E, values[5] as F)
        },
    )

internal class Optionals7Impl<E_ : Environment, S, A, B, C, D, E, F, G>(
    elementA: Element<E_, S, A>,
    elementB: Element<E_, S, B>,
    elementC: Element<E_, S, C>,
    elementD: Element<E_, S, D>,
    elementE: Element<E_, S, E>,
    elementF: Element<E_, S, F>,
    elementG: Element<E_, S, G>,
) :
    OptionalsImpl<E_, S, Arguments7<A, B, C, D, E, F, G>>(
        [elementA, elementB, elementC, elementD, elementE, elementF, elementG],
        { values ->
            Arguments7(
                values[0] as A,
                values[1] as B,
                values[2] as C,
                values[3] as D,
                values[4] as E,
                values[5] as F,
                values[6] as G,
            )
        },
    )

internal class Optionals8Impl<E_ : Environment, S, A, B, C, D, E, F, G, H>(
    elementA: Element<E_, S, A>,
    elementB: Element<E_, S, B>,
    elementC: Element<E_, S, C>,
    elementD: Element<E_, S, D>,
    elementE: Element<E_, S, E>,
    elementF: Element<E_, S, F>,
    elementG: Element<E_, S, G>,
    elementH: Element<E_, S, H>,
) :
    OptionalsImpl<E_, S, Arguments8<A, B, C, D, E, F, G, H>>(
        [elementA, elementB, elementC, elementD, elementE, elementF, elementG, elementH],
        { values ->
            Arguments8(
                values[0] as A,
                values[1] as B,
                values[2] as C,
                values[3] as D,
                values[4] as E,
                values[5] as F,
                values[6] as G,
                values[7] as H,
            )
        },
    )
