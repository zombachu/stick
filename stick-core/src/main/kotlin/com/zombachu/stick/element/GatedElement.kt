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
import com.zombachu.stick.Requirement
import com.zombachu.stick.SenderValidator
import com.zombachu.stick.Size
import com.zombachu.stick.Suggestion
import com.zombachu.stick.consuming
import com.zombachu.stick.isSuccess
import com.zombachu.stick.propagateError
import com.zombachu.stick.success

@PublishedApi
internal class GatedParameterImpl<E : Environment, S, T, P : Position>(
    private val base: Parameter<E, S, T, P>,
    private val requirement: Requirement<E, S>,
) : GatedParameter<E, S, T, P>, InternalConsumingElement<E, S, T>, SenderValidator<E, S> {

    override val size: Size = base.size
    override val type: GroupableType = base.type
    override val name: String = base.name
    override val description: String = base.description

    context(inv: Invocation<E, S>)
    override fun match(args: List<String>): MatchResult = base.match(args)

    context(inv: Invocation<E, S>)
    override fun suggest(preceding: List<String>, partial: String): List<Suggestion> = base.suggest(preceding, partial)

    context(ex: Execution<E, S>)
    override fun parse(args: List<String>): ConsumingResult<T> = base.parse(args)

    context(inv: Invocation<E, S>)
    override fun getSyntax(): String = base.getSyntax()

    context(inv: Invocation<E, S>)
    override fun getGroupedSyntax(): String = base.getGroupedSyntax()

    context(inv: Invocation<E, S>)
    override fun validateSender(): CommandResult<Unit> = requirement.validateSender()
}

@PublishedApi
internal class GatedValueFlag<E : Environment, S, T>(
    private val base: ValueFlag<E, S, T>,
    private val requirement: Requirement<E, S>,
    private val deniedDefault: ContextualValue<E, S, T>?,
) : ValueFlag<E, S, T> by base, InternalConsumingElement<E, S, T> {

    override val default: ContextualValue<E, S, T> = {
        if (deniedDefault == null || requirement.validateSender().isSuccess()) {
            base.default(this)
        } else {
            deniedDefault(this)
        }
    }

    context(inv: Invocation<E, S>)
    override fun match(args: List<String>): MatchResult {
        requirement.validateSender().propagateError {
            return MatchResult.unmatched()
        }
        return base.match(args)
    }

    context(inv: Invocation<E, S>)
    override fun suggest(preceding: List<String>, partial: String): List<Suggestion> {
        requirement.validateSender().propagateError {
            return []
        }
        return base.suggest(preceding, partial)
    }

    context(ex: Execution<E, S>)
    override fun parse(args: List<String>): ConsumingResult<T> = base.parse(args)

    context(inv: Invocation<E, S>)
    override fun getSyntax(): String {
        if (!requirement.validateSender().isSuccess()) return ""
        return base.getSyntax()
    }
}

@PublishedApi
internal class GatedHybridFlag<E : Environment, S, T>(
    private val base: HybridFlag<E, S, T>,
    private val requirement: Requirement<E, S>,
    private val deniedDefault: ContextualValue<E, S, HybridFlagResult<T>>?,
) : HybridFlag<E, S, T> by base, InternalConsumingElement<E, S, HybridFlagResult<T>> {

    override val default: ContextualValue<E, S, HybridFlagResult<T>> = {
        if (deniedDefault == null || requirement.validateSender().isSuccess()) {
            success(HybridFlagResult.Absent())
        } else {
            deniedDefault(this)
        }
    }

    context(inv: Invocation<E, S>)
    override fun match(args: List<String>): MatchResult {
        requirement.validateSender().propagateError {
            return MatchResult.unmatched()
        }
        return base.match(args)
    }

    context(inv: Invocation<E, S>)
    override fun suggest(preceding: List<String>, partial: String): List<Suggestion> {
        requirement.validateSender().propagateError {
            return []
        }
        return base.suggest(preceding, partial)
    }

    context(ex: Execution<E, S>)
    override fun parse(args: List<String>): ConsumingResult<HybridFlagResult<T>> = base.parse(args)

    context(inv: Invocation<E, S>)
    override fun getSyntax(): String {
        if (!requirement.validateSender().isSuccess()) return ""
        return base.getSyntax()
    }
}

@PublishedApi
internal class GatedOptionalParameter<E : Environment, S, T, P : Position>(
    private val base: OptionalParameter<E, S, T, P>,
    private val requirement: Requirement<E, S>,
    private val deniedDefault: ContextualValue<E, S, T>?,
) : OptionalParameter<E, S, T, P> by base, InternalConsumingElement<E, S, T>, InternalOptional<E, S> {

    context(inv: Invocation<E, S>)
    override fun match(args: List<String>): MatchResult {
        requirement.validateSender().propagateError {
            return if (args.isEmpty()) MatchResult.matchedAtLeast(0) else MatchResult.unmatched(it)
        }
        return base.match(args)
    }

    context(inv: Invocation<E, S>)
    override fun suggest(preceding: List<String>, partial: String): List<Suggestion> {
        requirement.validateSender().propagateError {
            return []
        }
        return base.suggest(preceding, partial)
    }

    context(ex: Execution<E, S>)
    override fun parse(args: List<String>): ConsumingResult<T> {
        requirement.validateSender().propagateError { failure ->
            if (args.isNotEmpty()) return failure
            if (deniedDefault != null) return deniedDefault(ex).consuming(0)
            base.validateDefault().propagateError {
                return it
            }
            return base.parse(args)
        }
        return base.parse(args)
    }

    context(inv: Invocation<E, S>)
    override fun getSyntax(): String {
        if (!requirement.validateSender().isSuccess()) return ""
        return base.getSyntax()
    }

    context(inv: Invocation<E, S>)
    override fun validateDefault(): CommandResult<Unit> {
        if (deniedDefault != null && !requirement.validateSender().isSuccess()) return success()
        return base.validateDefault()
    }
}

@PublishedApi
internal class GatedOptionalGroup<E : Environment, S, G : GroupResult?, P : Position>(
    private val base: OptionalGroup<E, S, G, P>,
    private val requirement: Requirement<E, S>,
    private val deniedDefault: ContextualValue<E, S, G>?,
) : OptionalGroup<E, S, G, P> by base, InternalElement<E, S, G>, InternalOptional<E, S> {

    context(inv: Invocation<E, S>)
    override fun match(args: List<String>): MatchResult {
        requirement.validateSender().propagateError {
            return if (args.isEmpty()) MatchResult.matchedAtLeast(0) else MatchResult.unmatched(it)
        }
        return base.match(args)
    }

    context(inv: Invocation<E, S>)
    override fun suggest(preceding: List<String>, partial: String): List<Suggestion> {
        requirement.validateSender().propagateError {
            return []
        }
        return base.suggest(preceding, partial)
    }

    context(ex: Execution<E, S>)
    override fun parse(args: List<String>): CommandResult<G> {
        requirement.validateSender().propagateError { failure ->
            if (args.isNotEmpty()) return failure
            if (deniedDefault != null) return deniedDefault(ex)
            base.validateDefault().propagateError {
                return it
            }
            return base.parse(args)
        }
        return base.parse(args)
    }

    context(inv: Invocation<E, S>)
    override fun getSyntax(): String {
        if (!requirement.validateSender().isSuccess()) return ""
        return base.getSyntax()
    }

    context(inv: Invocation<E, S>)
    override fun validateDefault(): CommandResult<Unit> {
        if (deniedDefault != null && !requirement.validateSender().isSuccess()) return success()
        return base.validateDefault()
    }
}

@PublishedApi
internal class GatedStructure<E : Environment, S, T_ : Arguments>(
    base: Structure<E, S, T_>,
    requirement: Requirement<E, S>,
) : GatedBranch<E, S, T_>(base, requirement), Structure<E, S, T_> {
    override val label: String = base.label
    override val aliases: Set<String> = base.aliases
}

@PublishedApi
internal open class GatedBranch<E : Environment, S, T_ : Arguments>(
    base: Branch<E, S, T_>,
    private val requirement: Requirement<E, S>,
) : InternalBranch<E, S, T_> by base as InternalBranch<E, S, T_>, SenderValidator<E, S> {

    private val base: InternalBranch<E, S, T_> = base as InternalBranch<E, S, T_>

    context(inv: Invocation<E, S>)
    override fun validateSender(): CommandResult<Unit> {
        requirement.validateSender().propagateError {
            return it
        }
        return base.validateSender()
    }
}
