package com.zombachu.stick

import com.zombachu.stick.dsl.requireSender
import com.zombachu.stick.element.GatedStructure
import com.zombachu.stick.element.SenderMappedStructure
import com.zombachu.stick.element.Structure
import com.zombachu.stick.failure.FailureHandler
import com.zombachu.stick.failure.TransformedFailureHandler
import kotlin.coroutines.CoroutineContext
import kotlin.reflect.KClass

abstract class Stick<E : Environment, S : Any>(
    private val platformSenderClass: KClass<S>,
    private val defaultEnvironment: Lazy<E>,
    private val defaultFailureHandler: Lazy<FailureHandler<E, S>>,
    private val defaultAsyncContext: CoroutineContext,
) {

    fun <E2 : E> withContext(
        env: E2,
        failureHandler: FailureHandler<E2, S> = defaultFailureHandler.value,
        asyncContext: CoroutineContext = defaultAsyncContext,
        block: context(E2, FailureHandler<E2, S>) StickScope<E2, S>.() -> Unit,
    ) {
        val scope =
            StickScope(platformSenderClass) { structure ->
                context(env, failureHandler) { registerCommand(structure, asyncContext) }
            }
        with(scope) { context(env, failureHandler) { block() } }
    }

    fun withContext(
        failureHandler: FailureHandler<E, S> = defaultFailureHandler.value,
        asyncContext: CoroutineContext = defaultAsyncContext,
        block: context(E, FailureHandler<E, S>) StickScope<E, S>.() -> Unit,
    ) = withContext(defaultEnvironment.value, failureHandler, asyncContext, block)

    fun <E2 : E, S2 : Any> withContext(
        env: E2,
        failureHandler: FailureHandler<E2, S2>,
        transform: (S) -> S2,
        validate: Invocation<E2, S>.() -> CommandResult<Unit>,
        asyncContext: CoroutineContext = defaultAsyncContext,
        block: context(E2, FailureHandler<E2, S2>) StickScope<E2, S2>.() -> Unit,
    ) {
        val requirement = Requirement(validate)
        val platformFailureHandler: FailureHandler<E2, S> = TransformedFailureHandler(failureHandler, transform)
        val scope =
            StickScope(null) { structure ->
                context(env, platformFailureHandler) {
                    registerCommand(
                        GatedStructure(SenderMappedStructure(structure, transform), requirement),
                        asyncContext,
                    )
                }
            }
        with(scope) { context(env, failureHandler) { block() } }
    }

    context(env: E2, failureHandler: FailureHandler<E2, S>)
    protected abstract fun <E2 : E> registerCommand(structure: Structure<E2, S, *>, asyncContext: CoroutineContext)
}

class StickScope<E : Environment, S : Any>
internal constructor(
    @PublishedApi internal val senderClass: KClass<S>?,
    @PublishedApi internal val registerStructure: (Structure<E, S, *>) -> Unit,
) {

    inline fun <reified S2 : S> register(structure: Structure<E, S2, *>) {
        @Suppress("UNCHECKED_CAST")
        registerStructure(
            if (S2::class == senderClass) {
                structure as Structure<E, S, *>
            } else {
                StructureScope.empty<E, S>().requireSender(S2::class) { structure }
            }
        )
    }

    inline fun <reified S2 : S> register(command: Command<E, S2>) {
        register(command.structure)
    }

    inline fun <reified S2 : S> register(noinline structure: StructureScope<E, S2>.() -> Structure<E, S2, *>) {
        register(structure(StructureScope.empty()))
    }
}
