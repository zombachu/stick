package com.zombachu.stick.impl

import com.zombachu.stick.CommandResult
import com.zombachu.stick.Environment
import com.zombachu.stick.Invocation
import com.zombachu.stick.PeekingResult
import com.zombachu.stick.SenderValidationResult
import com.zombachu.stick.TypedIdentifier
import com.zombachu.stick.element.Element
import com.zombachu.stick.element.Group
import com.zombachu.stick.element.Signature0
import com.zombachu.stick.element.Structure
import com.zombachu.stick.element.StructureImpl
import com.zombachu.stick.propagateError

internal open class InvocationImpl<E : Environment, S>(
    override val sender: S,
    override val env: E,
    override val label: String,
    override val args: List<String>,
    private val structure: Structure<E, S>,
    parent: InvocationImpl<*, *>?,
) : Invocation<E, S> {

    private val root: InvocationImpl<*, *> = parent?.root ?: this

    internal open var unparsed: MutableList<String> = args.toMutableList()
    internal open var parsed: MutableMap<TypedIdentifier<*>, Any?> = mutableMapOf()

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
        return "/${root.getSyntaxForSender()}"
    }

    internal open fun getSyntaxForSender(): String {
        context(this.env) {
            return structure.getSyntax()
        }
    }

    override fun <S2 : Any> forSender(transform: (S) -> S2): InvocationImpl<E, S2> {
        return TransformedInvocationImpl(this, transform)
    }

    internal fun peek(size: Size): PeekingResult {
        return when (size) {
            is Size.Fixed -> {
                if (unparsed.size < size.size) {
                    PeekingResult.failSize()
                } else {
                    PeekingResult.success(unparsed.subList(0, size.size))
                }
            }
            is Size.Deferred, is Size.Unbounded -> PeekingResult.success(unparsed)
        }
    }

    internal fun <T> processElement(
        element: Element<E, S, T>,
    ): CommandResult<T> {
        val peeked: PeekingResult = peek(element.size)
        if (peeked !is PeekingResult.Success) {
            @Suppress("UNCHECKED_CAST")
            return peeked as CommandResult<T>
        }

        context(this) {
            val result = element.parse(peeked.value)
            result.propagateError { return it }
            // Let groups manage their syntax element consumption
            if (element !is Group) {
                peeked.consume()
            }
            return result
        }
    }
}

private class TransformedInvocationImpl<E : Environment, S, S2>(
    val base: InvocationImpl<E, S>,
    transform: (S) -> S2,
) : InvocationImpl<E, S2>(
    transform(base.sender),
    base.env,
    base.label,
    base.args,
    // TransformedInvocationImpl forwards to structure of base invocation
    StructureImpl("", setOf(), "", Requirement { SenderValidationResult.success() }, Signature0()),
    parent = base,
) {
    override var unparsed: MutableList<String> = base.unparsed
    override var parsed: MutableMap<TypedIdentifier<*>, Any?> = base.parsed

    override fun getSyntaxForSender(): String {
        return base.getSyntaxForSender()
    }
}
