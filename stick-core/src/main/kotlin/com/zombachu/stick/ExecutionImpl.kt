package com.zombachu.stick

import com.zombachu.stick.element.Branch
import com.zombachu.stick.element.ConsumingElement
import com.zombachu.stick.element.Element
import com.zombachu.stick.element.Structure
import com.zombachu.stick.element.SyntaxElement
import com.zombachu.stick.element.parse
import com.zombachu.stick.failure.FailureOrigin
import com.zombachu.stick.failure.Reason

internal class ExecutionImpl<E : Environment, S>
private constructor(override val sender: S, override val env: E, private val state: ExecutionState) :
    Execution<E, S>(), ExecutionState by state {

    constructor(
        sender: S,
        env: E,
        label: String,
        args: List<String>,
        structure: Structure<E, S, *>,
    ) : this(sender, env, ExecutionStateImpl(label, args)) {
        currentBranch = CurrentBranch(0) { structure.getSyntax() }
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

    override fun getSyntax(): String = captureUsage()()

    private fun captureUsage(): () -> String {
        val branch = currentBranch
        val prefix = args.subList(0, branch.start)
        return { "/${(prefix + branch.getSyntax()).joinToString(" ")}" }
    }

    override fun createFailureOrigin(): FailureOrigin = FailureOrigin(currentBranch.element?.name, captureUsage())

    override fun <S2 : Any> forSender(transform: (S) -> S2): ExecutionImpl<E, S2> =
        ExecutionImpl(transform(sender), env, state)

    internal fun <T> processElement(element: Element<E, S, T>): CommandResult<T> {
        val branch = currentBranch
        currentBranch = branch.copy(element = element as? SyntaxElement)
        val result = parseElement(element)
        currentBranch = branch
        return result
    }

    private fun <T> parseElement(element: Element<E, S, T>): CommandResult<T> {
        context(this) {
            if (element !is SyntaxElement) {
                return element.parse([])
            }

            val window = this@ExecutionImpl.peek(element.size) ?: return noMatch()

            if (element is Branch) {
                val outer = this@ExecutionImpl.currentBranch
                this@ExecutionImpl.currentBranch =
                    CurrentBranch(this@ExecutionImpl.consumedArgs) { element.getSyntax() }
                val result = element.parse(window)
                this@ExecutionImpl.currentBranch = outer
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

            this@ExecutionImpl.currentMatch = matched
            val result = element.parse(window)
            this@ExecutionImpl.currentMatch = null

            result.propagateFailure {
                return it
            }
            if (result.consumed !in element.size.min..window.size) {
                // Bug in element implementation
                return fail(Reason.Unknown())
            }

            this@ExecutionImpl.consume(window, result.consumed)
            return result
        }
    }
}

private interface ExecutionState {
    val label: String
    val args: List<String>
    val unparsed: MutableList<String>
    val parsed: MutableMap<TypedIdentifier<*>, Any?>
    val consumedArgs: Int
    var currentBranch: CurrentBranch
    var currentMatch: MatchResult.Matched?

    fun peek(size: Size): MutableList<String>?

    fun consume(window: MutableList<String>, count: Int)
}

private class ExecutionStateImpl(override val label: String, override val args: List<String>) : ExecutionState {
    override lateinit var currentBranch: CurrentBranch
    override val unparsed: MutableList<String> = args.toMutableList()
    override val parsed: MutableMap<TypedIdentifier<*>, Any?> = mutableMapOf()
    override var consumedArgs: Int = 0
        private set

    override var currentMatch: MatchResult.Matched? = null

    override fun peek(size: Size): MutableList<String>? {
        if (size.matches(unparsed.size)) return unparsed
        if (size is Size.Bounded && unparsed.size > size.max) {
            return unparsed.subList(0, size.max)
        }
        return null
    }

    override fun consume(window: MutableList<String>, count: Int) {
        window.subList(0, count).clear()
        consumedArgs += count
    }
}

private data class CurrentBranch(
    val start: Int,
    val element: SyntaxElement<*, *, *>? = null,
    val getSyntax: () -> String,
)
