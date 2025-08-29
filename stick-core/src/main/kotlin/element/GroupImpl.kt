package com.zombachu.stick.element

import com.zombachu.stick.Environment
import com.zombachu.stick.GroupResult
import com.zombachu.stick.Invocation
import com.zombachu.stick.ParsingResult
import com.zombachu.stick.Result
import com.zombachu.stick.TypedIdentifier
import com.zombachu.stick.ValidationContext
import com.zombachu.stick.impl.InvocationImpl
import com.zombachu.stick.impl.Size
import com.zombachu.stick.isSuccess
import com.zombachu.stick.propagateError
import com.zombachu.stick.valueOrPropagateError

internal class GroupImpl<E : Environment, S>(
    override val id: TypedIdentifier<GroupResult<*>>,
    override val description: String,
    private val elements: List<Groupable<E, S, Any>>,
) : Group<E, S> {

    private val prioritizedElements: List<SyntaxElement<E, S, Any>> = elements.sortedWith(
        compareBy<SyntaxElement<E, S, Any>> { type.parsingPriority }
            .thenBy { size.parsingPriority }
            .thenByDescending { if (size is Size.Fixed) size.size else 0 }
    )

    override val size: Size = Size.Deferred
    override val type: ElementType = ElementType.Default

    context(inv: Invocation<E, S>)
    override fun parse(args: List<String>): Result<GroupResult<*>> {
        for (element in prioritizedElements) {
            // Ignore elements unable to be accessed by the sender
            element.validateSender().propagateError<GroupResult<*>> { continue }

            val value = (inv as InvocationImpl).processSyntaxElement(element).valueOrPropagateError {
                if (it is ParsingResult.TypeNotMatchedError) {
                    // Ignore type errors (element didn't match)
                    continue
                } else {
                    // If the element matched and an error occurred in parsing then propagate it up
                    return it
                }
            }
            // If successful, return
            return ParsingResult.success(GroupResult(element.id, value))
        }

        // No elements could be matched, fail syntax
        return ParsingResult.failSyntax(inv.getSyntax())
    }

    context(validationContext: ValidationContext<E, S>)
    override fun getSyntax(): String {
        val elementSyntax = elements.filter { it.validateSender().isSuccess() }.map { it.getGroupedSyntax() }
        return "<${elementSyntax.joinToString("|")}>"
    }
}
