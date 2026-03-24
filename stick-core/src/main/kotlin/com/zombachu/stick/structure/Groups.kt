package com.zombachu.stick.structure

import com.zombachu.stick.Environment
import com.zombachu.stick.GroupResult1
import com.zombachu.stick.GroupResult2
import com.zombachu.stick.GroupResult3
import com.zombachu.stick.GroupResult4
import com.zombachu.stick.GroupResult5
import com.zombachu.stick.GroupResult6
import com.zombachu.stick.GroupResult7
import com.zombachu.stick.GroupResult8
import com.zombachu.stick.SenderValidationResult
import com.zombachu.stick.element.Group
import com.zombachu.stick.element.Group1Impl
import com.zombachu.stick.element.Group2Impl
import com.zombachu.stick.element.Group3Impl
import com.zombachu.stick.element.Group4Impl
import com.zombachu.stick.element.Group5Impl
import com.zombachu.stick.element.Group6Impl
import com.zombachu.stick.element.Group7Impl
import com.zombachu.stick.element.Group8Impl
import com.zombachu.stick.element.Groupable
import com.zombachu.stick.impl.BuilderScope
import com.zombachu.stick.impl.StructureElement
import com.zombachu.stick.impl.StructureScope

fun <E_ : Environment, S, A> BuilderScope<E_, S>.group(
    element: StructureElement<E_, S, Groupable<E_, S, A>>,
    description: String = "",
): StructureElement<E_, S, Group<E_, S, GroupResult1<A>>> = {
    val groupScope = groupScope()
    Group1Impl(id(groupScope.name), description,
        element(groupScope),
    )
}

fun <E_ : Environment, S, A, B> BuilderScope<E_, S>.group(
    elementA: StructureElement<E_, S, Groupable<E_, S, A>>,
    elementB: StructureElement<E_, S, Groupable<E_, S, B>>,
    description: String = "",
): StructureElement<E_, S, Group<E_, S, GroupResult2<A, B>>> = {
    val groupScope = groupScope()
    Group2Impl(id(groupScope.name), description,
        elementA(groupScope),
        elementB(groupScope),
    )
}

fun <E_ : Environment, S, A, B, C> BuilderScope<E_, S>.group(
    elementA: StructureElement<E_, S, Groupable<E_, S, A>>,
    elementB: StructureElement<E_, S, Groupable<E_, S, B>>,
    elementC: StructureElement<E_, S, Groupable<E_, S, C>>,
    description: String = "",
): StructureElement<E_, S, Group<E_, S, GroupResult3<A, B, C>>> = {
    val groupScope = groupScope()
    Group3Impl(id(groupScope.name), description,
        elementA(groupScope),
        elementB(groupScope),
        elementC(groupScope),
    )
}

fun <E_ : Environment, S, A, B, C, D> BuilderScope<E_, S>.group(
    elementA: StructureElement<E_, S, Groupable<E_, S, A>>,
    elementB: StructureElement<E_, S, Groupable<E_, S, B>>,
    elementC: StructureElement<E_, S, Groupable<E_, S, C>>,
    elementD: StructureElement<E_, S, Groupable<E_, S, D>>,
    description: String = "",
): StructureElement<E_, S, Group<E_, S, GroupResult4<A, B, C, D>>> = {
    val groupScope = groupScope()
    Group4Impl(id(groupScope.name), description,
        elementA(groupScope),
        elementB(groupScope),
        elementC(groupScope),
        elementD(groupScope),
    )
}

fun <E_ : Environment, S, A, B, C, D, E> BuilderScope<E_, S>.group(
    elementA: StructureElement<E_, S, Groupable<E_, S, A>>,
    elementB: StructureElement<E_, S, Groupable<E_, S, B>>,
    elementC: StructureElement<E_, S, Groupable<E_, S, C>>,
    elementD: StructureElement<E_, S, Groupable<E_, S, D>>,
    elementE: StructureElement<E_, S, Groupable<E_, S, E>>,
    description: String = "",
): StructureElement<E_, S, Group<E_, S, GroupResult5<A, B, C, D, E>>> = {
    val groupScope = groupScope()
    Group5Impl(id(groupScope.name), description,
        elementA(groupScope),
        elementB(groupScope),
        elementC(groupScope),
        elementD(groupScope),
        elementE(groupScope),
    )
}

fun <E_ : Environment, S, A, B, C, D, E, F> BuilderScope<E_, S>.group(
    elementA: StructureElement<E_, S, Groupable<E_, S, A>>,
    elementB: StructureElement<E_, S, Groupable<E_, S, B>>,
    elementC: StructureElement<E_, S, Groupable<E_, S, C>>,
    elementD: StructureElement<E_, S, Groupable<E_, S, D>>,
    elementE: StructureElement<E_, S, Groupable<E_, S, E>>,
    elementF: StructureElement<E_, S, Groupable<E_, S, F>>,
    description: String = "",
): StructureElement<E_, S, Group<E_, S, GroupResult6<A, B, C, D, E, F>>> = {
    val groupScope = groupScope()
    Group6Impl(id(groupScope.name), description,
        elementA(groupScope),
        elementB(groupScope),
        elementC(groupScope),
        elementD(groupScope),
        elementE(groupScope),
        elementF(groupScope),
    )
}

fun <E_ : Environment, S, A, B, C, D, E, F, G> BuilderScope<E_, S>.group(
    elementA: StructureElement<E_, S, Groupable<E_, S, A>>,
    elementB: StructureElement<E_, S, Groupable<E_, S, B>>,
    elementC: StructureElement<E_, S, Groupable<E_, S, C>>,
    elementD: StructureElement<E_, S, Groupable<E_, S, D>>,
    elementE: StructureElement<E_, S, Groupable<E_, S, E>>,
    elementF: StructureElement<E_, S, Groupable<E_, S, F>>,
    elementG: StructureElement<E_, S, Groupable<E_, S, G>>,
    description: String = "",
): StructureElement<E_, S, Group<E_, S, GroupResult7<A, B, C, D, E, F, G>>> = {
    val groupScope = groupScope()
    Group7Impl(
        id(groupScope.name), description,
        elementA(groupScope),
        elementB(groupScope),
        elementC(groupScope),
        elementD(groupScope),
        elementE(groupScope),
        elementF(groupScope),
        elementG(groupScope),
    )
}

fun <E_ : Environment, S, A, B, C, D, E, F, G, H> BuilderScope<E_, S>.group(
    elementA: StructureElement<E_, S, Groupable<E_, S, A>>,
    elementB: StructureElement<E_, S, Groupable<E_, S, B>>,
    elementC: StructureElement<E_, S, Groupable<E_, S, C>>,
    elementD: StructureElement<E_, S, Groupable<E_, S, D>>,
    elementE: StructureElement<E_, S, Groupable<E_, S, E>>,
    elementF: StructureElement<E_, S, Groupable<E_, S, F>>,
    elementG: StructureElement<E_, S, Groupable<E_, S, G>>,
    elementH: StructureElement<E_, S, Groupable<E_, S, H>>,
    description: String = "",
): StructureElement<E_, S, Group<E_, S, GroupResult8<A, B, C, D, E, F, G, H>>> = {
    val groupScope = groupScope()
    Group8Impl(
        id(groupScope.name), description,
        elementA(groupScope),
        elementB(groupScope),
        elementC(groupScope),
        elementD(groupScope),
        elementE(groupScope),
        elementF(groupScope),
        elementG(groupScope),
        elementH(groupScope),
    )
}

private fun <E : Environment, S> StructureScope<E, S>.groupScope(): StructureScope<E, S> {
    return StructureScope(
        name = "${this.name}_group",
        aliases = setOf(),
        description = "",
        parent = this,
        requirement = requirement { SenderValidationResult.success() },
    )
}
