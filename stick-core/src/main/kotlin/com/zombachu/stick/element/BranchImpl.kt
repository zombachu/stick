package com.zombachu.stick.element

import com.zombachu.stick.Arguments
import com.zombachu.stick.CommandResult
import com.zombachu.stick.Environment
import com.zombachu.stick.Invocation
import com.zombachu.stick.InvocationImpl
import com.zombachu.stick.MatchResult
import com.zombachu.stick.ParsingResult
import com.zombachu.stick.PeekingResult
import com.zombachu.stick.Size
import com.zombachu.stick.Suggestion
import com.zombachu.stick.ValidationContext
import com.zombachu.stick.propagateError
import com.zombachu.stick.valueOrPropagateError

internal open class BranchImpl<E : Environment, S, T_ : Arguments>(
    private val leading: Parameter<E, S, *, *>,
    private val signature: Signature<E, S, T_>,
) : Branch<E, S, T_> {

    override val name: String = leading.name
    override val description: String = leading.description
    override val size: Size = Size.atLeast(1)
    override val type: GroupableType = leading.type

    context(validationContext: ValidationContext<E, S>)
    override fun match(args: List<String>): MatchResult =
        when (val match = leading.match(args)) {
            is MatchResult.Matched -> MatchResult.matchedAtLeast(args.size)
            else -> match
        }

    context(validationContext: ValidationContext<E, S>)
    override fun suggest(preceding: List<String>, partial: String): List<Suggestion> =
        signature.suggest(preceding, partial)

    context(inv: Invocation<E, S>)
    override fun parse(args: List<String>): CommandResult<T_> {
        when (val match = leading.match(args)) {
            is MatchResult.Unmatched -> return match.failure
            is MatchResult.Partial -> return PeekingResult.failSize()
            is MatchResult.Matched -> {}
        }
        // Run validation in case this branch is a Structure
        validateSender().propagateError {
            return it
        }
        val parsedArgs =
            context(inv as InvocationImpl) { signature.execute() }
                .valueOrPropagateError {
                    return it
                }
        return ParsingResult.success(parsedArgs)
    }

    context(validationContext: ValidationContext<E, S>)
    override fun getSyntax(): String = signature.getSyntax()

    context(validationContext: ValidationContext<E, S>)
    override fun getGroupedSyntax(): String = leading.getGroupedSyntax()
}
