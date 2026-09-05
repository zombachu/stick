package com.zombachu.stick.element

import com.zombachu.stick.CommandResult
import com.zombachu.stick.Environment
import com.zombachu.stick.GroupResult
import com.zombachu.stick.GroupResult.ResultA
import com.zombachu.stick.GroupResult.ResultB
import com.zombachu.stick.GroupResult.ResultC
import com.zombachu.stick.GroupResult.ResultD
import com.zombachu.stick.GroupResult.ResultE
import com.zombachu.stick.GroupResult.ResultF
import com.zombachu.stick.GroupResult.ResultG
import com.zombachu.stick.GroupResult.ResultH
import com.zombachu.stick.GroupResult1
import com.zombachu.stick.GroupResult2
import com.zombachu.stick.GroupResult3
import com.zombachu.stick.GroupResult4
import com.zombachu.stick.GroupResult5
import com.zombachu.stick.GroupResult6
import com.zombachu.stick.GroupResult7
import com.zombachu.stick.GroupResult8
import com.zombachu.stick.Invocation
import com.zombachu.stick.InvocationImpl
import com.zombachu.stick.ParsingResult
import com.zombachu.stick.PeekingResult
import com.zombachu.stick.Position
import com.zombachu.stick.Size
import com.zombachu.stick.ValidationContext
import com.zombachu.stick.element.GroupElement.Companion.to
import com.zombachu.stick.isSuccess
import com.zombachu.stick.propagateError
import com.zombachu.stick.valueOrPropagateError
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract

internal open class GroupImpl<E : Environment, S, G : GroupResult, P : Position>(
    override val name: String,
    override val description: String,
    private val elements: List<GroupElement<E, S, *, G>>,
) : Group<E, S, G, P> {

    private val prioritizedElements: List<GroupElement<E, S, *, G>> =
        elements.sortedWith(
            compareBy<GroupElement<E, S, *, G>> { it.groupable.type.parsingPriority }
                .thenBy { it.groupable.size.parsingPriority }
                .thenByDescending { (it.groupable.size as? Size.Bounded)?.max ?: 0 }
        )

    override val size: Size =
        elements
            .map { it.groupable.size }
            .reduce { a, b ->
                if (a is Size.Bounded && b is Size.Bounded) {
                    Size.between(minOf(a.min, b.min), maxOf(a.max, b.max))
                } else {
                    Size.atLeast(minOf(a.min, b.min))
                }
            }
    override val type: ElementType = ElementType.Default

    context(inv: Invocation<E, S>)
    override fun parse(args: List<String>): CommandResult<G> {
        for (element in prioritizedElements) {
            processGroupElement(
                element,
                onSuccess = {
                    return ParsingResult.success(it, Size(0))
                },
                onElementMismatch = { continue },
                onError = {
                    return it
                },
            )
        }
        // No elements could be matched, fail syntax
        return ParsingResult.failSyntax(inv.getSyntax())
    }

    context(validationContext: ValidationContext<E, S>)
    override fun getSyntax(): String {
        val elementSyntax =
            elements.filter { it.groupable.validateSender().isSuccess() }.map { it.groupable.getGroupedSyntax() }
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

        val invocation = inv as InvocationImpl
        val remaining = invocation.unparsed.size
        val value =
            invocation.processElement(groupElement.groupable).valueOrPropagateError {
                // If element consumed args then it matched, so error is real and not a matching error
                if (invocation.unparsed.size != remaining) onError(it)
                when (it) {
                    // Ignore matching errors
                    is ParsingResult.TypeNotMatchedInternal,
                    is ParsingResult.TypeNotMatchedError,
                    is ParsingResult.LiteralNotMatchedError,
                    is PeekingResult.InvalidSizeError -> onElementMismatch()
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
        infix fun <E : Environment, S, G : GroupResult, T> Groupable<E, S, T>.to(
            toResult: (T) -> G
        ): GroupElement<E, S, T, G> {
            return GroupElement(this, toResult)
        }
    }
}

internal class Group1Impl<E_ : Environment, S, A, P : Position>(
    name: String,
    description: String,
    element: Groupable<E_, S, A>,
) : GroupImpl<E_, S, GroupResult1<A>, P>(name, description, [element to { ResultA(it) }])

internal class Group2Impl<E_ : Environment, S, A, B, P : Position>(
    name: String,
    description: String,
    elementA: Groupable<E_, S, A>,
    elementB: Groupable<E_, S, B>,
) :
    GroupImpl<E_, S, GroupResult2<A, B>, P>(
        name,
        description,
        [elementA to { ResultA(it) }, elementB to { ResultB(it) }],
    )

internal class Group3Impl<E_ : Environment, S, A, B, C, P : Position>(
    name: String,
    description: String,
    elementA: Groupable<E_, S, A>,
    elementB: Groupable<E_, S, B>,
    elementC: Groupable<E_, S, C>,
) :
    GroupImpl<E_, S, GroupResult3<A, B, C>, P>(
        name,
        description,
        [elementA to { ResultA(it) }, elementB to { ResultB(it) }, elementC to { ResultC(it) }],
    )

internal class Group4Impl<E_ : Environment, S, A, B, C, D, P : Position>(
    name: String,
    description: String,
    elementA: Groupable<E_, S, A>,
    elementB: Groupable<E_, S, B>,
    elementC: Groupable<E_, S, C>,
    elementD: Groupable<E_, S, D>,
) :
    GroupImpl<E_, S, GroupResult4<A, B, C, D>, P>(
        name,
        description,
        [
            elementA to { ResultA(it) },
            elementB to { ResultB(it) },
            elementC to { ResultC(it) },
            elementD to { ResultD(it) },
        ],
    )

internal class Group5Impl<E_ : Environment, S, A, B, C, D, E, P : Position>(
    name: String,
    description: String,
    elementA: Groupable<E_, S, A>,
    elementB: Groupable<E_, S, B>,
    elementC: Groupable<E_, S, C>,
    elementD: Groupable<E_, S, D>,
    elementE: Groupable<E_, S, E>,
) :
    GroupImpl<E_, S, GroupResult5<A, B, C, D, E>, P>(
        name,
        description,
        [
            elementA to { ResultA(it) },
            elementB to { ResultB(it) },
            elementC to { ResultC(it) },
            elementD to { ResultD(it) },
            elementE to { ResultE(it) },
        ],
    )

internal class Group6Impl<E_ : Environment, S, A, B, C, D, E, F, P : Position>(
    name: String,
    description: String,
    elementA: Groupable<E_, S, A>,
    elementB: Groupable<E_, S, B>,
    elementC: Groupable<E_, S, C>,
    elementD: Groupable<E_, S, D>,
    elementE: Groupable<E_, S, E>,
    elementF: Groupable<E_, S, F>,
) :
    GroupImpl<E_, S, GroupResult6<A, B, C, D, E, F>, P>(
        name,
        description,
        [
            elementA to { ResultA(it) },
            elementB to { ResultB(it) },
            elementC to { ResultC(it) },
            elementD to { ResultD(it) },
            elementE to { ResultE(it) },
            elementF to { ResultF(it) },
        ],
    )

internal class Group7Impl<E_ : Environment, S, A, B, C, D, E, F, G, P : Position>(
    name: String,
    description: String,
    elementA: Groupable<E_, S, A>,
    elementB: Groupable<E_, S, B>,
    elementC: Groupable<E_, S, C>,
    elementD: Groupable<E_, S, D>,
    elementE: Groupable<E_, S, E>,
    elementF: Groupable<E_, S, F>,
    elementG: Groupable<E_, S, G>,
) :
    GroupImpl<E_, S, GroupResult7<A, B, C, D, E, F, G>, P>(
        name,
        description,
        [
            elementA to { ResultA(it) },
            elementB to { ResultB(it) },
            elementC to { ResultC(it) },
            elementD to { ResultD(it) },
            elementE to { ResultE(it) },
            elementF to { ResultF(it) },
            elementG to { ResultG(it) },
        ],
    )

internal class Group8Impl<E_ : Environment, S, A, B, C, D, E, F, G, H, P : Position>(
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
) :
    GroupImpl<E_, S, GroupResult8<A, B, C, D, E, F, G, H>, P>(
        name,
        description,
        [
            elementA to { ResultA(it) },
            elementB to { ResultB(it) },
            elementC to { ResultC(it) },
            elementD to { ResultD(it) },
            elementE to { ResultE(it) },
            elementF to { ResultF(it) },
            elementG to { ResultG(it) },
            elementH to { ResultH(it) },
        ],
    )
