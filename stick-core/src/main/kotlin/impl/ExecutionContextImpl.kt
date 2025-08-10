package com.zombachu.stick.impl

import com.zombachu.stick.ExecutionContext
import com.zombachu.stick.ParsingResult
import com.zombachu.stick.ParsingResult.Companion.isSuccess
import com.zombachu.stick.PeekingResult
import com.zombachu.stick.TypedIdentifier

class ExecutionContextImpl<S>(
    override val sender: S,
    override val label: String,
    override val args: List<String>,
) : ExecutionContext<S> {

    // Use a reversed view of a list to optimize removal of args in order
    private var unparsed: MutableList<String> = mutableListOf<String>().asReversed()
    private var parsed: MutableMap<TypedIdentifier<*>, Any> = mutableMapOf()

    override fun <T : Any> get(id: TypedIdentifier<T>): T {
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

    internal fun <S2> forSender(sender: S2): ExecutionContextImpl<S2> {
        return ExecutionContextImpl(
            sender,
            label,
            args,
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
        element: SyntaxElement<S, Any>,
    ): ParsingResult<out Any> {
        val peeked = peek(element.size)
        if (peeked !is PeekingResult.Success) {
            return ParsingResult.Companion.failSyntax()
        }

        val parseResult = element.parse(this, peeked.args)
        if (!parseResult.isSuccess()) {
            return parseResult
        }
        peeked.consume()

        put(element.id, parseResult.value)
        return parseResult
    }

    internal fun <T : Any> put(id: TypedIdentifier<out T>, parsedValue: T) {
        parsed[id] = parsedValue
    }
}
