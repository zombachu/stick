package com.zombachu.stick.element

import com.zombachu.stick.CommandResult
import com.zombachu.stick.ContextualValue
import com.zombachu.stick.Environment
import com.zombachu.stick.Invocation
import com.zombachu.stick.ParsingResult
import com.zombachu.stick.TypedIdentifier
import com.zombachu.stick.ValidationContext
import com.zombachu.stick.impl.Size
import com.zombachu.stick.isSuccess
import com.zombachu.stick.valueOrPropagateError

typealias PipelineOperation<E, S, A, B> = Invocation<E, S>.(A) -> CommandResult<B>

@PublishedApi
internal class PipelinedFixedSizeParameter<E : Environment, S, A, T>(
    private val base: FixedSize<E, S, A>,
    private val operations: List<PipelineOperation<E, S, *, *>>,
    id: TypedIdentifier<T>,
) : Parameter.FixedSize<E, S, T>(base.size, id, base.description) {

    context(inv: Invocation<E, S>)
    override fun parse(args: List<String>): CommandResult<T> = parsePipeline(args, base, operations)

    context(validationContext: ValidationContext<E, S>)
    override fun getSyntax(): String = base.getSyntax()
}

@PublishedApi
internal class PipelinedUnknownSizeParameter<E : Environment, S, A, T>(
    private val base: UnknownSize<E, S, A>,
    private val operations: List<PipelineOperation<E, S, *, *>>,
    id: TypedIdentifier<T>,
) : Parameter.UnknownSize<E, S, T>(base.size, id, base.description) {

    context(inv: Invocation<E, S>)
    override fun parse(args: List<String>): CommandResult<T> = parsePipeline(args, base, operations)

    context(validationContext: ValidationContext<E, S>)
    override fun getSyntax(): String = base.getSyntax()
}

@PublishedApi
internal class PipelinedFlagParameter<E : Environment, S, A, T>(
    override val id: TypedIdentifier<T>,
    private val base: Flag<E, S, A>,
    private val operations: List<PipelineOperation<E, S, *, *>>,
) : Flag<E, S, T> {

    override val description: String = base.description
    override val size: Size = base.size
    override val type: ElementType = base.type

    context(inv: Invocation<E, S>)
    override fun parse(args: List<String>): CommandResult<T> = parsePipeline(args, base, operations)

    context(validationContext: ValidationContext<E, S>)
    override fun getSyntax(): String = base.getSyntax()

    @Suppress("UNCHECKED_CAST")
    override val default: ContextualValue<E, S, T>
        get() = get@{
            val baseResult = base.default(this)
            if (!baseResult.isSuccess()) {
                return@get baseResult as ParsingResult<T>
            }
            var value: Any? = baseResult.value
            operations.forEach {
                val operation = it as PipelineOperation<E, S, Any?, Any?>
                val operationResult = operation(this, value)
                if (!operationResult.isSuccess()) {
                    return@get operationResult as ParsingResult<T>
                }
                value = operationResult.value
            }
            ParsingResult.success(value as T)
        }
}

@Suppress("UNCHECKED_CAST")
context(inv: Invocation<E, S>)
private fun <E : Environment, S, A, T> parsePipeline(
    args: List<String>,
    base: SyntaxElement<E, S, A>,
    operations: List<PipelineOperation<E, S, *, *>>,
): CommandResult<T> {
    var value: Any? = base.parse(args).valueOrPropagateError { return it }
    operations.forEach {
        val operation = it as PipelineOperation<E, S, Any?, Any?>
        value = operation(inv, value).valueOrPropagateError { return it }
    }
    return ParsingResult.success(value as T)
}
