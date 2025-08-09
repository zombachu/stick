package com.zombachu.stick.impl

import com.zombachu.stick.ExecutionContext
import com.zombachu.stick.GroupResult
import com.zombachu.stick.ParsingResult
import com.zombachu.stick.TypedIdentifier

class Group<S>(
    override val id: TypedIdentifier<GroupResult<*>>,
    override val description: String,
    private val elements: List<Groupable<S, *>>,
) : SyntaxElement<S, GroupResult<*>>, SignatureConstraint.Terminating<S, GroupResult<*>> {

    private val prioritizedElements: List<SyntaxElement<S, Any>> = elements.sortedWith(
        compareBy<SyntaxElement<S, Any>> { type.parsingPriority }
            .thenBy { size.parsingPriority }
            .thenByDescending { if (size is Size.Fixed) size.size else 0 }
    )

    override val size: Size = Size.Deferred
    override val type: ElementType = ElementType.Default

    override fun parse(context: ExecutionContext<S>, args: List<String>): ParsingResult<GroupResult<*>> {
        for (element in prioritizedElements) {
            // Ignore elements unable to be accessed by the sender
            if (!element.isSenderValid(context.sender)) {
                continue
            }

            val parseResult = context.processSyntaxElement(element)
            when (parseResult) {
                // If successful, return
                is ParsingResult.Success -> return ParsingResult.success(GroupResult(element.id, parseResult.value))
                // Ignore type errors (element didn't match)
                is ParsingResult.Failure.InvalidTypeError -> continue
                // If the element matched and an error occurred in parsing then propagate it up
                else -> return (parseResult as ParsingResult.Failure<*>).cast()
            }
        }

        // No elements could be matched, fail syntax
        return ParsingResult.failSyntax()
    }

    override fun getSyntax(sender: S): String {
        val elementSyntax = elements.map { it.getGroupedSyntax(sender) }
        return "<${elementSyntax.joinToString("|")}>"
    }

    private fun SyntaxElement<S, Any>.isSenderValid(sender: S): Boolean {
        return this !is Validator<*> || (this as Validator<S>).validate(sender)
    }
}
