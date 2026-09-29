package com.zombachu.stick.element

import com.zombachu.stick.Arguments
import com.zombachu.stick.CommandResult
import com.zombachu.stick.ConsumingResult
import com.zombachu.stick.ContextualValue
import com.zombachu.stick.Environment
import com.zombachu.stick.Execution
import com.zombachu.stick.GroupResult
import com.zombachu.stick.HybridFlagResult
import com.zombachu.stick.Invocation
import com.zombachu.stick.MatchResult
import com.zombachu.stick.Position
import com.zombachu.stick.SenderValidator
import com.zombachu.stick.Size
import com.zombachu.stick.Suggestion

internal class SenderMappedParameter<E : Environment, S, S2 : Any, T, P : Position>(
    private val base: Parameter<E, S2, T, P>,
    private val transform: (S) -> S2,
) : Parameter<E, S, T, P>(base.size, base.name, base.description) {

    override val type: GroupableType = base.type

    context(inv: Invocation<E, S>)
    override fun match(args: List<String>): MatchResult {
        val transformedInvocation = inv.forSender(transform)
        context(transformedInvocation) {
            return base.match(args)
        }
    }

    context(inv: Invocation<E, S>)
    override fun suggest(preceding: List<String>, partial: String): List<Suggestion> {
        val transformedInvocation = inv.forSender(transform)
        context(transformedInvocation) {
            return base.suggest(preceding, partial)
        }
    }

    context(ex: Execution<E, S>)
    override fun parse(args: List<String>): ConsumingResult<T> {
        val transformedExecution = ex.forSender(transform)
        context(transformedExecution) {
            return base.parse(args)
        }
    }

    context(inv: Invocation<E, S>)
    override fun getSyntax(): String {
        val transformedInvocation = inv.forSender(transform)
        context(transformedInvocation) {
            return base.getSyntax()
        }
    }

    context(inv: Invocation<E, S>)
    override fun getGroupedSyntax(): String {
        val transformedInvocation = inv.forSender(transform)
        context(transformedInvocation) {
            return base.getGroupedSyntax()
        }
    }
}

internal class SenderMappedValueFlag<E : Environment, S, S2 : Any, T>(
    private val base: ValueFlag<E, S2, T>,
    private val transform: (S) -> S2,
) : ValueFlag<E, S, T>, InternalConsumingElement<E, S, T> {

    override val default: ContextualValue<E, S, T> = { base.default(forSender(transform)) }

    override val size: Size.Bounded = base.size
    override val name: String = base.name
    override val description: String = base.description

    context(inv: Invocation<E, S>)
    override fun match(args: List<String>): MatchResult {
        val transformedInvocation = inv.forSender(transform)
        context(transformedInvocation) {
            return base.match(args)
        }
    }

    context(inv: Invocation<E, S>)
    override fun suggest(preceding: List<String>, partial: String): List<Suggestion> {
        val transformedInvocation = inv.forSender(transform)
        context(transformedInvocation) {
            return base.suggest(preceding, partial)
        }
    }

    context(ex: Execution<E, S>)
    override fun parse(args: List<String>): ConsumingResult<T> {
        val transformedExecution = ex.forSender(transform)
        context(transformedExecution) {
            return base.parse(args)
        }
    }

    context(inv: Invocation<E, S>)
    override fun getSyntax(): String {
        val transformedInvocation = inv.forSender(transform)
        context(transformedInvocation) {
            return base.getSyntax()
        }
    }
}

internal class SenderMappedHybridFlag<E : Environment, S, S2 : Any, T>(
    private val base: HybridFlag<E, S2, T>,
    private val transform: (S) -> S2,
) : HybridFlag<E, S, T>, InternalConsumingElement<E, S, HybridFlagResult<T>> {

    override val default: ContextualValue<E, S, HybridFlagResult<T>> = { base.default(forSender(transform)) }

    override val size: Size.Bounded = base.size
    override val name: String = base.name
    override val description: String = base.description

    context(inv: Invocation<E, S>)
    override fun match(args: List<String>): MatchResult {
        val transformedInvocation = inv.forSender(transform)
        context(transformedInvocation) {
            return base.match(args)
        }
    }

    context(inv: Invocation<E, S>)
    override fun suggest(preceding: List<String>, partial: String): List<Suggestion> {
        val transformedInvocation = inv.forSender(transform)
        context(transformedInvocation) {
            return base.suggest(preceding, partial)
        }
    }

    context(ex: Execution<E, S>)
    override fun parse(args: List<String>): ConsumingResult<HybridFlagResult<T>> {
        val transformedExecution = ex.forSender(transform)
        context(transformedExecution) {
            return base.parse(args)
        }
    }

    context(inv: Invocation<E, S>)
    override fun getSyntax(): String {
        val transformedInvocation = inv.forSender(transform)
        context(transformedInvocation) {
            return base.getSyntax()
        }
    }
}

internal class SenderMappedOptionalParameter<E : Environment, S, S2 : Any, T, P : Position>(
    private val base: OptionalParameter<E, S2, T, P>,
    private val transform: (S) -> S2,
) : OptionalParameter<E, S, T, P>, InternalConsumingElement<E, S, T> {

    override val size: Size = base.size
    override val name: String = base.name
    override val description: String = base.description

    context(inv: Invocation<E, S>)
    override fun match(args: List<String>): MatchResult {
        val transformedInvocation = inv.forSender(transform)
        context(transformedInvocation) {
            return base.match(args)
        }
    }

    context(inv: Invocation<E, S>)
    override fun suggest(preceding: List<String>, partial: String): List<Suggestion> {
        val transformedInvocation = inv.forSender(transform)
        context(transformedInvocation) {
            return base.suggest(preceding, partial)
        }
    }

    context(ex: Execution<E, S>)
    override fun parse(args: List<String>): ConsumingResult<T> {
        val transformedExecution = ex.forSender(transform)
        context(transformedExecution) {
            return base.parse(args)
        }
    }

    context(inv: Invocation<E, S>)
    override fun getSyntax(): String {
        val transformedInvocation = inv.forSender(transform)
        context(transformedInvocation) {
            return base.getSyntax()
        }
    }
}

internal class SenderMappedOptionalGroup<E : Environment, S, S2 : Any, G : GroupResult?, P : Position>(
    private val base: OptionalGroup<E, S2, G, P>,
    private val transform: (S) -> S2,
) : OptionalGroup<E, S, G, P>, InternalElement<E, S, G> {

    override val size: Size = base.size
    override val name: String = base.name
    override val description: String = base.description

    context(inv: Invocation<E, S>)
    override fun match(args: List<String>): MatchResult {
        val transformedInvocation = inv.forSender(transform)
        context(transformedInvocation) {
            return base.match(args)
        }
    }

    context(inv: Invocation<E, S>)
    override fun suggest(preceding: List<String>, partial: String): List<Suggestion> {
        val transformedInvocation = inv.forSender(transform)
        context(transformedInvocation) {
            return base.suggest(preceding, partial)
        }
    }

    context(ex: Execution<E, S>)
    override fun parse(args: List<String>): CommandResult<G> {
        val transformedExecution = ex.forSender(transform)
        context(transformedExecution) {
            return base.parse(args)
        }
    }

    context(inv: Invocation<E, S>)
    override fun getSyntax(): String {
        val transformedInvocation = inv.forSender(transform)
        context(transformedInvocation) {
            return base.getSyntax()
        }
    }
}

internal class SenderMappedStructure<E : Environment, S, S2 : Any, T_ : Arguments>(
    base: Structure<E, S2, T_>,
    transform: (S) -> S2,
) : SenderMappedBranch<E, S, S2, T_>(base, transform), Structure<E, S, T_> {
    override val label: String = base.label
    override val aliases: Set<String> = base.aliases
}

internal open class SenderMappedBranch<E : Environment, S, S2 : Any, T_ : Arguments>(
    base: Branch<E, S2, T_>,
    private val transform: (S) -> S2,
) : InternalBranch<E, S, T_>, SenderValidator<E, S> {

    private val base: InternalBranch<E, S2, T_> = base as InternalBranch<E, S2, T_>

    override val name: String = base.name
    override val description: String = base.description
    override val size: Size = base.size
    override val type: GroupableType = base.type

    context(inv: Invocation<E, S>)
    override fun match(args: List<String>): MatchResult {
        val transformedInvocation = inv.forSender(transform)
        context(transformedInvocation) {
            return base.match(args)
        }
    }

    context(inv: Invocation<E, S>)
    override fun suggestBranch(
        preceding: List<String>,
        partial: String,
        leadingParameterMatch: MatchResult?,
    ): List<Suggestion> {
        val transformedInvocation = inv.forSender(transform)
        context(transformedInvocation) {
            return base.suggestBranch(preceding, partial, leadingParameterMatch)
        }
    }

    context(ex: Execution<E, S>)
    override fun parse(args: List<String>): CommandResult<T_> {
        val transformedExecution = ex.forSender(transform)
        context(transformedExecution) {
            return base.parse(args)
        }
    }

    context(inv: Invocation<E, S>)
    override fun getSyntax(): String {
        val transformedInvocation = inv.forSender(transform)
        context(transformedInvocation) {
            return base.getSyntax()
        }
    }

    context(inv: Invocation<E, S>)
    override fun getGroupedSyntax(): String {
        val transformedInvocation = inv.forSender(transform)
        context(transformedInvocation) {
            return base.getGroupedSyntax()
        }
    }

    context(inv: Invocation<E, S>)
    override fun validateSender(): CommandResult<Unit> {
        val transformedInvocation = inv.forSender(transform)
        context(transformedInvocation) {
            return base.validateSender()
        }
    }
}
