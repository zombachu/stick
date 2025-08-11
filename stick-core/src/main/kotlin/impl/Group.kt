package com.zombachu.stick.impl

import com.zombachu.stick.ExecutionContext
import com.zombachu.stick.GroupResult
import com.zombachu.stick.ParsingResult
import com.zombachu.stick.Result
import com.zombachu.stick.TypedIdentifier
import com.zombachu.stick.cast
import com.zombachu.stick.isSuccess

internal class GroupImpl<S>(
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
            if (!element.isSenderValid(context.sender)) {
                continue
            }

            val processResult = (context as ExecutionContextImpl<S>).processSyntaxElement(element)
            if (processResult.isSuccess()) {
                // If successful, return
                return ParsingResult.success(GroupResult(element.id, processResult.value))
            } else if (processResult is ParsingResult.UnknownTypeError) {
                // Ignore type errors (element didn't match)
                continue
            } else {
                // If the element matched and an error occurred in parsing then propagate it up
                return processResult.cast()
            }
        }

        // No elements could be matched, fail syntax
        return ParsingResult.failSyntax()
    }

    override fun getSyntax(sender: S): String {
        val elementSyntax = elements.filter { it.isSenderValid(sender) }.map { it.getGroupedSyntax(sender) }
        return "<${elementSyntax.joinToString("|")}>"
    }
}
