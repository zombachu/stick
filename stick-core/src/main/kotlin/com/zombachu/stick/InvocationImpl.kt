package com.zombachu.stick

import com.zombachu.stick.element.Branch
import com.zombachu.stick.element.ConsumingElement
import com.zombachu.stick.element.Element
import com.zombachu.stick.element.LeadingElementType
import com.zombachu.stick.element.Signature0
import com.zombachu.stick.element.Structure
import com.zombachu.stick.element.StructureImpl
import com.zombachu.stick.element.SyntaxElement
import com.zombachu.stick.element.parse

internal open class InvocationImpl<E : Environment, S>(
    override val sender: S,
    override val env: E,
    override val label: String,
    override val args: List<String>,
    structure: Structure<E, S, *>,
    parent: InvocationImpl<*, *>?,
) : Invocation<E, S> {

    private val root: InvocationImpl<*, *> = parent?.root ?: this

    internal open var unparsed: MutableList<String> = args.toMutableList()
    internal open var parsed: MutableMap<TypedIdentifier<*>, Any?> = mutableMapOf()

    private var rootConsumedArgs: Int = 0
    internal var consumedArgs: Int
        get() = root.rootConsumedArgs
        private set(value) {
            root.rootConsumedArgs = value
        }

    private val rootEnteredBranches: MutableList<EnteredBranch> =
        mutableListOf(context(this) { EnteredBranch { structure.getSyntax() } })
    private val enteredBranches: MutableList<EnteredBranch>
        get() = root.rootEnteredBranches

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
        val entered = enteredBranches
        val segments = entered.dropLast(1).flatMap { it.args } + entered.last().getSyntax()
        return "/${segments.joinToString(" ")}"
    }

    fun <S2 : Any> forSender(transform: (S) -> S2): InvocationImpl<E, S2> {
        return TransformedInvocationImpl(this, transform)
    }

    private fun consume(peeked: PeekingResult.Success, count: Int) {
        enteredBranches.last().args += peeked.value.subList(0, count)
        peeked.consume(count)
        consumedArgs += count
    }

    internal fun peek(size: Size): PeekingResult {
        if (size.matches(unparsed.size)) return PeekingResult.success(unparsed)
        if (size is Size.Bounded && unparsed.size > size.max) {
            return PeekingResult.success(unparsed.subList(0, size.max))
        }
        return PeekingResult.failSize()
    }

    internal fun <T> processElement(element: Element<E, S, T>): CommandResult<T> {
        context(this) {
            if (element !is SyntaxElement) {
                return element.parse([])
            }

            val peeked: PeekingResult = this@InvocationImpl.peek(element.size)
            if (peeked !is PeekingResult.Success) {
                return PeekingResult.failSize()
            }

            if (element is Branch) {
                this@InvocationImpl.enteredBranches += EnteredBranch { element.getSyntax() }
                val result = element.parse(peeked.value)
                this@InvocationImpl.enteredBranches.removeLast()
                return result
            }

            if (element !is ConsumingElement) {
                return element.parse(peeked.value)
            }

            val matched =
                when (val match = element.match(peeked.value)) {
                    is MatchResult.Unmatched -> return match.failure
                    is MatchResult.Partial -> return PeekingResult.failSize()
                    is MatchResult.Matched -> match
                }

            this@InvocationImpl.currentMatch = matched
            val result = element.parse(peeked.value)
            this@InvocationImpl.currentMatch = null

            result.propagateError {
                return it
            }
            if (result.consumed !in element.size.min..peeked.value.size) {
                // Bug in element implementation
                return ParsingResult.failUnknown()
            }

            this@InvocationImpl.consume(peeked, result.consumed)
            return result
        }
    }
}

private class EnteredBranch(val getSyntax: () -> String) {
    val args: MutableList<String> = mutableListOf()
}

private class TransformedInvocationImpl<E : Environment, S, S2>(base: InvocationImpl<E, S>, transform: (S) -> S2) :
    InvocationImpl<E, S2>(
        transform(base.sender),
        base.env,
        base.label,
        base.args,
        StructureImpl("", [], "", Requirement { SenderValidationResult.success() }) {
            Signature0({}, LeadingElementType.Label, [it])
        }, // Unused
        parent = base,
    ) {
    override var unparsed: MutableList<String> = base.unparsed
    override var parsed: MutableMap<TypedIdentifier<*>, Any?> = base.parsed
}
