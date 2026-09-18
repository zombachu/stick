package com.zombachu.stick.element

import com.zombachu.stick.CommandResult
import com.zombachu.stick.Environment
import com.zombachu.stick.GroupResult
import com.zombachu.stick.Invocation
import com.zombachu.stick.MatchResult
import com.zombachu.stick.ParsingResult
import com.zombachu.stick.Position
import com.zombachu.stick.Size
import com.zombachu.stick.Suggestion
import com.zombachu.stick.ValidationContext
import com.zombachu.stick.isSuccess
import com.zombachu.stick.propagateError

internal class OptionalGroupImpl<E : Environment, S, G : GroupResult?, P : Position>(
    val requirementDefault: InvalidSenderDefault<E, S, G>,
    val presenceDefault: ValidSenderDefault<E, S, G>,
    val group: Group<E, S, out G, *>,
) : OptionalGroup<E, S, G, P> {

    override val size: Size = group.size.orNothing()
    override val name: String = group.name
    override val description: String = group.description

    context(validationContext: ValidationContext<E, S>)
    override fun match(args: List<String>): MatchResult {
        if (args.isEmpty()) return MatchResult.matchedAtLeast(0)
        requirementDefault.validateSender().propagateError {
            return MatchResult.unmatched(it)
        }
        return group.match(args)
    }

    context(validationContext: ValidationContext<E, S>)
    override fun suggest(preceding: List<String>, partial: String): List<Suggestion> {
        requirementDefault.validateSender().propagateError {
            return []
        }
        return group.suggest(preceding, partial)
    }

    context(inv: Invocation<E, S>)
    override fun parse(args: List<String>): CommandResult<G> {
        if (args.isEmpty()) {
            // If the sender isn't allowed to specify an alternative use the default
            requirementDefault.validateSender().propagateError {
                return requirementDefault.value(inv)
            }
            // Check if an alternative is required to be specified by the sender
            presenceDefault.validateSender().propagateError {
                return ParsingResult.failSyntax(inv.getSyntax())
            }
            return presenceDefault.value(inv)
        }

        // Check if the sender specified an alternative when they're not allowed to
        requirementDefault.validateSender().propagateError {
            return it
        }

        return group.parse(args)
    }

    context(validationContext: ValidationContext<E, S>)
    override fun getSyntax(): String {
        // Check if the sender is allowed to specify an alternative
        if (!requirementDefault.validateSender().isSuccess()) return ""
        if (!presenceDefault.validateSender().isSuccess()) return group.getSyntax()
        return "[${group.getGroupedSyntax()}]"
    }
}
