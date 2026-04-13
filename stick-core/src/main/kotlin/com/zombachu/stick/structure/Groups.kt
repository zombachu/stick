package com.zombachu.stick.structure

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
import com.zombachu.stick.impl.StructureScope

@JvmName("groupNonTerminating")
fun <E_ : Environment, S, A> StructureScope<E_, S>.group(
    element: Groupable.NonTerminating<E_, S, A>,
    description: String = "",
): Group.FiniteSize<E_, S, GroupResult1<A>> = createGroup { Group1Impl(name, description, element) }

@JvmName("groupNonTerminating")
fun <E_ : Environment, S, A, B> StructureScope<E_, S>.group(
    elementA: Groupable.NonTerminating<E_, S, A>,
    elementB: Groupable.NonTerminating<E_, S, B>,
    description: String = "",
): Group.FiniteSize<E_, S, GroupResult2<A, B>> = createGroup { Group2Impl(name, description, elementA, elementB) }

@JvmName("groupNonTerminating")
fun <E_ : Environment, S, A, B, C> StructureScope<E_, S>.group(
    elementA: Groupable.NonTerminating<E_, S, A>,
    elementB: Groupable.NonTerminating<E_, S, B>,
    elementC: Groupable.NonTerminating<E_, S, C>,
    description: String = "",
): Group.FiniteSize<E_, S, GroupResult3<A, B, C>> = createGroup {
    Group3Impl(name, description, elementA, elementB, elementC)
}

@JvmName("groupNonTerminating")
fun <E_ : Environment, S, A, B, C, D> StructureScope<E_, S>.group(
    elementA: Groupable.NonTerminating<E_, S, A>,
    elementB: Groupable.NonTerminating<E_, S, B>,
    elementC: Groupable.NonTerminating<E_, S, C>,
    elementD: Groupable.NonTerminating<E_, S, D>,
    description: String = "",
): Group.FiniteSize<E_, S, GroupResult4<A, B, C, D>> = createGroup {
    Group4Impl(name, description, elementA, elementB, elementC, elementD)
}

@JvmName("groupNonTerminating")
fun <E_ : Environment, S, A, B, C, D, E> StructureScope<E_, S>.group(
    elementA: Groupable.NonTerminating<E_, S, A>,
    elementB: Groupable.NonTerminating<E_, S, B>,
    elementC: Groupable.NonTerminating<E_, S, C>,
    elementD: Groupable.NonTerminating<E_, S, D>,
    elementE: Groupable.NonTerminating<E_, S, E>,
    description: String = "",
): Group.FiniteSize<E_, S, GroupResult5<A, B, C, D, E>> = createGroup {
    Group5Impl(name, description, elementA, elementB, elementC, elementD, elementE)
}

@JvmName("groupNonTerminating")
fun <E_ : Environment, S, A, B, C, D, E, F> StructureScope<E_, S>.group(
    elementA: Groupable.NonTerminating<E_, S, A>,
    elementB: Groupable.NonTerminating<E_, S, B>,
    elementC: Groupable.NonTerminating<E_, S, C>,
    elementD: Groupable.NonTerminating<E_, S, D>,
    elementE: Groupable.NonTerminating<E_, S, E>,
    elementF: Groupable.NonTerminating<E_, S, F>,
    description: String = "",
): Group.FiniteSize<E_, S, GroupResult6<A, B, C, D, E, F>> = createGroup {
    Group6Impl(name, description, elementA, elementB, elementC, elementD, elementE, elementF)
}

@JvmName("groupNonTerminating")
fun <E_ : Environment, S, A, B, C, D, E, F, G> StructureScope<E_, S>.group(
    elementA: Groupable.NonTerminating<E_, S, A>,
    elementB: Groupable.NonTerminating<E_, S, B>,
    elementC: Groupable.NonTerminating<E_, S, C>,
    elementD: Groupable.NonTerminating<E_, S, D>,
    elementE: Groupable.NonTerminating<E_, S, E>,
    elementF: Groupable.NonTerminating<E_, S, F>,
    elementG: Groupable.NonTerminating<E_, S, G>,
    description: String = "",
): Group.FiniteSize<E_, S, GroupResult7<A, B, C, D, E, F, G>> = createGroup {
    Group7Impl(name, description, elementA, elementB, elementC, elementD, elementE, elementF, elementG)
}

@JvmName("groupNonTerminating")
fun <E_ : Environment, S, A, B, C, D, E, F, G, H> StructureScope<E_, S>.group(
    elementA: Groupable.NonTerminating<E_, S, A>,
    elementB: Groupable.NonTerminating<E_, S, B>,
    elementC: Groupable.NonTerminating<E_, S, C>,
    elementD: Groupable.NonTerminating<E_, S, D>,
    elementE: Groupable.NonTerminating<E_, S, E>,
    elementF: Groupable.NonTerminating<E_, S, F>,
    elementG: Groupable.NonTerminating<E_, S, G>,
    elementH: Groupable.NonTerminating<E_, S, H>,
    description: String = "",
): Group.FiniteSize<E_, S, GroupResult8<A, B, C, D, E, F, G, H>> = createGroup {
    Group8Impl(name, description, elementA, elementB, elementC, elementD, elementE, elementF, elementG, elementH)
}

@JvmName("groupTerminating")
fun <E_ : Environment, S, A> StructureScope<E_, S>.group(
    element: Groupable<E_, S, A>,
    description: String = "",
): Group.UnknownSize<E_, S, GroupResult1<A>> = createGroup { Group1Impl(name, description, element) }

@JvmName("groupTerminating")
fun <E_ : Environment, S, A, B> StructureScope<E_, S>.group(
    elementA: Groupable<E_, S, A>,
    elementB: Groupable<E_, S, B>,
    description: String = "",
): Group.UnknownSize<E_, S, GroupResult2<A, B>> = createGroup { Group2Impl(name, description, elementA, elementB) }

@JvmName("groupTerminating")
fun <E_ : Environment, S, A, B, C> StructureScope<E_, S>.group(
    elementA: Groupable<E_, S, A>,
    elementB: Groupable<E_, S, B>,
    elementC: Groupable<E_, S, C>,
    description: String = "",
): Group.UnknownSize<E_, S, GroupResult3<A, B, C>> = createGroup {
    Group3Impl(name, description, elementA, elementB, elementC)
}

@JvmName("groupTerminating")
fun <E_ : Environment, S, A, B, C, D> StructureScope<E_, S>.group(
    elementA: Groupable<E_, S, A>,
    elementB: Groupable<E_, S, B>,
    elementC: Groupable<E_, S, C>,
    elementD: Groupable<E_, S, D>,
    description: String = "",
): Group.UnknownSize<E_, S, GroupResult4<A, B, C, D>> = createGroup {
    Group4Impl(name, description, elementA, elementB, elementC, elementD)
}

@JvmName("groupTerminating")
fun <E_ : Environment, S, A, B, C, D, E> StructureScope<E_, S>.group(
    elementA: Groupable<E_, S, A>,
    elementB: Groupable<E_, S, B>,
    elementC: Groupable<E_, S, C>,
    elementD: Groupable<E_, S, D>,
    elementE: Groupable<E_, S, E>,
    description: String = "",
): Group.UnknownSize<E_, S, GroupResult5<A, B, C, D, E>> = createGroup {
    Group5Impl(name, description, elementA, elementB, elementC, elementD, elementE)
}

@JvmName("groupTerminating")
fun <E_ : Environment, S, A, B, C, D, E, F> StructureScope<E_, S>.group(
    elementA: Groupable<E_, S, A>,
    elementB: Groupable<E_, S, B>,
    elementC: Groupable<E_, S, C>,
    elementD: Groupable<E_, S, D>,
    elementE: Groupable<E_, S, E>,
    elementF: Groupable<E_, S, F>,
    description: String = "",
): Group.UnknownSize<E_, S, GroupResult6<A, B, C, D, E, F>> = createGroup {
    Group6Impl(name, description, elementA, elementB, elementC, elementD, elementE, elementF)
}

@JvmName("groupTerminating")
fun <E_ : Environment, S, A, B, C, D, E, F, G> StructureScope<E_, S>.group(
    elementA: Groupable<E_, S, A>,
    elementB: Groupable<E_, S, B>,
    elementC: Groupable<E_, S, C>,
    elementD: Groupable<E_, S, D>,
    elementE: Groupable<E_, S, E>,
    elementF: Groupable<E_, S, F>,
    elementG: Groupable<E_, S, G>,
    description: String = "",
): Group.UnknownSize<E_, S, GroupResult7<A, B, C, D, E, F, G>> = createGroup {
    Group7Impl(name, description, elementA, elementB, elementC, elementD, elementE, elementF, elementG)
}

@JvmName("groupTerminating")
fun <E_ : Environment, S, A, B, C, D, E, F, G, H> StructureScope<E_, S>.group(
    elementA: Groupable<E_, S, A>,
    elementB: Groupable<E_, S, B>,
    elementC: Groupable<E_, S, C>,
    elementD: Groupable<E_, S, D>,
    elementE: Groupable<E_, S, E>,
    elementF: Groupable<E_, S, F>,
    elementG: Groupable<E_, S, G>,
    elementH: Groupable<E_, S, H>,
    description: String = "",
): Group.UnknownSize<E_, S, GroupResult8<A, B, C, D, E, F, G, H>> = createGroup {
    Group8Impl(name, description, elementA, elementB, elementC, elementD, elementE, elementF, elementG, elementH)
}

private fun <E : Environment, S, R : GroupResult, G : Group<E, S, R>> StructureScope<E, S>.createGroup(
    block: StructureScope<E, S>.() -> G
): G {
    val scope =
        StructureScope<E, S>(
            name = "${this.name}_group",
            aliases = setOf(),
            description = "",
            parent = this,
            requirement = requirement { SenderValidationResult.success() },
        )
    return block(scope)
}
