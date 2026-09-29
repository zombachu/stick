package com.zombachu.stick.element

import com.zombachu.stick.CommandResult
import com.zombachu.stick.Environment
import com.zombachu.stick.Execution
import com.zombachu.stick.GroupResult
import com.zombachu.stick.Invocation
import com.zombachu.stick.MatchResult
import com.zombachu.stick.Position
import com.zombachu.stick.Size
import com.zombachu.stick.Suggestion
import com.zombachu.stick.failSyntax
import com.zombachu.stick.isSuccess
import com.zombachu.stick.propagateError

internal class OptionalGroupImpl<E : Environment, S, G : GroupResult?, P : Position>(
    val group: Group<E, S, out G, *>,
    val presenceDefault: ValidSenderDefault<E, S, G>,
) : OptionalGroup<E, S, G, P>, InternalElement<E, S, G> {

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
            // Check if an alternative is required to be specified by the sender
            presenceDefault.validateSender().propagateError {
                return failSyntax()
            }
            return presenceDefault.value(ex)
        }

        return group.parse(args)
    }

    context(inv: Invocation<E, S>)
    override fun getSyntax(): String {
        if (!presenceDefault.validateSender().isSuccess()) return group.getSyntax()
        return "[${group.getGroupedSyntax()}]"
    }
}
