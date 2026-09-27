package com.zombachu.stick

import com.zombachu.stick.element.Branch
import com.zombachu.stick.element.ConsumingElement
import com.zombachu.stick.element.Element
import com.zombachu.stick.element.LeadingParameterRole
import com.zombachu.stick.element.Signature0
import com.zombachu.stick.element.Structure
import com.zombachu.stick.element.StructureImpl
import com.zombachu.stick.element.SyntaxElement
import com.zombachu.stick.element.parse
import com.zombachu.stick.failure.Reason

internal open class InvocationImpl<E : Environment, S>(
    override val sender: S,
    override val env: E,
    override val label: String,
    override val args: List<String>,
    structure: Structure<E, S, *>,
    parent: InvocationImpl<*, *>?,
) : Invocation<E, S>() {

    private val root: InvocationImpl<*, *> = parent?.root ?: this

    internal open var unparsed: MutableList<String> = args.toMutableList()
    internal open var parsed: MutableMap<TypedIdentifier<*>, Any?> = mutableMapOf()

    private var rootConsumedArgs: Int = 0
    internal var consumedArgs: Int
        get() = root.rootConsumedArgs
        private set(value) {
            root.rootConsumedArgs = value
        }

    private var rootCurrentBranch: CurrentBranch = CurrentBranch(0) { structure.getSyntax() }
    private var currentBranch: CurrentBranch
        get() = root.rootCurrentBranch
        set(value) {
            root.rootCurrentBranch = value
        }

    private var rootCurrentMatch: MatchResult.Matched? = null
    internal var currentMatch: MatchResult.Matched?
        get() = root.rootCurrentMatch
        set(value) {
            root.rootCurrentMatch = value
        }

    override fun <T> get(id: TypedIdentifier<T>): T {
        @Suppress("UNCHECKED_CAST")
        return parsed[id] as T
    }

    override fun <T> put(id: TypedIdentifier<T>, value: T) {
        parsed[id] = value
    }

    override fun <T> getOrPut(id: TypedIdentifier<T>, value: T): T {
        if (parsed.containsKey(id)) {
            return get(id)
        }
        put(id, value)
        return value
    }

    override fun getSyntax(): String {
        val branch = currentBranch
        return "/${(args.subList(0, branch.start) + branch.getSyntax()).joinToString(" ")}"
    }

    override fun <S2 : Any> forSender(transform: (S) -> S2): InvocationImpl<E, S2> {
        return TransformedInvocationImpl(this, transform)
    }

    private fun consume(window: MutableList<String>, count: Int) {
        window.subList(0, count).clear()
        consumedArgs += count
    }

    internal fun peek(size: Size): MutableList<String>? {
        if (size.matches(unparsed.size)) return unparsed
        if (size is Size.Bounded && unparsed.size > size.max) {
            return unparsed.subList(0, size.max)
        }
        return null
    }

    internal fun <T> processElement(element: Element<E, S, T>): CommandResult<T> {
        context(this) {
            if (element !is SyntaxElement) {
                return element.parse([])
            }

            val window = this@InvocationImpl.peek(element.size) ?: return noMatch()

            if (element is Branch) {
                val outer = this@InvocationImpl.currentBranch
                this@InvocationImpl.currentBranch =
                    CurrentBranch(this@InvocationImpl.consumedArgs) { element.getSyntax() }
                val result = element.parse(window)
                this@InvocationImpl.currentBranch = outer
                return result
            }

            if (element !is ConsumingElement) {
                return element.parse(window)
            }

            val matched =
                when (val match = element.match(window)) {
                    is MatchResult.Unmatched -> return match.failure
                    is MatchResult.Partial -> return noMatch()
                    is MatchResult.Matched -> match
                }

            this@InvocationImpl.currentMatch = matched
            val result = element.parse(window)
            this@InvocationImpl.currentMatch = null

            result.propagateError {
                return it
            }
            if (result.consumed !in element.size.min..window.size) {
                // Bug in element implementation
                return fail(Reason.Unknown())
            }

            this@InvocationImpl.consume(window, result.consumed)
            return result
        }
    }
}

private class CurrentBranch(val start: Int, val getSyntax: () -> String)

private class TransformedInvocationImpl<E : Environment, S, S2>(base: InvocationImpl<E, S>, transform: (S) -> S2) :
    InvocationImpl<E, S2>(
        transform(base.sender),
        base.env,
        base.label,
        base.args,
        StructureImpl("", [], "", Requirement { success() }) {
            Signature0({}, LeadingParameterRole.Label, [it])
        }, // Unused
        parent = base,
    ) {
    override var unparsed: MutableList<String> = base.unparsed
    override var parsed: MutableMap<TypedIdentifier<*>, Any?> = base.parsed
}
