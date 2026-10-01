package com.zombachu.stick.element

import com.zombachu.stick.CommandResult
import com.zombachu.stick.ContextualValue
import com.zombachu.stick.Environment
import com.zombachu.stick.Execution
import com.zombachu.stick.GroupResult
import com.zombachu.stick.Invocation
import com.zombachu.stick.MatchResult
import com.zombachu.stick.Position
import com.zombachu.stick.SenderValidator
import com.zombachu.stick.Size
import com.zombachu.stick.Suggestion
import com.zombachu.stick.failSyntax
import com.zombachu.stick.isSuccess
import com.zombachu.stick.propagateFailure
import com.zombachu.stick.success

internal class OptionalGroupImpl<E : Environment, S, G : GroupResult?, P : Position>(
    private val group: Group<E, S, out G, *>,
    private val default: ContextualValue<E, S, G>,
    private val defaultRequirement: SenderValidator<E, S>?,
) : OptionalGroup<E, S, G, P>, InternalElement<E, S, G>, InternalOptional<E, S> {

    override val size: Size = group.size.orNothing()
    override val name: String = group.name
    override val description: String = group.description

    context(inv: Invocation<E, S>)
    override fun match(args: List<String>): MatchResult {
        if (args.isEmpty()) return MatchResult.matchedAtLeast(0)
        return group.match(args)
    }

    context(inv: Invocation<E, S>)
    override fun suggest(preceding: List<String>, partial: String): List<Suggestion> = group.suggest(preceding, partial)

    context(ex: Execution<E, S>)
    override fun parse(args: List<String>): CommandResult<G> {
        if (args.isEmpty()) {
            validateDefault().propagateFailure {
                return failSyntax()
            }
            return default(ex)
        }

        return group.parse(args)
    }

    context(inv: Invocation<E, S>)
    override fun getSyntax(): String {
        if (!validateDefault().isSuccess()) return group.getSyntax()
        return "[${group.getGroupedSyntax()}]"
    }

    context(inv: Invocation<E, S>)
    override fun validateDefault(): CommandResult<Unit> = defaultRequirement?.validateSender() ?: success()
}
