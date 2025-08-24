package com.zombachu.stick.element

import com.zombachu.stick.ExecutionContext
import com.zombachu.stick.GroupResult
import com.zombachu.stick.ParsingResult
import com.zombachu.stick.Result
import com.zombachu.stick.SenderContext
import com.zombachu.stick.TypedIdentifier
import com.zombachu.stick.impl.ExecutionContextImpl
import com.zombachu.stick.impl.Size
import com.zombachu.stick.isSuccess
import com.zombachu.stick.propagateError
import com.zombachu.stick.valueOrPropagateError

internal class GroupImpl<S : SenderContext>(
    override val id: TypedIdentifier<GroupResult<*>>,
    override val description: String,
    private val elements: List<Groupable<S, *>>,
) : Group<S> {

    private val prioritizedElements: List<SyntaxElement<S, Any>> = elements.sortedWith(
        compareBy<SyntaxElement<S, Any>> { type.parsingPriority }
            .thenBy { size.parsingPriority }
            .thenByDescending { if (size is Size.Fixed) size.size else 0 }
    )

    override val size: Size = Size.Deferred
    override val type: ElementType = ElementType.Default

    override fun parse(context: ExecutionContext<S>, args: List<String>): Result<GroupResult<*>> {
        for (element in prioritizedElements) {
            // Ignore elements unable to be accessed by the sender
            element.validateSender(context.senderContext).propagateError<GroupResult<*>> { continue }

            val value = (context as ExecutionContextImpl<S>).processSyntaxElement(element).valueOrPropagateError {
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
        return ParsingResult.failSyntax(context.getSyntax())
    }

    override fun getSyntax(senderContext: S): String {
        val elementSyntax = elements.filter { it.validateSender(senderContext).isSuccess() }.map { it.getGroupedSyntax(senderContext) }
        return "<${elementSyntax.joinToString("|")}>"
    }
}
