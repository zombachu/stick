package com.zombachu.stick

import com.zombachu.stick.ParsingResult.Companion.isSuccess
import com.zombachu.stick.impl.SenderScope
import com.zombachu.stick.impl.Size
import com.zombachu.stick.impl.SyntaxElement

class ExecutionContext<S>(
    val sender: S,
    val label: String,
    val args: List<String>,
) : SenderScope<S> {

    // Use a reversed view of a list to optimize removal of args in order
    private var unparsed: MutableList<String> = mutableListOf<String>().asReversed()
    private var parsed: MutableMap<TypedIdentifier<*>, Any> = mutableMapOf()

    fun <T : Any> get(id: TypedIdentifier<T>): T {
        return parsed[id] as T
    }

    fun <T : Any> getOrPut(id: TypedIdentifier<T>, defaultValue: () -> T): T {
        if (parsed.containsKey(id)) {
            return get(id)
        }
        val value = defaultValue()
        put(id, value)
        return value
    }

    fun <S2> forSender(sender: S2): ExecutionContext<S2> {
        return ExecutionContext(
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
            return ParsingResult.failSyntax()
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

internal sealed class PeekingResult : Result {
    class Success internal constructor(private val mutableArgs: MutableList<String>): PeekingResult(), Result.Success {
        val args: List<String> = mutableArgs
        fun consume() {
            mutableArgs.clear()
        }
    }
    class InvalidSizeError internal constructor() : PeekingResult(), Result.Failure

    companion object {
        fun success(mutableArgs: MutableList<String>): Success = Success(mutableArgs)
        fun failSize(): InvalidSizeError = InvalidSizeError()
    }
}
