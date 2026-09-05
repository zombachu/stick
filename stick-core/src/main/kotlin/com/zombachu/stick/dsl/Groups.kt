package com.zombachu.stick.dsl

import com.zombachu.stick.Environment
import com.zombachu.stick.GroupResult
import com.zombachu.stick.GroupResult1
import com.zombachu.stick.GroupResult2
import com.zombachu.stick.GroupResult3
import com.zombachu.stick.GroupResult4
import com.zombachu.stick.GroupResult5
import com.zombachu.stick.GroupResult6
import com.zombachu.stick.GroupResult7
import com.zombachu.stick.GroupResult8
import com.zombachu.stick.Position
import com.zombachu.stick.SenderValidationResult
import com.zombachu.stick.StructureScope
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

fun <E_ : Environment, S, A, P : Position> StructureScope<E_, S>.group(
    element: Groupable.Positioned<E_, S, A, P>,
    description: String = "",
): Group<E_, S, GroupResult1<A>, P> = createGroup { Group1Impl(name, description, element) }

fun <E_ : Environment, S, A, B, P : Position> StructureScope<E_, S>.group(
    elementA: Groupable.Positioned<E_, S, A, P>,
    elementB: Groupable.Positioned<E_, S, B, P>,
    description: String = "",
): Group<E_, S, GroupResult2<A, B>, P> = createGroup { Group2Impl(name, description, elementA, elementB) }

fun <E_ : Environment, S, A, B, C, P : Position> StructureScope<E_, S>.group(
    elementA: Groupable.Positioned<E_, S, A, P>,
    elementB: Groupable.Positioned<E_, S, B, P>,
    elementC: Groupable.Positioned<E_, S, C, P>,
    description: String = "",
): Group<E_, S, GroupResult3<A, B, C>, P> = createGroup { Group3Impl(name, description, elementA, elementB, elementC) }

fun <E_ : Environment, S, A, B, C, D, P : Position> StructureScope<E_, S>.group(
    elementA: Groupable.Positioned<E_, S, A, P>,
    elementB: Groupable.Positioned<E_, S, B, P>,
    elementC: Groupable.Positioned<E_, S, C, P>,
    elementD: Groupable.Positioned<E_, S, D, P>,
    description: String = "",
): Group<E_, S, GroupResult4<A, B, C, D>, P> = createGroup {
    Group4Impl(name, description, elementA, elementB, elementC, elementD)
}

fun <E_ : Environment, S, A, B, C, D, E, P : Position> StructureScope<E_, S>.group(
    elementA: Groupable.Positioned<E_, S, A, P>,
    elementB: Groupable.Positioned<E_, S, B, P>,
    elementC: Groupable.Positioned<E_, S, C, P>,
    elementD: Groupable.Positioned<E_, S, D, P>,
    elementE: Groupable.Positioned<E_, S, E, P>,
    description: String = "",
): Group<E_, S, GroupResult5<A, B, C, D, E>, P> = createGroup {
    Group5Impl(name, description, elementA, elementB, elementC, elementD, elementE)
}

fun <E_ : Environment, S, A, B, C, D, E, F, P : Position> StructureScope<E_, S>.group(
    elementA: Groupable.Positioned<E_, S, A, P>,
    elementB: Groupable.Positioned<E_, S, B, P>,
    elementC: Groupable.Positioned<E_, S, C, P>,
    elementD: Groupable.Positioned<E_, S, D, P>,
    elementE: Groupable.Positioned<E_, S, E, P>,
    elementF: Groupable.Positioned<E_, S, F, P>,
    description: String = "",
): Group<E_, S, GroupResult6<A, B, C, D, E, F>, P> = createGroup {
    Group6Impl(name, description, elementA, elementB, elementC, elementD, elementE, elementF)
}

fun <E_ : Environment, S, A, B, C, D, E, F, G, P : Position> StructureScope<E_, S>.group(
    elementA: Groupable.Positioned<E_, S, A, P>,
    elementB: Groupable.Positioned<E_, S, B, P>,
    elementC: Groupable.Positioned<E_, S, C, P>,
    elementD: Groupable.Positioned<E_, S, D, P>,
    elementE: Groupable.Positioned<E_, S, E, P>,
    elementF: Groupable.Positioned<E_, S, F, P>,
    elementG: Groupable.Positioned<E_, S, G, P>,
    description: String = "",
): Group<E_, S, GroupResult7<A, B, C, D, E, F, G>, P> = createGroup {
    Group7Impl(name, description, elementA, elementB, elementC, elementD, elementE, elementF, elementG)
}

fun <E_ : Environment, S, A, B, C, D, E, F, G, H, P : Position> StructureScope<E_, S>.group(
    elementA: Groupable.Positioned<E_, S, A, P>,
    elementB: Groupable.Positioned<E_, S, B, P>,
    elementC: Groupable.Positioned<E_, S, C, P>,
    elementD: Groupable.Positioned<E_, S, D, P>,
    elementE: Groupable.Positioned<E_, S, E, P>,
    elementF: Groupable.Positioned<E_, S, F, P>,
    elementG: Groupable.Positioned<E_, S, G, P>,
    elementH: Groupable.Positioned<E_, S, H, P>,
    description: String = "",
): Group<E_, S, GroupResult8<A, B, C, D, E, F, G, H>, P> = createGroup {
    Group8Impl(name, description, elementA, elementB, elementC, elementD, elementE, elementF, elementG, elementH)
}

private fun <E : Environment, S, R : GroupResult, P : Position, G : Group<E, S, R, P>> StructureScope<E, S>.createGroup(
    block: StructureScope<E, S>.() -> G
): G {
    val scope =
        StructureScope(
            name = "${this.name}_group",
            aliases = [],
            description = "",
            parent = this,
            requirement = requirement { SenderValidationResult.success() },
        )
    return block(scope)
}
