package com.zombachu.stick.element

import com.zombachu.stick.CommandResult
import com.zombachu.stick.ContextualValue
import com.zombachu.stick.Environment
import com.zombachu.stick.HybridFlagResult
import com.zombachu.stick.Invocation
import com.zombachu.stick.ParsingResult
import com.zombachu.stick.ValidationContext
import com.zombachu.stick.impl.Size
import com.zombachu.stick.isSuccess
import com.zombachu.stick.valueOrPropagateError

typealias PipelineOperation<E, S, A, B> = Invocation<E, S>.(A) -> CommandResult<B>

@PublishedApi
internal class PipelinedFixedSizeParameter<E : Environment, S, A, T>(
    private val base: FixedSize<E, S, A>,
    private val operations: List<PipelineOperation<E, S, *, *>>,
) : Parameter.FixedSize<E, S, T>(base.size, base.name, base.description) {

    context(inv: Invocation<E, S>)
    override fun parse(args: List<String>): CommandResult<T> = parsePipeline(args, base, operations)

    context(validationContext: ValidationContext<E, S>)
    override fun getSyntax(): String = base.getSyntax()
}

@PublishedApi
internal class PipelinedUnknownSizeParameter<E : Environment, S, A, T>(
    private val base: UnknownSize<E, S, A>,
    private val operations: List<PipelineOperation<E, S, *, *>>,
) : Parameter.UnknownSize<E, S, T>(base.size, base.name, base.description) {

    context(inv: Invocation<E, S>)
    override fun parse(args: List<String>): CommandResult<T> = parsePipeline(args, base, operations)

    context(validationContext: ValidationContext<E, S>)
    override fun getSyntax(): String = base.getSyntax()
}

@PublishedApi
internal class PipelinedValueFlag<E : Environment, S, A, T>(
    private val base: ValueFlag<E, S, A>,
    private val operations: List<PipelineOperation<E, S, *, *>>,
) : ValueFlag<E, S, T> {

    override val size: Size = base.size
    override val type: ElementType = base.type
    override val name: String = base.name
    override val description: String = base.description

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

@PublishedApi
internal class PipelinedHybridFlag<E : Environment, S, A, T>(
    private val base: HybridFlag<E, S, A>,
    private val operations: List<PipelineOperation<E, S, *, *>>,
) : HybridFlag<E, S, T> {

    override val size: Size = base.size
    override val type: ElementType = base.type
    override val name: String = base.name
    override val description: String = base.description

    context(inv: Invocation<E, S>)
    override fun parse(args: List<String>): CommandResult<HybridFlagResult<T>> = parsePipeline(args, base, operations)

    context(validationContext: ValidationContext<E, S>)
    override fun getSyntax(): String = base.getSyntax()

    @Suppress("UNCHECKED_CAST")
    override val default: ContextualValue<E, S, HybridFlagResult<T>>
        get() = get@{
            val baseResult = base.default(this)
            if (!baseResult.isSuccess()) {
                return@get baseResult as ParsingResult<HybridFlagResult<T>>
            }
            var value: Any? = baseResult.value
            operations.forEach {
                val operation = it as PipelineOperation<E, S, Any?, Any?>
                val operationResult = operation(this, value)
                if (!operationResult.isSuccess()) {
                    return@get operationResult as ParsingResult<HybridFlagResult<T>>
                }
                value = operationResult.value
            }
            ParsingResult.success(value as HybridFlagResult<T>)
        }
}

@PublishedApi
internal class PipelinedOptionalParameter<E : Environment, S, A, T>(
    private val base: OptionalParameter<E, S, A>,
    private val operations: List<PipelineOperation<E, S, *, *>>,
) : Parameter.UnknownSize<E, S, T>(base.size, base.name, base.description), OptionalParameter<E, S, T> {

    context(inv: Invocation<E, S>)
    override fun parse(args: List<String>): CommandResult<T> = parsePipeline(args, base, operations)

    context(validationContext: ValidationContext<E, S>)
    override fun getSyntax(): String = base.getSyntax()
}

@Suppress("UNCHECKED_CAST")
context(inv: Invocation<E, S>)
private fun <E : Environment, S, A, T> parsePipeline(
    args: List<String>,
    base: SyntaxElement<E, S, A>,
    operations: List<PipelineOperation<E, S, *, *>>,
): CommandResult<T> {
    var value: Any? =
        base.parse(args).valueOrPropagateError {
            return it
        }
    operations.forEach {
        val operation = it as PipelineOperation<E, S, Any?, Any?>
        value =
            operation(inv, value).valueOrPropagateError {
                return it
            }
    }
    val consumed = base.size as? Size.Fixed ?: Size(args.size)
    return ParsingResult.success(value as T, consumed)
}
