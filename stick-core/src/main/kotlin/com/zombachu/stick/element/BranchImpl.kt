package com.zombachu.stick.element

import com.zombachu.stick.Arguments
import com.zombachu.stick.CommandResult
import com.zombachu.stick.Environment
import com.zombachu.stick.Execution
import com.zombachu.stick.ExecutionImpl
import com.zombachu.stick.Invocation
import com.zombachu.stick.MatchResult
import com.zombachu.stick.Size
import com.zombachu.stick.Suggestion
import com.zombachu.stick.success
import com.zombachu.stick.valueOrPropagateFailure

internal open class BranchImpl<E : Environment, S, T_ : Arguments>(private val signature: Signature<E, S, T_>) :
    InternalBranch<E, S, T_> {

    private val leadingParameter: Parameter<E, S, *, *> = signature.leadingParameter

    override val name: String = leadingParameter.name
    override val description: String = leadingParameter.description
    override val size: Size = Size.atLeast(1)
    override val type: GroupableType = leadingParameter.type

    // GroupImpl.matchBranches handles the rest of matching once a branch is committed
    context(inv: Invocation<E, S>)
    override fun match(args: List<String>): MatchResult = leadingParameter.match(args)

    context(inv: Invocation<E, S>)
    override fun suggestBranch(
        preceding: List<String>,
        partial: String,
        leadingParameterMatch: MatchResult?,
    ): List<Suggestion> = signature.suggest(preceding, partial, leadingParameterMatch)

    context(ex: Execution<E, S>)
    override fun parse(args: List<String>): CommandResult<T_> {
        val parsedArgs =
            context(ex as ExecutionImpl) { signature.execute() }
                .valueOrPropagateFailure {
                    return it
                }
        return success(parsedArgs)
    }

    context(inv: Invocation<E, S>)
    override fun getSyntax(): String = signature.getSyntax()

    context(inv: Invocation<E, S>)
    override fun getGroupedSyntax(): String = leadingParameter.getGroupedSyntax()
}
