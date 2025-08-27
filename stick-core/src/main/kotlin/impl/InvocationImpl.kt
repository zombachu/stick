package com.zombachu.stick.impl

import com.zombachu.stick.Environment
import com.zombachu.stick.Invocation
import com.zombachu.stick.ParsingResult
import com.zombachu.stick.PeekingResult
import com.zombachu.stick.Result
import com.zombachu.stick.TypedIdentifier
import com.zombachu.stick.element.Structure
import com.zombachu.stick.element.SyntaxElement
import com.zombachu.stick.valueOrPropagateError

internal class InvocationImpl<E : Environment, S>(
    override val sender: S,
    override val env: E,
    override val label: String,
    override val args: List<String>,
    private val structure: Structure<E, S>,
    parent: InvocationImpl<*, *>?,
) : Invocation<E, S> {

    private val root: InvocationImpl<*, *> = parent?.root ?: this

    // Use a reversed view of a list to optimize removal of args in order
    private var unparsed: MutableList<String> = mutableListOf<String>().asReversed()
    private var parsed: MutableMap<TypedIdentifier<*>, Any> = mutableMapOf()

    override fun <T : Any> get(id: TypedIdentifier<T>): T {
        @Suppress("UNCHECKED_CAST")
        return parsed[id] as T
    }

    override fun <T : Any> getOrPut(id: TypedIdentifier<T>, defaultValue: () -> T): T {
        if (parsed.containsKey(id)) {
            return get(id)
        }
        val value = defaultValue()
        put(id, value)
        return value
    }

    override fun getSyntax(): String {
        return "/${root.getSyntaxForSender()}"
    }

    private fun getSyntaxForSender(): String {
        context(this.env) {
            return structure.getSyntax()
        }
    }

    override fun <S2 : Any> forSender(transform: (S) -> S2): InvocationImpl<E, S2> {
        return InvocationImpl(
            transform(this.sender),
            this.env,
            this.label,
            this.args,
            this.structure as Structure<E, S2>, // TODO: Handle safer
            parent = this,
        ).also {
            it.unparsed = this.unparsed
            it.parsed = this.parsed
        }
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

    internal fun processSyntaxElement(
        element: SyntaxElement<E, S, Any>,
    ): Result<out Any> {
        val peeked = peek(element.size)
        if (peeked !is PeekingResult.Success) {
            return ParsingResult.failSyntax(getSyntax())
        }

        context(this) {
            val value = element.parse(peeked.value).valueOrPropagateError { return it }
            peeked.consume()
            put(element.id, value)
            return ParsingResult.success(value)
        }
    }

    internal fun <T : Any> put(id: TypedIdentifier<out T>, parsedValue: T) {
        parsed[id] = parsedValue
    }
}
