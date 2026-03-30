package com.zombachu.stick.element

import com.zombachu.stick.CommandResult
import com.zombachu.stick.Environment
import com.zombachu.stick.GroupResult
import com.zombachu.stick.GroupResult.*
import com.zombachu.stick.GroupResult1
import com.zombachu.stick.GroupResult2
import com.zombachu.stick.GroupResult3
import com.zombachu.stick.GroupResult4
import com.zombachu.stick.GroupResult5
import com.zombachu.stick.GroupResult6
import com.zombachu.stick.GroupResult7
import com.zombachu.stick.GroupResult8
import com.zombachu.stick.Invocation
import com.zombachu.stick.ParsingResult
import com.zombachu.stick.PeekingResult
import com.zombachu.stick.ValidationContext
import com.zombachu.stick.element.GroupElement.Companion.to
import com.zombachu.stick.impl.InvocationImpl
import com.zombachu.stick.impl.Size
import com.zombachu.stick.isSuccess
import com.zombachu.stick.propagateError
import com.zombachu.stick.valueOrPropagateError
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract

internal open class GroupImpl<E : Environment, S, G : GroupResult>(
    override val name: String,
    override val description: String,
    private val elements: List<GroupElement<E, S, *, G>>,
) : Group.UnknownSize<E, S, G>, Group.FiniteSize<E, S, G> {

    private val prioritizedElements: List<GroupElement<E, S, *, G>> = elements.sortedWith(
        compareBy<GroupElement<E, S, *, G>> { it.groupable.type.parsingPriority }
            .thenBy { it.groupable.size.parsingPriority }
            .thenByDescending { (it.groupable.size as? Size.Fixed)?.size ?: 0 }
    )

    override val size: Size = Size.Deferred
    override val type: ElementType = ElementType.Default

    context(inv: Invocation<E, S>)
    override fun parse(args: List<String>): CommandResult<G> {
        for (element in prioritizedElements) {
            processGroupElement(
                element,
                onSuccess = { return ParsingResult.success(it) },
                onElementMismatch = { continue },
                onError = { return it }
            )
        }
        // No elements could be matched, fail syntax
        return ParsingResult.failSyntax(inv.getSyntax())
    }

    context(validationContext: ValidationContext<E, S>)
    override fun getSyntax(): String {
        val elementSyntax = elements.filter { it.groupable.validateSender().isSuccess() }.map { it.groupable.getGroupedSyntax() }
        return "<${elementSyntax.joinToString("|")}>"
    }

    @OptIn(ExperimentalContracts::class)
    context(inv: Invocation<E, S>)
    private inline fun <T> processGroupElement(
        groupElement: GroupElement<E, S, T, G>,
        onSuccess: (G) -> Nothing,
        onElementMismatch: () -> Nothing,
        onError: (CommandResult.InternalFailure) -> Nothing,
    ) {
        contract {
            callsInPlace(onSuccess, InvocationKind.AT_MOST_ONCE)
            callsInPlace(onElementMismatch, InvocationKind.AT_MOST_ONCE)
            callsInPlace(onError, InvocationKind.AT_MOST_ONCE)
        }

        // Ignore elements unable to be accessed by the sender
        groupElement.groupable.validateSender().propagateError { onElementMismatch() }

        val value = (inv as InvocationImpl).processSyntaxElement(groupElement.groupable).valueOrPropagateError {
            when (it) {
                // Ignore type errors (element didn't match)
                is ParsingResult.TypeNotMatchedInternal, is ParsingResult.TypeNotMatchedError, is PeekingResult.InvalidSizeError -> onElementMismatch()
                // If the element matched and an error occurred in parsing then propagate it up
                else -> onError(it)
            }
        }
        // If successful, return
        onSuccess(groupElement.toResult(value))
    }
}

internal class GroupElement<E : Environment, S, T, G : GroupResult>(
    val groupable: Groupable<E, S, T>,
    val toResult: (T) -> G,
) {
    companion object {
        infix fun <E : Environment, S, G : GroupResult, T> Groupable<E, S, T>.to(toResult: (T) -> G): GroupElement<E, S, T, G> {
            return GroupElement(this, toResult)
        }
    }
}

internal class Group1Impl<E_ : Environment, S, A>(
    name: String,
    description: String,
    element: Groupable<E_, S, A>,
) : GroupImpl<E_, S, GroupResult1<A>>(name, description, listOf(
    element to ::ResultA,
))

internal class Group2Impl<E_ : Environment, S, A, B>(
    name: String,
    description: String,
    elementA: Groupable<E_, S, A>,
    elementB: Groupable<E_, S, B>,
) : GroupImpl<E_, S, GroupResult2<A, B>>(name, description, listOf(
    elementA to ::ResultA,
    elementB to ::ResultB,
))

internal class Group3Impl<E_ : Environment, S, A, B, C>(
    name: String,
    description: String,
    elementA: Groupable<E_, S, A>,
    elementB: Groupable<E_, S, B>,
    elementC: Groupable<E_, S, C>,
) : GroupImpl<E_, S, GroupResult3<A, B, C>>(name, description, listOf(
    elementA to ::ResultA,
    elementB to ::ResultB,
    elementC to ::ResultC,
))

internal class Group4Impl<E_ : Environment, S, A, B, C, D>(
    name: String,
    description: String,
    elementA: Groupable<E_, S, A>,
    elementB: Groupable<E_, S, B>,
    elementC: Groupable<E_, S, C>,
    elementD: Groupable<E_, S, D>,
) : GroupImpl<E_, S, GroupResult4<A, B, C, D>>(name, description, listOf(
    elementA to ::ResultA,
    elementB to ::ResultB,
    elementC to ::ResultC,
    elementD to ::ResultD,
))

internal class Group5Impl<E_ : Environment, S, A, B, C, D, E>(
    name: String,
    description: String,
    elementA: Groupable<E_, S, A>,
    elementB: Groupable<E_, S, B>,
    elementC: Groupable<E_, S, C>,
    elementD: Groupable<E_, S, D>,
    elementE: Groupable<E_, S, E>,
) : GroupImpl<E_, S, GroupResult5<A, B, C, D, E>>(name, description, listOf(
    elementA to ::ResultA,
    elementB to ::ResultB,
    elementC to ::ResultC,
    elementD to ::ResultD,
    elementE to ::ResultE,
))

internal class Group6Impl<E_ : Environment, S, A, B, C, D, E, F>(
    name: String,
    description: String,
    elementA: Groupable<E_, S, A>,
    elementB: Groupable<E_, S, B>,
    elementC: Groupable<E_, S, C>,
    elementD: Groupable<E_, S, D>,
    elementE: Groupable<E_, S, E>,
    elementF: Groupable<E_, S, F>,
) : GroupImpl<E_, S, GroupResult6<A, B, C, D, E, F>>(name, description, listOf(
    elementA to ::ResultA,
    elementB to ::ResultB,
    elementC to ::ResultC,
    elementD to ::ResultD,
    elementE to ::ResultE,
    elementF to ::ResultF,
))

internal class Group7Impl<E_ : Environment, S, A, B, C, D, E, F, G>(
    name: String,
    description: String,
    elementA: Groupable<E_, S, A>,
    elementB: Groupable<E_, S, B>,
    elementC: Groupable<E_, S, C>,
    elementD: Groupable<E_, S, D>,
    elementE: Groupable<E_, S, E>,
    elementF: Groupable<E_, S, F>,
    elementG: Groupable<E_, S, G>,
) : GroupImpl<E_, S, GroupResult7<A, B, C, D, E, F, G>>(name, description, listOf(
    elementA to ::ResultA,
    elementB to ::ResultB,
    elementC to ::ResultC,
    elementD to ::ResultD,
    elementE to ::ResultE,
    elementF to ::ResultF,
    elementG to ::ResultG,
))

internal class Group8Impl<E_ : Environment, S, A, B, C, D, E, F, G, H>(
    name: String,
    description: String,
    elementA: Groupable<E_, S, A>,
    elementB: Groupable<E_, S, B>,
    elementC: Groupable<E_, S, C>,
    elementD: Groupable<E_, S, D>,
    elementE: Groupable<E_, S, E>,
    elementF: Groupable<E_, S, F>,
    elementG: Groupable<E_, S, G>,
    elementH: Groupable<E_, S, H>,
) : GroupImpl<E_, S, GroupResult8<A, B, C, D, E, F, G, H>>(name, description, listOf(
    elementA to ::ResultA,
    elementB to ::ResultB,
    elementC to ::ResultC,
    elementD to ::ResultD,
    elementE to ::ResultE,
    elementF to ::ResultF,
    elementG to ::ResultG,
    elementH to ::ResultH,
))
